package com.lg.document.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.lg.document.dao.IDocumentDao;
import com.lg.document.dto.AttachDto;
import com.lg.document.model.DepDocument;
import com.lg.document.model.Department;
import com.lg.document.model.Document;
import com.lg.document.model.SystemContext;
import com.lg.document.model.User;
import com.lg.document.service.IDepartmentService;
import com.lg.document.service.IDocumentService;
import com.lg.document.service.IUserService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/beans.xml")
public class TestDocument {
	@Resource
	private IDocumentDao documentDao;
	
	@Resource(name="departmentService")
	private IDepartmentService departmentService;
	
	@Resource(name="userService")
	private IUserService userService;
	
	
	@Resource(name="documentService")
	private IDocumentService documentService;
	
	@Before
	public void init() {
		User u = userService.load(108);
		SystemContext.setLoginUser(u);
	}
	/**
	 * 测试添加
	 */
	@Test
	public void testAdd() {
		try {
			Integer[] depIds = new Integer[]{2,3,4,5,6};
			Document doc = new Document();
			doc.setContent("一只小猫");
			doc.setTitle("小猫");
	     	documentService.add(doc, depIds, new AttachDto(false));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 *测试updateRead
	 */
	@Test
	public void testUpdateRead(){
		User u=userService.load(109);
		SystemContext.setLoginUser(u);
		Document doc=documentService.updateRead(7,0);
		System.out.println(doc.getContent());
	}
	
	/**
	 * 查看某个人所发送的所有的文章
	 */
	@Test
	public void testSendMsg(){
		SystemContext.setPageOffset(0);
		SystemContext.setPageSize(15);
		List<Document> docs=documentService.findSendDocument(108,null).getDatas();
		for(Document d:docs){
			System.out.println(d.getContent());
		}
	}
	
	/**
	 * 测试查看读过的部门的文章
	 */
	@Test 
	public void testReadMsg(){
		User u=userService.load(109);
		SystemContext.setLoginUser(u);
		SystemContext.setPageOffset(0);
		SystemContext.setPageSize(15);
		List<Document> docs=documentService.findReadDocument("一只", 6).getDatas();
		for(Document d:docs){
			System.out.println(d.getContent());
		}
	}
	
	/**
	 * 测试没有读过的部门的文章
	 */
	
	@Test 
	public void testNotReadMsg(){
		User u=userService.load(109);
		SystemContext.setLoginUser(u);
		SystemContext.setPageOffset(0);
		SystemContext.setPageSize(15);
		List<Document> docs=documentService.findNotReadDocument("小猫",0).getDatas();
		for(Document d:docs){
			System.out.println(d.getContent());
		}
	}
	/**
	 * 测试删除
	 */
	
	@Test
	public void testDelete(){
		documentService.delete(6);
	}
	
	/**
	 * 查看某个公文所对应的所有的发文的公文
	 */
	@Test
	public void testListSendDepByDoc(){
		List<Department> deps=documentService.listSendDepByDoc(8);
		for(Department dep:deps){
			System.out.println(dep.getName());
		}
		
	}
	/**
	 * 找到某个人所接收到的所有的公文
	 */
	@Test
	public void testFindAllReceiveDoc(){
	List<Document> list=documentDao.findAllReceiveDoc(109);
	for(Document document:list){
		System.out.println(document.getId());
	}
	/*for(DepDocument u:list){
	System.out.println(u.getDocument().getId());
	}*/
		
	}
	
	
}
