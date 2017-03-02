package com.lg.document.dao;


import org.springframework.stereotype.Repository;

import com.lg.document.model.Message;
import com.lg.document.model.UserMessage;
/**
 * 在这里我想提及的一点是什么呢？
 * 在增加新的方法的时候，我们应该Dao类中添加呢?
 * 还是应该在service层中添加呢？
 * 我们的dao层毫无疑问是为service层服务的。那么
 * 新增加的方法应该尽可能地在dao层写。
 * 当然这也不是唯一。要看具体的情况。
 * 比如说，在这里的话，loadUserMessage和checkIsRead()方法
 * 都是为service层中的updateRead()方法服务的。这是要注意的。
 * 
 * @author 李果
 *
 */

@Repository("messageDao")
public class MessageDao extends BaseDao<Message> implements IMessageDao {

	@Override
	public boolean checkIsRead(int userId, int msgId) {
		return false;
	}
	
	/**
	 * 根据用户id和私人信件的id在
	 * userMessage中查找相应的记录
	 * 这是要注意的。
	 */
	@Override
	public UserMessage loadUserMessage(int userId, int msgId) {
		String hql=" select um from UserMessage um where um.user.id=? and um.message.id=?";
		return (UserMessage) this.queryByHql(hql,new Object[]{userId,msgId});
	}
}
