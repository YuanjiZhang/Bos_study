package cn.itcast.bos.web.action.base;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

import cn.itcast.bos.domain.base.Area;
import cn.itcast.bos.service.base.AreaService;
import cn.itcast.bos.utils.PinYin4jUtils;
import cn.itcast.bos.web.action.common.BaseAction;

@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("json-default")
public class AreaAction extends BaseAction<Area>{

	private Area area = new Area();
	@Override
	public Area getModel() {
		return area;
	}

	@Autowired
	private AreaService areaService;
	
	//接受文件
	private File file ;
	public void setFile(File file) {
		this.file = file;
	}

	
	
	//分页查询数据
	
	@Action(value="area_findAll",results={@Result(name="success",type="json")})
	public String findAll(){
		Pageable pageable = new PageRequest(page-1, rows);
		
		Specification<Area> specification = new Specification<Area>() {

			@Override
			public Predicate toPredicate(Root<Area> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> list = new ArrayList<>();
				
				//条件查询省份
				if(StringUtils.isNotBlank(area.getProvince())){
					Predicate p1 = cb.like(root.get("province").as(String.class),"%"+area.getProvince()+"%" );
					list.add(p1);
				}
				
				//条件查询城市
				if(StringUtils.isNotBlank(area.getCity())){
					Predicate p2 = cb.like(root.get("city").as(String.class),"%"+ area.getCity()+"%");
					list.add(p2);
				}
				
				//条件查询区县
				if(StringUtils.isNotBlank(area.getDistrict())){
					Predicate p3 = cb.like(root.get("district").as(String.class),"%"+area.getDistrict()+"%");
					list.add(p3);
				}
				
				return cb.and(list.toArray(new Predicate[0]));
			}
		};
		
		Page<Area> pageDate = areaService.findAll(specification,pageable);
		
		pushPageDateToValustack(pageDate);
		
		return SUCCESS;
	}

	//文件一键上传  批量区域数据导入
	@Action(value="area_import")
	public String fileImport() throws Exception{
		List<Area> areas = new ArrayList<Area>();
		//编写解析代码逻辑
		//基于.xls 格式解析HSSF
		//1.加载 EXCEL文件对象
		HSSFWorkbook hssfWorkbook = new HSSFWorkbook(new FileInputStream(file));
		//2.读取一个sheet文件对象
		HSSFSheet sheet = hssfWorkbook.getSheetAt(0);
		//3.读取sheet的每一行
		for (Row row : sheet) {
			//一行数据 对应一个区域对象
			if(row.getRowNum()==0){
				//跳过表头第一行数据
				continue;
			}
			//跳过空行
			if(row.getCell(0)==null || StringUtils.isBlank(row.getCell(0).getStringCellValue())){
				continue;
			}
			
			Area area = new Area();
			area.setId(row.getCell(0).getStringCellValue());
			area.setProvince(row.getCell(1).getStringCellValue());
			area.setCity(row.getCell(2).getStringCellValue());
			area.setDistrict(row.getCell(3).getStringCellValue());
			area.setPostcode(row.getCell(4).getStringCellValue());
			//基于pinyin4j生成城市编码和简码
			String province = area.getProvince();
			String city = area.getCity();
			String district = area.getDistrict();
			province = province.substring(0, province.length()-1);
			city = city.substring(0, city.length()-1);
			district = district.substring(0, district.length()-1);
			
			//简码
			String[] headArray = PinYin4jUtils.getHeadByString(province+city+district);
			StringBuffer buffer = new StringBuffer();
			for (String hearStr : headArray) {
				buffer.append(hearStr);
			}
			String shortcode = buffer.toString();
			area.setShortcode(shortcode);
			//城市编码
			String citycode = PinYin4jUtils.hanziToPinyin(city, "");
			area.setCitycode(citycode);
			
			areas.add(area);
			
		}		
		
		areaService.save(areas);
		return NONE;
	}
	
}
