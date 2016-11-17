package com.shineoxygen.work.other.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;

import javax.security.auth.Refreshable;
import javax.wsdl.Input;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.util.ResourceUtils;

import com.qiniu.common.Constants;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Client;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Recorder;
import com.qiniu.storage.model.FileInfo;
import com.qiniu.storage.persistent.FileRecorder;
import com.shineoxygen.work.base.utils.RequestResponseUtil;
import com.shineoxygen.work.base.utils.qiniu.QiniuHelpler;
import com.shineoxygen.work.base.utils.qiniu.QiniuHelpler.UploadDir;
import com.shineoxygen.work.base.utils.qiniu.ResumeUploaderExt;

/**
 * 七牛测试
 * 
 * @author ShineOxygen
 * @description 注意缓存问题，手动api调用刷新缓存也要十分钟生效，可在访问url时候加上随机参数
 * @date 2016年11月4日下午9:18:23
 * 
 *       scope b:key--scope b:key 覆盖了--scope b上传失败，已经存在，覆盖的方式上传，存在同名的覆盖，否在直接新建
 * 
 *       scope: b
 *       以新增的方式上传，一般情况下以新增的方式为好，免得莫名被覆盖了一些文件，注意这种方式，如果是原文件，会直接返回地址，表示上传成功，但是文件变更后，如大小，会提示文件已经存在的错误
 * 
 */
@SuppressWarnings("all")
public class QiniuUtilsTest {

	@Test
	public void upload() throws IllegalAccessException, InvocationTargetException, FileNotFoundException {
		File file = ResourceUtils.getFile("classpath:wallpaper.png");
		try {
			System.out.println(QiniuHelpler.getInstance().upload(getBytes(file), UploadDir.IMG, file.getName(), false));
		} catch (QiniuException e) {
			try {
				System.out.println(e.response.bodyString());
			} catch (QiniuException e1) {
			}
		}
	}

	@Test
	public void refresh() throws QiniuException {
		QiniuHelpler.getInstance().refresh("http://og3nodshf.bkt.clouddn.com/temp/test.zip");
	}

	/**
	 * 分快上传，根据一个文件，上传所有块
	 * 
	 * @throws Exception
	 */
	@Test
	public void resumeUploadByFile() throws Exception {// 断点分块比普通的快一点
		long start = System.currentTimeMillis();
		System.out.println(start);
		File file = ResourceUtils.getFile("classpath:纽约-廖逸君作品《Walking in My Shoes》.mp4");
		System.out.println(QiniuHelpler.getInstance().resumeUpload(file, UploadDir.TEMP, file.getName(), true));
		System.out.println(System.currentTimeMillis() - start);
	}

	/**
	 * 删除七牛云的文件
	 * 
	 * @throws QiniuException
	 */
	@Test
	public void testDelete() throws QiniuException {
		QiniuHelpler.getInstance().deleteFile("temp/test.zip");
	}

	@Test
	public void downloadUrl() throws QiniuException {
		System.out.println(QiniuHelpler.getInstance().downloadUrl("temp/test.zip"));
	}

	@Test
	public void rename() throws QiniuException {
		QiniuHelpler.getInstance().rename("temp/test.zip", "test123.zip");
	}

	/**
	 * 查询资源
	 * 
	 * @throws QiniuException
	 */
	@Test
	public void stat() {
		FileInfo info = QiniuHelpler.getInstance().stat("temp/test.zip");
		System.out.println(info == null ? "不存在" : info);
	}

	/**
	 * 单块上传，合并七牛云上的分块为文件，实现断点分块续传
	 * 
	 * @throws Exception
	 */
	@Test
	public void resumeUpload() throws Exception {// 支持byte字节的断点分块上传
		long start = System.currentTimeMillis();

		Resource resource = new ClassPathResource("纽约-廖逸君作品《Walking in My Shoes》.mp4");

		ResumeUploaderExt uploader = QiniuHelpler.getInstance().resumeUploaderExt(QiniuHelpler.UploadDir.VEDIO, resource.getFilename(), resource.getFile().length(),
				resource.getFile().lastModified());

		InputStream inputStream = resource.getInputStream();
		// 每次分4mb上传
		byte[] part = new byte[Constants.BLOCK_SIZE];
		while (!uploader.finishUploadBlock()) {
			inputStream.read(part, 0, Constants.BLOCK_SIZE);

			if (uploader.uploadBlock(part)) {
				uploader.blacks2File();
			}
		}

		System.out.println(System.currentTimeMillis() - start);
	}

	public static byte[] getBytes(File file) {
		byte[] buffer = null;
		try {
			FileInputStream fis = new FileInputStream(file);
			ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
			byte[] b = new byte[1000];
			int n;
			while ((n = fis.read(b)) != -1) {
				bos.write(b, 0, n);
			}
			fis.close();
			bos.close();
			buffer = bos.toByteArray();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return buffer;
	}
}
