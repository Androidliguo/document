package com.lg.document.util;

import java.io.IOException;
import java.util.Properties;
/**
 * 注意这里的话，是设置了权限的控制问题
 * 而且，一定要注意的是什么呢
 * 在加载这种配置文件的时候，一定需要
 * 使用单例模式。
 * 否则的话，肯定是会影响我们的效率的。
 * 这是要注意的。
 * @author 李果
 *
 */

public class PropertiesUtil {
	private static  Properties prop;
	
	public static Properties getProp(){
		if(prop==null){
			prop=new Properties();
			try {
				/**
				 * 加载对应的配置文件
				 * 这个方法需要记住，
				 * 也就是如何加载根目录下的配置文件的问题
				 * 这是要注意的。
				 */
				prop.load(PropertiesUtil.class.getClassLoader().getResourceAsStream("auth.properties"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return prop;
	}

}