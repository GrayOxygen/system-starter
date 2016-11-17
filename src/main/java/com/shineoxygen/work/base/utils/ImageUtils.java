package com.shineoxygen.work.base.utils;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

/**
 * 图像工具
 * 
 * @author KelvinZ
 * 
 */
public class ImageUtils {
	public static final String JPG = "JPEG";
	public static final String GIF = "gif";
	public static final String PNG = "png";
	public static final String BMP = "bmp";

	/**
	 * 缩放图片
	 * 
	 * @param source
	 *            源图
	 * @param width
	 *            目标图片宽
	 * @param height
	 *            目标图片高
	 * @param isLongSideIn
	 *            最长边在范围内，true则长边计算比例，false则短边计算比例
	 * @param padding
	 *            如果为true，则原图大小不变，留空处填色
	 * @throws IOException
	 */
	public static Image resize(InputStream source, int width, int height, boolean isLongSideIn, boolean padding) throws IOException {
		double ratio = 0.0; // 缩放比
		BufferedImage bi = ImageIO.read(source);
		Image itemp = bi.getScaledInstance(width, height, Image.SCALE_SMOOTH);

		if ((bi.getHeight() > height) || (bi.getWidth() > width)) { // 计算收缩比
			if (bi.getHeight() > bi.getWidth() && height > 0) {
				if (isLongSideIn) {
					ratio = (new Integer(height)).doubleValue() / bi.getHeight();
				} else {
					ratio = (new Integer(width)).doubleValue() / bi.getWidth();
				}
			} else {
				if (isLongSideIn) {
					ratio = (new Integer(width)).doubleValue() / bi.getWidth();
				} else {
					ratio = (new Integer(height)).doubleValue() / bi.getHeight();
				}
			}
			AffineTransformOp op = new AffineTransformOp(AffineTransform.getScaleInstance(ratio, ratio), null);
			// System.out.println(bi.getWidth() + "x" + bi.getWidth() +
			// "| ratio: " + ratio);
			itemp = op.filter(bi, null);
		}
		if (padding) { // 需要补
			BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			Graphics2D g = image.createGraphics();
			g.setColor(Color.white);
			g.fillRect(0, 0, width, height);
			if (width == itemp.getWidth(null))
				g.drawImage(itemp, 0, (height - itemp.getHeight(null)) / 2, itemp.getWidth(null), itemp.getHeight(null), Color.white, null);
			else
				g.drawImage(itemp, (width - itemp.getWidth(null)) / 2, 0, itemp.getWidth(null), itemp.getHeight(null), Color.white, null);
			g.dispose();
			itemp = image;
		}
		return itemp;
	}

	/**
	 * 图片缩放
	 * 
	 * @param source
	 * @param target
	 * @param width
	 * @param height
	 * @param format
	 *            不能为null
	 * @param padding
	 * @throws IOException
	 */
	public static void resize(InputStream source, OutputStream target, int width, int height, String format, boolean isLongSideIn, boolean padding) throws IOException {
		Image image = resize(source, width, height, isLongSideIn, padding);
		ImageIO.write((BufferedImage) image, format, target);
	}

	/**
	 * 图片缩放
	 * 
	 * @param source
	 * @param target
	 * @param width
	 * @param height
	 * @param format
	 *            为null自动判断
	 * @param padding
	 * @throws IOException
	 */
	public static void resize(File source, File target, int width, int height, String format, boolean isLongSideIn, boolean padding) throws IOException {
		if (format == null)
			format = ImageUtils.getFormatName(source);
		InputStream ins = FileUtils.openInputStream(source);
		try {
			Image image = resize(ins, width, height, isLongSideIn, padding);
			ImageIO.write((BufferedImage) image, format, target);
		} finally {
			IOUtils.closeQuietly(ins);
		}
	}

	/**
	 * 
	 * @author 王辉阳
	 * @date 2015年11月5日 下午12:43:48
	 * @param newWidth
	 *            单位为像素，需重新生成的图片的宽度
	 * @param newHeight
	 *            单位为像素，需重新生成的图片的高度
	 * @param extensions
	 *            图片后缀名，如"jpg" "png"
	 * @param preImage
	 *            需要重新调整大小的图片的输入流
	 * @param os
	 *            将调整大小后的图片写到流中，最好是文件流，以免生成图片有问题
	 */
	public static void resize(int newWidth, int newHeight, String extensions, InputStream preImage, OutputStream os) {
		// 将图片转成正方形图片
		BufferedImage prevImage;
		try {
			prevImage = ImageIO.read(preImage);
			BufferedImage image = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_BGR);
			Graphics graphics = image.createGraphics();
			graphics.drawImage(prevImage, 0, 0, newWidth, newHeight, null);
			ImageIO.write(image, extensions, os);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Get Image type. Caution: will invoke close if o is an InputStream
	 * 
	 * @param o
	 *            an <code>Object</code> to be used as an input source, such as
	 *            a <code>File</code>, readable <code>RandomAccessFile</code>,
	 *            or <code>InputStream</code>.
	 * 
	 * @return gif/JPEG/png/bmp
	 */
	public static String getFormatName(Object o) {
		ImageInputStream iis = null;
		try {
			// Create an image input stream on the image
			iis = ImageIO.createImageInputStream(o);
			// Find all image readers that recognize the image format
			Iterator<ImageReader> iter = ImageIO.getImageReaders(iis);
			if (!iter.hasNext()) {
				// No readers found
				return null;
			}
			// Use the first reader
			ImageReader reader = iter.next();
			// Return the format name
			return reader.getFormatName();
		} catch (IOException e) {
			// Do nothing
		} finally {
			// Close stream
			IOUtils.closeQuietly((Reader) iis);
			iis = null;
		}
		// The image could not be read
		return null;
	}

	public static void main(String[] args) throws Exception {
		long begin, end; // 统计时间
		String path = "D:\\beacon\\搜哒\\数据准备\\商场地图\\北京\\北京银泰中心\\B1.png";
		// String path = ImageUtils.class.getResource("/").getPath();
		File source = new File(path);
		String format = "jpg";
		if (!source.exists()) {
			System.out.println("File not exits: " + source.getAbsolutePath());
		} else {
			begin = System.currentTimeMillis();
			format = getFormatName(source);
			end = System.currentTimeMillis();
			System.out.println(String.format("It's a %s file, cost %s", format, end - begin));
		}

		//
		OutputStream os = new ByteArrayOutputStream();
		ImageUtils.resize(FileUtils.openInputStream(source), os, 720, 720, "png", false, false);
		os.close();
		System.out.println("图像流缩放测试完成");

		//
		File target = new File(path + "." + format);
		ImageUtils.resize(source, target, 720, 720, "png", false, false);
		System.out.println("图像文件缩放测试完成");
	}

}
