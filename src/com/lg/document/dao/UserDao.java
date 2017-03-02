package com.lg.document.dao;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;
import com.lg.document.model.User;
@SuppressWarnings("unchecked")
@Repository("userDao")
public class UserDao extends BaseDao<User> implements IUserDao {

	/**
	 * 必须要注意这种写法
	 * 很容易将query .serParameterList()遗漏
	 * 这是要注意的
	 */
	@Override
	public List<User> findByUserIds(Integer[] ids) {
		/**
		 * 对于hql而言，延迟加载不起作用的原因是，基于annotation的抓取策略
		 * 默认情况 下是会自动取关联对象的。所以会取出关联的对象。
		 * 由于默认情况下是join方式，所以会将部门的信息全部抓取出来
		 * 这是不好的。
		 */
		//String hql=" from User where id in (:ids) ";
		
		/**
		 * 使用join fetch来完成抓取。但是还是不太好，所发出的sql语句太复杂了
		 * 这样子做的话，会影响运行的效率
		 */
		//String hql="select u from User u  left join fetch u.department where u.id in (:ids) ";
		/**
		 * 可以使用基于对象的方式来完成.这个时候，就不需要使用join fetch了。这是要注意。
		 * 这个时候必须在user.java中增加相应的构造方法
		 * 和一个空的构造方法。这是必须的。
		 * 
		 */
		String hql="select new User(u.id) from User u where u.id in (:ids) ";
		Query query=this.getSession().createQuery(hql);
		query.setParameterList("ids", ids);
		return query.list();
	}

	
	/* * 找到可以接收的公文的所有的人。这是要注意的。
	 * 一般来说，如果是三张表进行连接的话，那么肯定是要由一个是left join,还有一个是right join。
	 * 不能俩次都是left join 
	 * 否则的话，是不行的。就很可能查处一条是null的值。*/

	@Override
	public List<User> listAllUserCanSend(int userId) {
		/**
		 * 在这里我们需要注意的是什么呢？
		 * 如果在这里的话，我们俩个都使用了left join 的话，
		 * 那么带来的问题是什么呢？也就是说，以左边的表为准。
		 * 那么这样的话，始终会查询出一条的数据。即使本来是没有可以发送的
		 * 用户的。但是我们知道的，在这里的话，userId肯定不能是空的，也就是null的
		 * 所以我们需要使用right join
		 * 我们首先要清楚的是我们需要哪些数据。
		 * 所谓的join其实就是将几张表的数据联合起来进行查询。
		 * from t_user t1 其实是为t_user起了一个别名。这是要注意的。
		 * 否则的话，我们是不能够使用t1.;;;;t3.;;;;;;;的。
		 */
		/*String sql="select  t3.id,t3.username,t3.nickname from t_user t1 LEFT JOIN t_dep_scope t2 on(t1.dep_id=t2.dep_id)"+ 
				 "  LEFT JOIN  t_user t3 ON(t3.dep_id=t2.s_dep_id) where t1.id=?";*/
		String sql="select  t3.id,t3.username,t3.nickname from t_user t1 LEFT JOIN t_dep_scope t2 on(t1.dep_id=t2.dep_id)"+ 
				 "  RIGHT JOIN  t_user t3 ON(t3.dep_id=t2.s_dep_id) where t1.id=?";
		/**
		 * 这里的话，特别要注意的一点是什么呢？我们不能使用addEntity这种方式
		 * 因为在默认的情况下，如果是使用了原生态的sql来进行查询的话，那么这个就不会被session所管理
		 * 也就是什么呢？也就是没有所谓的缓存了。这样的话，会影响我们的效率问题
		 * 这里的话，如果使用了addEntity的方式的话，那么
		 * 就会往user这个类中注入相应的属性的值。但是，由于department也是user类中的一个属性
		 * 但是这个department也是被session所管理的。这样的话，就相互矛盾了
		 * 所以我们就不能使用这种方式
		 * 使用原生态来进行查询的注意的事项。
		 */
		//return this.getSession().createSQLQuery(sql).addEntity(User.class).setParameter(0, userId).list();
		/**
		 * 我们可以使用下面的这种方式将非被session所管理的对象设置进去。
		 */
		return this.getSession().createSQLQuery(sql).setResultTransformer(Transformers.aliasToBean(User.class))
				.setParameter(0, userId).list();
	}
	
}
