package com.shineoxygen.work.temp.controller;

import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.shineoxygen.work.base.controller.BaseAdminController;
import com.shineoxygen.work.base.model.ResultObject;
import com.shineoxygen.work.base.utils.pupload.Plupload;
import com.shineoxygen.work.base.utils.qiniu.QiniuHelpler;
import com.shineoxygen.work.base.utils.qiniu.QiniuHelpler.UploadDir;
import com.shineoxygen.work.base.utils.qiniu.ResumeUploaderExt;
import com.shineoxygen.work.base.utils.qiniu.UploadException;

/**
 * 
 * @author 王辉阳
 * @date 2016年11月16日 下午10:17:44
 * @Description 七牛云演示
 */
@RequestMapping("tempTest")
@Controller
@SuppressWarnings("all")
public class TempTestController extends BaseAdminController {
	@RequestMapping("/upload2QiniuPage")
	public String updateSelfPage(ModelMap modelMap, HttpServletRequest req) {
		QiniuHelpler qiniuUtils = QiniuHelpler.getInstance();
		modelMap.put("uptoken_url", qiniuUtils.getToken());
		modelMap.put("expireTime", qiniuUtils.getExpireTime());
		modelMap.put("startTime", qiniuUtils.getStartTime());
		modelMap.put("blockSize", qiniuUtils.getBlockSize());

		return "tempTest/upload2QiniuPage";
	}

	@RequestMapping("/getKey")
	public String getKey(ModelMap modelMap, HttpServletRequest req) {
		return "tempKey";
	}

	@RequestMapping("/upToken")
	public @ResponseBody String upToken(ModelMap modelMap, HttpServletResponse res) {
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("uptoken", QiniuHelpler.getInstance().getToken());
		return jsonObj.toString();
	}

	/**
	 * puload+qiniu实现断点续传： 前端分块到后台，后台每次上传一个分块到七牛
	 * 
	 * @param id
	 * @param needUpdateNewFile
	 * @param pupload
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/uploadFile")
	public @ResponseBody ResultObject uploadFile(String id, Integer needUpdateNewFile, Plupload plupload) throws Exception {
		long cur = System.currentTimeMillis();

		// 临时文件存储上传的分片内容，分片都传到后台后，更改临时文件名即最终的上传文件，定时删除临时文件
		if (plupload.getChunks() <= 0) {
			return ResultObject.errResult("参数chunks错误！");
		}

		try {
			ResumeUploaderExt uploader = QiniuHelpler.getInstance().resumeUploaderExt(UploadDir.VEDIO, plupload);
			InputStream inputStream = plupload.getFile().getInputStream();
			// 上传单块
			if (uploader.uploadBlock(inputStream) && plupload.getChunk() == plupload.getChunks() - 1) {
				// 单块已上传完，合并七牛云的块为文件
				String url = uploader.blacks2File();
				System.out.println(url);
			}
		} catch (UploadException e) {
			return ResultObject.errResult(e.getMessage());
		}

		System.out.println("last:" + System.currentTimeMillis());
		return ResultObject.sucResult("上传成功！");
	}

}
