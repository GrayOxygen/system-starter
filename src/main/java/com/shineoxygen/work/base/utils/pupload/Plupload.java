package com.shineoxygen.work.base.utils.pupload;

import org.springframework.web.multipart.MultipartFile;

import com.qiniu.common.Constants;

/**
 * 
 * @author 王辉阳
 * @date 2016年11月16日 下午10:15:51
 * @Description pupload分块上传对象
 */
public class Plupload {
	// pupload插件开启分块上传时所传参数
	private Integer chunk;
	private Integer chunks;
	private String name;// 文件名，也只是七牛云上保存的文件名
	private MultipartFile file;// 单块文件

	// 自定义接口所需便于qiniu分块上传的参数
	private Integer fileTotalSize;
	private Long lastModifiedTime;
	private Integer blockSize = Constants.BLOCK_SIZE;// 七牛云指定的分块大小，见七牛java sdk

	public Integer getChunk() {
		return chunk;
	}

	public void setChunk(Integer chunk) {
		this.chunk = chunk;
	}

	public Integer getChunks() {
		return chunks;
	}

	public void setChunks(Integer chunks) {
		this.chunks = chunks;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public MultipartFile getFile() {
		return file;
	}

	public void setFile(MultipartFile file) {
		this.file = file;
	}

	public Integer getFileTotalSize() {
		return fileTotalSize;
	}

	public void setFileTotalSize(Integer fileTotalSize) {
		this.fileTotalSize = fileTotalSize;
	}

	public Long getLastModifiedTime() {
		return lastModifiedTime;
	}

	public void setLastModifiedTime(Long lastModifiedTime) {
		this.lastModifiedTime = lastModifiedTime;
	}

	public Integer getBlockSize() {
		return blockSize;
	}

	public void setBlockSize(Integer blockSize) {
		this.blockSize = blockSize;
	}

}
