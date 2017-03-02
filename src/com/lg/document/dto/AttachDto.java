package com.lg.document.dto;
import java.io.File;
/**
 * 注意这是一个Dto的对象。
 * @author 李果
 *
 */
public class AttachDto {
	//这里为什么需要使用数组呢？因为可能会有多个的附件，所以在这里的话，我们使用了数组。
	private File[] atts;
	private String[] attsContentType;
	private String[] attsFileName;
	/**
	 * 新文件上传到的路径
	 */
	private String uploadPath;
	
	public String getUploadPath() {
		return uploadPath;
	}
	public void setUploadPath(String uploadPath) {
		this.uploadPath = uploadPath;
	}

	/**
	 * 判断是否有附件
	 */
	private boolean hasAttach;
	
	public AttachDto(boolean hasAttach) {
		this.hasAttach = hasAttach;
	}
	public boolean isHasAttach() {
		return hasAttach;
	}
	public void setHasAttach(boolean hasAttach) {
		this.hasAttach = hasAttach;
	}
	public File[] getAtts() {
		return atts;
	}
	public void setAtts(File[] atts) {
		this.atts = atts;
	}
	public String[] getAttsContentType() {
		return attsContentType;
	}
	public void setAttsContentType(String[] attsContentType) {
		this.attsContentType = attsContentType;
	}
	public String[] getAttsFileName() {
		return attsFileName;
	}
	public void setAttsFileName(String[] attsFileName) {
		this.attsFileName = attsFileName;
	}
	public AttachDto(File[] atts, String[] attsContentType, String[] attsFileName,String uploadPath) {
		super();
		this.atts = atts;
		this.attsContentType = attsContentType;
		this.attsFileName = attsFileName;
		this.uploadPath=uploadPath;
		/**
		 * 注意这里的话，我们一定要记得将hasAttach设置为true;
		 * 否则的话，就默认为false.这个时候，是不会上传附件的。
		 */
		this.hasAttach=true;
	}
	
	public AttachDto(){}

}
