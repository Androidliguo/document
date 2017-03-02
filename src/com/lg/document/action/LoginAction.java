package com.lg.document.action;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.lg.document.model.User;
import com.lg.document.service.IUserService;
import com.lg.document.util.ActionUtil;
import com.opensymphony.xwork2.ActionContext;
/**
 * 在Action中，我想提醒的一点是什么呢？
 * 在一般的情况下，我们的action肯定是需要从一个表单form中获取
 * 数据的。那么在struts中，如果需要这样做的话，
 * 那么我们就需要在action中将对应的属性的名称或者类的对象的名称写下来
 * 这样的话，在表单中当值传过来的时候，我们就可以在action中
 * 将相对应的值设进来。这是要注意的。
 * @author 李果
 *
 */
@Controller("loginAction")
@Scope("prototype")
public class LoginAction {
    private String username;
    private String password;
    private IUserService userService;
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public IUserService getUserService() {
		return userService;
	}
	@Resource
	public void setUserService(IUserService userService) {
		this.userService = userService;
	}
	
	public String loginInput(){
		return "login";
	}
	
	public String login(){
		User loginUser=userService.loginByUsernameAndPassword(username, password);
		ActionContext.getContext().getSession().put("loginUser", loginUser);
		ActionContext.getContext().put("url","user_showSelf.action?id="+loginUser.getId());
		return ActionUtil.REDIRECT;
	}
	
    public String logout(){
    	ActionContext.getContext().getSession().clear();
    	return "login";
    }
    
    
    
}
