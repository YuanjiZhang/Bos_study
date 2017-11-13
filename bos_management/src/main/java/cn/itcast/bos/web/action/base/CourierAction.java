package cn.itcast.bos.web.action.base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
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

	
	//无条件的分页查询所有快递员的信息
	@Action(value="findAll",results={@Result(name="success",type="json")})
	public String findAll(){
		Pageable pageable = new PageRequest(page-1, rows);
		//Page<Courier> pageData =courierService.findAll(pageable);
		
		
		//封装查询语句
		Specification<Courier> specification = new Specification<Courier>() {

			@Override
			public Predicate toPredicate(Root<Courier> root, CriteriaQuery<?> query,
					CriteriaBuilder cb) {
				List<Predicate> list = new ArrayList<Predicate>();
				
				
				//判断工号是否相同   From Courier c  where c.courierNum=?
				if(StringUtils.isNotBlank(courier.getCourierNum())){
					Predicate p1 = cb.equal(root.get("courierNum").as(String.class),courier.getCourierNum() );
					list.add(p1);
				}
				
				//用模糊查询所属单位
				if(StringUtils.isNotBlank(courier.getCompany())){
					Predicate p2 = cb.like(root.get("company").as(String.class), "%"+courier.getCompany()+"%");
					list.add(p2);
				}
				
				//查询类型是否相等
				if(StringUtils.isNoneBlank(courier.getType())){
					Predicate p3 = cb.equal(root.get("type").as(String.class), courier.getType());
					list.add(p3);
				}
				
				//模糊查询收派标准
				Join<Object, Object> standRoot = root.join("standard",JoinType.INNER);
				if(courier.getStandard() != null && StringUtils.isNotBlank(courier.getStandard().getName())){
					Predicate p4 = cb.like(standRoot.get("name").as(String.class), "%"+courier.getStandard().getName()+"%");
					list.add(p4	);
				}
				
				return cb.and(list.toArray(new Predicate[0]));
			}
		};
		
		Page<Courier> pageData = courierService.findAll(specification,pageable);
		
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
