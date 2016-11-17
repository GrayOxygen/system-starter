package com.shineoxygen.work.base.utils.msg;

import java.io.Serializable;

/**
 * 
 * @author 王辉阳
 * @date 2016年11月8日 下午11:23:38
 * @Description 消息基类：邮件，短信等消息继承
 */
public class Msg implements Serializable {
	private String content;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
