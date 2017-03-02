package com.lg.document.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Entity
@Table(name="t_user")
public class User {
	/**
	 * 0表示的是普通人员，1表示的是管理人员
	 */
	private int type;
    private int id;
    private String username;
    private String password;
    private String nickname;
    private Department department;
    private String email;
    
    
    
    
    
    
    public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	@ManyToOne
    @JoinColumn(name="dep_id")
	public Department getDepartment() {
		return department;
	}
	public void setDepartment(Department department) {
		this.department = department;
	}
	
	@Id
	@GeneratedValue
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public User(String username, String password, String nickname,int type,String email) {
		super();
		this.username = username;
		this.password = password;
		this.nickname = nickname;
		this.type=type;
		this.email=email;
	}
	
	public User(){}
	
	public User(int id){
		this.id=id;
	}
    
    
    
}
