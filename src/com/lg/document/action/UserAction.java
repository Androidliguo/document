package com.lg.document.action;
import javax.annotation.Resource;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import com.lg.document.model.Pager;
import com.lg.document.model.User;
import com.lg.document.service.IDepartmentService;
import com.lg.document.service.IUserService;
import com.lg.document.util.ActionUtil;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
/**
 * 在com.lg.document.model中，
 * 几乎是每一个class就是数据库中的一张表。这是要注意的。
 * @author 李果
 *
 */
@Controller("userAction")
@Scope("prototype")
public class UserAction  extends ActionSupport implements ModelDriven<User>{
	public  String URL="user_list.action";
	private User user;
	private IUserService userService;
	private IDepartmentService departmentService;
	/**
	 * 这个人是属于哪一个部门的。这是要注意的
	 */
	private Integer depId;
	public Integer getDepId() {
		return depId;
	}

	public void setDepId(Integer depId) {
		this.depId = depId;
	}

	public IDepartmentService getDepartmentService() {
		return departmentService;
	}

	@Resource
	public void setDepartmentService(IDepartmentService departmentService) {
		this.departmentService = departmentService;
	}

	public IUserService getUserService() {
		return userService;
	}

	@Resource
	public void setUserService(IUserService userService) {
		this.userService = userService;
	}

	public String list(){
		//选一个部门来进行查找。
		ActionContext.getContext().put("ds", departmentService.listAllDep());
		//如果depId为null或者是小于0，那么就表示查询的是所有的user.
		Pager<User> pages=userService.findUsersByDepId(depId);
		ActionContext.getContext().put("pages",pages);
		ActionContext.getContext().put("totalRecord",pages.getTotalRecord());
		return SUCCESS;
	}
	
	public String addInput(){
		ActionContext.getContext().put("ds",departmentService.listAllDep());
		return SUCCESS;
	}
	
	/**
	 * 这里的话，是用来做验证的。当出现错误的时候，必须注意
	 * 要将ds重新进行发送。
	 * 这是服务器端验证。
	 */
	public void validateAdd(){
		if(user.getUsername()==null||"".equals(user.getUsername().trim())){
			this.addFieldError("username", "用户名称不能为空");
		}
		
		if(user.getPassword()==null||"".equals(user.getPassword().trim())){
			this.addFieldError("password", "用户密码不能为空");
		}
		
		if(user.getEmail()==null||"".equals(user.getEmail().trim())){
			this.addFieldError("email", "必须填写邮箱");
		}
		//如果没有这个的话，那么部门的信息就不会传到界面层。
		//那么你就不可以选择user是属于哪一个用户的了。这是要注意的。
		if(this.hasErrors()){
			this.addInput();
		}
	}
	
	
	public String add(){
		if(userService.loadByUsername(user.getUsername())!=null){
			throw new com.lg.document.exception.DocumentException("所添加的用户已经存在了");
		}
		userService.add(user,depId);
		ActionContext.getContext().put("url", URL);
		return  ActionUtil.REDIRECT;
	}
	
	/**
	 * 更新的输入 。这是要注意的。
	 * 这里的话，由于始终user是位于compoundpool中的，也就是顶层。
	 * 那么在页面中，<s:form>就可以根据相应的属性的值来进行注入。
	 * 这是要注意的。
	 * depId是位于userAction中的，
	 * 而username,nickname,password,email
	 * 是位于user中的。这是要注意的。
	 * @return
	 */
	public String updateInput(){
		User temp=userService.load(user.getId());
		depId=temp.getDepartment().getId();
		user.setEmail(temp.getEmail());
		user.setUsername(temp.getUsername());
	    user.setNickname(temp.getNickname());
	    user.setPassword(temp.getPassword());
	    user.setType(temp.getType());
	    ActionContext.getContext().put("ds", departmentService.listAllDep());
		return SUCCESS;
	}
	/**
	 * 注意这里是validateUpdate，而不是updateUpdateInput
	 */
	
	public void validateUpdate(){
		if(user.getUsername()==null||"".equals(user.getUsername().trim())){
			this.addFieldError("username", "用户名称不能为空");
		}
		
		if(user.getPassword()==null||"".equals(user.getPassword().trim())){
			this.addFieldError("password", "用户密码不能为空");
		}
		
		if(user.getEmail()==null||"".equals(user.getEmail().trim())){
			this.addFieldError("email", "必须填写邮箱");
		}
		
		if(this.hasErrors()){
			this.updateInput();
		}
	}
	
	public String update(){
		userService.update(user, depId);
		ActionContext.getContext().put("url", URL);
		return  ActionUtil.REDIRECT;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 332323773920681811L;
	
	/**
	 * 删除操作
	 * @return
	 */
	public String delete(){
		//点击的时候，通过链接将id值传递过来。
		userService.delete(user.getId());
		ActionContext.getContext().put("url", URL);
		return  ActionUtil.REDIRECT;
	}
	
	/**
	 * 查看操作
	 */
	public String show(){
		//不能直接赋值。
		User temp=userService.load(user.getId());
		depId=temp.getDepartment().getId();
		user.setEmail(temp.getEmail());
		user.setUsername(temp.getUsername());
	    user.setNickname(temp.getNickname());
	    user.setPassword(temp.getPassword());
	    user.setDepartment(temp.getDepartment());
	    user.setType(temp.getType());
		return SUCCESS;
	}
	
	/**
	 * 登录用户更新自己的信息
	 */
	  public String updateSelfInput(){
		  User temp=userService.load(user.getId());
			depId=temp.getDepartment().getId();
			user.setEmail(temp.getEmail());
			user.setUsername(temp.getUsername());
		    user.setNickname(temp.getNickname());
		    user.setPassword(temp.getPassword());
		  return SUCCESS;
	  }
	  
	  
	  /**
	   * 服务器端进行验证
	   * 
	   */
	  
	  public void validateUpdateSelf(){
			if(user.getUsername()==null||"".equals(user.getUsername().trim())){
				this.addFieldError("username", "用户名称不能为空");
			}
			
			if(user.getPassword()==null||"".equals(user.getPassword().trim())){
				this.addFieldError("password", "用户密码不能为空");
			}
			
			if(user.getEmail()==null||"".equals(user.getEmail().trim())){
				this.addFieldError("email", "必须填写邮箱");
			}
		}
		
	  /**
	   * 
	   */
	 public String updateSelf(){
		 /**
		  * 这里的话，必须要这样子做如果
		  * 直接像上面那样子的话，也就是
		  *  userService.updateSelf(temp);
		  *  ，那么就会出现nullpointer的问题。
		  *  因为这个时候的话，由于我们更新自己的信息的时候
		  *  部门是不能自己更新的。那么我们这样子做的话，
		  *  也就是这时候的department的值为null
		  *  所以我们不能这样子进行设置。
		  *  否则的话，是会出错的。
		  *  这是要注意的。
		  */
		  User temp=userService.load(user.getId());
		  temp.setEmail(user.getEmail());
		  temp.setNickname(user.getNickname());
		  temp.setUsername(user.getUsername());
		  temp.setPassword(user.getPassword());
		  userService.updateSelf(temp);
		  /**
		   * 这里必须要注意的问题是什么呢？这里的话，必须要将id传过去，
		   * 否则的话，在showSelf中得到的id的值就为0
		   * 这是要注意的。
		   */
		  ActionContext.getContext().put("url", "user_showSelf.action?id="+user.getId());
		  return ActionUtil.REDIRECT;
	  }
	  
	  
	  
	  /**
	   * 登录用户查看自己的个人的信息
	   */
	  
	  public String showSelf(){
		  User temp=userService.load(user.getId());
			depId=temp.getDepartment().getId();
			user.setEmail(temp.getEmail());
			user.setUsername(temp.getUsername());
		    user.setNickname(temp.getNickname());
		    user.setPassword(temp.getPassword());
		    user.setDepartment(temp.getDepartment());
		    user.setType(temp.getType());
		  return SUCCESS;
	  }
	  
	

	@Override
	public User getModel() {
		if(user==null){
			user=new User();
		}
		return user;
	}
	

}
