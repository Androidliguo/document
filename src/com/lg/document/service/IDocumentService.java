package com.lg.document.service;
import java.io.IOException;
import java.util.List;
import com.lg.document.dto.AttachDto;
import com.lg.document.model.Attachment;
import com.lg.document.model.DepDocument;
import com.lg.document.model.Department;
import com.lg.document.model.Document;
import com.lg.document.model.Pager;
public interface IDocumentService {
	/**
	 * 为一组部门插入公文和附件
	 * @param doc
	 * @param depIds
	 * @param ad
	 */
	public void add(Document doc,Integer[] depIds,AttachDto ad) throws IOException;
	/**
	 * 删除某个公文
	 * @param id
	 */
	public void delete(int id);
	/**
	 * 这个方法中的主要的操作就是
	 * 如果这条公文已经被读过了的话，那么就往userReadDocument表中插入一条数据
	 * @param id
	 * @return
	 */
	public Document updateRead(int id,Integer isRead);
	
	/**
	 * 获取某个用户的所有的所发送的公文
	 * @param userId
	 * @return
	 */
	public Pager<Document> findSendDocument(int userId,String conn);
	
	/**
	 * 获取某个部门的已经读过了的公文
	 * 如果docId为null或者等于0的话，那么就表示获取的是所有的已经读过了的公文
	 * @param conn
	 * @param docId
	 * @return
	 */
	public Pager<Document> findReadDocument(String conn,Integer depId);
	/**
	 * 获取某个部门的还没有读过的公文
	 * @param conn
	 * @param docId
	 * @return
	 */
	
	public Pager<Document> findNotReadDocument(String conn,Integer depId);
	/**
	 * 获取这篇公文所对应的所有的附件
	 * @param id
	 * @return
	 */
	public List<Attachment> listAttachByDocument(int id);
	
	/**
	 * 获取这篇公文所对应的所有的部门
	 * @param docId
	 * @return
	 */
	public List<Department> listSendDepByDoc(int docId);
	/**
	 * 根据公文id来加载公文
	 * @param id
	 * @return
	 */
	public  Document load(int id);
	
	/**
	 * 找到发给某个人的所有的公文
	 * @param userId
	 * @return
	 */
	public List<DepDocument> findAllReceiveDocs(int userId);
	
	/**
	 * 所有某人应当接收的公文的id
	 * @param userId
	 * @return
	 */
	public int[] findAllReceiveDocIds(int userId);
	

}
