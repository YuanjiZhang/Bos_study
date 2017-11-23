package cn.itcast.bos.web.action.take_delivery;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;

import cn.itcast.bos.domain.take_delivery.Promotion;
import cn.itcast.bos.service.take_delivery.PromotionService;
import cn.itcast.bos.web.action.common.BaseAction;

@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("json-default")
public class PromotionAction  extends BaseAction<Promotion>{

	@Autowired
	private PromotionService promotionService;
	
	private File titleImgFile;
	private String titleImgFileFileName;
	public void setTitleImgFileFileName(String titleImgFileFileName) {
		this.titleImgFileFileName = titleImgFileFileName;
	}
	public void setTitleImgFile(File titleImgFile) {
		this.titleImgFile = titleImgFile;
	}



	//分页展示活动信息
	@Action(value="promotion_pageQuery",results={@Result(name="success",type="json")})
	public String pageQuery(){
		Pageable pageable = new PageRequest(page-1, rows);
		
		Page<Promotion> pageData =  promotionService.findAll(pageable);
		
		pushPageDateToValustack(pageData);
		return SUCCESS;
	}

	
	
	//添加活动信息的操作
	@Action(value="promotion_save",results={@Result(name="success"
			,type="redirect",location="/pages/take_delivery/promotion.html")})
	public String save() throws IOException{
		//宣传图上传，在数据表中保存图片路径
		String savePath = ServletActionContext.getServletContext().getRealPath("/upload/");
		String saveUrl = ServletActionContext.getRequest().getContextPath()+"/upload/";
		
		System.out.println(titleImgFileFileName);
		
		//生成随机文件名
		UUID uuid = UUID.randomUUID();
		String ext = titleImgFileFileName.substring(titleImgFileFileName.lastIndexOf("."));
		String randomFileName = uuid + ext;
		//保存路径
		FileUtils.copyFile(titleImgFile, new File(savePath,randomFileName));
		
		//保存路径，完成活动任务数据保存
		model.setTitleImg(ServletActionContext.getRequest().getContextPath()
				+"/upload/"+randomFileName);
		//调用业务层完成数据保存
		promotionService.save(model);
		
		return SUCCESS;
	}
	
	
	
}
