package cn.itcast.bos.web.action;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.MediaType;

import org.apache.commons.io.FileUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;

import cn.itcast.bos.domain.constants.Constants;
import cn.itcast.bos.domain.take_delivery.PageBean;
import cn.itcast.bos.domain.take_delivery.Promotion;
import freemarker.template.Configuration;
import freemarker.template.Template;

@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("json-default")
public class PromotionAction  extends BaseAction<Promotion>{
	
	
	//展示活动详细信息
	@Action(value="promotion_showDetail")
	public String showDetail() throws Exception{
		//先判断id 对应html是否存在， 如果存在，直接返回
		String htmlRealPath = ServletActionContext.getServletContext()
				.getRealPath("/freemarker");
		File htmlFile = new File(htmlRealPath+"/" + model.getId() + ".html");
		
		//如果html文件不存在，查询数据库后，结合freemarker模版生成页面
		if(!htmlFile.exists()){
			Configuration configuration = new Configuration(
					Configuration.VERSION_2_3_22);
			configuration.setDirectoryForTemplateLoading(new File(
					ServletActionContext.getServletContext().getRealPath(
							"/WEB-INF/freemarker_templates")));
			
			//获取模版对象
			Template template = configuration.getTemplate("promotion_detail.ftl");
			//动态数据
			Promotion promotion = WebClient.create(
					Constants.BOS_MANAGEMENT_URL+"/bos_management/services/"
					+ "promotionService/promotion/"
					+model.getId()).accept(MediaType.APPLICATION_JSON)
			.get(Promotion.class);
			
			
			Map<String, Object> paramterMap = new HashMap<String,Object>();
			paramterMap.put("promotion",promotion);
			
			// 合并输出
			template.process(paramterMap, new OutputStreamWriter(
					new FileOutputStream(htmlFile),"utf-8"));
		}
		
		//存在，直接将文件信息返回
		ServletActionContext.getResponse().setContentType(
				"text/html;charset=utf-8");
		FileUtils.copyFile(htmlFile, ServletActionContext
				.getResponse().getOutputStream() );
		return NONE;
	}
	
	//前台展示活动分页信息
	@Action(value="promotion_pageQuery",results={@Result(name="success",type="json")})
	public String pageQuery(){
		//基于webservice请求bos_management中的promotion数据
		PageBean<Promotion> pageBean = WebClient.create(
				"http://localhost:8080/bos_management/services/promotionService/pageQuery"
				+ "?page="+page+"&rows="+rows)
		.accept(MediaType.APPLICATION_JSON).get(PageBean.class);
		
		ActionContext.getContext().getValueStack().push(pageBean);
		return SUCCESS;
	}
	

}
