package com.lg.document.service;
import java.io.IOException;
import java.util.List;
import com.lg.document.dto.AttachDto;
import com.lg.document.model.Attachment;
import com.lg.document.model.Message;
import com.lg.document.model.Pager;

public interface IMessageService {
	/**
	 * 这里的话，还需要上传附件
	 * 但是这里的话，我们存的是一个Dto的对象。
	 * @param message  私人信件的内容
	 * @param userIds   发送给谁
	 * @throws IOException 
	 */
	public void add(Message message,Integer[] userIds,AttachDto at) throws IOException;
	
	/**
	 * 删除接收到的信件对象
	 * @param message
	 */
	public void deleteReceive(int msgId);
	
	/**
	 * 删除所发送的信件对象
	 * @param message
	 */
	public void deleteSend(int msgId);
	
	/**
	 * 加载相应的msg对象
	 * 如果这个message的状态由未读转化为已读的话，那么
	 * 需要更新了。
	 * @param msgId
	 */
	public Message updateRead(int msgId,int isRead);
	
	
	/**
	 * 根据条件和是否已经读过了来查找所接收到的信件
	 * @param conn
	 * @param isRead
	 * @return
	 */
	public Pager<Message> findReceive(String conn,int isRead);
	
	
	/**
	 * 根据条件来找到所发送的信件
	 * @param conn
	 * @return
	 */
	public Pager<Message> findSend(String conn);
	

	/**
	 * 根据私人信件的Id来获取这个信件的所有的附件
	 * @param msgId
	 * @return
	 */
	public List<Attachment> listAttachmentByMsg(int msgId);
	
	
	

	
	
	
	

}
