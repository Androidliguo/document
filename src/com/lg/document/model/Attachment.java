package com.lg.document.model;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
@Entity
@Table(name="t_attach")
public class Attachment {
	private int id;
	private String newName;
	private String oldName;
	private String contentType;
	private String type;
	private long size;
	private Date createDate;
	/**
	 * 这附件是属于哪一个公文的
	 */
	private Document document;
	/**
	 * 这个附件是属于哪一条message的
	 */
	private Message message;
	@Id
	@GeneratedValue
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	/**
	 * 附件的新的名称。这个名称是不会重复的。是以uuid或者是以时间的毫秒数来获得的。如果要起别名的话，就使用column(name="")
	 * 这个属性。也就是在数据库中的表的字段的名称
	 * @return
	 */
	@Column(name="new_name")
	public String getNewName() {
		return newName;
	}
	public void setNewName(String newName) {
		this.newName = newName;
	}
	
	/**
	 * 附件的旧的名称
	 * @return
	 */
	@Column(name="old_name")
	public String getOldName() {
		return oldName;
	}
	public void setOldName(String oldName) {
		this.oldName = oldName;
	}
	
	/**
	 * 附件的文件类型名称
	 * @return
	 */
	@Column(name="content_type")
	public String getContentType() {
		return contentType;
	}
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	
	/**
	 * 附件的后缀名
	 * @return
	 */
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	/**
	 * 附件的大小
	 * @return
	 */
	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}
	@JoinColumn(name="create_date")
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	/**
	 * 这个附件是属于那哪一个公文的。在这里的话，肯定是引入引用对象的id的/
	 * @return
	 */
	@ManyToOne
	@JoinColumn(name="doc_id")
	public Document getDocument() {
		return document;
	}
	public void setDocument(Document document) {
		this.document = document;
	}
	/**
	 * 这个附件是属于哪一条消息的
	 * @return
	 */
	@ManyToOne
	@JoinColumn(name="msg_id")
	public Message getMessage() {
		return message;
	}
	public void setMessage(Message message) {
		this.message = message;
	}
	
	
	
	

}
