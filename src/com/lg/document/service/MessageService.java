package com.lg.document.service;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import com.lg.document.dao.IAttachmentDao;
import com.lg.document.dao.IMessageDao;
import com.lg.document.dao.IUserDao;
import com.lg.document.dto.AttachDto;
import com.lg.document.model.Attachment;
import com.lg.document.model.Message;
import com.lg.document.model.Pager;
import com.lg.document.model.SystemContext;
/**
 * 在这里我想提醒的一点是什么呢？
 * 在这里的话，如果需要发私人信件的话，那么肯定是登录的用户才能够
 * 发。那么在这里的话，我们就可以通过sesssion来获取对应的登录用户
 * 来完成我们的操作。
 * 所以说，时刻不能忘记我们的登录用户
 * 否则的话，是要吃亏的。
 * @author 李果
 *
 */
import com.lg.document.model.User;
import com.lg.document.model.UserMessage;


/**
 * 这里特别要提醒的是什么呢？
 * 我们在添加的时候，可以发出多条的sql语句
 * 但是我们在查询的时候，一定是不能够发出多条的sql语句的。
 * 我们需要想尽一切的办法来让这个发出尽量少的sql语句。
 * 
 * 比如说，在这里我们在取user对象的时候，那么
 * 它也会把这个user关联的department对象给我们取出来。
 * 这样的话，也会导致发出多条的sql语句。这个时候，
 * 我们可以使用join fetch来处理。
 * 
 * 在这里，我们取出一组对象的时候，我们也可以通过
 * from User where id in(:ids)
 * 这种形式来进行处理。
 * 这是要注意的。
 * 这样的话，只需要发一条sql语句就已经足够了
 * 如果不是这样的话，就需要发很多条的sql语句
 * 这样的话，就会严重影响我们的效率
 * @author 李果
 *
 */

@Service("messageService")
public class MessageService implements IMessageService {
	private IMessageDao messageDao;
	private IUserDao userDao;
	private IAttachmentDao attachmentDao;
	

	public IAttachmentDao getAttachmentDao(){
		return attachmentDao;
	}

	@Resource
	public void setAttachmentDao(IAttachmentDao attachmentDao) {
		this.attachmentDao = attachmentDao;
	}

	public IUserDao getUserDao() {
		return userDao;
	}

	@Resource
	public void setUserDao(IUserDao userDao) {
		this.userDao = userDao;
	}

	public IMessageDao getMessageDao() {
		return messageDao;
	}

	@Resource
	public void setMessageDao(IMessageDao messageDao) {
		this.messageDao = messageDao;
	}
	
	/**
	 * 这里的话，我们还需要添加附件的信息
	 * 在这里的话，我们肯定是先添加message，然后再添加attachment
	 * 因为我们要知道这个attachment是从属于哪一个message的
	 * 所以在添加的时候，肯定需要有一定的先后的顺序
	 * @throws IOException 
	 */

	@Override
	public void add(Message message, Integer[] userIds,AttachDto at) throws IOException {
		//设置该信件的发件人为当前的登录用户
		message.setUser(SystemContext.getLoginUser());
		message.setCreateDate(new Date());
		//将私人信件保存到数据库中。
		messageDao.add(message);
		//添加一组收件人
		UserMessage userMessage=null;
		List<User> users=userDao.findByUserIds(userIds);
		for(User user:users){
			userMessage=new UserMessage();
			userMessage.setIsRead(0);
			userMessage.setMessage(message);
			userMessage.setUser(user);
			messageDao.addObj(userMessage);
		}
		
		/**
		 * 如果是有附件的话，需要进行附件对象的添加
		 */
		if(at.isHasAttach()){
			File[] atts=at.getAtts();
			String[] attsContentType=at.getAttsContentType();
			String[] attsFileName=at.getAttsFileName();
			String[] newNames=new String[atts.length];
			for(int i=0;i<atts.length;i++){
				File file=atts[i];
				String fn=attsFileName[i];
				String contentType=attsContentType[i];
				Attachment a=new Attachment();
				//文件的类型
				a.setContentType(contentType);
				a.setCreateDate(new Date());
				a.setMessage(message);
				//原来的文件的名称。这里的话是不包括拓展名称的
				a.setOldName(fn);
				//文件的大小
				a.setSize(file.length());
				/**
				 * 在fileUtils这个类中，有一个方法就可以得到文件的拓展名称
				 */
				a.setType(FilenameUtils.getExtension(fn));
				/**
				 * 为这个文件生成一个新的名称
				 * 并且将这个名字保存下来
				 * 因为在上传到服务器中的时候
				 * 会使用到这个
				 * 
				 */
				String newName=getNewName(fn);
				a.setNewName(newName);
				newNames[i]=newName;
				/**
				 * 完成添加
				 */
				attachmentDao.add(a);
			}
			/**
			 * 完成了附件的添加了以后，我们还要考虑的就是附件的上传
			 * 而且这里的话，我们要考虑它们之间的先后的顺序。
			 * 如果我们先将附件上传到数据库中，而不是先保存到数据库中的话，
			 * 那么如果附件保存到数据库中失败以后，那么上传到服务器中的附件相当于
			 * 就是一堆垃圾了。所以我们先要将附件保存到数据库中，然后再考虑上传附件
			 * 这里的话，所有这些都是已经被spring所管理的。一旦在数据库中保存失败的话，
			 * 那么事务就会回滚因为我们这里的是声明式事务。
			 * 
			 * 注意在这里的话，我们也需要考虑什么呢？我们不能数据库中保存了一个附件了以后，
			 * 我们就上传一个附件。应该是当数据库将所有的附件都保存成功了以后，我们
			 * 再统一将附件上传到服务器中。
			 */
			String uploadPath=at.getUploadPath();
			for(int i=0;i<atts.length;i++){
				File f=atts[i];
				String n=newNames[i];
				String path=uploadPath+"/"+n;
				/**
				 * 一般的话，文件的上传我们会使用到IO流的操作。但是
				 * 在这里的话，我们可以通过FileUtil来完成操作。
				 * 这里的话，会有异常，那么我们为什么是
				 * 抛出异常，而不是捕获异常呢？因为一旦发生了异常的话，
				 * 那么操作肯定是不能让它继续执行下去了。
				 * 所以这里的话，我们将异常抛出
				 * 不让这个继续执行下去。
				 */
			
					FileUtils.copyFile(f, new File(path));
			}
			
		}
		
		
		
		
	}
	
	/**
	 * 这里的话，是根据日期的毫秒数来获得文件的新的名称
	 * @param name
	 * @return
	 */
	private String getNewName(String name){
		String n=new Long(new Date().getTime()).toString();
		n=n+"."+FilenameUtils.getExtension(name);
		return n;
		
	}

	/**
	 *这里的话，需要注意删除的条件
	 *包括msgId和userId
	 *将对应的message的id和对应的user的id删除掉。
	 *俩者都对应的时候才删除
	 *还要将这条message对应的所有的附件都删除掉哦
	 *这里接收附件的话，我们不能将message所对应的附件信息在这里删除
	 *因为在这里删除了得话，那么其它的userMessage就找不到对应的附件的信息了
	 *这是要注意的
	 */
	@Override
	public void deleteReceive(int msgId) {
		User loginUser=SystemContext.getLoginUser();
		String hql="delete UserMessage um where um.message.id=? and um.user.id=?";
		messageDao.executeByHql(hql,new Object[]{msgId,loginUser.getId()});

	}
	
	/**
	 * 这里特别想要提醒的一点是什么呢？
	 * 如果我们想要删除某个对象的时候
	 * 除了删除这个对象以外
	 * 还有一点就是需要删除和它相关联的关系
	 * 一定要考虑周全了
	 * 否则的话，就很容易出错
	 * 这是要注意的。
	 */

	@Override
	public void deleteSend(int msgId) {
		//删除所有已经接收到msg的Id
		String hql="delete UserMessage um where um.message.id=?";
		messageDao.executeByHql(hql, msgId);
		//接下来删除所有的附件的信息
		/**
		 * 注意在删除附件的时候，我们必须首先将这些附件信息保存下来
		 * 接下来我们才能将服务器中的上传的附件信息删除
		 * 否则的话，如果附件被删除了的话，那么
		 * 我们就不能够删除服务器中上传的附件了。
		 */
		List<Attachment> atts=this.listAttachmentByMsg(msgId);
		String hql2="delete Attachment att where att.message.id=?";
		//这里的话，肯定是不能使用attachmentDao.delete()来删除附件。
		messageDao.executeByHql(hql2, msgId);
		//删除msg对象
		messageDao.delete(msgId);
		/**
		 * 我们的附件已经上传到服务器中了。需要删除
		 * 与文件处理相关的操作应该在最后才删除
		 * 比如说，如果我们在message删除之前就将服务器中的附件信息删除了。
		 * 那么假如message删除失败了。那么就会事务回滚。就相当于没有删除message
		 * 但是这时候，服务器重的附件文件已经被我们删除了。
		 * 这是要注意的
		 */		
		//这里的话，我们要根据附件的的具体的名称来将附件删除.这里的话，是根据新名称来删除的。
		String realPath=SystemContext.getRealPath()+"/upload";
		for(int i=0;i<atts.size();i++){
			File f=new File(realPath+"/"+atts.get(i).getNewName());
			f.delete();
		}
		

	}

	/**
	 * 查找接收到的信息.注意这里为什么是left join fetch um.message.user 
	 * 而不是um.user呢？这里需要注意的是，我们取出来的是message,但是message又和
	 * user关联起来，user又和department关联起来。所以我们需要取um.message.user
	 * 而不是um.user
	 */
	@Override
	public Pager<Message> findReceive(String conn, int isRead) {
		/**
		 * 事实证明，
		 * 在这里的话，我们不一定需要使用ThreadLocal的方式来获取值
		 * 我们一样可以通过ActionContext.getContext.getSession().get()
		 * 来进行获取。
		 * 这是要注意的。
		 */
		/*System.out.println("###########################################");
		User user=(User) ActionContext.getContext().getSession().get("loginUser");
		System.out.println(user.getNickname());*/
		User loginUser=SystemContext.getLoginUser();
		String hql=" select um.message from UserMessage um left join fetch um.message.user u left join fetch u.department"
				+ " where um.isRead=? and um.user.id=?";
		if(conn!=null && !"".equals(conn.trim())){
			hql=hql+" and (um.message.title like ? or um.message.content like ? ) order by um.message.createDate desc";
			return messageDao.find(hql, new Object[]{isRead,loginUser.getId(),"%"+conn+"%","%"+conn+"%"});
		}
		hql=hql+" order by um.message.createDate desc ";
		return messageDao.find(hql, new Object[]{isRead,loginUser.getId()});
	}
/**
 * 注意我们在一个表中取出一组 数据的时候，一定要注意效率的问题。
 * 因为如果是使用了Annotation的时候，那么默认的方式就是join的方式
 * 在这种方式 下，会把默认的关联的表给取出来。这种情况下效率是很低的。
 * 所以在 一般的情况下，我们可以使用通过对象的方式来查询一组对象，需要什么属性
 * 就将什么属性取出来。这种情况下，效率是最高的。
 */
	@Override
	public Pager<Message> findSend(String conn) {
		User loginUser =SystemContext.getLoginUser();
		/**
		 * 如果我们使用的是这条sql语句的话，由于message和user之间是相互
		 * 关联的。那么我们在取message的时候，也会将所关联的user对象取出来
		 * 这样的话，就会影响效率
		 */
		//String hql=" select msg from Message msg where msg.user.id=?";
		/**
		 * 如果是像下面这种方式的话，虽然我将user对象在抓取了出来，但是，
		 * 由于user和department之间又是相互关联的，所以也会将department
		 * 取出来。所以还是3条sql语句
		 */
		//String hql=" select msg from Message msg left join fetch msg.user where msg.user.id=?";

		/**
		 * 这样的话，就只会发出俩条的sql语句。一条是取出3个相互关联的表
		 * 还有一条就是取出select count(*) 
		 * 但是这样的话，明显就会影响我们的效率
		 */
		//String hql=" select msg from Message msg left join fetch msg.user u left join fetch u.department where msg.user.id=?";

		/**
		 * 最佳实践就是通过关联表来取
		 * 但是必须要注意的是，
		 * 这里的话，必须要在Message 这个类中写上相应的构造方法
		 * 和无参的构造方法
		 * 这是要注意的。
		 */
		String hql="select new Message(msg.id,msg.title,msg.content,msg.createDate) from Message msg where msg.user.id=? ";
		/**
		 * 注意这里的话，需要使用&&而不是||
		 * 这是要注意的。
		 */
		if(conn!=null&&!"".equals(conn.trim())){
			hql=hql+" and (msg.content like ? or msg.title like ?) order by msg.createDate desc";
			return messageDao.find(hql, new Object[]{loginUser.getId(),"%"+conn+"%","%"+conn+"%"});
		}
		hql=hql+" order by msg.createDate desc";
		
		return messageDao.find(hql, loginUser.getId());
	}

	/**
	 * 根据是否已经读过了这部分的内容来确定是否需要更新
	 * 只有在未读的情况下，我们才需要将相关的userMessage
	 * 查询出来
	 */
	@Override
	public Message updateRead(int msgId,int isRead) {
		/**
		 * 这里的话，更多的是考虑了效率上的问题
		 * 这是要注意的。
		 * 只有在由未读转化为已读的过程当中
		 * 我们才将userMessage查询出来
		 * 这是要注意的。
		 */
		if(isRead==0){
		User loginUser=SystemContext.getLoginUser();
		UserMessage um=messageDao.loadUserMessage(loginUser.getId(), msgId);
		if(um.getIsRead()==0){
			um.setIsRead(1);
			messageDao.updateObj(um);
		}
		}
		return messageDao.load(msgId);
	}

	/**
	 * 获取某个私人信件的所有的附件信息
	 */
	@Override
	public List<Attachment> listAttachmentByMsg(int msgId) {
		String hql="from Attachment att where att.message.id=?";
		return attachmentDao.list(hql, msgId);
	}


}
