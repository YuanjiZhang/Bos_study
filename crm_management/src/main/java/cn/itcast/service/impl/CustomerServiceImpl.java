package cn.itcast.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.crm.domain.Customer;
import cn.itcast.dao.CustomerRepository;
import cn.itcast.service.CustomerService;

@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {

	@Autowired
	private CustomerRepository customerRepository;
	
	//查询没有关联定区信息的客户
	@Override
	public List<Customer> findNoAssociationCustomers() {
		return customerRepository.findByFixedAreaIdIsNull();
	}

	//查询已经关联定区信息的客户信息
	@Override
	public List<Customer> findHadAssociationCustomers(String fixedAreaId) {
		return customerRepository.findByFixedAreaId(fixedAreaId);
	}

	//将未关联定区的客户，关联定区
	@Override
	public void associationCustomrToFixedArea(String customerIdStr, String fixedAreaId) {
		//先解除关联操作
		customerRepository.clearFixedAreaId(fixedAreaId);
		
		if(StringUtils.isNotBlank(customerIdStr)){
			String[] coustomerIds = customerIdStr.split(",");
			for (String costomerId : coustomerIds) {
				try{
				Integer id = Integer.parseInt(costomerId);
				customerRepository.associationCustomerToFixedArea(fixedAreaId,id);
				}catch(Exception e){
					e.printStackTrace();
					System.out.println("数字不能转化成nul");
				}
				
			}
		}

		
	}

	//增加客户信息
	@Override
	public void regist(Customer customer) {
		customerRepository.save(customer);
	}

	@Override
	public Customer findByTelephone(String telephone) {

		//对查询到的用户个集合长度进行判断
		try {
			Customer customer= customerRepository.findByTelephone(telephone);
			return  customer;
		} catch (Exception e) {
			throw new RuntimeException("该手机号已经被占用！");
		}
		
		
	}

	@Override
	public void updateType(String telephone) {
		customerRepository.updateType(telephone);
	}

	//登录功能查询客户信息
	@Override
	public Customer login(String telephone, String password) {
		return customerRepository.findByTelephoneAndPassword(telephone,password);
	}

	//保存订单——获取定区编号
	@Override
	public String findFixedAreaIdByAddress(String address) {
		System.out.println(address);
		String aa=customerRepository.findFixedAreaIdByAddress(address);
		System.out.println(aa);
		return aa;
	}

}
