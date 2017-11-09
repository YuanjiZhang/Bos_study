package cn.itcast.bos.web.action.base;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

import cn.itcast.bos.domain.base.Standard;
import cn.itcast.bos.service.base.StandardService;

@Controller
@Scope("prototype")
@Namespace("/base/standard_action")
@ParentPackage("struts-default")
public class StandardAction  extends ActionSupport implements ModelDriven<Standard>{

	private Standard standard = new Standard();
	@Override
	public Standard getModel() {
		return standard;
	}
	
	@Autowired 
	private StandardService standardService;
	
	@Action(value="save",results={@Result(name="success",type="redirect",location="/pages/base/standard.html")})
	public String save(){
		
		standardService.save(standard);
		return SUCCESS;
	}

}
