package com.shineoxygen.work.base.utils.qiniu;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import com.google.gson.Gson;
import com.qiniu.common.Constants;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Client;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Recorder;
import com.qiniu.storage.model.ResumeBlockInfo;
import com.qiniu.util.StringMap;
import com.qiniu.util.StringUtils;
import com.qiniu.util.UrlSafeBase64;

/**
 * 
 * @author 王辉阳
 * @date 2016年11月16日 下午6:05:50
 * @Description 断点续传扩展：创建块、整合七牛云的块为完整文件的接口
 */
public class ResumeUploaderExt {
	private final String upToken;
	private final String key;
	// private final File f;
	// private byte[] fileBytes;// 分块二进制数据
	private final String fileName;
	private final long size;
	private final long modifyTime;

	private final StringMap params;

	private final String mime;
	private final String[] contexts;
	private final Configuration configuration;
	private final Client client;
	private final byte[] blockBuffer;
	private final Recorder recorder;
	private final RecordHelper helper;
	private ByteArrayInputStream file;
	private String host;

	private boolean blockUploadDone = false;
	private boolean combineBlockDone = false;

	long uploaded = 0;

	private ResumeUploaderExt(Client client, String upToken, String key, String fileName, long fileSize, long fileLastModified, StringMap params, String mime, Recorder recorder,
			Configuration configuration) {
		this.configuration = configuration;
		this.client = client;
		this.upToken = upToken;
		this.key = key;
		// this.f = file;
		this.fileName = fileName;
		this.size = fileSize;
		this.modifyTime = fileLastModified;
		this.params = params;
		this.mime = mime == null ? Client.DefaultMime : mime;
		long count = (size + Constants.BLOCK_SIZE - 1) / Constants.BLOCK_SIZE;
		this.contexts = new String[(int) count];
		this.blockBuffer = new byte[Constants.BLOCK_SIZE];
		this.recorder = recorder;
		helper = new RecordHelper();
	}

	private static StringMap filterParam(StringMap params) {
		final StringMap ret = new StringMap();
		if (params == null) {
			return ret;
		}

		params.forEach(new StringMap.Consumer() {
			@Override
			public void accept(String key, Object value) {
				if (value == null) {
					return;
				}
				String val = value.toString();
				if (key.startsWith("x:") && !val.equals("")) {
					ret.put(key, val);
				}
			}
		});

		return ret;
	}

	/**
	 * 工厂方法：断点分块续传实例
	 * 
	 * @param key
	 * @param token
	 * @param recorder
	 * @param configuration
	 * @param fileName
	 * @param fileSize
	 * @param fileLastModified
	 * @param params
	 * @param mime
	 * @param checkCrc
	 * @return
	 */
	public static ResumeUploaderExt create(String key, String token, Recorder recorder, Configuration configuration, String fileName, long fileSize, long fileLastModified,
			StringMap params, String mime, boolean checkCrc) {
		checkArgs(key, token, fileName, fileSize, fileLastModified);

		if (fileSize <= configuration.putThreshold) {// 必须大于4mb才可以断点续传
			throw new UploadException("文件大小必须大于配置的门限值才能使用断点续传！");
		}

		mime = mime == null ? Client.DefaultMime : mime;
		params = filterParam(params);

		Client client = new Client(configuration.dns, configuration.dnsHostFirst, configuration.proxy, configuration.connectTimeout, configuration.responseTimeout,
				configuration.writeTimeout);

		return new ResumeUploaderExt(client, token, key, fileName, fileSize, fileLastModified, params, mime, recorder, configuration);
	}

	private static void checkArgs(String key, String token, String fileName, long fileSize, long fileLastModified) {
		String message = null;
		if (org.apache.commons.lang3.StringUtils.isAnyBlank(key, token, fileName)) {
			message = "key or  token or fileName is blank";
		}
		if (fileSize < 0 || fileLastModified < 0) {
			message = "fileSize or  fileLastModified  is not right";
		}
		if (message != null) {
			throw new UploadException(message);
		}
	}

	/**
	 * 单独块上传，这里按默认的块大小4mb
	 * 
	 * @param inputStream
	 * @return
	 * @throws QiniuException
	 */
	public boolean uploadBlock(InputStream inputStream) throws QiniuException {
		// 每次分4mb上传
		byte[] part = new byte[Constants.BLOCK_SIZE];
		try {
			inputStream.read(part, 0, Constants.BLOCK_SIZE);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return uploadBlock(part);
	}

	/**
	 * 上传单独块
	 * 
	 * @param fileBytes
	 *            当前分块二进制数据
	 * @return
	 * @throws QiniuException
	 */
	public boolean uploadBlock(byte[] fileBytes) throws QiniuException {
		if (blockUploadDone) {
			return true;
		}
		if (fileBytes.length != Constants.BLOCK_SIZE) {//
			return false;
		}

		if (host == null) {
			this.host = configuration.zone.upHost(upToken);
		}
		// 已上传的字节，如1024表示0-1024的字节已上传
		uploaded = helper.recoveryFromRecord();

		boolean retry = false;
		int contextIndex = blockIdx(uploaded);

		// 循环上传后续分块
		if (uploaded < size) {
			// long crc = Crc32.bytes(blockBuffer, 0, blockSize);
			Response response = null;
			int blockSize = nextBlockSize(uploaded);
			try {
				// 创建七牛云的块
				response = makeBlock(fileBytes, blockSize);
			} catch (QiniuException e) {
				if (e.code() < 0) {
					host = configuration.zone.upHostBackup(upToken);
				}
				if (e.response == null || e.response.needRetry()) {
					retry = true;
				} else {
					throw e;
				}
			}

			// 上传重试
			if (retry) {
				try {
					response = makeBlock(fileBytes, blockSize);
					retry = false;
				} catch (QiniuException e) {
					throw e;
				}

			}
			// 更新进度信息
			ResumeBlockInfo blockInfo = response.jsonToObject(ResumeBlockInfo.class);
			// TODO check return crc32
			// if blockInfo.crc32 != crc{}
			// 已上传的上下文
			contexts[contextIndex++] = blockInfo.ctx;
			// 更新已上传的字节标记
			uploaded += blockSize;
			helper.record(uploaded);

		}
		if (uploaded >= size) {
			blockUploadDone = true;
			return true;
		}
		return false;
	}

	/**
	 * 整合七牛云上的临时分块分件
	 * 
	 * @return
	 * @throws QiniuException
	 */
	private Response combineBlocks() throws QiniuException {
		try {
			// 将七牛云的分块整合为文件
			combineBlockDone = true;
			return makeFile();
		} catch (QiniuException e) {
			try {
				return makeFile();
			} catch (QiniuException e1) {
				combineBlockDone = false;
				throw e1;
			}
		} finally {
			helper.removeRecord();
		}
	}

	/**
	 * 合并七牛云上的文件
	 * 
	 * @return 返回七牛云上的文件地址
	 * @throws QiniuException
	 */
	public String blacks2File() throws QiniuException {
		combineBlocks();
		return QiniuHelpler.getInstance().getBUCKET_HOST_NAME() + key;
	}

	private Response makeBlock(byte[] block, int blockSize) throws QiniuException {
		String url = host + "/mkblk/" + blockSize;
		return post(url, block, 0, blockSize);
	}

	/**
	 * 整合七牛云分块的接口url
	 * 
	 * @return
	 */
	private String fileUrl() {
		String url = host + "/mkfile/" + size + "/mimeType/" + UrlSafeBase64.encodeToString(mime) + "/fname/" + UrlSafeBase64.encodeToString(fileName);
		final StringBuilder b = new StringBuilder(url);
		if (key != null) {
			b.append("/key/");
			b.append(UrlSafeBase64.encodeToString(key));
		}
		if (params != null) {
			params.forEach(new StringMap.Consumer() {
				@Override
				public void accept(String key, Object value) {
					b.append("/");
					b.append(key);
					b.append("/");
					b.append(UrlSafeBase64.encodeToString("" + value));
				}
			});
		}
		return b.toString();
	}

	/**
	 * 整合七牛云的分块为完整文件
	 * 
	 * @return
	 * @throws QiniuException
	 */
	private Response makeFile() throws QiniuException {
		String url = fileUrl();
		String s = StringUtils.join(contexts, ",");
		return post(url, StringUtils.utf8Bytes(s));
	}

	private Response post(String url, byte[] data) throws QiniuException {
		return client.post(url, data, new StringMap().put("Authorization", "UpToken " + upToken));
	}

	private Response post(String url, byte[] data, int offset, int size) throws QiniuException {
		return client.post(url, data, offset, size, new StringMap().put("Authorization", "UpToken " + upToken), Client.DefaultMime);
	}

	/**
	 * 下一个分块大小
	 * 
	 * @param uploaded
	 *            已上传的二进制数据
	 * @return
	 */
	private int nextBlockSize(long uploaded) {
		if (size < uploaded + Constants.BLOCK_SIZE) {
			return (int) (size - uploaded);
		}
		return Constants.BLOCK_SIZE;
	}

	/**
	 * 分块下标，0开始
	 * 
	 * @param offset
	 * @return
	 */
	private int blockIdx(long offset) {
		return (int) (offset / Constants.BLOCK_SIZE);
	}

	public boolean finishUploadBlock() {
		return blockUploadDone;
	}

	private class RecordHelper {
		/**
		 * 检查进度信息文件
		 * 
		 * @return
		 */
		long recoveryFromRecord() {
			try {
				return recoveryFromRecord0();
			} catch (Exception e) {
				e.printStackTrace();
				// ignore

				return 0;
			}
		}

		long recoveryFromRecord0() {
			if (recorder == null) {
				return 0;
			}

			String recorderKey = recorderKeyGenerate(key, fileName);

			byte[] data = recorder.get(recorderKey);
			if (data == null) {
				return 0;
			}
			String jsonStr = new String(data);
			Record r = new Gson().fromJson(jsonStr, Record.class);
			// 有效的进度信息：上传过字节，进度信息的修改时间吻合，进度信息的文件大小吻合，进度信息的上下文存在不为null、0
			if (r.offset == 0 || r.modify_time != modifyTime || r.size != size || r.contexts == null || r.contexts.length == 0) {
				return 0;
			}
			for (int i = 0; i < r.contexts.length; i++) {
				contexts[i] = r.contexts[i];
			}

			return r.offset;
		}

		/**
		 * 移除临时的进度信息文件
		 */
		void removeRecord() {
			try {
				if (recorder != null) {
					String recorderKey = recorderKeyGenerate(key, fileName);
					recorder.del(recorderKey);
				}
			} catch (Exception e) {
				e.printStackTrace();
				// ignore
			}
		}

		// save json value
		// {
		// "size":filesize,
		// "offset":lastSuccessOffset,
		// "modify_time": lastFileModifyTime,
		// "contexts": contexts
		// }
		/**
		 * 记录进度信息
		 * 
		 * @param offset
		 */
		void record(long offset) {
			try {
				if (recorder == null || offset == 0) {
					return;
				}
				String recorderKey = recorderKeyGenerate(key, fileName);
				// 进度信息
				String data = new Gson().toJson(new Record(size, offset, modifyTime, contexts));
				recorder.set(recorderKey, data.getBytes());
			} catch (Exception e) {
				e.printStackTrace();
				// ignore
			}
		}

		/**
		 * 生成进度信息文件名称
		 * 
		 * @param key
		 * @param fileName
		 * @return
		 */
		String recorderKeyGenerate(String key, String fileName) {
			return key + "_._" + fileName;
		}

		private class Record {
			long size;
			long offset;
			// CHECKSTYLE:OFF
			long modify_time;
			// CHECKSTYLE:ON
			String[] contexts;

			Record() {
			}

			Record(long size, long offset, long modify_time, String[] contexts) {
				this.size = size;// 文件大小
				this.offset = offset;// 已上传二进制数据，即偏移量
				this.modify_time = modify_time;// 文件的最后修改时间
				this.contexts = contexts;// 上传过的分块的上下文信息
			}
		}
	}

}
