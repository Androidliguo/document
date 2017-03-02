package com.lg.document.dao;
import java.util.List;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;
import com.lg.document.model.Document;

@Repository("documentDao")
public class DocumentDao extends BaseDao<Document> implements IDocumentDao {
/**
 * 找到某个人应当接收的所有的公文
 * 对于这个的话，我们可以怎么来理解呢？比如说是：
 * 我们可以怎么来理解呢？这个人属于哪一个部门，
 * 这个部门可以接收哪些公文。
 * 这样的话，就将这个联合起来了。
 */
	@Override
	public List<Document> findAllReceiveDoc(int userId) {
		/**
		 * 下面的俩条的sql都是可以的。但是明显是第二条的sql比较容易理解。这是肯定的。
		 */
		//String sql="SELECT doc.id,doc.title  FROM t_doc doc WHERE doc.id IN (SELECT dd.doc_id FROM t_dep_document dd LEFT JOIN  t_dep dep ON(dd.dep_id=dep.id)"
		//+" LEFT JOIN t_user user ON (user.dep_id=dep.id) WHERE user.id=?)";
	String sql=" select doc.id,doc.title from t_doc doc where doc.id in(select dd.doc_id from t_dep_document "
			+ " dd left join t_user user on (user.dep_id=dd.dep_id) where user.id=?)";
		
		
		return this.getSession().createSQLQuery(sql).setResultTransformer(Transformers.aliasToBean(Document.class))
			.setParameter(0, userId).list();	
		/*return this.getSession().createSQLQuery(sql).addEntity(DepDocument.class)
				.setParameter(0, userId).list();*/
				
	}

}