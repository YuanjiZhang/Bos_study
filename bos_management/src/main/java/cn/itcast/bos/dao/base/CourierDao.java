package cn.itcast.bos.dao.base;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import cn.itcast.bos.domain.base.Courier;

public interface CourierDao extends JpaRepository<Courier, Integer>,JpaSpecificationExecutor<Courier> {


}
