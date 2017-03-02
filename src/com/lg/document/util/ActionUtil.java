package com.lg.document.util;

import java.util.Properties;

public class ActionUtil {
	public static String REDIRECT="redirect";
	/**
	 * 采用以下的方式来确定访问权限的缺点是
	 * 每次都需要从配置文件中读取
	 * 这样的话，就可能会影响效率
	 * 
	 * 还有一种方法就是
	 * 在用户登录的时候，
	 * 我就将这个存到session中，
	 * 这样的话，直接到内存当中去取就可以了
	 * 这样子的话，就可以节省效率了
	 */
	
	/**
	 * 获取用户能够访问的
	 */

	public static String[] getUserAuth(){
		Properties prop=PropertiesUtil.getProp();
		String url=prop.getProperty("user");
		return url.split(",");
	}
	/**
	 * 获取用户不能访问的
	 */
	public static String[] getUserNotAuth(){
		Properties prop=PropertiesUtil.getProp();
		String url=prop.getProperty("admin");
		return url.split(",");
		
	}
	
	/**
	 * 请求过来的时候，判断能否访问
	 */
	
	public static boolean checkUrl(String action){
		for(String url:getUserAuth()){
			if(action.startsWith(url)){
				return true;
			}
		}
		
		for(String url:getUserNotAuth()){
			if(action.startsWith(url)){
				return false;
			}
		}
		
		return true;
	}
}
