package cn.itcast.bos.utils;

import java.util.List;

public class PageBean<T> {

	public PageBean() {
		super();
		// TODO Auto-generated constructor stub
	}
	public PageBean(Integer page, Integer rows) {
		super();
		this.page = page;
		this.rows = rows;
	}
	public Integer getPage() {
		return page;
	}
	public void setPage(Integer page) {
		this.page = page;
	}
	public Integer getRows() {
		return rows;
	}
	public void setRows(Integer rows) {
		this.rows = rows;
	}
	public List<T> getContent() {
		return content;
	}
	public void setContent(List<T> content) {
		this.content = content;
	}
	private Integer page;
	private Integer rows;
	private List<T> content;
}
