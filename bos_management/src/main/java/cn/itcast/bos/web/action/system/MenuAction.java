package cn.itcast.bos.web.action.system;

import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;

import cn.itcast.bos.domain.system.Menu;
import cn.itcast.bos.domain.system.User;
import cn.itcast.bos.service.system.MenuService;
import cn.itcast.bos.web.action.common.BaseAction;

@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("json-default")
public class MenuAction extends BaseAction<Menu> {

	@Autowired
	private MenuService menuService;

	@Action(value="menu_showmenu",results={@Result(name="success",type="json")})
	public String showMenu(){
		//调用业务层，查询当前用户具有的菜单列表
		Subject subject = SecurityUtils.getSubject();
		User user = (User) subject.getPrincipal();
		
		List<Menu> menus = menuService.findByUser(user);
		
		ActionContext.getContext().getValueStack().push(menus);
		return SUCCESS;
	}
	
	//添加menu的方法
	@Action(value="menu_save",results={@Result(name="success",type="redirect",location="pages/system/menu.html")})
	public String save(){
		menuService.save(model);
		return SUCCESS;
	}
	
	// 查询所有菜单
	@Action(value = "menu_list", results = { @Result(name = "success", type = "json") })
	public String findAll() {
		List<Menu> menus = menuService.findAll();
		ActionContext.getContext().getValueStack().push(menus);
		return SUCCESS;
	}

}
