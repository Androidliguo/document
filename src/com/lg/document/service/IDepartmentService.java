package com.lg.document.service;

import java.util.List;

import com.lg.document.model.Department;

public interface IDepartmentService {
	/**
	 * 基本的增删改查
	 */
	public void add(Department dep);
	public void delete(int id);
	public void update(Department dep);
	public Department load(int id);
	
	/**
	 * 查找所有的部门
	 */
	public List<Department> listAllDep();
	
	
	/**
	 * 为某个部门添加管理部门
	 */
	public void addScopeDep(int depId,int sDepId);
	
	/**
	 * 为某个部门添加一组管理部门
	 * 
	 */
	public void addScopeDeps(int depId,int[] sDepIds);
	
	/**
	 * 为某个部门删除一个管理部门
	 */
	public void deleteScopeDep(int depId,int sDepId);
	/**
	 * 删除所有
	 */
	public void deleteScopeDep(int depId);
	
	
	/**
	 * 获取某个用户所能够管理的所有的部门
	 */
	
	public List<Department> listUserDep(int userId);
	/**
	 * 一个部门所对应的所有的发文部门
	 * @param depId
	 * @return
	 */
	
	public List<Department> listDepScopeDep(int depId);

}
