package cn.itcast.bos.service.base.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.base.CourierDao;
import cn.itcast.bos.domain.base.Courier;
import cn.itcast.bos.domain.base.Standard;
import cn.itcast.bos.service.base.CurierService;

@Service
@Transactional
public class CourierServiceImpl implements CurierService {

	@Autowired
	private CourierDao courierDao;


	@Override
	public void save(Courier courier) {
		courierDao.save(courier);
	}

	//分页查询所有快递员信息
	@Override
	public Page<Courier> findAll(Pageable pageable) {
		return courierDao.findAll(pageable);
	}

}
