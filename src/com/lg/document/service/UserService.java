package com.lg.document.service;


import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.lg.document.dao.IDepartmentDao;
import com.lg.document.dao.IUserDao;
import com.lg.document.exception.DocumentException;
import com.lg.document.model.Department;
import com.lg.document.model.Pager;
import com.lg.document.model.User;

@Service("userService")
public class UserService implements IUserService {
	private IUserDao UserDao;
	private IDepartmentDao departmentDao;
	
	public IDepartmentDao getDepartmentDao() {
		return departmentDao;
	}
    
	
    @Resource
	public void setDepartmentDao(IDepartmentDao departmentDao) {
		this.departmentDao = departmentDao;
	}

	public IUserDao getUserDao() {
		return UserDao;
	}

	@Resource(name="userDao")
	public void setUserDao(IUserDao userDao) {
		UserDao = userDao;
	}

	@Override
	public void add(User t,int depId) {
		Department department=departmentDao.load(depId);
		t.setDepartment(department);
		UserDao.add(t);
	}

	@Override
	public void update(User t,int depId) {
		Department department=departmentDao.load(depId);
		t.setDepartment(department);
		UserDao.update(t);
		}

	@Override
	public void delete(int id) {
		UserDao.delete(id);
	}

	@Override
	public User load(int id) {
		return UserDao.load(id);
	}

	
	/**
	 * 查找所有的用户但是不进行分页。
	 * 注意这个方法实际上是不存在的。在这里，实际上
	 * 这个方法根本就没有使用过。
	 * 如果需要使用的话，那么我们肯定是使用join fetch的形式的。
	 * 因为基于Annotation的话，虽然默认的是join，
	 * 但是对list方式是没有作用的
	 * 所以实际上，我们会使用left join的方式来进行加载
	 * 这是要注意的。
	 */
	@Override
	public List<User> listAllUsers() {
		String hql=" from User";
		return UserDao.list(hql);
	}

	/**
	 * 在查询的时候，我们一定要尽量少的发出sql语句
	 * 所以这里的话，我们可以使用from User u
	 * left join fetch u.department
	 * dep 来进行处理。
	 * 这样的话，一次就可以抓取多个的department
	 * 就可以减少查询的sql语句了。这是要注意的。
	 * 
	 * 
	 * 尽管基于Annotation的话，它的加载方式是join方式的，
	 * 但是这种join方式的话，实际上只是对load方式才是有用的
	 * 对于list方式的话，实际上是没有作用的。那么这个时候，我们应该怎么做呢？
	 * 我们可以使用join fetch 或者是设置batch-size的方式来完成
	 * 相关的抓取。
	 */
	@Override
	public Pager<User> findUsersByDepId(Integer depId) {
		Pager<User> pages=null;
		//这样的话，查找的就是所有的用户。
		if(depId==null||depId<=0){
			pages=UserDao.find(" from User u left join fetch u.department dep ");
		}else{
			pages=UserDao.find(" from User u left join fetch u.department dep where dep.id=?",depId);
		}
		return pages;
	}


	/**
	 * 根据用户名称来查找用户
	 */
	@Override
	public User loadByUsername(String username) {
		String hql="select u from User u where u.username=?";
		return (User) UserDao.queryByHql(hql,username);
	}


	@Override
	public User loginByUsernameAndPassword(String username, String password) {
		User user=this.loadByUsername(username);
		if(user==null){
			throw new DocumentException("用户名称不存在");
			
		}
		if(!user.getPassword().equals(password)){
			throw new DocumentException("用户密码错误");
		}
		return user;
	}


	@Override
	public void updateSelf(User user) {
	  UserDao.update(user);		
	}


	@Override
	public List<User> listUsersCanSend(int userId) {
		return UserDao.listAllUserCanSend(userId);
	}

	/**
	 * 查找所有的用户并且进行分页
	 *//*
	public Pager<User> findUsersByDepId(Integer depId) {
		if(depId==null||"".equals(depId)){
			
		}
		String hql=" from User";
		return UserDao.find(hql);
	}
*/
	
	
}
