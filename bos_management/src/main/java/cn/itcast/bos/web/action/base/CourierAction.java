package cn.itcast.bos.web.action.base;

import java.util.HashMap;
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

import cn.itcast.bos.domain.base.Courier;
import cn.itcast.bos.service.base.CurierService;

@Controller
@Scope("prototype")
@Namespace("/base/courier_action")
@ParentPackage("json-default")
public class CourierAction extends ActionSupport implements ModelDriven<Courier> {

	//模型封装数据
	private Courier courier = new Courier();
	@Override
	public Courier getModel() {
		return courier;
	}

	@Autowired
	private CurierService courierService;
	
	
	//接收页面传来数据
	private int page;
	private int rows;
	
	public void setPage(int page) {
		this.page = page;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	
	//分页查询所有快递员的信息
	@Action(value="findAll",results={@Result(name="success",type="json")})
	public String findAll(){
		Pageable pageable = new PageRequest(page-1, rows);
		Page<Courier> pageData =courierService.findAll(pageable);
		
		/**
		 * 将查询的信息以
		 * ｛total:n,
		 * rows:[{
		 * objece
		 * }]
		 * ｝的键值对的形式
		 */
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("total", pageData.getTotalElements());
		map.put("rows", pageData.getContent());
		
		//将map集合压入栈顶转化成json数据
		ActionContext.getContext().getValueStack().push(map);
		
		return SUCCESS;
	}



	//添加快递员信息
	@Action(value="save",results={@Result(name="success",location="/pages/base/courier.html",type="redirect")})
	public String save(){
		courierService.save(courier);
		
		return SUCCESS;
	}
	

	
}
