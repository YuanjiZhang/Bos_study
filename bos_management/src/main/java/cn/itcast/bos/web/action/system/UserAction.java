package cn.itcast.bos.web.action.system;

import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;

import cn.itcast.bos.domain.system.User;
import cn.itcast.bos.service.system.UserService;
import cn.itcast.bos.web.action.common.BaseAction;

@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("json-default")
public class UserAction extends BaseAction<User>{

	@Autowired
	private UserService userService ;
	
	
	//属性驱动
	private String[] roleIds;
	
	public void setRoleIds(String[] roleIds) {
		this.roleIds = roleIds;
	}

	@Action(value="user_save",results={@Result(name="success",type="redirect"
			,location="pages/system/userlist.html")})
	public String save(){
		//调用业务层
		userService.saveUser(model,roleIds);
		return SUCCESS;
	}

	@Action(value="user_list" ,results={@Result(name="success",type="json")})
	public String list(){
		//调用业务层，返货用户列表
		List<User> users = userService.findAll();
		ActionContext.getContext().getValueStack().push(users);
		return SUCCESS;
	}
	
	
	//退出用户登录
	@Action(value="user_logout",results={@Result(name="success",type="redirect"
			,location="login.html")})
	public String logout(){
		//基于shiro完成退出
		Subject subject = SecurityUtils.getSubject();
		subject.logout();
		return SUCCESS;
	}
	
	//后端服务登录功能
	@Action(value="user_login",results={@Result(name="login",type="redirect",location="login.html"),
			@Result(name="success",type="redirect",location="index.html")})
	public String login(){
		//用户名和密码 都保存在model中
		//基于shiro实现登录
		Subject subject = SecurityUtils.getSubject();
		
		//用户名和密码信息
		AuthenticationToken token = new UsernamePasswordToken(model.getUsername(), model.getPassword());
		
		try {
			subject.login(token);
			//登录成功
			//将用户信息保存到session
			return SUCCESS;
		} catch (AuthenticationException e) {
			//登录失败
			e.printStackTrace();
			return LOGIN;
		}
	}
	
}
