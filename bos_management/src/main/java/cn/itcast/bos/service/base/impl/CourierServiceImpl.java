package cn.itcast.bos.service.base.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
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
	public void save(Courier model) {
		courierDao.save(model);
	}


	
	//带条件的分页查询
	@Override
	public Page<Courier> findAll(Specification<Courier> specification, Pageable pageable) {
		return courierDao.findAll(specification,pageable);
	}



	//作废快递员
	@Override
	public void del(String ids) {
		String[] id = ids.split(",");
		for (int i = 0; i < id.length; i++) {
			courierDao.update(Integer.parseInt(id[i]));
		}
	}


	//还原快递员
	@Override
	public void fix(String ids) {
		String[] id = ids.split(",");
		for (String i : id) {
			courierDao.update2(Integer.parseInt(i));
		}
	}

	/*//分页查询所有快递员信息
	@Override
	public Page<Courier> findAll(Pageable pageable) {
		return courierDao.findAll(pageable);
	}*/

}
