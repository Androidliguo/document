package com.lg.document.model;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
/**
 * 我应该如何来利用这个类呢？在MessageService中，我们或许可以循环遍历这个类
 * 然后将所有的关系都存储到这个表中
 * 我们在需要的时候，就可以取出来了。这是要注意的
 * @author 李果
 *
 */
/**
 * 这张表的作用是什么呢？
 * 这张表的作用主要是用来维护哪些消息是
 * 可以发送给哪些用户的。
 * 这是要注意的。
 * 这里并没有说明这条消息是谁发的。
 * 这条消息是谁发的，因为肯定是登录用户才能够发message
 * 所以消息是谁发的，已经在message中说明了。这是要注意的。
 * 
 */

/**
 * 这个主要是负责处理接受到的信件
 * @author 李果
 *
 */

@Entity
@Table(name="t_user_message")
public class UserMessage {
	private int id;
	/**
	 * 由多对多的关系转化为一对多的关系的时候，我们一般会引用
	 * 这俩个对象的引用。或者是id的话也可以。不过，这里的话，
	 * 使用的是基于对象的引用。我发现，其实在很多的情况下，使用的都是基于对象的引用。
	 */
	private User user;
	private Message message;
	/**
	 * isRead表示的是这封信件是否已经是被读的了
	 * 0表示的是还没有被读
	 * 1表示的是已经读了
	 * 刚刚被保存的时候，肯定是还没有读的。
	 */
	private int isRead;
	
	@Id
	@GeneratedValue
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	/**
	 * 对于manytoone和oneTomany，我们可以怎么来理解呢？
	 * 我们就可以使利用这张表的id和引用的表的id来进行比较就可以了。
	 * 就可以得到onetomany还是manytoone了。
	 * 这个公文可以让那你接收，另外一个公文也可以让你接收。
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
	@ManyToOne
	@JoinColumn(name="msg_id")
	public Message getMessage() {
		return message;
	}
	public void setMessage(Message message) {
		this.message = message;
	}
	/**
	 * 
	 * 该消息是被发送的还是被接受的。
	 * 如果是被发送的话，如果删除了该信息的话，那么其他人接受到的信息也会一并被删除
	 * 如果是被接受的话，只删除自己接收的这一部分
	 * @return
	 */
	/**
	 * 该消息是否已经被被读了
	 * @return
	 */
	 @Column(name="id_read")
	public int getIsRead() {
		return isRead;
	}
	public void setIsRead(int isRead) {
		this.isRead = isRead;
	}
	public UserMessage(User user, Message message, int isRead) {
		super();
		this.user = user;
		this.message = message;
		this.isRead = isRead;
	}
	
	
	public UserMessage(){}
	

}
