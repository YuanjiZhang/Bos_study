package cn.itcast.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import cn.itcast.crm.domain.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Integer>{

	//查询没有被关联的所有客户
	public List<Customer> findByFixedAreaIdIsNull();

	//查询已经被关联定区的客户
	public List<Customer> findByFixedAreaId(String fixedAreaId);

	//是客户关联定区
	@Modifying
	@Query("update Customer set fixedAreaId = ? where id = ?")
	public void associationCustomerToFixedArea(String fixedAreaId,Integer id);

	//解除定区关联客户信息
	@Modifying
	@Query("update Customer set fixedAreaId = null where fixedAreaId = ?")
	public void clearFixedAreaId(String fixedAreaId);

	
}
