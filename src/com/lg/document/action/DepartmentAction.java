package com.lg.document.action;
/**
 * 如果我们使用了struts2的话，
 * 我们需要注意的一点就是什么呢
 * 我们需要时刻注意表单中究竟
 * 给我们的action中传过来什么样的值
 * 对于这些的话，我们需要在
 * action中将名称设置出来
 * 这是要注意的。
 */

/**
 * 在书写的时候，不能将scope
 * 漏掉，否则的话，很容易就会出错的。
 */

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.lg.document.model.Department;
import com.lg.document.service.IDepartmentService;
import com.lg.document.util.ActionUtil;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

@Controller("departmentAction")
@Scope("prototype")
public class DepartmentAction extends ActionSupport implements ModelDriven<Department>{
	public  String URL="department_list.action";
	private IDepartmentService departmentService;
	/**
	 * 这个属性是可发文部门的Id
	 * 我们定义了set和get方法后，传过来的参数就可以自动地设进去了。这是要注意的。
	 */
	private int[] scopeDepIds;
	
	

	public int[] getScopeDepIds() {
		return scopeDepIds;
	}

	public void setScopeDepIds(int[] scopeDepIds) {
		this.scopeDepIds = scopeDepIds;
	}

	private Department department;
	
	
	public IDepartmentService getDepartmentService() {
		return departmentService;
	}

	@Resource
	public void setDepartmentService(IDepartmentService departmentService) {
		this.departmentService = departmentService;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 2266227100421587401L;
	
	/**
	 * 这是部门列表
	 */
	
	public String list(){
		ActionContext.getContext().put("ds", departmentService.listAllDep());
		return SUCCESS;
	}
	
	/**
	 * 到添加部门的界面
	 */
	public String addInput(){
		return  SUCCESS;
	}
	/**
	 * 这是服务器端的验证。
	 */
	public void validateAdd(){
		if(department.getName()==null||"".equals(department.getName().trim())){
			this.addFieldError("name", "部门名称不能为空");
		}
		
	}
	
	/**
	 * 添加部门
	 */
	
	public String add(){
	    departmentService.add(department);
		ActionContext.getContext().put("url",URL);
		return ActionUtil.REDIRECT;
	}
	
	/**
	 * 删除部门
	 */
	public String delete(){
		departmentService.delete(department.getId());
		ActionContext.getContext().put("url",URL);
		return ActionUtil.REDIRECT;
	}
	
	/**
	 * 到更新部门的输入的界面
	 */
	public String updateInput(){
        Department dep=departmentService.load(department.getId());
        department.setName(dep.getName());
		return SUCCESS;
	}
	
	/**
	 * 这是服务器端的验证。
	 */
	public void validateUpdate(){
			if(department.getName()==null||"".equals(department.getName().trim())){
				this.addFieldError("name", "部门名称不能为空");
			}
		
	}
	
	/**
	 * 真正更新部门
	 */
	
	public String update(){
		departmentService.update(department);
		ActionContext.getContext().put("url",URL);
		return ActionUtil.REDIRECT;
	}
	
	/**
	 * 查看部门的信息
	 */
	public String show(){
		Department dep=departmentService.load(department.getId());
		List<Department> deps=departmentService.listDepScopeDep(department.getId());
		
       // department.setName(dep.getName());
		ActionContext.getContext().put("name", dep.getName());
		ActionContext.getContext().put("deps", deps);
		return SUCCESS;
	}
	
	/**
	 * 其实设置可发文部门的这一部分，也就是复选框的这一部分的内容是比较的复杂的，
	 * 需要经过一系列的过程，我们才能够完成这些操作。
	 * 这是要注意的。
	 * 设置可发文部门
	 * @return
	 */
	public String depScopeInput(){
		Department temp=departmentService.load(department.getId());
		department.setName(temp.getName());
		List<Integer> ids=new ArrayList<Integer>();
		/**
		 * 下面的这几步是什么意思呢？
		 * 其实就是
		 * 我假设是办公室这个部门发文章了
		 * 那么在复选框中就不应该显示办公室这个部门了
		 * 这个方法起的就是这样的作用
		 * 这是要注意的。
		 */
		List<Department> dps=departmentService.listDepScopeDep(department.getId());
		for(int i=0;i<dps.size();i++){
			ids.add(dps.get(i).getId());
		}
		ActionContext.getContext().put("ids", ids);
		List<Department> ds=departmentService.listAllDep();
		/**
		 * 这里的设置是如果id相等的话，就认定俩个对象是相等的。这是要注意的。
		 * 这里的话，必须要在department中重写equals和hasocode
		 * 这俩个方法。否则的话，这里就会出错。
		 * 不过，经过事实证明，这俩个方法也是可写可不写的
		 * 事实上，这俩个方法在这里好像也没有什么作用嘛
		 * 事实胜于雄辩
		 * 这是要注意的。
		 */
		for(int i=0;i<ds.size();i++){
			if(ds.get(i).getId()==department.getId()){
				ds.remove(ds.get(i));
			}
		}
	  //  ds.remove(department);
		ActionContext.getContext().put("ds", ds);
		return SUCCESS;
	}
	
	/**
	 * 处理可发文部门
	 */
	
	public String addDepScope(){
		departmentService.addScopeDeps(department.getId(), scopeDepIds);
        /**
         * 这里需要注意的是这里需要将id传过去，否则的话是不行的。
         */
		ActionContext.getContext().put("url","department_depScopeInput.action?id="+department.getId());
		return ActionUtil.REDIRECT;
	}
	

	@Override
	public Department getModel() {
		if(department==null){
			department=new Department();
		}

		
		return department;
	}

}
