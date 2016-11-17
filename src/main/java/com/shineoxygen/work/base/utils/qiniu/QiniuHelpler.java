package com.shineoxygen.work.base.utils.qiniu;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;

import com.qiniu.cdn.CdnManager;
import com.qiniu.common.Constants;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Recorder;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.FileInfo;
import com.qiniu.storage.model.FileListing;
import com.qiniu.storage.persistent.FileRecorder;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import com.shineoxygen.work.base.utils.RequestResponseUtil;
import com.shineoxygen.work.base.utils.pupload.Plupload;

/**
 * 
 * @author ShineOxygen
 * @date 2016年11月4日下午8:34:02
 * @description 七牛云服务工具类(默认支持单个bucket，可设置)
 * 
 *              关于覆盖上传：
 * 
 *              上传凭证scope为bucket上传的文件，只允许新增方式上传，建议使用这种，1，避免错误覆盖文件的几率，2，不会有覆盖上传的缓存不即使刷新问题
 * 
 *              scope为bucket:key上传的文件，只允许覆盖上传，文件不变(大小、名称等)会直接返回上传成功实际上是原来的问题，文件变更时，返回同名文件已存在
 * 
 *              关于缓存刷新：
 * 
 *              注意缓存问题，手动api调用刷新缓存也要十分钟生效，可在访问时url的querystring加上随机参数，如?random=xx，但是下载连接仍然会拿缓存的数据
 * 
 * 
 */
@SuppressWarnings("unused")
public class QiniuHelpler {
	private static Logger logger = LogManager.getLogger(QiniuHelpler.class);

	// 设置好账号的ACCESS_KEY和SECRET_KEY
	private static String ACCESS_KEY;
	private static String SECRET_KEY;
	// 七牛给的域名
	private static String BUCKET_HOST_NAME;
	// 要上传的空间
	private static String bucketName;
	// 上传文件的路径
	private static String TEMP;// 临时存储地址
	private static String IMG;// 图片存储地址

	private static String AUDIO;// 音频存储地址
	private static String VEDIO;// 视频存储地址

	private static long expireTime = 3600 * 5;// 5小时有效期
	private static long startTime;// 有效期开始时间毫秒数

	// 密钥配置
	private static Auth auth;
	// 访问token
	private static String token;
	// 回调地址
	private static String callbackUrl;
	private static String callbackBody;

	// 创建上传对象
	private static UploadManager uploadManager;
	// 资源操作
	private static BucketManager bucketManager;
	// cdn操作
	private static CdnManager cdnManager;

	private static Configuration configuration;
	private static Integer blockSize;

	// 分快上传临时文件存储目录
	private static String tempPath = RequestResponseUtil.getCurrentRequest() == null ? "C://"
			: RequestResponseUtil.getCurrentRequest().getServletContext().getRealPath("/") + "qiniuChunk";

	// 静态内部类实现单例
	private QiniuHelpler() {
	}

	private static class Singleton {
		private static final QiniuHelpler INSTANCE = new QiniuHelpler();
	}

	public static QiniuHelpler getInstance() {
		return Singleton.INSTANCE;
	}

	static {
		Properties properties = new Properties();
		InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("qiniu.properties");
		try {
			properties.load(is);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// 必须存在空间名和断点暂存路径
		Assert.assertNotNull(properties.getProperty("qiniu.bucketName"));
		Assert.assertNotNull(properties.getProperty("qiniu.callbackUrl"));
		Assert.assertNotNull(properties.getProperty("qiniu.callbackBody"));
		Assert.assertNotNull(properties.getProperty("qiniu.ACCESS_KEY"));
		Assert.assertNotNull(properties.getProperty("qiniu.SECRET_KEY"));
		Assert.assertNotNull(properties.getProperty("qiniu.BUCKET_HOST_NAME"));

		BUCKET_HOST_NAME = properties.getProperty("qiniu.BUCKET_HOST_NAME");
		ACCESS_KEY = properties.getProperty("qiniu.ACCESS_KEY");
		SECRET_KEY = properties.getProperty("qiniu.SECRET_KEY");
		bucketName = properties.getProperty("qiniu.bucketName");
		callbackUrl = properties.getProperty("qiniu.callbackUrl");
		callbackBody = properties.getProperty("qiniu.callbackBody");
		// 七牛仅支持4mb块
		blockSize = Constants.BLOCK_SIZE;

		// UploadDir
		TEMP = null != properties.getProperty("TEMP") ? properties.getProperty("TEMP") : UploadDir.TEMP.label;
		IMG = null != properties.getProperty("IMG") ? properties.getProperty("IMG") : UploadDir.IMG.label;
		AUDIO = null != properties.getProperty("AUDIO") ? properties.getProperty("AUDIO") : UploadDir.AUDIO.label;
		VEDIO = null != properties.getProperty("VEDIO") ? properties.getProperty("VEDIO") : UploadDir.VEDIO.label;

		auth = Auth.create(ACCESS_KEY, SECRET_KEY);

		startTime = System.currentTimeMillis() + expireTime;

		token = auth.uploadTokenWithDeadline(bucketName, null, startTime, null, true);

		// 华东地区的服务器，这里需要改成读配置文件
		configuration = new Configuration(Zone.zone0());
		uploadManager = new UploadManager(configuration);

		bucketManager = new BucketManager(auth, configuration);

		cdnManager = new CdnManager(auth);

	}

	/**
	 * 
	 * @author ShineOxygen
	 * @date 2016年11月4日下午7:50:03
	 * @description 文件夹类型：暂存，图片，音频，视频的存放文件夹
	 */
	public enum UploadDir {
		// label为默认的上传存储文件夹名，位于七牛空间下直接子级
		TEMP("temp"), IMG("img"), AUDIO("audio"), VEDIO("vedio");

		private String label;

		private UploadDir(String label) {
			this.label = label;
		}

		public String getLabel() {
			return label;
		}
	}

	/**
	 * 普通上传
	 * 
	 * UploadDir.TEMP和hello.png，则会存在temp/hello.png
	 * 
	 * @param fileBytes
	 *            文件二进制数据
	 * @param dir
	 *            存储目录，以UploadDir的label值为名的文件夹，不可为null
	 * @param fileNameWitSuffix
	 *            带有后缀名的文件名
	 * @param overwrite
	 *            true为覆盖七牛云上的文件(名称为dir+"/"+fileNameWitSuffix)，false为新增上传，建议设为false
	 * @return 七牛云上可访问的文件地址
	 * @throws QiniuException
	 */
	public String upload(byte[] fileBytes, UploadDir dir, String fileNameWitSuffix, boolean overwrite) throws QiniuException {
		if (null == dir) {
			return null;
		}
		if (overwrite) {
			token = auth.uploadTokenWithDeadline(bucketName, dir.label + "/" + fileNameWitSuffix, startTime, null, true);
		}
		Response res = uploadManager.put(fileBytes, dir.label + "/" + fileNameWitSuffix, token);

		logger.debug("upload response from qiniu " + res.bodyString());
		return BUCKET_HOST_NAME + dir.label + "/" + fileNameWitSuffix;
	}

	/**
	 * 
	 * 分片（分块）断点上传
	 * 
	 * @param file
	 *            物理存在的文件
	 * @param dir
	 *            存储目录，以UploadDir的label值为名的文件夹，不可为null
	 * @param fileNameWitSuffix
	 *            带有后缀名的文件名
	 * @param overwrite
	 *            true覆盖上传，false新增上传，建议新增上传
	 * @throws Exception
	 * @return 七牛云上可访问的文件地址
	 */
	public String resumeUpload(File file, UploadDir dir, String fileNameWitSuffix, boolean overwrite) throws Exception {
		if (null == dir) {
			return null;
		}

		if (overwrite) {
			token = auth.uploadTokenWithDeadline(bucketName, dir.label + "/" + fileNameWitSuffix, startTime, null, true);
		}

		// 设置断点记录文件保存位置
		Recorder recorder = new FileRecorder(tempPath);

		UploadManager uploadManager = new UploadManager(new Configuration(Zone.zone0()), recorder);

		try {
			Response res = uploadManager.put(file, dir.label + "/" + fileNameWitSuffix, token);

			logger.debug("upload response from qiniu " + res.bodyString());

			return BUCKET_HOST_NAME + dir.label + "/" + fileNameWitSuffix;
		} catch (QiniuException e) {
			logger.debug("upload异常，上传失败，七牛返回的错误信息(response.bodyString())" + e.response.bodyString());
			return null;
		}
	}

	/**
	 * 单分块断点续传，新增上传
	 * 
	 * @param dir
	 * @param fileNameWitSuffix
	 *            上传的文件名
	 * @param fileSize
	 * @param lastModified
	 * @return
	 * @throws Exception
	 */
	public ResumeUploaderExt resumeUploaderExt(UploadDir dir, String fileNameWitSuffix, long fileSize, long lastModified) throws Exception {
		if (null == dir) {
			return null;
		}
		// 设置断点记录文件保存在指定文件夹或File对象中
		Recorder recorder = new FileRecorder(tempPath);

		return ResumeUploaderExt.create(dir.getLabel() + "/" + fileNameWitSuffix, token, recorder, configuration, fileNameWitSuffix, fileSize, lastModified, null, null, false);
	}

	/**
	 * 单块上传对象
	 * 
	 * @param dir
	 *            保存的目录
	 * @param plupload
	 *            plupload插件对象
	 * @return
	 * @throws Exception
	 */
	public ResumeUploaderExt resumeUploaderExt(UploadDir dir, Plupload plupload) throws Exception {
		if (null == dir) {
			return null;
		}
		// 设置断点记录文件保存在指定文件夹或File对象中
		Recorder recorder = new FileRecorder(tempPath);

		return ResumeUploaderExt.create(dir.getLabel() + "/" + plupload.getName(), token, recorder, configuration, plupload.getName(), plupload.getFileTotalSize(),
				plupload.getLastModifiedTime(), null, null, false);
	}

	/**
	 * 该方法上传后，会回调指定callbackUrl，传回文件信息，如文件名，文件的访问url
	 * 
	 * @param fileBytes
	 *            上传的文件
	 * @param destFilePath
	 *            指定上传文件所保存在的文件路径
	 * @throws QiniuException
	 */
	public void uploadCallback(byte[] fileBytes, UploadDir dir, String fileNameWitSuffix) throws QiniuException {
		token = auth.uploadToken(bucketName, null, 3600, new StringMap().put("callbackUrl", callbackUrl).put("callbackBody", callbackBody));
		// 调用put方法上传
		Response res = uploadManager.put(fileBytes, null, token);
		// 打印返回的信息
		logger.debug("upload response from qiniu " + res.bodyString());
	}

	/**
	 * 删除七牛云上的指定文件
	 * 
	 * @param key
	 * @return
	 * @throws QiniuException
	 */
	public void deleteFile(String key) throws QiniuException {
		bucketManager.delete(bucketName, key);
	}

	/**
	 * 查询资源
	 * 
	 * @param key
	 * @return
	 */
	public FileInfo stat(String key) {
		try {
			return bucketManager.stat(bucketName, key);
		} catch (QiniuException e) {
			if (e.response.statusCode == 612) {
				return null;
			}
			if (e.response.statusCode != 612) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * 重命名
	 * 
	 * @param oldName
	 *            老的key
	 * @param newName
	 *            新的key
	 * @throws QiniuException
	 */
	public void rename(String oldName, String newName) throws QiniuException {
		bucketManager.rename(bucketName, oldName, newName);
	}

	/**
	 * 刷新指定资源： 非实时生效
	 * 
	 * @param url
	 *            如果http://xxx.com/temp/test.zip
	 * @throws QiniuException
	 */
	public void refresh(String url) throws QiniuException {
		cdnManager.refreshUrls(new String[] { url });
	}

	/**
	 * 生成下载连接
	 * 
	 * @param key
	 *            七牛上的资源名
	 * @return
	 */
	public String downloadUrl(String key) {
		// 构造私有空间需要生成的下载的链接
		String URL = BUCKET_HOST_NAME + key;

		// 调用privateDownloadUrl方法生成下载链接,第二个参数可以设置Token的过期时间
		return auth.privateDownloadUrl(URL + "?v=" + new Date().getTime(), 3600);
	}

	public String getToken() {
		return token;
	}

	public long getExpireTime() {
		return expireTime;
	}

	public long getStartTime() {
		return startTime;
	}

	public Integer getBlockSize() {
		return blockSize;
	}

	public String getBUCKET_HOST_NAME() {
		return BUCKET_HOST_NAME;
	}

}
