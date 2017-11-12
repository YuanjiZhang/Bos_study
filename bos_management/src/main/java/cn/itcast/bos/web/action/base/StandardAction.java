package cn.itcast.bos.web.action.base;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

import cn.itcast.bos.domain.base.Standard;
import cn.itcast.bos.service.base.StandardService;

@Controller
@Scope("prototype")
@Namespace("/base/standard_action")
@ParentPackage("json-default")
public class StandardAction  extends ActionSupport implements ModelDriven<Standard>{

	private Standard standard = new Standard();
	@Override
	public Standard getModel() {
		return standard;
	}
	
	@Autowired 
	private StandardService standardService;
	
	//定义分页显示数据
	//采用属性注入的方式接收封装表单数据
	private int page;
	private int rows;

	public void setPage(int page) {
		this.page = page;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	@Action(value="findAllStandards",results={@Result(name="success",type="json")})
	public String findAllStandard(){
		
		List<Standard> standards = standardService.findAllStandards();
		ActionContext.getContext().getValueStack().push(standards);
		return SUCCESS;
	}
	
	
	@Action(value="findAll",results={@Result(name="success",type="json")})
	public String findAll(){
		Pageable pageable = new PageRequest(page-1,rows);
		Page<Standard> pageBean = standardService.findAll(pageable);
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("total",pageBean.getTotalElements());
		result.put("rows", pageBean.getContent());
		ActionContext.getContext().getValueStack().push(result);
		
		return SUCCESS;
	}
	
	@Action(value="save",results={@Result(name="success",type="redirect",location="/pages/base/standard.html")})
	public String save(){
		
		standardService.save(standard);
		return SUCCESS;
	}

}
