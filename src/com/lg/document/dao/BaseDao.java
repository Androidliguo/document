package com.lg.document.dao;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import javax.annotation.Resource;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import com.lg.document.model.Pager;
import com.lg.document.model.SystemContext;
@SuppressWarnings("unchecked")
/**
 * HibernateDaoSupport是Spring为较快捷地获取Hibernate中的数据
 * 而提出来的一个类。这个类可以比较快速地帮助我们管理Hibernate中的数据。
 * @author 李果
 *
 * @param <T>
 */
public class BaseDao<T>  extends HibernateDaoSupport implements IBaseDao<T>  {
	
	/**
	 * 使用hibernateDaoSupport之前，首先一定要注入sessionFactory。
	 * @param sessionFactory
	 */
	@Resource(name="sessionFactory")
	  public  void setSuperSessionFactory(SessionFactory sessionFactory){
		  super.setSessionFactory(sessionFactory);
	  }
	
	/**
	 * 创建一个Class的对象来获取泛型的class
	 * 这是获取泛型的class对象
	 * 这是要注意的。
	 */
	private Class<T> clz;
	
	public Class<T> getClz() {
		if(clz==null) {
			//获取泛型的Class对象
			clz = ((Class<T>)
					(((ParameterizedType)(this.getClass().getGenericSuperclass())).getActualTypeArguments()[0]));
		}
		return clz;
	}

	@Override
	public void add(T t) {
		this.getHibernateTemplate().save(t);
	}

	@Override
	public void update(T t) {
		this.getHibernateTemplate().update(t);
	}

	/**
	 * 注意这里的删除的话，是先将对应id的对象load起来
	 * 然后再进行删除
	 * 这是要注意的
	 * 这和更新的道理其实是一样的
	 */
	@Override
	public void delete(int id) {
		T t=this.load(id);
		this.getHibernateTemplate().delete(t);
	}

	@Override
	public T load(int id) {
		return this.getHibernateTemplate().load(getClz(), id);
	}
	
	/**
	 * 找query。
	 * 为query设置参数。
	 * 因为在setParameter方法中的参数的类型是(int,Object)
	 * 这是要注意的。
	 */
	private Query setParamterQuery(String hql,Object[] args){
		Query query=this.getSession().createQuery(hql);
		if(args!=null){
			for(int i=0;i<args.length;i++){
				query.setParameter(i, args[i]);
			}
		}
		return query;
	}
	
	@Override
	public List<T> list(String hql, Object[] args) {
		Query query=setParamterQuery(hql, args);
		return query.list();
	}

	@Override
	public List<T> list(String hql, Object arg) {
		return list(hql,new Object[]{arg});
	}

	@Override
	public List<T> list(String hql) {
		return list(hql,null);
	}

	/**
	 * 
	 */
	@Override
	public List<Object> listByObj(String hql, Object[] args) {
		Query query=setParamterQuery(hql, args);
		return query.list();
	}

	@Override
	public List<Object> listByObj(String hql, Object arg) {
		return listByObj(hql,new Object[]{arg});
	}

	@Override
	public List<Object> listByObj(String hql) {
		return listByObj(hql,null);
	}
	
/**
 * getcountSql我们应该怎么来理解呢？
 * @param hql
 * @return
 */
	private String getCountSql(String hql){
	//找到from前面的字符串。这是要注意的。
		String s=hql.substring(0,hql.indexOf("from"));
		/**
		 * 这里的话，必须要注意的一个问题就是这里的话，必须是
		 * hql=" select count(*) "+hql;
		 * 如果写成hql=hql+" select count(*) "的话，
		 * 就会出现错误，这是要注意的。
		 */
		if(s==null||"".equals(s.trim())){
			hql=" select count(*) "+hql;
		}else{
			hql=hql.replace(s, " select count(*) ");
			}
		hql=hql.replace("fetch", " ");
		return hql;
	}
	/**
	 * 这里需要注意的是，每页显示多少条的数据是由filter中的数据
	 * 设定的数据来进行决定的。而不是在pager.jsp中来进行设定的
	 * 但是为了保持一致性的话，那么必须要肯定的是，这俩者
	 * 之间设定的数据最好是一样的。否则的话，得到的结果
	 * 可能是不符合要求的。这是要注意的。
	 */

	@Override
	public Pager<T> find(String hql, Object[] args) {
		Pager<T> pages=new Pager<T>();
		int pageOffset=SystemContext.getPageOffset();
		int pageSize=SystemContext.getPageSize();
		if(pageSize<=0) pageSize = 10;
		if(pageOffset<0) pageOffset = 0;
		Query query=setParamterQuery(hql,args);
		/**
		 * 这里的话，必须要注意的是，这里的话，必须要对显示的数据进行设置，否则的话，
		 * 是不会进行数据的显示的。这是要注意的。
		 * 从哪里开始，一共显示多少条。这是要注意的。
		 */
		query.setFirstResult(pageOffset).setMaxResults(pageSize);
		
		
		List<T> datas=query.list();
		String cHql=getCountSql(hql);
		Query cQuery=setParamterQuery(cHql, args);
		long totalRecord=(long) cQuery.uniqueResult();
		pages.setDatas(datas);
		pages.setPageOffset(pageOffset);
		pages.setPageSize(pageSize);
		pages.setTotalRecord(totalRecord);
		return pages;
	}

	@Override
	public Pager<T> find(String hql, Object arg) {
		return find(hql,new Object[]{arg});
	}

	@Override
	public Pager<T> find(String hql) {
		return find(hql,null);
	}

	@Override
	public Pager<Object> findByObj(String hql, Object[] args) {
		Pager<Object> pages=new Pager<Object>();
		int pageOffset=SystemContext.getPageOffset();
		int pageSize=SystemContext.getPageSize();
		if(pageSize<=0) pageSize = 10;
		if(pageOffset<0) pageOffset = 0;
		Query query=setParamterQuery(hql,args);
		List<Object> datas=query.list();
		String cHql=getCountSql(hql);
		Query cQuery=setParamterQuery(cHql, args);
		long totalRecord=(long) cQuery.uniqueResult();
		pages.setDatas(datas);
		pages.setPageOffset(pageOffset);
		pages.setPageSize(pageSize);
		pages.setTotalRecord(totalRecord);
		return pages;
	}

	@Override
	public Pager<Object> findByObj(String hql, Object arg) {
		return this.findByObj(hql,new Object[]{arg});
	}

	@Override
	public Pager<Object> findByObj(String hql) {
		return this.findByObj(hql,null);
	}

	/**
	 * 这里是要返回某一个要查询的值。这是要注意的。
	 */
	@Override
	public Object queryByHql(String hql, Object[] args) {
		Query query=setParamterQuery(hql, args);
		return query.uniqueResult();
	}

	@Override
	public Object queryByHql(String hql, Object arg) {
		return queryByHql(hql,new Object[]{arg});
	}

	@Override
	public Object queryByHql(String hql) {
		return queryByHql(hql,null);
	}

	/**
	 * 这些executeByHql是什么时候执行的呢？
	 * 比如说，当我想通过hql语句来删除一组对象的时候，
	 * 那么这个时候，我就可以使用这个方法
	 * 这是要注意的。
	 */
	@Override
	public void executeByHql(String hql, Object[] args) {
		Query query=setParamterQuery(hql,args);
		query.executeUpdate();
	}

	@Override
	public void executeByHql(String hql, Object arg) {
		executeByHql(hql,new Object[]{arg});
	}

	@Override
	public void executeByHql(String hql) {
		executeByHql(hql);
	}
	/**
	 * 这个是用来添加维护关系的那个类的对象的。这是要注意的。
	 * 比如说在userMessage中，我们就会使用到这个方法
	 * 因为我也需要将userMessage这个对象进行保存
	 * 这是要注意的。所以我们需要这个方法
	 */

	@Override
	public void addObj(Object obj) {
		this.getHibernateTemplate().save(obj);
		
	}

	@Override
	public void updateObj(Object obj) {
       this.getHibernateTemplate().update(obj);
	}

	@Override
	public void deleteObj(Object obj) {
		this.getHibernateTemplate().delete(obj);
	}

}
