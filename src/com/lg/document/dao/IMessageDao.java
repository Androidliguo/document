package com.lg.document.dao;
import com.lg.document.model.Message;
import com.lg.document.model.UserMessage;
public interface IMessageDao extends IBaseDao<Message> {
	/**
	 * 判断某个私人信件是否是已经读过了的。
	 * 如果一个私人信件由未读状态转化为已读状态的话，
	 * 那么我们就需要完成更新
	 * @param userId
	 * @param msgId
	 * @return
	 */
	public boolean checkIsRead(int userId,int msgId);
	
	/**
	 * 根据用户Id和相应的message的Idt_user_message
	 * 中找到相对应的记录
	 * @param userId
	 * @param msgId
	 * @return
	 */
	public UserMessage loadUserMessage(int userId,int msgId);
}
