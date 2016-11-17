package com.shineoxygen.work.base.utils.msg.sem;

import com.shineoxygen.work.base.utils.msg.Msg;

/**
 * 
 * @author 王辉阳
 * @date 2016年11月8日 下午11:24:46
 * @Description 具体邮件消息 TODO 不同邮件服务商的邮件消息POJO继承该类
 */
public class EmailMsg extends Msg {
	private String from;// 发送者邮件地址
	private String fromName;// 发送方名称
	private String to;// 目标邮件地址
	private String subject;// 标题

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getFromName() {
		return fromName;
	}

	public void setFromName(String fromName) {
		this.fromName = fromName;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

}
