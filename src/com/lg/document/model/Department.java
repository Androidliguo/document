package com.lg.document.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
@Entity
@Table(name="t_dep")
public class Department {
	private int id;
	private String name;
	
	@Id
	@GeneratedValue
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public Department(){}
	public Department( String name) {
		this.name = name;
	}
	/**
	 * 重写了hasCode和equals这俩个
	 * 方法，说明在俩个department的id
	 * 相等的时候，就可以认为这俩个对象是
	 * 相等的了。
	 */
	
	@Override
	public int hashCode() {
		/**
		 * 将hasCode变得大一点而已
		 * 这是要注意的
		 */
		return this.id+10000;
	}
	
	@Override
	public boolean equals(Object obj) {
		Department department=(Department)obj;
		if(department.getId()==this.id){
			return true;
		}
		return false;
	}
	public Department(int id, String name) {
		super();
		this.id = id;
		this.name = name;
	}
	
	
	
	

}
