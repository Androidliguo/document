package com.lg.document.action;


import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import javax.annotation.Resource;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.struts2.ServletActionContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import com.lg.document.dto.AttachDto;
import com.lg.document.model.Message;
import com.lg.document.model.User;
import com.lg.document.service.IMessageService;
import com.lg.document.service.IUserService;
import com.lg.document.util.ActionUtil;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
@Controller("messageAction")
@Scope("prototype")
public class MessageAction extends ActionSupport implements ModelDriven<Message>{
 /**
	 * 
	 */
	private static final long serialVersionUID = 8894199441725441126L;
    private IMessageService messageService;
    private IUserService userService;
    private Message message;
    private String con;
    private int isRead;
    /**
     * 接收发文的一组用户的id
     */
    private Integer[] sus;

    /**
     * 这是附件上传的必须要设置的属性。
     * 这些属性的名称是有规定的。
     * 不能随便乱用的。
     * 这里的话，由于需要添加的属性太多了，
     * 所以我们考虑使用Dto
     * 也就是数据传输对象。这个对象是不保存在数据库中的。只是起到传输数据的作用
     */
    private File[] atts;
    /**
     * 文件的类型。是jpg还是其它类型的文件
     */
    private String[] attsContentType;
    private String[] attsFileName;
    
	public File[] getAtts() {
		return atts;
	}


	public void setAtts(File[] atts) {
		this.atts = atts;
	}

	public String[] getAttsContentType() {
		return attsContentType;
	}

	public void setAttsContentType(String[] attsContentType) {
		this.attsContentType = attsContentType;
	}

	public String[] getAttsFileName() {
		return attsFileName;
	}

	public void setAttsFileName(String[] attsFileName) {
		this.attsFileName = attsFileName;
	}

	public Integer[] getSus() {
		return sus;
	}

	public void setSus(Integer[] sus) {
		this.sus = sus;
	}

	public IUserService getUserService() {
		return userService;
	}

    @Resource
	public void setUserService(IUserService userService) {
		this.userService = userService;
	}

	public IMessageService getMessageService() {
	return messageService;
}

@Resource(name="messageService")
public void setMessageService(IMessageService messageService) {
	this.messageService = messageService;
}

public String getCon() {
	return con;
}


public void setCon(String con) {
	this.con = con;
}

public int getIsRead() {
	return isRead;
}


public void setIsRead(int isRead) {
	this.isRead = isRead;
}


/**
 * 发送的私人信件列表
 * @return
 */
  public String listSend(){
	 ActionContext.getContext().put("pages",messageService.findSend(con));
	  return SUCCESS;
  }
  
  /**
   * 接收到的私人信件列表
   * @return
   */
  public String listReceive(){
	  ActionContext.getContext().put("pages",messageService.findReceive(con,isRead));
	  return SUCCESS;
  }
  
  /**
   * 查看私人信件的内容
   * 在查看了私人信件的内容了以后，
   * 我们需要将isRead设置为1
   *，并且需要完成更新
   *还需要将附件的信息加进去
   * @return
   */
  public String show(){
	   Message temp=messageService.updateRead(message.getId(),isRead);
	   
	   /**
	    * 注意这里的话，是完成了一个对象到另外一个对象的复制
	    */
	   try {
		BeanUtils.copyProperties(message, temp);
	} catch (IllegalAccessException | InvocationTargetException e) {
		e.printStackTrace();
	}
	   ActionContext.getContext().put("atts", messageService.listAttachmentByMsg(message.getId()));
	   return SUCCESS;
	   
   }
  
  /**
   * 删除发送的私人信件
   * @return
   */
  public String deleteSend(){
	  messageService.deleteSend(message.getId());
	  ActionContext.getContext().put("url", "message_listSend.action");
	  return ActionUtil.REDIRECT;
  }
  
  /**
   * 删除接收到的私人信件
   * @return
   */
  public String deleteReceive(){
	  messageService.deleteReceive(message.getId());
	  ActionContext.getContext().put("url", "message_listReceive.action");
	  return ActionUtil.REDIRECT;
  }
  /**
   * 添加私人信件
   * @return
   */
  public String addInput(){
	  /**
	   * 这里需要注意的是，在这里我们需要通过actionContext.getContext.getSession.
	   * get("loginUser")来获取值，否则的话，就会出现空指针异常;
	   */
	  User loginUser=(User) ActionContext.getContext().getSession().get("loginUser");
	  /*if(loginUser==null){
		  System.out.println("loginUser=null");
	  }
	  else{
	  System.out.println(loginUser.getId());
	  }*/
	  ActionContext.getContext().put("us", userService.listUsersCanSend(loginUser.getId()));
	  return SUCCESS;
  }
  
   public void validateAdd(){
	  if(sus==null||sus.length<=0){
		  this.addFieldError("sus", "必须选择用户");
	  }
	  if(message.getTitle()==null||"".equals(message.getTitle().trim())){
		  this.addFieldError("title", "私人信件一定要写上标题");
	  }
	  if(hasFieldErrors()){
		  this.addInput();
	  }
  }
  
  /**
   * 这里的话，还需要上传附件
   * 如果使用ServletActionContext.getServletContext().getRealPath("upload");
   * 获取绝对路径的话，那么在STS中获得的绝对路径如下：
   * C:\\Users\\liguo\\javaweb\\.metadata\\.plugins\\org.eclipse.wst.server.core\\tmp2\wtpwebapps\\document01\\upload
   * 所以我们不能够使用这种方式来获取绝对路径
   * 出现这个问题的原因是什么呢？
   * 因为使用STS的特别之处就是，web项目在发布的时候，会在一个独立的空间里面生成一个项目
   * 而不会在webcontent中拷贝。所以就会出现这个问题。
   * @return
 * @throws IOException 
   */
  public String add() throws IOException{
	  if(atts==null||atts.length==0){
		  messageService.add(message, sus,new AttachDto(false));
	  }else{
      String uploadPath=ServletActionContext.getServletContext().getRealPath("upload");
      //我们这里可以使用\\来代替\。否则的话，也是不行的。
       uploadPath="C:\\Users\\liguo\\javaweb\\document01\\WebContent\\upload";
     messageService.add(message, sus,new AttachDto(atts,attsContentType,attsFileName,uploadPath));
	  }
	  ActionContext.getContext().put("url", "message_listSend.action");
	  return ActionUtil.REDIRECT;
  }
  
  



	@Override
	public Message getModel() {
		if(message==null){
			message=new Message();
		}
		return message;
	}
}
