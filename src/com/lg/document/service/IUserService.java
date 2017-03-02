package com.lg.document.service;
import java.util.List;
import com.lg.document.model.Pager;
import com.lg.document.model.User;
public interface IUserService {
	/**
	 * 基本的增删改查。是属于哪一个部门的。
	 * 我们可以想象一下，在添加用户的时候，可能是需要选择部门的。
	 * 那么我们这样做的话，就刚好将部门加入进来了。
	 * 这是要注意的。
	 */
	public  void add(User User,int depId);
	public void update(User User,int depId);
	public void delete(int id);
	public  User load(int id);
	/**
	 * 根据用户的名称来查找用户。在注册的时候，判断用户是已经存在的了。
	 */
	public User loadByUsername(String username);
	/**
	 * 查找所有的用户
	 */
	public List<User> listAllUsers();
	
	
	/**
	 * 查找用户，并且进行分页。每一个部门的用户。
	 */
	
	public Pager<User> findUsersByDepId(Integer depId);
	/**
	 * 根据用户名和密码来进行确定是否登录成功
	 * 
	 */
	
	 public User loginByUsernameAndPassword(String username,String password);
	
	
	 /**
	  * 更新自己的个人的信息.部门的信息是不能够被更改的
	  */
 	public void updateSelf(User user);
	
 	
 	/**
 	 * 根据用户的id找到这个用户
 	 * 能够发私人信件的所有的用户。
 	 * 这是要注意的。
 	 * @param userId
 	 * @return
 	 */
 	public List<User> listUsersCanSend(int userId);
	

}
