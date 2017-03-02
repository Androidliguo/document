package com.lg.document.test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.lg.document.util.PropertiesUtil;

public class TestPropertiesUtil {
/**
 * 测试配置文件
 */
	@Test
	public void test01(){
		PropertiesUtil util=new PropertiesUtil();
		String string=util.getProp().getProperty("user","aaa");
		System.out.println(string);
	}

}
