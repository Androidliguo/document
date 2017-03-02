package com.lg.document.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.lg.document.dao.IDepartmentDao;
import com.lg.document.dao.IUserDao;
import com.lg.document.model.DepScope;
import com.lg.document.model.Department;

@Service("departmentService")
public class DepartmentService implements IDepartmentService {
	private IUserDao userDao;
	
	public IUserDao getUserDao() {
		return userDao;
	}

	@Resource(name="userDao")
	public void setUserDao(IUserDao userDao) {
		this.userDao = userDao;
	}


	private IDepartmentDao departmentDao;
	
	
	public IDepartmentDao getDepartmentDao() {
		return departmentDao;
	}

	@Resource(name="departmentDao")
	public void setDepartmentDao(IDepartmentDao departmentDao) {
		this.departmentDao = departmentDao;
	}

	@Override
	public void add(Department dep) {
          departmentDao.add(dep);
	}
	
	/**
	 * 找到某个部门的用户的数量
	 */
	
	public long getDepUserCount(int dep_id) {
		String hql="select count(*) from User where dep_id=?";
		/**
		 * 使用HibernateDaoSupport的好处是什么呢？
		 * 好处就是继承了这个类以后呢？就可以直接通过
		 * this.getSession来获取session了。
		 * 这就是它的方便之处。
		 */
		long count=(long) departmentDao.queryByHql(hql, dep_id);
		return count;
		
	}

	/**
	 * 1.如果该部门中还有用户的话，就不能删除
	 * 2.删除部门之前必须删除部门之间的关联的信息
	 * 
	 */
	@Override
	public void delete(int id){
          long count=getDepUserCount(id);
          if(count>0){
        	throw new com.lg.document.exception.DocumentException("该部门中还有用户，不能删除");
          }
          
          String hql="delete DepScope ds where ds.depId=? or ds.scopeDep.id=?";
          departmentDao.executeByHql(hql, new Object[]{id,id});
          departmentDao.delete(id);
	}

	@Override
	public void update(Department dep) {
           departmentDao.update(dep);
	}

	@Override
	public Department load(int id) {
		return  departmentDao.load(id);
	}

	@Override
	public List<Department> listAllDep() {
		String hql="from Department";
		return departmentDao.list(hql);
	}

	/**
	 * 为某个部门添加管理部门
	 */
	@Override
	public void addScopeDep(int depId, int sDepId) {
		/**
		 * 当我们使用了Hibernate后，在一般的情况下，都不会再出现insert into这样的语句了。
		 */
		/*String  hql="insert into DepScope(dep_id,s_dep_id) values(?,?)";
        departmentDao.executeByHql(hql,new Object[]{depId,sDepId});	*/
	    String hql="select ds from DepScope ds where ds.depId=? and ds.scopeDep.id=?";
		DepScope ds=(DepScope) this.departmentDao.queryByHql(hql,new Object[]{depId,sDepId});
		if(ds!=null){
			return;
		}
		DepScope depScope=new DepScope();
		depScope.setDepId(depId);
		depScope.setScopeDep(departmentDao.load(sDepId));
		departmentDao.addObj(depScope);
		
	}
	/**
	 * 下面的来个方法的目的是什么呢？
	 * 如果我们使用了复选框来进行提供选择的话，
	 * 第一次的时候，我们可以对某个部门进行部门的添加功能
	 * 但是以后呢，由于struts2的一些局限性，我们就无法再为
	 * 这个部门添加或者是删除某个部门
	 * 所以我们需要使用下面的俩个方法来进行设置
	 * 来达到我们的母的
	 */
	/**
	 * 
	 * @param esd  原来在复选框中已经被选中的部门
	 * @param sDepIds  在复选框中新的被选中的部门的id
	 * @return
	 */
	private List<Integer> getDelDeps(List<Integer> esd,List<Integer> sDepIds) {
		List<Integer> result = new ArrayList<Integer>();
		for(int id:esd) {
			if(!sDepIds.contains(id)) {
				//表示原来的能够发文的id中有，但是新添加的没有，就需要进行删除
				result.add(id);
			}
		}
		return result;
	}
	
	private List<Integer> getAddDeps(List<Integer> esd,List<Integer> sDepIds) {
		List<Integer> result = new ArrayList<Integer>();
		for(int id:sDepIds) {
			if(!esd.contains(id)) {
				result.add(id);
			}
		}
		return result;
	}

	/**
	 * 找到复选框中新的一批被选中的部门
	 * 然后进行添加或者是删除操作
	 */
	@Override
	public void addScopeDeps(int depId, int[] sDepIds) {
		List<Integer> sDepIdList = new ArrayList<Integer>();
		for(int sid:sDepIds) {
			sDepIdList.add(sid);
		}
		List<Integer> esd = departmentDao.listAllExistIds(depId);
		List<Integer> delIds = getDelDeps(esd, sDepIdList);
		List<Integer> addIds = getAddDeps(esd, sDepIdList);
		for(int id:delIds) {
			this.deleteScopeDep(depId, id);
		}
		for(int sDepId:addIds) {
			this.addScopeDep(depId, sDepId);
		}
		/*for(int sDepId:sDepIds){
			this.addScopeDep(depId, sDepId);
		}*/
          
	}

	@Override
	public void deleteScopeDep(int depId, int sDepId) {
	    String hql="delete DepScope ds where ds.depId=? and ds.scopeDep.id=?";
		departmentDao.executeByHql(hql,new Object[]{depId,sDepId});
		/**
		 * 既然是删除的话，那么我们又怎么能够去new一个对象出来呢。update和delete都是一样的，
		 * 必须需要先把对象load出来，然后将这个对象进行删除。
		 */
	/*	DepScope depScope=new DepScope();
		depScope.setDepId(depId);
		depScope.setScopeDep(departmentDao.load(sDepId));
		departmentDao.deleteObj(depScope);*/
		/*
		String hql="select ds from DepScope ds where ds.depId=? and ds.scopeDep.id=?";
		Department department=(Department) departmentDao.queryByHql(hql,new Object[]{depId,sDepId});
		departmentDao.deleteObj(department);*/
	}
	
	@Override
	public void deleteScopeDep(int depId) {

		String hql="delete DepScope ds where ds.depId=?";
		departmentDao.executeByHql(hql,new Object[]{depId});
	}
	

	/**
	 * 找到某个用户所能够管理的所有的部门
	 * 在departmentDao中是使用了原生态的sql语句来进行查询的。
	 * 这是要注意的。
	 */
	@Override
	public List<Department> listUserDep(int userId) {
		 return departmentDao.listSendDepsByUserId(userId);
		
	}

	/**
	 * 找到某个部门所能管理的所有部门
	 */
	@Override
	public List<Department> listDepScopeDep(int depId) {
		/**
		 * 下面的俩种方法都是可以的。
		 */
		/*String hql="select dep from DepScope  ds left join ds.scopeDep dep where ds.depId=?";
		return departmentDao.list(hql, new Object[]{depId});*/
		
		String hql2="select scopeDep from DepScope ds where ds.depId=?";
		return departmentDao.list(hql2,new Object[]{depId});

		
	}

	

}
