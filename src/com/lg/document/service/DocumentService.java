package com.lg.document.service;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.lg.document.dao.AttachmentDao;
import com.lg.document.dao.IDepartmentDao;
import com.lg.document.dao.IDocumentDao;
import com.lg.document.dao.IUserDao;
import com.lg.document.dto.AttachDto;
import com.lg.document.model.Attachment;
import com.lg.document.model.DepDocument;
import com.lg.document.model.Department;
import com.lg.document.model.Document;
import com.lg.document.model.Pager;
import com.lg.document.model.SystemContext;
import com.lg.document.model.User;
import com.lg.document.model.UserReadDocument;
import com.lg.document.util.DocumentUtil;

@Service("documentService")
public class DocumentService implements IDocumentService {
	private IDocumentDao documentDao;
	private IDepartmentDao departmentDao;
	private AttachmentDao attachmentDao;
	private IUserDao userDao;
	
	
	
	

	public IUserDao getUserDao() {
		return userDao;
	}
	@Resource
	public void setUserDao(IUserDao userDao) {
		this.userDao = userDao;
	}
	public IDepartmentDao getDepartmentDao() {
		return departmentDao;
	}
    @Resource
	public void setDepartmentDao(IDepartmentDao departmentDao) {
		this.departmentDao = departmentDao;
	}

	public AttachmentDao getAttachmentDao() {
		return attachmentDao;
	}
	@Resource
	public void setAttachmentDao(AttachmentDao attachmentDao) {
		this.attachmentDao = attachmentDao;
	}

	public IDocumentDao getDocumentDao() {
		return documentDao;
	}

	@Resource
	public void setDocumentDao(IDocumentDao documentDao) {
		this.documentDao = documentDao;
	}
/**
 * 将公文添加给一组部门，
 * 将附件也添加给一组部门
 */
	@Override
	public void add(Document doc, Integer[] depIds, AttachDto ad) throws IOException {
		
		if(depIds==null||depIds.length==0){
			throw new com.lg.document.exception.DocumentException("你所在部门没有权利发送公文");
		}
		doc.setCreateDate(new Date());
		doc.setUser(SystemContext.getLoginUser());
		documentDao.add(doc);
		
		List<Department> deps=departmentDao.listByIds(depIds);
		DepDocument depDocument=null;
		for(Department dep:deps){
			depDocument=new DepDocument();
			depDocument.setDepartment(dep);
			depDocument.setDocument(doc);
			documentDao.addObj(depDocument);
		}
      
		DocumentUtil.addAttach(ad, attachmentDao, null, doc);
	

	}
/**
 * 删除公文是比较复杂的。
 * 除了需要删除这个对象以外
 * 还需要删除它们之间的对应的关系。
 */
	@Override
	public void delete(int id) {
		//1.删除和用户的对应关系
		String hql="delete UserReadDocument urd where urd.document.id=?";
		documentDao.executeByHql(hql, id);
		//2.删除和部门之间的对应关系
		hql=" delete DepDocument dd where dd.document.id=?";
		documentDao.executeByHql(hql, id);
		//3.删除附件
		List<Attachment> atts=this.listAttachByDocument(id);
		hql=" delete Attachment att where att.document.id=?";
		documentDao.executeByHql(hql, id);
		//4.删除公文
		documentDao.delete(id);
		//5删除已经上传到服务器中的附件
		String realPath=SystemContext.getRealPath()+"/upload";
		for(int i=0;i<atts.size();i++){
			File f=new File(realPath+"/"+atts.get(i).getNewName());
			f.delete();
		}

	}
	/**
	 * 找到这个公文对应的所有的附件
	 * @param docId
	 * @return
	 */
	public List<Attachment> listAttachByDocument(int docId){
		String hql="from Attachment where document.id=?";
		return attachmentDao.list(hql, docId);
	}
/**
 * 如果这个公文是已经读过了的话，那么就往UserReadDocument中插入一条对应的关系
 * 否则的话，就直接返回document
 */
	@Override
	public Document updateRead(int id,Integer isRead) {
		/*User loginUser=SystemContext.getLoginUser();
		Document document=documentDao.load(id);
		System.out.println(document.getContent());
		System.out.println(checkDocIsRead(loginUser.getId(), id));
		if(isRead==null||isRead==0){
			if(!checkDocIsRead(loginUser.getId(),id)){
				UserReadDocument urd=new UserReadDocument();
				urd.setDoc(document);
				urd.setUser(loginUser);
				documentDao.addObj(urd);
			}
		}
		return document;*/
		/*User u = SystemContext.getLoginUser();
		Document d = documentDao.load(id);
		if(isRead==null||isRead==0) {
			if(!checkDocIsRead(u.getId(), id)) {
				//将该文档添加为已读
				UserReadDocument urd = new UserReadDocument();
				urd.setUser(u);
				urd.setDocument(d);
				documentDao.addObj(urd);
			}
		}
		return d;*/
		/*User u = SystemContext.getLoginUser();
		Document d = documentDao.load(id);
		if(isRead==null||isRead==0) {
			if(!checkDocIsRead(u.getId(), id)) {
				//将该文档添加为已读
				UserReadDocument urd = new UserReadDocument();
				urd.setUser(u);
				urd.setDocument(d);
				documentDao.addObj(urd);
			}
		}
		return d;*/
		User u = SystemContext.getLoginUser();
		Document d = documentDao.load(id);
		if(isRead==null||isRead==0) {
			if(!checkDocIsRead(u.getId(), id)) {
				//将该文档添加为已读
				UserReadDocument urd = new UserReadDocument();
				urd.setUser(u);
				urd.setDocument(d);
				documentDao.addObj(urd);
			}
		}
		return d;
	}
	
	/**
	 * 判断某条公文是否已经被读过了
	 * @param userId
	 * @param docId
	 * @return
	 */
	private boolean checkDocIsRead(int userId,int docId){
		/**
		 * 查找这个表中是否存在。注意使用select count(*) 来进行引用
		 * select count(*) 来进行引用。
		 * select count(*) 来进行引用。
		 */
	/*	String hql=" select count(*) from UserReadDocument urd where urd.user.id=? and urd.document.id=?";
		Long count=(Long) attachmentDao.queryByHql(hql, new Object[]{userId,docId});
		if(count==null||count==0){
			return false;
		}
		return true;*/
		String hql = "select count(*) from UserReadDocument urd where urd.user.id=? and urd.document.id=?";
		Long count = (Long)documentDao.queryByHql(hql, new Object[]{userId,docId});
		if(count==null||count==0) return false;
		return true;
	}

	/**
	 * 找到某个用户所发的公文
	 */
	@Override
	public Pager<Document> findSendDocument(int userId,String conn) {
		/**
		 * 由于Document和User之间是相互关联的。那么
		 * 如果是这样取的话，那么所关联的user对象也会被取出来
		 * 所以我们不必要这样来取
		 * 其实我们是没有必要取user对象出来的。因为user对象
		 * 本就是登录用户，所以没有必要取出来
		 */
		//String hql="from Document doc where doc.user.id=? ";
		/**
		 * 可以通过对象来取值
		 * 这样就避免了去取关联的对象。
		 */
		String hql="select new Document(doc.id,doc.title,doc.content,doc.createDate) from Document doc where doc.user.id=?";
		if(conn!=null&&!"".equals(conn.trim())) {
			hql+=" and (doc.title like '%"+conn+"%' or doc.content like '%"+conn+"%')";
		}
		hql+=" order by doc.createDate desc";
		return documentDao.find(hql,userId);
	}

	/**
	 * 根据条件获取已经读过的某个部门的公文
	 */
	@Override
	public Pager<Document> findReadDocument(String con, Integer depId) {
		User loginUser=SystemContext.getLoginUser();
		/**
		 * 这里的话，可能是由于写了太多的sql语句。
		 * 所以导致这条sql语句无法执行。
		 */
		/*String hql=" select urd.document.doc  from UserReadDocument urd  "
				+" left join fetch urd.document doc left join fetch doc.user u left join fetch u.department dep"
				+ " where urd.user.id=?";*/
		/**
		 * 可以使用子查询的方式来进行查询.这样理解起来比较容易
		 * 这是要注意的。
		 */
		/*String hql=" select doc from Document doc "
				+" left join fetch doc.user u left join fetch u.department dep"
				+" where doc.id in "
				+ " (select urd.document.id from UserReadDocument urd where urd.user.id=?)";
		*//**
		 * 这里的话，需要灵活应用hql.否则的话，我们需要写很多的sql语句
		 *//*
		if(conn!=null&&!"".equals(conn.trim())) {
			hql+=" and (doc.title like '%"+conn+"%' or doc.content like '%"+conn+"%')";
		}
		if(depId!=null&&depId>0) {
			hql+=" and dep.id="+depId;
		}
		hql+=" order by doc.createDate desc";
		return documentDao.find(hql, loginUser.getId());*/
		User u = SystemContext.getLoginUser();
		String hql = "select doc from Document doc left join fetch doc.user u left join fetch u.department dep " +
				"where doc.id in (select urd.document.id from UserReadDocument urd where urd.user.id=?)";
		if(con!=null&&!"".equals(con)) {
			hql+=" and (doc.title like '%"+con+"%' or doc.content like '%"+con+"%')";
		}
		if(depId!=null&&depId>0) {
			hql+=" and dep.id="+depId;
		}
		hql+=" order by doc.createDate desc";
		return documentDao.find(hql, u.getId());
	}

	/**
	 * 根据条件获取还没有读过的某个部门的公文
	 * 我们是怎么使用left join fetch的呢？
	 * 也就是在一个表的引用中有一个对其它表中的引用
	 * 但是在取得时候，也会将这个一起取出来
	 * 所以为了避免发很多的sql语句
	 * 那么就需要使用left join fetch
	 */
	/**
	 * 这条的sql语句找到了属于某个人的所接收的所有的公文
	 * SELECT dd.doc_id FROM t_dep_document dd LEFT JOIN  t_dep dep ON(dd.dep_id=dep.id)
     *  LEFT JOIN t_user user ON (user.dep_id=dep.id) WHERE user.id=108;
	 */
	@Override
	public Pager<Document> findNotReadDocument(String con, Integer depId) {
	   
		/*User u = SystemContext.getLoginUser();
		int[] docIds=this.findAllReceiveDocIds(u.getId());
		*//**
		 * 使用了子查询的方式来进行查询
		 * 使用了left join fetch的方式来进行查询
		 *//*
		String sql="SELECT * FROM t_doc doc WHERE doc.id IN (SELECT dd.doc_id FROM t_dep_document dd LEFT JOIN  t_dep dep ON(dd.dep_id=dep.id)"+
     "  LEFT JOIN t_user user ON (user.dep_id=dep.id)  WHERE user.id=?) and doc.id "+
     " not in(SELECT urd.doc_id  from t_user_read_doc urd WHERE urd.user_id=?)";
		String hql = "select doc from Document doc left join fetch doc.user u left join fetch u.department dep where " +
				"doc.id not in (select urd.document.id from UserReadDocument urd where urd.user.id=?)";
			
		if(conn!=null&&!"".equals(conn)) {
			hql+=" and (doc.title like '%"+conn+"%' or doc.content like '%"+conn+"%')";
		}
		if(depId!=null&&depId>0) {
			hql+=" and dep.id="+depId;
		}
		return documentDao.find(hql, new Object[]{u.getId()});*/
		/*User u = SystemContext.getLoginUser();
		String hql = "select doc from Document doc left join fetch doc.user u left join fetch u.department dep where " +
				"doc.id not in (select urd.document.id from UserReadDocument urd where urd.user.id=?)";
		if(con!=null&&!"".equals(con)) {
			hql+=" and (doc.title like '%"+con+"%' or doc.content like '%"+con+"%')";
		}
		if(depId!=null&&depId>0) {
			hql+=" and dep.id="+depId;
		}
		return documentDao.find(hql, u.getId());*/
		/**
		 * 注意这个方法是想了比较久才想到的。
		 * 一方面是这些公文的id不是在userReadDocument中的，
		 * 另外一方面是这些公文的id应该是这个用户所属部门才能够接收的。
		 * 必须要是俩方面综合起来才能达到想要的效果。否则你发的公文在任何
		 * 一个用户的未读公文中都可以找到。很明显，这是不对的。所以才需要这样做。
		 * 这是要注意的。
		 */
		User u = SystemContext.getLoginUser();
		String hql = "select doc from Document doc left join fetch doc.user u left join fetch u.department dep where " +
				"doc.id not in (select urd.document.id from UserReadDocument urd where urd.user.id=?)"
				 +" and doc.id in (select dd.document.id from DepDocument dd where dd.department.id=?)";
		if(con!=null&&!"".equals(con)) {
			hql+=" and (doc.title like '%"+con+"%' or doc.content like '%"+con+"%')";
		}
		if(depId!=null&&depId>0) {
			hql+=" and dep.id="+depId;
		}
		return documentDao.find(hql,new Object[]{u.getId(),u.getDepartment().getId()});
	}
	/**
	 * 找到某个公文所对应的所有的可以发文部门
	 * 这里的话，我们不用使用left join fetch 
	 * 因为在department中
	 * 并没有对user的引用
	 * 所以这里的话，不需要使用left join fetch
	 * 来进行抓取。
	 * 这是要注意的。
	 */
	@Override
	public List<Department> listSendDepByDoc(int docId) {
		String hql="select dd.department from DepDocument dd where dd.document.id=?";
		return departmentDao.list(hql, docId);
	}
	/**
	 * 根据公文id来获取公文
	 */
	@Override
	public Document load(int id) {
		return documentDao.load(id);
	}
	@Override
	public List<DepDocument> findAllReceiveDocs(int userId) {
		String hql="select dd from DepDocument dd left join Department dep where dd.department.id=dep.id"
 + " left join User user where user.department.id=dep.id and where  user.id=130";
		return null;
	}
	/**
	 * 找到某个人应当接收的所有公文的id
	 */
	@Override
	public int[] findAllReceiveDocIds(int userId) {
	List<Document> docs=documentDao.findAllReceiveDoc(userId);
	if(docs.size()>0){
	int[] docIds=new int[docs.size()];
		for(int i=0;i<docs.size();i++){
			docIds[i]=docs.get(i).getId();
		}
		return docIds;
	}
	return null;
	}
	

}
