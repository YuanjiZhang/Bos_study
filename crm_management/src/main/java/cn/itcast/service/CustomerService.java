package cn.itcast.service;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import cn.itcast.crm.domain.Customer;

public interface CustomerService {
	
	//查询所有未关联客户列表
	@GET
	@Path("/noAssociationCustomers")
	@Produces({"application/xml","application/json"})
	public List<Customer> findNoAssociationCustomers();
	
	//查询已关联客户列表
	@GET
	@Path("/hadAssociationCustomers/{fixedareaid}")
	@Produces({"application/xml","application/json"})
	public List<Customer> findHadAssociationCustomers(
			@PathParam("fixedareaid") String fixedAreaId);
	
	//关联客户到定区，将所有客户id 拼成字符串
	@PUT
	@Path("/associationCustomerToFixedArea")
	public void associationCustomrToFixedArea(
			@QueryParam("customerIdStr") String customerIdStr,
			@QueryParam("fixedAreaId") String fixedAreaId);
	
	//实现保存客户信息
	@POST
	@Path("/customer")
	@Consumes({"application/xml","application/json"})
	public void regist(Customer customer);
	
	
	//通过手机号查询客户信息
	@GET
	@Path("customer/telephone/{telephone}")
	@Produces({"application/xml","application/json"})
	public Customer findByTelephone(@PathParam("telephone") String telephone);
	
	//确认无误后更改用户状态
	@PUT
	@Path("/customer/updatetype/{telephone}")
	public void updateType(@PathParam("telephone") String telephone);
	
	//客户登录功能——查询客户信息
	@GET
	@Path("/customer/login")
	@Consumes({"application/xml","application/json"})
	public Customer login(@QueryParam("telephone")
	String telephone,@QueryParam("password") String password);
	
	
	/*//保存订单——获取定区编码
	@GET
	@Path("/customer/findFixedAreaIdByAddress/{address}")
	@Produces({"application/xml","application/json"})
	public String findFixedAreaIdByAddress(@PathParam("address") String address);*/
	//保存订单——获取定区编码
	@GET
	@Path("/customer/findFixedAreaIdByAddress")
	@Produces({"application/xml","application/json"})
	public String findFixedAreaIdByAddress(@QueryParam("address") String address);
	
}
