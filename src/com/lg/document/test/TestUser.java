package com.lg.document.test;

import static org.junit.Assert.*;

import java.util.List;
import java.util.Random;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.lg.document.model.User;
import com.lg.document.service.IUserService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/beans.xml")
public class TestUser {
	Random ran = new Random();
	
	@Resource(name="userService")
	private IUserService userService;
	
	
	@Test
	public void initUser() {
		int[] depIds = {2,3,4,5,6,7,8};
		for(int i=0;i<100;i++) {
			User u = new User();
			u.setNickname(getName());
			u.setUsername("user"+i);
			u.setPassword("123");
			u.setEmail("482433034@qq.com");
			u.setType(0);
			userService.add(u, depIds[ran.nextInt(depIds.length)]);
		}
	}
	
	private String getName() {
		String[] name1 = new String[]{"孔","张","叶","李","叶入","孔令",
				"张立","陈","刘","牛","夏侯","令","令狐","赵","母","穆","倪",
				"张毅","称","程","王","王志","刘金","冬","吴","马","沈"};
		
		String[] name2 = new String[]{"凡","课","颖","页","源","都",
				"浩","皓","西","东","北","南","冲","昊","力","量","妮",
				"敏","捷","杰","坚","名","生","华","鸣","蓝","春","虎","刚","诚"};
		
		String[] name3 = new String[]{"吞","明","敦","刀","备","伟",
				"唯","楚","勇","诠","佺","河","正","震","点","贝","侠",
				"伟","大","凡","琴","青","林","星","集","财"};
		
		boolean two = ran.nextInt(50)>=45?false:true;
		if(two) {
			String n1 = name1[ran.nextInt(name1.length)];
			String n2;
			int n = ran.nextInt(10);
			if(n>5) {
				n2 = name2[ran.nextInt(name2.length)];
			} else {
				n2 = name3[ran.nextInt(name3.length)];
			}
			return n1+n2;
		} else {
			String n1 = name1[ran.nextInt(name1.length)];
			String n2 = name2[ran.nextInt(name2.length)];
			String n3 = name3[ran.nextInt(name3.length)];
			return n1+n2+n3;
		}
	}
	
	
	@Test
	public void testListAllUsersCanSend(){
		List<User> users=userService.listUsersCanSend(209);
		for(User user:users){
			System.out.println(user.getNickname());
		}
	}
}
