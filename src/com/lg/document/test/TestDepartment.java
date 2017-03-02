package com.lg.document.test;

import static org.junit.Assert.*;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.lg.document.model.Department;
import com.lg.document.service.IDepartmentService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/beans.xml")	
public class TestDepartment {
	
	@Resource
	private IDepartmentService departmentService;

	
	@Test
	public void testAdd(){
		
		Department department=new Department();
		department.setName("食堂");
		departmentService.add(department);
		
	}
	@Test
	public void testAddScopeDep(){
		departmentService.addScopeDep(2, 5);
	}
	@Test
	public void testAddScopeDeps(){
		departmentService.addScopeDeps(4,new int[]{1,2,3,5});
	}
	@Test
	public void testListDepScopeDep(){
		List<Department> deps=departmentService.listDepScopeDep(4);
		for(Department department:deps){
			System.out.println(department.getName());
		}
	}
	
	@Test
	public void testDelScopeDep(){
		departmentService.deleteScopeDep(4, 2);
	}
	
	@Test
	public void testListAllDep(){
		List<Department> departments=departmentService.listAllDep();
		for(int i=0;i<departments.size();i++){
			System.out.println(departments.get(i).getName());
		}
	}
	
	@Test
	public void testListUserDep(){
		List<Department> deps=departmentService.listUserDep(130);
		
	}

}
