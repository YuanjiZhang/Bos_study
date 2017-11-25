package cn.itcast.bos.service.take_delivery;

import java.util.Date;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import cn.itcast.bos.domain.take_delivery.PageBean;
import cn.itcast.bos.domain.take_delivery.Promotion;

public interface PromotionService {

	void save(Promotion model);

	Page<Promotion> findAll(Pageable pageable);

	
	//查询返回分页数据
	@GET 
	@Path("/pageQuery")
	@Produces({"application/xml","application/json"})
	PageBean<Promotion> findPageData(@QueryParam("page") int page,
			@QueryParam("rows") int rows);
	
	//根据id查询
	@GET
	@Path("/promotion/{id}")
	@Produces({"application/xml","application/json"})
	Promotion findById(@PathParam("id") Integer id);

	void updateStatus(Date date);
}
