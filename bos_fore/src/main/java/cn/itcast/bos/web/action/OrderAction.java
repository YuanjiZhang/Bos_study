package cn.itcast.bos.web.action;

import javax.ws.rs.core.MediaType;

import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.itcast.bos.domain.base.Area;
import cn.itcast.bos.domain.take_delivery.Order;
import cn.itcast.crm.domain.Customer;

@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("json-default")
public class OrderAction extends BaseAction<Order>{

	//发件人省市区信息
	private String sendAreaInfo;
	//收件人省市区信息
	private String recAreaInfo;
	public void setSendAreaInfo(String sendAreaInfo) {
		this.sendAreaInfo = sendAreaInfo;
	}
	public void setRecAreaInfo(String recAreaInfo) {
		this.recAreaInfo = recAreaInfo;
	}
	
	
	//添加订单信息
	@Action(value="order_add",results={@Result(name="success",location="index.html",type="redirect")})
	public String add(){
		//手动封装Area关联
		Area sendArea = new Area();
		String[] sendAreaData = sendAreaInfo.split("/");
		sendArea.setProvince(sendAreaData[0]);
		sendArea.setCity(sendAreaData[1]);
		sendArea.setDistrict(sendAreaData[2]);
		
		Area recArea = new Area();
		String[] recAreaData = recAreaInfo.split("/");
		recArea.setProvince(recAreaData[0]);
		recArea.setCity(recAreaData[1]);
		recArea.setDistrict(recAreaData[2]);
		
		model.setSendArea(sendArea);
		model.setRecArea(recArea);
		
		//关联当前登录用户
		Customer customer = (Customer) ServletActionContext.getRequest().getSession().getAttribute("customer");
		System.out.println(customer);
		model.setCustomer_id(customer.getId());
		
		//调用webservice层 将数据传递给bos_management层
		WebClient.create("http://localhost:8080")
			.path("bos_management")
			.path("services")
			.path("orderService")
			.path("order")
			.path("add")
			.accept(MediaType.APPLICATION_JSON)
			.post(model);
		return SUCCESS;
	}
}
