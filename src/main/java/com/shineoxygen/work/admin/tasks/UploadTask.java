package com.shineoxygen.work.admin.tasks;

import java.io.File;

import javax.servlet.ServletContext;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ServletContextAware;

import com.shineoxygen.work.base.utils.FileUtils;

/**
 * 
 * @author 王辉阳
 * @date 2016年11月16日 下午11:50:52
 * @Description 定期删除存在两天的临时文件
 */
@Component
public class UploadTask implements ServletContextAware {
	private ServletContext servletContext;

	@Scheduled(fixedRate = 1000)
	public void clearTempQiniuFile() {
		String path = servletContext.getRealPath("/") + "qiniuChunk";

		FileUtils.deleteFiles(new File(path), 3600 * 24 * 2 * 1000, false);
	}

	@Override
	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}
}
