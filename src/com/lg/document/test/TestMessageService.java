package com.lg.document.test;

import static org.junit.Assert.*;
import javax.annotation.Resource;

import org.apache.struts2.ServletActionContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.lg.document.model.Message;
import com.lg.document.model.Pager;
import com.lg.document.model.SystemContext;
import com.lg.document.model.User;
import com.lg.document.service.IMessageService;
/**
 * 被spring所管理的时候，需要加上下面的内容
 * @author 李果
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/beans.xml")
public class TestMessageService {
	@Resource
	private IMessageService messageService;
	
	
	@Test
	public void testAdd(){
		//messageService.add(null, null);
	}
	/**
	 * 这里的话，由于我们需要使用SystemContext中传过来的值
	 * 所以呢，我们 需要自己进行设置。不然的话，就会出现nullpointer
	 * 的问题。这是要注意的。
	 */
	@Test
	public void testAddMessage(){
		Message message=new Message();
		message.setContent("Hi");
		message.setTitle("Hi");
		
		User user=new User();
		user.setId(130);
		SystemContext.setLoginUser(user);
		
		//messageService.add(message,new Integer[]{123,119,120,121,129,124,140});
	}
	
	
	@Test
	public void testDeleteReceiveMsg(){
		User user=new User();
		user.setId(118);
		SystemContext.setLoginUser(user);
		messageService.deleteReceive(2);
	}
	
	@Test
	public void testDeleteSendMsg(){
		messageService.deleteSend(2);
	}
	
	@Test
	public void testFindSendMsg(){
		User user=new User();
		user.setId(130);
		SystemContext.setLoginUser(user);
		SystemContext.setPageOffset(0);
		SystemContext.setPageSize(15);
		
	 Pager<Message> pages=messageService.findSend("你好");
	 for(Message message:pages.getDatas()){
		 System.out.println(message.getContent());
	 }
	}
	
	
	@Test
	public void testFindReceiveMsg(){
		User user=new User();
		user.setId(119);
		SystemContext.setLoginUser(user);
		SystemContext.setPageOffset(0);
		SystemContext.setPageSize(15);
		Pager<Message> pages=messageService.findReceive("", 0);
		for(Message message:pages.getDatas()){
			System.out.println(message.getContent());
		}
	}
	
	@Test
	public void testPath(){
	      
	      String uploadPath=ServletActionContext.getServletContext().getRealPath("upload");
	      System.out.println(uploadPath);

	}
	

}
