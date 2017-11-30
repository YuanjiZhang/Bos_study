package cn.itcast.bos.dao.take_delivery;

import org.springframework.data.jpa.repository.JpaRepository;

import cn.itcast.bos.domain.take_delivery.Order;

public interface OrderRepository extends JpaRepository<Order, Integer>{

	//根据订单号查询订单数据，进行运单界面订单信息回显
	Order findByOrderNum(String orderNum);

}
