package cn.itcast.bos.dao.take_delivery;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;

import cn.itcast.bos.domain.take_delivery.WayBill;

public interface WayBillRepository extends JpaRepository<WayBill, Integer>{

	
	//根据快速录入的waybillNum查询回显信息
	WayBill findByWayBillNum(String wayBillNum);

}
