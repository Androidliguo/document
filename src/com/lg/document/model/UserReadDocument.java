package com.lg.document.model;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
/**
 * 这张表的主要的作用是什么呢？
 * 这张表的主要作用是用来存储已经被阅读过的公文。
 * 主要公文是被阅读过了的，当进行load方法的时候，那么就证明已经加载了，读过 了。那么
 * 就存储在这张表中。
 * @author 李果
 *
 */
@Entity
@Table(name="t_user_read_doc")
public class UserReadDocument{

	/*private int id;
	private User user;
	private Document document;
	
	@Id
	@GeneratedValue
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	@ManyToOne
	@JoinColumn(name="user_id")
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	
	@ManyToOne
	@JoinColumn(name="doc_id")
	public Document getDocument() {
		return document;
	}
	public void setDocument(Document doc) {
		this.document = doc;
	}
	public UserReadDocument(User user, Document doc) {
		super();
		this.user = user;
		this.document = doc;
	}
	public UserReadDocument() {
		super();
	}
	*/
	private int id;
	private User user;
	private Document document;
	
	@Id
	@GeneratedValue
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	@ManyToOne
	@JoinColumn(name="user_id")
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	@ManyToOne
	@JoinColumn(name="doc_id")
	public Document getDocument() {
		return document;
	}
	public void setDocument(Document document) {
		this.document = document;
	}
	
	
}
