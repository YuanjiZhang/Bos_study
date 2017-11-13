package cn.itcast.bos.dao.base;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import cn.itcast.bos.domain.base.Courier;

public interface CourierDao extends JpaRepository<Courier, Integer>,JpaSpecificationExecutor<Courier> {

	//废除快递员
	@Modifying
	@Query("update Courier set deltag='1' where id =? ")
	void update(int parseInt);

	//还原快递员de方法
	@Modifying
	@Query("update Courier set deltag='' where id = ?")
	void update2(int parseInt);


}
