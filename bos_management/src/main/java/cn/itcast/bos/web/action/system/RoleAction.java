package cn.itcast.bos.web.action.system;

import java.util.List;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;

import cn.itcast.bos.domain.system.Role;
import cn.itcast.bos.service.system.RoleService;
import cn.itcast.bos.web.action.common.BaseAction;

@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("json-default")
public class RoleAction  extends BaseAction<Role>{

	@Autowired
	private RoleService roleService;
	
	private String [] permissionIds;
	private String menuIds;
	
	public void setPermissionIds(String[] permissionIds) {
		this.permissionIds = permissionIds;
	}

	public void setMenuIds(String menuIds) {
		this.menuIds = menuIds;
	}

	@Action(value="role_save",results={@Result(name="success",type="redirect",
			location="pages/system/role.html")})
	public String save(){
		//调用业务层，保存角色
		roleService.saveRole(model,permissionIds,menuIds);
		return SUCCESS;
	}
	
	@Action(value="role_list",results={@Result(name="success",type="json")})
	public String findAll(){
		List<Role> roles = roleService.findAll(model);
		ActionContext.getContext().getValueStack().push(roles);
		return SUCCESS;
	}
}
