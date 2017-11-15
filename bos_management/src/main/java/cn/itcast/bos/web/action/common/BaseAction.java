package cn.itcast.bos.web.action.common;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import org.springframework.data.domain.Page;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

public abstract class BaseAction<T> extends ActionSupport implements ModelDriven<T> {

	//模型驱动
	protected T model;
	@Override
	public T getModel() {
		return model;
	}
	
	//利用构造器完成Model实例化
	public BaseAction(){
		//构造子类action对象，获得继承父类型的泛型
		Type superclass = this.getClass().getGenericSuperclass();
		//获取类型第一个泛型参数
		ParameterizedType parameterizedType = (ParameterizedType)superclass;
		Class<T> modelClass = (Class<T>)parameterizedType.getActualTypeArguments()[0];
		
		try {
			model=modelClass.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("模型构造失败。。。");
		} 
	}



	// 接收页面传回参数
	protected int page;
	protected int rows;

	public void setPage(int page) {
		this.page = page;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}
	
	public void pushPageDateToValustack(Page<T> pageDate){
		Map<String,Object> map = new HashMap<>();
		map.put("total", pageDate.getTotalElements());
		map.put("rows", pageDate.getContent());
		
		ActionContext.getContext().getValueStack().push(map);
	}
}
