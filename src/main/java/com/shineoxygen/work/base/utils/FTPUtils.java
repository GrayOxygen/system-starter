package com.shineoxygen.work.base.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.SocketException;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <pre>
 * 该工具提供往FTP服务器上传、删除、下载文件的功能。 需要在配置文件设置FTP参数。
 * 
 * @author KelvinZ改
 * 
 */
public class FTPUtils {
	protected final static Logger log = LoggerFactory.getLogger(FTPUtils.class);

	private static String ftpServers =  SystemConfig.getProperty("ftp.sever"); // 可用|分隔多个服务器地址
	private static int ftpPort = Integer.parseInt(SystemConfig.getProperty("ftp.port"));
	private static String ftpUser = SystemConfig.getProperty("ftp.user");
	private static String ftpPassword = SystemConfig.getProperty("ftp.password");
	private static String mode = SystemConfig.getProperty("ftp.mode");

	private static int BUF_SIZE = 1024 * 1024;
	private static ThreadLocal<FTPClient> ftpClientThreadLocal = new ThreadLocal<FTPClient>();

	/**
	 * 获得已登录的FTP连接
	 * 
	 * @param url
	 * @param port
	 * @param username
	 * @param password
	 * @param mode
	 * @return
	 * @throws SocketException
	 * @throws IOException
	 */
	public static FTPClient getFTPClient(String url, int port, String username, String password, String mode) throws SocketException, IOException {
		FTPClient ftpClient = ftpClientThreadLocal.get();
		if (ftpClient != null && ftpClient.isAvailable()) {
			try {
				ftpClient.changeToParentDirectory();
				return ftpClient;
			} catch (Exception ex) {
				// do nothing
			}
		} else {
			if (ftpClient != null) {
				ftpClient.disconnect();
			}
			ftpClientThreadLocal.remove();
			ftpClient = null;
		}
		long begin = System.currentTimeMillis();
		ftpClient = new FTPClient();
		// ftpClient.setConnectTimeout(10000);
		// ftpClient.setDataTimeout(10000);
		ftpClient.connect(url, port);// 连接FTP服务器//如果采用默认端口，可以使用ftp.connect(url)的方式直接连接FTP服务器
		if (ftpClient.login(username, password)) {
			if (log.isDebugEnabled()) {
				log.debug("Login success");
			}
		} else {
			throw new SocketException("Login failed");
		}

		if (!FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
			ftpClient.disconnect();
			throw new SocketException("Reply not positive");
		}
		if (StringUtils.equals(mode, "0")) {
			// 传输模式使用passive被动模式(FTPClient默认为主动模式)
			ftpClient.enterLocalPassiveMode();
			System.out.println("FTP PassivePort:" + ftpClient.getPassivePort());
		}
		ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
		long end = System.currentTimeMillis();
		System.out.println("getFTPClient: " + (end - begin));
		ftpClientThreadLocal.set(ftpClient);
		return ftpClient;
	}

	/**
	 * 上传文件，将文件存放在FTP服务器参数ftp.img.path设置的路径下
	 * 
	 * @param input
	 *            文件流
	 * @param filename
	 *            文件名，例如：uuid.jpg
	 * @param oldFilename
	 *            旧文件名，如果指定，文件会被删除，绝对路径：/app/img1/logo/201504/olduuid.jpg
	 * @return 原样返回参数filename
	 * @throws Exception
	 */
	public static String upload(InputStream input, String filename, String oldFilename) throws Exception {
		boolean flag = false;
		if (StringUtils.isNotBlank(ftpServers)) {
			String ftpServer[] = ftpServers.split("\\|");
			for (int i = 0; i < ftpServer.length; i++) {
				if (StringUtils.isNotBlank(ftpServer[i])) {
					flag = uploadFile(ftpServer[i], ftpPort, ftpUser, ftpPassword, mode, input, filename, oldFilename);
				}
			}
		}
		if (flag) {
			return filename;
		} else {
			return null;
		}
	}

	/**
	 * 删除文件
	 * 
	 * @param filename绝对路径，例如：/app/img1/logo/201504/uuid.jpg
	 * @return 成功或者失败
	 */
	public static boolean delete(String filename) {
		boolean flag = false;
		if (StringUtils.isNotBlank(ftpServers)) {
			String ftpServer[] = ftpServers.split("\\|");
			for (int i = 0; i < ftpServer.length; i++) {
				if (StringUtils.isNotBlank(ftpServer[i])) {
					flag = deleteFile(ftpServer[i], ftpPort, ftpUser, ftpPassword, filename);
				}
			}
		}
		return flag;
	}

	/**
	 * 下载文件
	 * 
	 * @param path
	 * @param filename
	 *            FTP文件路径，绝对路径
	 * @return
	 */
	public static InputStream download(String filename) {
		return downloadFile(ftpServers, ftpPort, ftpUser, ftpPassword, mode, filename);
	}

	/**
	 * 上传文件
	 * 
	 * @param url
	 *            FTP服务地址
	 * @param port
	 *            FTP服务端口
	 * @param username
	 *            FTP服务登录帐号
	 * @param password
	 *            FTP服务登录密码
	 * @param mode
	 *            传输模式
	 * @param input
	 *            文件流
	 * @param filename
	 *            保存路径（绝对路径），例如：/app/img1/logo/201504
	 * @param oldFile
	 *            旧文件的保存地址（绝对路径），例如：/app/img1/logo/201504/uuid.jpg
	 * 
	 * @return
	 * @throws Exception
	 */
	private static boolean uploadFile(final String url, final int port, final String username, final String password, final String mode, final InputStream input,
			final String filename, final String oldFile) throws Exception {
		FTPClient ftp = getFTPClient(url, port, username, password, mode);
		long begin = System.currentTimeMillis();
		ftp.setBufferSize(BUF_SIZE);

		// filename = StringUtils.replace(filename, "/+", "/");
		// oldFile = StringUtils.replace(oldFile, "/+", "/");

		if (StringUtils.isNotBlank(oldFile)) {
			if (log.isInfoEnabled()) {
				log.info("Del oldFile: " + oldFile);
			}
			ftp.deleteFile(oldFile);
		}
		ensureDirecroty(filename, ftp);
		boolean b = ftp.storeFile(filename, input);
		if (log.isDebugEnabled()) {
			log.debug("uploadFile: " + filename);
		}
		long end = System.currentTimeMillis();
		System.out.println("上传用时ms：" + (end - begin));
		if (!b) {
			log.error("上传文件不成功!" + b);
			return false;
		}
		return true;
	}

	/**
	 * 删除FTP服务器上的文件
	 * 
	 * @param url
	 *            FTP服务地址
	 * @param port
	 *            FTP服务端口
	 * @param username
	 *            FTP服务登录帐号
	 * @param password
	 *            FTP服务登录密码
	 * @param filename
	 *            绝对路径，例如：/app/img1/logo/201504/uuid.jpg
	 * 
	 * @return
	 */
	private static boolean deleteFile(String url, int port, String username, String password, String filename) {
		try {
			if (StringUtils.isNotBlank(filename)) {
				FTPClient ftp = getFTPClient(url, port, username, password, mode);
				ftp.deleteFile(filename);
			}
			return true;
		} catch (IOException e) {
			log.error("Error", e);
		}
		return false;
	}

	/**
	 * 递归创建指定文件所需的远程服务器目录，如果只有路径，请确保以‘/’结尾
	 * 
	 * @param file
	 *            远程服务器文件绝对路径，例如：/app/img1/201504/uuid.jpg
	 * @param ftpClient
	 *            FTPClient对象
	 * @return 目录创建是否成功
	 * @throws IOException
	 */
	public static boolean ensureDirecroty(String file, FTPClient ftpClient) throws IOException {
		boolean status = true;
		final String directory = file.substring(0, file.lastIndexOf("/") + 1);
		String s = new String(directory.getBytes("UTF-8"), "iso-8859-1");
		if (!directory.equalsIgnoreCase("/") && !ftpClient.changeWorkingDirectory(s)) {
			// 如果远程目录不存在，则递归创建远程服务器目录
			final String[] dirs = StringUtils.split(directory, '/');
			for (String dir : dirs) {
				final String subDirectory = new String(dir.getBytes("UTF-8"), "iso-8859-1");
				if (!ftpClient.changeWorkingDirectory(subDirectory)) {
					if (ftpClient.makeDirectory(subDirectory)) {
						ftpClient.changeWorkingDirectory(subDirectory);
					} else {
						log.info("创建目录失败:" + subDirectory);
						status = false;
						break;
					}
				}
			}
		}
		ftpClient.changeWorkingDirectory("/");
		return status;
	}

	/**
	 * 下载文件
	 * 
	 * @param url
	 *            FTP服务地址
	 * @param port
	 *            FTP服务端口
	 * @param username
	 *            FTP登录帐号
	 * @param password
	 *            FTP登录密码
	 * @param mode
	 * @param filename
	 *            文件名绝对路径
	 * 
	 * @return 文件流，外面负责关闭
	 */
	private static InputStream downloadFile(String url, int port, String username, String password, String mode, String filename) {
		InputStream inputStream = null;
		try {
			FTPClient ftp = getFTPClient(url, port, username, password, mode);
			ftp.setBufferSize(BUF_SIZE);
			String path = StringUtils.substringBefore(filename, "/");
			ftp.changeWorkingDirectory(path);
			inputStream = ftp.retrieveFileStream(filename);
			if (log.isDebugEnabled()) {
				log.debug("downloadFile: " + filename);
			}
		} catch (IOException e) {
			log.error("Error", e);
		}
		return inputStream;
	}

}
