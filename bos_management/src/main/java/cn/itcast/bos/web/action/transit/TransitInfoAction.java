package cn.itcast.bos.web.action.transit;

import java.util.HashMap;
import java.util.Map;

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

import com.opensymphony.xwork2.ActionContext;

import cn.itcast.bos.domain.transit.TransitInfo;
import cn.itcast.bos.service.transit.TransitInfoService;
import cn.itcast.bos.web.action.common.BaseAction;

@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("json-default")
public class TransitInfoAction  extends BaseAction<TransitInfo>{
	@Autowired
	private TransitInfoService transitInfoService;
	
	//属性输入
	private String wayBillIds;

	public void setWayBillIds(String wayBillIds) {
		this.wayBillIds = wayBillIds;
	}
	
	
	@Action(value="transit_pageQuery",results={@Result(name="success",type="json")})
	public String pageQuery(){
		Pageable pageable = new PageRequest(page-1, rows);
		
		Page<TransitInfo> pageData = transitInfoService.findPageData(pageable);
		
		pushPageDateToValustack(pageData);
		return SUCCESS;
	}

	@Action(value="transit_create",results={@Result(name="success",type="json")})
	public String create(){
		//调用业务层 保存transitInfo信息
		Map<String,Object> results = new HashMap<>();
		
		try {
			transitInfoService.createTransits(wayBillIds);
			//成功
			results.put("success", true);
			results.put("msg", "开启中转配送成功");
		} catch (Exception e) {
			e.printStackTrace();
			//失败
			results.put("success", false);
			results.put("msg", "开启中转配送失败");
		}
		
		ActionContext.getContext().getValueStack().push(results);
		return SUCCESS;
	}
	
}
