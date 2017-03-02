package com.lg.document.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
/**
 * 
 * 哪些部门可以接收到这些公文
 * @author 李果
 *
 */
@Entity
@Table(name="t_dep_document")
public class DepDocument {
	private int id;
	private Document document;
	private Department department;
	
	
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
	
	
	@ManyToOne
	@JoinColumn(name="doc_id")
	public Document getDocument() {
		return document;
	}
	public void setDocument(Document document) {
		this.document = document;
	}
	
	
	public DepDocument(){}
	
	public DepDocument(Document document) {
		this.document = document;
	}
	public DepDocument(Document document, Department department) {
		this.document = document;
		this.department = department;
	}
	
	/*
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
	
	
	
*/
}
