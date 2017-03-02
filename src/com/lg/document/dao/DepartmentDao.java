package com.lg.document.dao;


import java.util.List;

import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.lg.document.model.Department;
@SuppressWarnings("unchecked")
@Repository("departmentDao")
public class DepartmentDao extends BaseDao<Department> implements IDepartmentDao {

	/**
	 * 获取所有能够接收公文的部门的id
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public List listAllExistIds(int depId) {
		String hql="select ds.scopeDep.id from DepScope ds where ds.depId=?";
		return (List)this.listByObj(hql, depId);
	}

	/**
	 * 根据部门id来获取一组部门
	 */
	@Override
	public List<Department> listByIds(Integer[] depIds) {
		String hql="select new Department(d.id,d.name) from Department d where d.id in (:ids) ";
		Query query=this.getSession().createQuery(hql);
		query.setParameterList("ids",depIds);
		return query.list();
	}

	/**
	 * 根据用户的Id来获取某个用户可以发文的部门
	 * 这里是使用了原生态的sql语句来查询某个用户的可发文部门
	 * 如果是使用了原生态的sql查询的话，那么注意需要使用这样的查询语句来进行查询
	 * 否则的话，是不行的。这是要注意的。
	 */
	/**
	 * 这里的话，需要注意的是什么呢？
	 * 这里的第二个join应该使用right join
	 * 否则的话，是不行的。是会出错的。
	 */
	@Override
	public List<Department> listSendDepsByUserId(int userId) {
		String hql="select dep.id,dep.name from t_user user LEFT JOIN t_dep_scope ds on(user.dep_id=ds.dep_id) "+
                    " RIGHT JOIN t_dep dep on(ds.s_dep_id=dep.id) where user.id=?";
		return this.getSession().createSQLQuery(hql).setResultTransformer(Transformers.aliasToBean(Department.class))
		.setParameter(0, userId).list();
		
	}

}
