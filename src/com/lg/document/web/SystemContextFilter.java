package com.lg.document.web;

/**
 * 这里的话，我一直想要提醒的一点就是什么呢？
 * 如果我们想要通过界面层像业务层传递数据的话，
 * 那么我们就需要使用ThreadLocal来进行
 * 否则的话，是不行的。这是要这注意的。
 * 
 * 可能会有的一个疑问是什么呢？
 * 为什么我们不适用ActionContext.getContext.getSession()来获取数据呢
 * 其实这是有原因的。因为这个Filter是比Struts的拦截器更前。这是要注意的。
 * 所有的请求过来的时候，都会首先经过这个拦截器对象。
 */
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import com.lg.document.model.SystemContext;
import com.lg.document.model.User;
/**
 * 这个就是我们的过滤器。
 * 所有的请求在过来的时候，都会经过这个过滤器的。
 * 如果我们需要从界面层中获取数据的时候，那么我们就可以在这里进行相关的设置
 * 注意这个拦截器是在struts2拦截器之前起作用的。
 * 这是要注意的。
 * @author 李果
 *
 */
public class SystemContextFilter implements Filter {
	private int pageSize;

	@Override
	public void destroy() {

	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
			throws IOException, ServletException {
		try {
			int tps=pageSize;
			int pageOffset=0;
			try {
				tps=Integer.parseInt(req.getParameter("pageSize"));
			} catch (NumberFormatException e) {
			}
			try {
				pageOffset=Integer.parseInt(req.getParameter("pager.offset"));
				/**
				 * 这里必须要注意的问题是什么呢？
				 * 这里的话，我们不能把getAttribute("loginUser")
				 * 的内容放在这里，否则的话是会出错的。
				 * 这是要注意的。
				 * 为什么呢？
				 * 
				 */
				/*HttpServletRequest hreq=(HttpServletRequest) req;
				User loginUser=(User) hreq.getSession().getAttribute("loginUser");
				if(loginUser!=null){
					SystemContext.setLoginUser(loginUser);
				}*/

			} catch (NumberFormatException e) {
			}
			HttpServletRequest hreq=(HttpServletRequest) req;
			/**
			 * 注意在这里的话，我们也不能使用ActionContext
			 * 还是因为servlet的过滤器在struts2的过滤器之前
			 * 所以我们在这里是不能够使用ActionContext的
			 * 这是要注意的。
			 * 
			 */
			/*System.out.println("#################################");
			User loginUser2=(User) ActionContext.getContext().getSession().get("loginUser");
			if(loginUser2!=null){
			System.out.println(loginUser2.getNickname());
			}else{
				System.out.println("loginUser的value为null");
			}*/
			User loginUser=(User) hreq.getSession().getAttribute("loginUser");
			if(loginUser!=null){
				SystemContext.setLoginUser(loginUser);
			}
			/**
			 * 这里的话，由于servlet的过滤器是在struts2的过滤器之前的。那么
			 * 在这里的话，只有在struts2的过滤器起作用的时候，才能够使用
			 * servletActionContext.否则，是不能使用的。
			 * 不然的话，就会出现错误
			 */
		   // String path=ServletActionContext.getServletContext().getRealPath("");
		   // System.out.println(path);
			//注意这个方法的使用。这是要注意的。ServletContext，说白了就是application。这是要注意的。
			String realPath=hreq.getSession().getServletContext().getRealPath("");
		      //我们这里可以使用\\来代替\。否则的话，也是不行的。。。。。。
		     realPath="C:\\Users\\liguo\\javaweb\\document01\\WebContent";
		    SystemContext.setRealPath(realPath);
			SystemContext.setPageOffset(pageOffset);
			SystemContext.setPageSize(tps);
			//这里的话，一定要加上这句话，否则的话，程序是不会往下执行的。这是要注意的。
			chain.doFilter(req, resp);
			
		} finally {
			SystemContext.removePageOffset();
			SystemContext.removePageSize();
			SystemContext.removeLoginUser();
			SystemContext.removeRealPath();
		}
		
		
		

	}

	@Override
	public void init(FilterConfig cfg) throws ServletException {
		try {
			pageSize=Integer.parseInt(cfg.getInitParameter("pageSize"));
		} catch (NumberFormatException e) {
		}

	}

}
