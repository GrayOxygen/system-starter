package com.shineoxygen.work.base.utils;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * 
 * @author 王辉阳
 * 
 * @date 2015年10月12日 下午4:42:23
 * 
 * @Description 请求响应工具类
 */
public class RequestResponseUtil {
	public static HttpServletRequest getCurrentRequest() {
		RequestAttributes reqAttrs = RequestContextHolder.getRequestAttributes();
		if (null == reqAttrs) {
			return null;
		}
		return ((ServletRequestAttributes) reqAttrs).getRequest();
	}

}
