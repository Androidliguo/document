package com.lg.document.web;

import org.springframework.stereotype.Component;

import com.lg.document.exception.DocumentException;
import com.lg.document.model.User;
import com.lg.document.util.ActionUtil;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
/**
 * 创建了一个拦截器对象。所有的请求过来的时候，都会经过这样的一个拦截器的对象。
 * 主要是确定登录的人员是否是管理员还是其它的用户
 * 但是在实际的开发中，我们可能很少会使用这样的方式来进行权限的设置。
 * 但是因为在这里的话，由于是比较简单的一个小项目，所以就使用了这样的方式来进行设置。
 * 这里想提出的一个问题是是通过ThreadLocal来传，还是通过ActionContext来
 * 进行传值呢？通过怎样的方式来获取值呢？
 * @author 李果
 *
 */
@Component("authInterceptor")
public class AuthInterceptor extends AbstractInterceptor{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3573047429480958483L;

	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		/**
		 * 得到具体的访问的名称
		 * 比如说是message_list
		 * login_addInput等的信息
		 */
		String url=invocation.getProxy().getActionName();
		User loginUser=(User) ActionContext.getContext().getSession().get("loginUser");
		if(!url.startsWith("login_")){
			if(loginUser==null){
				return "login";
			}
			if(loginUser.getType()!=1){
				if(!ActionUtil.checkUrl(url)){
					throw new DocumentException("只有管理员才能够访问该功能");
				}
			}
		}
		/**
		 * 最后一定要加上invocation.invoke()
		 * 然后程序才能够继续往下执行。
		 */
		return invocation.invoke();
	}

}
