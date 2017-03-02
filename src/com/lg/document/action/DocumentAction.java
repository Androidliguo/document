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
import com.lg.document.model.Document;
import com.lg.document.model.User;
import com.lg.document.service.IDepartmentService;
import com.lg.document.service.IDocumentService;
import com.lg.document.util.ActionUtil;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
/**
 * 这里的话，我们为什么会想到使用con,isRead,depId,之类的东西的呢？
 * 其实就是因为我们需要接收从界面层传过来的值。所以我们需要在Action中声明这个变量。
 * 这是要注意的。
 * @author 李果
 *
 */
@Controller("documentAction")
@Scope("prototype")
public class DocumentAction extends ActionSupport implements ModelDriven<Document>{
	private Document document;
	private String con;
	private Integer isRead;
	private Integer depId;
	private IDocumentService documentService;
	//因为我们需要将一组部门传到界面层中去，所以我们需要使用departmentService。
	private IDepartmentService departmentService;
	
	//和附件相关的
	private File[] atts;
	private String[] attsFileName;
	private String[] attsContentType;
	//一组可以发文部门的Id
	private Integer[] depIds;
	
	
	
	
	

	public Integer[] getDepIds() {
		return depIds;
	}

	public void setDepIds(Integer[] depIds) {
		this.depIds = depIds;
	}

	public File[] getAtts() {
		return atts;
	}

	public void setAtts(File[] atts) {
		this.atts = atts;
	}

	public String[] getAttsFileName() {
		return attsFileName;
	}

	public void setAttsFileName(String[] attsFileName) {
		this.attsFileName = attsFileName;
	}

	public String[] getAttsContentType() {
		return attsContentType;
	}

	public void setAttsContentType(String[] attsContentType) {
		this.attsContentType = attsContentType;
	}

	public String getCon() {
		return con;
	}

	public void setCon(String con) {
		this.con = con;
	}

	public Integer getIsRead() {
		return isRead;
	}

	public void setIsRead(Integer isRead) {
		this.isRead = isRead;
	}

	public Integer getDepId() {
		return depId;
	}

	public void setDepId(Integer depId) {
		this.depId = depId;
	}

	public IDocumentService getDocumentService() {
		return documentService;
	}
	@Resource
	public void setDocumentService(IDocumentService documentService) {
		this.documentService = documentService;
	}

	public IDepartmentService getDepartmentService() {
		return departmentService;
	}
	@Resource
	public void setDepartmentService(IDepartmentService departmentService) {
		this.departmentService = departmentService;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -6638043319186155071L;
	/**
	 * 接收到的某个部门的信息
	 * @return
	 */
	public String listReceive(){
		//将所有的部门都发送过去
		ActionContext.getContext().put("deps", departmentService.listAllDep());
		if(isRead==null||isRead==0){
		ActionContext.getContext().put("pages",documentService.findNotReadDocument(con, depId));
		}else{
		
			ActionContext.getContext().put("pages", documentService.findReadDocument(con, depId));
		}
		
		return SUCCESS;
	}
	
	public String listSend(){
		//注意这里的话，需要使用getSession.get()来获取。否则的话，是获取不到loginUser的。
		//这是要注意的。
		User loginUser=(User) ActionContext.getContext().getSession().get("loginUser");
		ActionContext.getContext().put("pages", documentService.findSendDocument(loginUser.getId(),con));
		return SUCCESS;
	}
	
	/**
	 * 公文的输入
	 * @return
	 */
	public String addInput(){
		User loginUser=(User) ActionContext.getContext().getSession().get("loginUser");
		ActionContext.getContext().put("deps", departmentService.listUserDep(loginUser.getId()));
		return SUCCESS;
	}
	
	 public void validateAdd(){
		  if(depIds==null||depIds.length<=0){
			  this.addFieldError("sus", "必须选择部门");
		  }
		  if(document.getTitle()==null||"".equals(document.getTitle().trim())){
			  this.addFieldError("title", "公文一定要写上标题");
		  }
		  if(hasFieldErrors()){
			  this.addInput();
		  }
	  }
	
	/**
	 * 增加公文
	 * @return
	 * @throws IOException 
	 */
	public String add() throws IOException{
		 if(atts==null||atts.length==0){
			  documentService.add(document, depIds,new AttachDto(false));
		  }else{
	      String uploadPath=ServletActionContext.getServletContext().getRealPath("upload");
	      //我们这里可以使用\\来代替\。否则的话，也是不行的。
	       uploadPath="C:\\Users\\liguo\\javaweb\\document01\\WebContent\\upload";
	     documentService.add(document,depIds,new AttachDto(atts,attsContentType,attsFileName,uploadPath));
		  }
		ActionContext.getContext().put("url", "document_listSend.action");
		return ActionUtil.REDIRECT;
	}

	/**
	 * 查看所发送的公文
	 * 这是要注意的。
	 * @return
	 */
	public String show(){
		  //Document temp=documentService.updateRead(document.getId(),isRead);
		   Document temp=documentService.load(document.getId());
		   
		   /**
		    * 注意这里的话，是完成了一个对象到另外一个对象的复制
		    */
		   try {
			BeanUtils.copyProperties(document, temp);
		} catch (IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}
		   /**
		    * 将所有的对应的可以接收这条公文的部门传送过去
		    * 这是要注意到的。
		    */
		   ActionContext.getContext().put("deps", documentService.listSendDepByDoc(document.getId()));
		   ActionContext.getContext().put("atts",documentService.listAttachByDocument(document.getId()));
		   return SUCCESS;
	}
	/**
	 * 查看所接收的公文
	 * @return
	 */
	public String updateRead(){
		
		 Document temp=documentService.updateRead(document.getId(),isRead);
		   //Document temp=documentService.load(document.getId());
		   
		   /**
		    * 注意这里的话，是完成了一个对象到另外一个对象的复制
		    */
		   try {
			BeanUtils.copyProperties(document, temp);
		} catch (IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}
		   /**
		    * 将所有的对应的可以接收这条公文的部门传送过去
		    * 这是要注意到的。
		    */
		   ActionContext.getContext().put("deps", documentService.listSendDepByDoc(document.getId()));
		   ActionContext.getContext().put("atts",documentService.listAttachByDocument(document.getId()));
		return SUCCESS;
	}
	
	/**
	 * 删除所发送的公文
	 * @return
	 */
	public String delete(){
		documentService.delete(document.getId());
		ActionContext.getContext().put("url", "document_listSend.action");
		return ActionUtil.REDIRECT;
	}
	@Override
	public Document getModel() {
		if(document==null){
			document=new Document();
		}
		//注意这里的话，返回的是document
		//这是要注意的。
		return document;
	}
	



}
