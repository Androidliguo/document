package com.lg.document.model;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
/**
 * 由于Message和User这个类之间的关系是
 * 多对多的关系。那么在存储它们之间的关系的时候
 * 我们就不会存储它们之间的相互的引用
 * 而是使用一个类来维持它们之间的关系
 * 这是要注意的。
 * 这和User和Department之间是
 * 完全不一样的。这是要注意的。
 * 在维护关系的时候，尽量不要在一的这一方进行
 * 关系的维护，而尽量要在多的一方进行关系的维护，
 * 这是要注意的。
 * 由于userMessage和Message之间是多对一的关系
 * 那么在进行维护的时候，就由多的一方来进行关系的维护
 * 这是要注意的。
 *
 */
@Entity
@Table(name="t_msg")
public class Message {
	private int id;
	private String title;
	private String content;
	private Date createDate;
	/**
	 * 发送信件的用户。也就是我们的登录用户
	 */
	private User user;
	
	
	
	/**
	 * 这里需要注意的问题是什么呢？
	 * 这里的话，是joinColumn,而不是Column。
	 * Column是起别名的意思。这是要注意的。
	 * @return
	 */
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
	public Message(String title, String content, Date createDate,User user) {
		super();
		this.title = title;
		this.content = content;
		this.createDate = createDate;
		this.user=user;
		
	}
	
	
	public Message(){}
	public Message(int id, String title, String content, Date createDate) {
		super();
		this.id = id;
		this.title = title;
		this.content = content;
		this.createDate = createDate;
	}
	
	
	
	

}
