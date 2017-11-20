package cn.itcast.bos.service.base.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.base.CourierDao;
import cn.itcast.bos.dao.base.FixedAreaRepository;
import cn.itcast.bos.dao.base.TakeTimeRepository;
import cn.itcast.bos.domain.base.Courier;
import cn.itcast.bos.domain.base.FixedArea;
import cn.itcast.bos.domain.base.TakeTime;
import cn.itcast.bos.service.base.FixedAreaService;

@Service
@Transactional
public class FixedAreaServiceImpl implements FixedAreaService {

	
	@Autowired
	private FixedAreaRepository repostory;
	@Autowired
	private CourierDao courierDao;
	@Autowired
	private TakeTimeRepository takeTimeRepository;
	
	@Override
	public void save(FixedArea model) {
		repostory.save(model);
	}
	@Override
	public Page<FixedArea> findAll(Specification<FixedArea> spec, Pageable pageable) {
		return repostory.findAll(spec, pageable);
	}
	
	//关联快递员到定区
	@Override
	public void associationCourierToFixedArea(FixedArea model, Integer courierId, Integer takeTimeId) {
		FixedArea persistFixedArea = repostory.findOne(model.getId());
		
		Courier courier = courierDao.findOne(courierId);
		
		TakeTime takeTime = takeTimeRepository.findOne(takeTimeId);
		
		//快递员关联到定区上  外键在谁那 就由谁来关联
		persistFixedArea.getCouriers().add(courier);
		
		//将排班信息关联到快递员上
		courier.setTakeTime(takeTime);
	}

}
