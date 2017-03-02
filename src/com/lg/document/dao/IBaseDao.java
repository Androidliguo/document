package com.lg.document.dao;
import java.util.List;
import com.lg.document.model.Pager;
public interface IBaseDao<T> {
	/**
	 * 基本的增删改查
	 * @param t
	 */
	public void add(T t);
	/**
	 * 这里的话，我们为什么需要增加addObj诸如此类的方法呢？
	 * 其实原因也是很简单的。比如说，我们在model中有一些类型并不是基本类型，
	 * 比如说userReadDocument这样的类型。我们要知道，一般来说，只要是在
	 * 数据库中建有表，那么在model中都会有一种类型和它对应。这是要注意的。
	 * @param obj
	 */
	public void addObj(Object obj);
	public void update(T t);
	public void updateObj(Object obj);
	public void delete(int id);
	public void deleteObj(Object obj);
	public T load(int id);
	
	/**
	 * 查询一组对象，不进行分页
	 * @param hql hql语句
	 * @param args 查询的条件
	 * @return
	 */
	public List<T> list(String hql,Object[] args);
	public List<T> list(String hql,Object arg);
	public List<T> list(String hql);
	
	/**
	 * 比如说在userReadDocument中就有可能会使用到listByObj.
	 * 这是要注意的。
	 * @param hql
	 * @param args
	 * @return
	 */
	public List<Object> listByObj(String hql,Object[] args);
	public List<Object> listByObj(String hql,Object arg);
	public List<Object> listByObj(String hql);
	
	
  /**
   * 获取一组对象，进行分页处理
   * @param hql
   * @param args
   * @return
   */
   public Pager<T> find(String hql,Object[] args);
   public Pager<T> find (String hql,Object arg);
   public Pager<T> find(String hql);
   
   public Pager<Object> findByObj(String hql,Object[] args);
   public Pager<Object> findByObj(String hql,Object arg);
   public Pager<Object> findByObj(String hql);
   
   
   /**
    * 通过Hql来查询一个对象，返回一个结果
    * @param hql
    * @param args
    * @return
    */
   public Object queryByHql(String hql,Object[] args);
   public Object queryByHql(String hql,Object arg);
   public Object queryByHql(String hql);
   
   
   /**
    * 通过Hql来更新一组对象
    * @param hql
    * @param args
    */
   public void executeByHql(String hql,Object[] args);
   public void executeByHql(String hql,Object arg);
   public void executeByHql(String hql);
   
   
   
   
	

}
