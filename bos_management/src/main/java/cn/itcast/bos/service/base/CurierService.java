package cn.itcast.bos.service.base;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import cn.itcast.bos.domain.base.Courier;
import cn.itcast.bos.domain.base.Standard;

public interface CurierService {

	

	void save(Courier courier);

	//Page<Courier> findAll(Pageable pageable);

	Page<Courier> findAll(Specification<Courier> specification, Pageable pageable);

}
