package cn.itcast.service.impl.test;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.itcast.service.CustomerService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:applicationContext.xml")
public class CustomerServiceImplTest {

	@Autowired
	private CustomerService customerService;
	@Test
	public void testFindNoAssociationCustomers() {
		System.out.println(customerService.findNoAssociationCustomers());
	}

	@Test
	public void testFindHadAssociationCustomers() {

		System.out.println(customerService.findHadAssociationCustomers("dp001"));
	}

	@Test
	public void testAssociationCustomrToFixedArea() {
		customerService.associationCustomrToFixedArea("2", "dp001");
	}

}
