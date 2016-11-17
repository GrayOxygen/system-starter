package com.shineoxygen.work.base.utils.qiniu;

public class UploadException extends RuntimeException {
	static final long serialVersionUID = -3387516993124229948L;
	private String message;

	public UploadException() {
	}

	public UploadException(String message) {
		super(message);
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
