package com.lg.document.model;
/**
 * 当考虑从界面层传递到业务层的时候，
 * 那么我们就可以考虑使用ThreadLocal
 * 来进行传递。
 * @author 李果
 *
 */
public class SystemContext {
	private static ThreadLocal<Integer> pageOffset=new ThreadLocal<Integer>();
	private static ThreadLocal<Integer> pageSize=new ThreadLocal<Integer>();
	private static ThreadLocal<User>  loginUser=new ThreadLocal<User>();
	/**
	 * 传递删除服务器中的附件的时候路径
	 * @return
	 */
	private static ThreadLocal<String> realPath=new ThreadLocal<String>();
	
	
	public static String getRealPath(){
		return realPath.get();
	}
	
	public static void setRealPath(String _realPath){
		realPath.set(_realPath);
	}
	
	public static void removeRealPath(){
		realPath.remove();
	}
	
	
	public static User getLoginUser(){
		return loginUser.get();
	}
	
	public static void setLoginUser(User _loginUser){
		loginUser.set(_loginUser);
		
	}
	
	public static void removeLoginUser(){
		loginUser.remove();
	}
	
	public static void setPageOffset(int _pageOffset){
		pageOffset.set(_pageOffset);
		
	}
	
	public static int getPageOffset(){
		return pageOffset.get();
	}
	
	
	public static void removePageOffset(){
		pageOffset.remove();
	}
	
	public static void setPageSize(int _pageSize){
		pageSize.set(_pageSize);
		
	}
	
	public static  int getPageSize(){
		return pageSize.get();
	}
	
	public static void removePageSize(){
		pageSize.remove();
	}

}
