package com.shineoxygen.work.base.utils;

import org.springframework.web.multipart.MultipartFile;

/**
 * 文件上传工具类
 */
public class FileUploadUtils {

	public static String uploadFile(MultipartFile file, String oldFilename) throws Exception {
		final String fileExt = com.saysth.commons.utils.io.FileUtils.getFilenameExtension(file.getOriginalFilename());
		final String fileName = StrUtils.getShortUUID() + "." + fileExt;
		String path = ImgUploader.upload(file.getInputStream(), ImgUploader.SavePath.gameBg, fileName, oldFilename);
		return path;
	}
}
