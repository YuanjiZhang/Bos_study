package cn.itcast.bos.service.base.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.base.StandardDao;
import cn.itcast.bos.domain.base.Standard;
import cn.itcast.bos.service.base.StandardService;

@Service
@Transactional
public class StandardServiceImpl implements StandardService {

	@Autowired
	private StandardDao standardDao;
	
	@Override
	@CacheEvict(value="standard",allEntries=true)
	public void save(Standard standard) {
		standardDao.save(standard);
	}
	
	//分页查询页面
	@Override
	public Page<Standard> findAll(Pageable pageable) {
		return standardDao.findAll(pageable);
	}

	@Override
	@Cacheable("standard")
	public List<Standard> findAllStandards() {
		return standardDao.findAll();
	}

}
