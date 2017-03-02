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
@Table(name="t_doc")
public class Document {
	private int id;
	private String title;
	private String content;
	private Date createDate;
	/**
	 * 需要确定是哪一个用户发送的。然后就可以找到是哪一个部门发的了。
	 * 这个user指的是这条公文是哪一个用户发的。由于一个用户可可以
	 * 发很多的公文，所以这里的话，使用了manytoone这个属性。
	 * 其实发公文的话，就是我们的登录用户。
	 * 
	 */
	private User user;
	
	@ManyToOne
	@JoinColumn(name="user_id")
	public User getUser() {
		return user;
	}
	
	public void setUser(User user) {
		this.user = user;
	}
	@Id
	@GeneratedValue
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	@Column(name="content",columnDefinition="text")
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	@Column(name="create_date")
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Document( String title, String content, Date createDate) {
		this.title = title;
		this.content = content;
		this.createDate = createDate;
	}

	public Document(String title, String content, Date createDate, User user) {
		super();
		this.title = title;
		this.content = content;
		this.createDate = createDate;
		this.user = user;
	}

	public Document() {
		super();
	}

	public Document(int id, String title, String content, Date createDate) {
		super();
		this.id = id;
		this.title = title;
		this.content = content;
		this.createDate = createDate;
	}
	
	
	
	
	

}
