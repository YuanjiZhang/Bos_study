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
import org.apache.struts2.ServletActionContext;
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
import cn.itcast.bos.web.action.common.BaseAction;

@Controller
@Scope("prototype")
@Namespace("/base/courier_action")
@ParentPackage("json-default")
public class CourierAction extends BaseAction<Courier> {

	@Autowired
	private CurierService courierService;
	
	
	//查询未关联定区快递员信息
	@Action(value="courier_findnoassociation",results={@Result(name="success",type="json")})
	public String findnoassociation(){
		//调用业务层查询未关联快递员信息
		List<Courier> couriers = courierService.findNoAssociation();
		//将查询道德快递员列表压入栈顶
		ActionContext.getContext().getValueStack().push(couriers);
		return SUCCESS;
	}
	
	//还原快递员
	@Action(value="fix",results={@Result(name="success",location="/pages/base/courier.html",type="redirect")})
	public String fix(){
		String ids = ServletActionContext.getRequest().getParameter("ids");
		courierService.fix(ids);
		return SUCCESS;
	}
	
	
	//作废快递员
	@Action(value="del",results={@Result(name="success",location="/pages/base/courier.html",type="redirect")})
	public String del(){
		String ids = ServletActionContext.getRequest().getParameter("ids");
		courierService.del(ids);
		return SUCCESS;
	}
	
	
	//无条件的分页查询所有快递员的信息
	@Action(value="findAll",results={@Result(name="success",type="json")})
	public String findAll(){
		Pageable pageable = new PageRequest(page-1, rows);
		
		
		//封装查询语句
		Specification<Courier> specification = new Specification<Courier>() {

			@Override
			public Predicate toPredicate(Root<Courier> root, CriteriaQuery<?> query,
					CriteriaBuilder cb) {
				List<Predicate> list = new ArrayList<Predicate>();
				
				
				//判断工号是否相同   From Courier c  where c.courierNum=?
				if(StringUtils.isNotBlank(model.getCourierNum())){
					Predicate p1 = cb.equal(root.get("courierNum").as(String.class),model.getCourierNum() );
					list.add(p1);
				}
				
				//用模糊查询所属单位
				if(StringUtils.isNotBlank(model.getCompany())){
					Predicate p2 = cb.like(root.get("company").as(String.class), "%"+model.getCompany()+"%");
					list.add(p2);
				}
				
				//查询类型是否相等
				if(StringUtils.isNoneBlank(model.getType())){
					Predicate p3 = cb.equal(root.get("type").as(String.class), model.getType());
					list.add(p3);
				}
				
				//模糊查询收派标准
				Join<Object, Object> standRoot = root.join("standard",JoinType.INNER);
				if(model.getStandard() != null && StringUtils.isNotBlank(model.getStandard().getName())){
					Predicate p4 = cb.like(standRoot.get("name").as(String.class), "%"+model.getStandard().getName()+"%");
					list.add(p4	);
				}
				
				return cb.and(list.toArray(new Predicate[0]));
			}
		};
		
		Page<Courier> pageData = courierService.findAll(specification,pageable);
		
		pushPageDateToValustack(pageData);
		
		return SUCCESS;
	}

	//添加快递员信息
	@Action(value="save",results={@Result(name="success",location="/pages/base/courier.html",type="redirect")})
	public String save(){
		courierService.save(model);
		
		return SUCCESS;
	}
	
}
