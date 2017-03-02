package com.lg.document.dao;

import java.util.List;

import com.lg.document.model.Department;

public interface IDepartmentDao extends IBaseDao<Department> {
	@SuppressWarnings("rawtypes")
	public List listAllExistIds(int depId) ;
	/**
	 * 根据部门的id来获取一组部门
	 * @param depIds
	 * @return
	 */
	public List<Department> listByIds(Integer[] depIds);
	
	/**
	 * 根据用户的id来获取某个用户可以发文的所有的部门
	 * @param userId
	 * @return
	 */
	public List<Department> listSendDepsByUserId(int userId);
}
