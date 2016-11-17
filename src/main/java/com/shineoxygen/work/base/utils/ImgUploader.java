package com.shineoxygen.work.base.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.saysth.commons.utils.DateUtils;
import com.saysth.commons.utils.io.FileUtils;

/**
 * <pre>
 * 图片文件上传封装 *************微标各类图片规范************ [分类] 宽x高 允许最大 统一目录 场景例图 640*1008
 * 25k app 模版例图 640*1008 25k template 风格例图 640*1008 25k style
 *
 * 备注： 如上述图片存在?，请联系负责人确定好图片大小； 如一边为空，例如“720x”，表示按宽720进行等比例缩放；
 * 形如[720x480]，表示宽<=720，高<=480，然后等比缩放；
 *
 * @author KelvinZ
 */
public class ImgUploader {
	protected final static Logger log = LoggerFactory.getLogger(ImgUploader.class);

	public static interface SavePath {
		String image = "image";
		String postImg = "postImg";
		String account = "account";
		String gameBg = "gameBg";
		String logo = "logo";
	}

	private final static String staticBucket = SystemConfig.getProperty("static.bucket"); // 例如：static[1,2,3....]
	private final static String staticUrl = SystemConfig.getProperty("static.url", ""); // 例如：http://119.147.54.153:8090
	private static String staticImgRoot = SystemConfig.getProperty("static.img.root", ""); // 例如：/soda-dev
	private static String staticImgPath = SystemConfig.getProperty("static.img.path"); // 例如：
																						// /app/img1
	private final static String staticTemplatePath = SystemConfig.getProperty("static.template.path");

	static {
		if (!staticImgPath.endsWith("/")) {
			staticImgPath = staticImgPath + "/";
		}
	}

	/**
	 * 上传文件，将文件存放在图片服务器规划的路径下
	 *
	 * @param input
	 *            文件流
	 * @param savePath
	 *            保存的相对路径，相对路径不要以‘/’开头哦，例如： mall/logo
	 * @param filename
	 *            文件名，例如：uuid.jpg
	 * @param oldDbFilename
	 *            旧文件名，如果指定，文件会被删除，绝对路径：/app/img1/mall/logo/2015/0413/olduuid.jpg
	 * @return
	 * @throws Exception
	 */
	public static String upload(InputStream input, String savePath, String filename, String oldDbFilename) throws Exception {
		final String date = DateUtils.format(new Date(), "/yyyy/MMdd/");
		final String filePath = FTPUtils.upload(input, staticImgRoot + staticImgPath + savePath + date + filename, staticImgRoot + StringUtils.substringAfter(oldDbFilename, "@"));
		final String path = StringUtils.replace(filePath, staticImgRoot, "", 1);
		return staticBucket + "@" + path;
	}

	/**
	 * 删除文件
	 *
	 * @param dbFilename
	 * @return
	 */
	public static boolean delete(String dbFilename) {
		return FTPUtils.delete(staticImgRoot + StringUtils.substringAfter(dbFilename, "@"));
	}

	/**
	 * 获取图片Web访问路径，例如传入:
	 * static@/app/img1/uuid.jpg，返回：http://static.welineup.com/app/img1/uuid.jpg
	 *
	 * @param dbFilename
	 *            存储在DB的文件名
	 * @return
	 */
	public static String getResUrl(String dbFilename) {
		if (StringUtils.startsWithIgnoreCase(dbFilename, "http://")) {
			return dbFilename;
		} else {
			dbFilename = StringUtils.replace(dbFilename, "@", "@" + staticImgRoot, 1);
			dbFilename = dbFilename.replace(staticBucket + "@", staticUrl.replace("{bucket}", staticBucket));
			return dbFilename;
		}
	}

	public static void main(String[] args) throws IOException, Exception {
		// String s = "http://110.76.39.140/logo/mall/149.jpg";
		// System.out.println(getResUrl(s));

		// 测试上传文件
		File upFile = new File("D:\\test.jpg");
		String s = upload(org.apache.commons.io.FileUtils.openInputStream(upFile), "test", "test1.jpg", null);
		System.out.println(s);
		System.out.println(getResUrl(s));
	}

}
