package cn.itcast.bos.service.take_delivery.impl;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.take_delivery.WayBillRepository;
import cn.itcast.bos.domain.take_delivery.WayBill;
import cn.itcast.bos.service.take_delivery.WayBillService;

@Service
@Transactional
public class WayBillServiceImpl implements WayBillService {

	@Autowired
	private WayBillRepository wayBillRepository;
	@Override
	public void save(WayBill model) {
		//判断运单号是否存在
		WayBill persistWayBill = wayBillRepository.findByWayBillNum(model.getWayBillNum());
		if(persistWayBill.getId() == null){
			//运单不存在
			wayBillRepository.save(model);
		}else{
			//运单存在
			try {
				Integer id = persistWayBill.getId();
				BeanUtils.copyProperties(persistWayBill, model);
				persistWayBill.setId(id);
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException(e.getMessage());
			}
		}
	}
	
	//分页查询
	@Override
	public Page<WayBill> findAll(Pageable pageable) {
		return wayBillRepository.findAll(pageable);
	}

	//根据快速录入的waybillNum查询回显信息
	@Override
	public WayBill findByWayBillNum(String wayBillNum) {
		return wayBillRepository.findByWayBillNum(wayBillNum);
	}

}
