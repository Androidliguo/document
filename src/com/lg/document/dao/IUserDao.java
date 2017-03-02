package com.lg.document.dao;

import java.util.List;

import com.lg.document.model.User;

public interface IUserDao extends IBaseDao<User> {
	/**
	 * 根据一组用户Id来找用户
	 * 注意这里使用到了in
	 * @param ids
	 * @return
	 */
	public List<User> findByUserIds(Integer[] ids);
	/**
	 * 根据用户的id来查找这个用户能够对那些用户发私人信件
	 * 这里的话，我们可以使用原生态的sql查询
	 * 具体思路是：
	 * 找到用户所对应的部门，
	 * 然后就可以找到这个用户可以对哪些部门发送私人信件了
	 * 然后再找部门里面的人就可以了
	 * @param userId
	 * @return
	 */
	
    public List<User> listAllUserCanSend(int userId);

}
