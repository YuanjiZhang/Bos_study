package cn.itcast.bos.web.action.base;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.jaxrs.client.WebClient;
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

import cn.itcast.bos.domain.base.FixedArea;
import cn.itcast.bos.service.base.FixedAreaService;
import cn.itcast.bos.web.action.common.BaseAction;
import cn.itcast.crm.domain.Customer;

@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("json-default")
public class FixedAreaAction extends BaseAction<FixedArea>{
	
	@Autowired
	private FixedAreaService fixedAreaService;

	//属性驱动
	private String[] customerIds;

	public void setCustomerIds(String[] customerIds) {
		this.customerIds = customerIds;
	}
	
	//又是属性驱动
	private Integer courierId;
	private Integer takeTimeId;
	

	public void setCourierId(Integer courierId) {
		this.courierId = courierId;
	}

	public void setTakeTimeId(Integer takeTimeId) {
		this.takeTimeId = takeTimeId;
	}

	//关联快递员到定区
	@Action(value="fixedArea_associationCourierToFixedArea",results={@Result(
			name="success",type="redirect",location="/pages/base/fixed_area.html")})
	public String associationCourierToFixedArea(){
		fixedAreaService.associationCourierToFixedArea(model,courierId,takeTimeId);
		return SUCCESS;
	}
	
	
		//提交关联客户订单 使客户完成关联定区操作
	// 关联客户到定区
		@Action(value = "fixedArea_associationCustomersToFixedArea", results = { @Result(name = "success", type = "redirect", location = "./pages/base/fixed_area.html") })
		public String associationCustomersToFixedArea() {
			String customerIdStr = StringUtils.join(customerIds, ",");
			WebClient.create(
					"http://localhost:9002/crm_management/services/customerService"
							+ "/associationCustomerToFixedArea?customerIdStr="
							+ customerIdStr + "&fixedAreaId=" + model.getId()).put(
					null);
			return SUCCESS;
		}
	
	//异步查询已关联定区客户信息
	@Action(value="fixedArea_findHasAssociationFixedAreaCustomers",results={@Result(name="success",type="json")})
	public String findHasAssociationFixedAreaCustomers() {
		// 使用webClient调用 webService接口
		Collection<? extends Customer> collection = WebClient
				.create("http://localhost:9002/crm_management/services/customerService/hadAssociationCustomers/"
						+ model.getId()).accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON).getCollection(Customer.class);
		ActionContext.getContext().getValueStack().push(collection);
		return SUCCESS;
	}
	
	//异步查询未关联定区客户信息
	@Action(value="fixedArea_findNoAssociationCustomers",results={@Result(name="success",type="json")})
	public String findNoAssociationCustomers(){
		
		Collection<? extends Customer> collection = WebClient.create("http://localhost:9002/")
		.path("crm_management")
		.path("services")
		.path("customerService")
		.path("noAssociationCustomers")
		.accept(MediaType.APPLICATION_XML)
		.getCollection(Customer.class);
		
		ActionContext.getContext().getValueStack().push(collection);
		return SUCCESS;
	}
	
	//分页查询
	@Action(value="fixedArea_findAll",results={@Result(name="success",type="json")})
	public String findAll(){
		Pageable pageable = new PageRequest(page-1, rows);
		
		Specification<FixedArea> spec = new Specification<FixedArea>() {

			@Override
			public Predicate toPredicate(Root<FixedArea> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

				List<Predicate> list = new ArrayList<>();
				//查询地区编码
				if(StringUtils.isNotBlank(model.getFixedAreaName())){
					Predicate p1 = cb.like(root.get("fixedAreaName").as(String.class),"%"+model.getFixedAreaName()+"%" );
					list.add(p1);
				}
				
				//查询所属单位
				if(StringUtils.isNotBlank(model.getCompany())){
					Predicate p2 = cb.like(root.get("company").as(String.class), "%"+model.getCompany()+"%");
					list.add(p2);
				}
				return cb.and(list.toArray(new Predicate[0]));
			}
		};
		Page<FixedArea> pageData = fixedAreaService.findAll(spec,pageable);
		
		pushPageDateToValustack(pageData);
		return SUCCESS;
	}

	//增加定区方法
	@Action(value="fixedArea_save",results={@Result(name="success",location="/pages/base/fixed_area.html",type="redirect")})
	public String save(){
		fixedAreaService.save(model);
		return SUCCESS;
	}
}
