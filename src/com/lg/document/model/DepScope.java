package com.lg.document.model;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
/**
 * DepScope和DepDocument之间的关系
 * 就我个人觉得是有点重复了。
 * User-Message-UserMessage
 * Department-Document-DepartmentDocument
 * 既然Department和Document之间是多对多的关系。而且使用了DepartmentDocument
 * 来进行连接，那么在一方中肯定不能有对另外一方的引用。所以在Document中就引入了User这个属性
 *。这个Document究竟是谁发的。
 * @author 李果
 *
 */
@Entity
@Table(name="t_dep_scope")
public class DepScope {
	private int id;
	private int depId;//发文部门的Id。为了避免俩个属性都是Department类型的。这是要注意的。
	private Department scopeDep;//接收发文部门的部门Id。
	
	@Id
	@GeneratedValue
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	/**
	 * 这是发公文的部门所对应的Id
	 * @return
	 */
	@Column(name="dep_id")
	public int getDepId() {
		return depId;
	}
	public void setDepId(int depId) {
		this.depId = depId;
	}
	/**
	 * 可以接收公文的部门.这里的话，为什么我们需要使用这个manytoone进行维护呢？在数据库表中，如果引入了
	 * 数据库表中的另外一个表的引用，那么必须说明它们之间的关系。它们之间是什么关系呢？是一对多，还是多对一，
	 * 还是多对多，这样的关系呢?我们必须要说清楚，否则的话，用hibernate来维护关系的话，是会出错的。
	 * 因为不知道它们之间的关系，那么这样的话，就无法进行维护了。这是要注意的。除了需要说明它们之间的关系
	 * 以外，还需要说明的是在表中的表示的名称。但是对于这样的引入的话，一般使用得最多的就是使用id.
	 * 不过这个关系是由hibernate来帮助我们维护的。
	 * 
	 * 
	 * 注意无论你是使用oneToMany还是使用了ManyToOne，需要注意的是，你必须说明自己在对方被引用时的
	 * 名称。这是必须的。否则的话，是无法找到对应关系的。
	 * 
	 * 看来自己还是没有弄清楚真实的情况。这里的话，如果我们是使用了oneToMany的话，那么在
	 * 这个类中应该是有一个set<Department>的属性的。但是实际情况呢？在这里并没有
	 * 什么set<Department>之类的属性，所以如果是在这里使用了oneToMany属性的话，
	 * 那么肯定是会出错的。所以这里的话，是需要注意的。注意在一般的情况下，我们都是在一的这一方
	 * 来维护关系的。而不会再多的一方来维护关系，因此一般情况下，很少出现set<>之类的。这是要注意的。
	 * 
	 * 
	 * 其实对于oneToone和manyToone我们可以怎么理解呢？其实可以像下面这样理解。所谓的manyToOne
	 * 说的就是什么呢？说的就是在一个关系中，它的实质就是多个Id可以对应另外一个的多个的Id,这是要注意的。
	 * 
	 * 
	 * 以后的话，当出现many2one的时候，可以考虑多个Id对应唯一的一个Id，
	 * 这样的话，就可以了。这是要注意的。
	 * 
	 * 这里的话，其实使用one2one也是完全可以的。这是要注意的。
	 * 其实认真想一想，就知道这完全是可以的。
	 * 
	 * 
	 * 注意这里我们插入到表中的是所对应的Id,而不是什么一些具体的名称。这是要注意的。
	 * 
	 * @return
	 */
	@ManyToOne
	@JoinColumn(name="s_dep_id")
	public Department getScopeDep() {
		return scopeDep;
	}
	
	public void setScopeDep(Department scopeDep) {
		this.scopeDep = scopeDep;
	}
	
	public DepScope(int depId, Department scopeDep) {
		super();
		this.depId = depId;
		this.scopeDep = scopeDep;
	}
	public DepScope() {
		super();
	}
	
	

}
