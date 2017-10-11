package com.master.api;

import java.util.Map;

public class R {
	private Map<String,Object> data;
	public static final Integer error = 1;
	public static final Integer success = 0;
	private Integer status;
	private String errorMsg;
	public R(){}
	public R(Map<String, Object> data, Integer status, String errorMsg) {
		super();
		this.data = data;
		this.status = status;
		this.errorMsg = errorMsg;
	}
	public R(Map<String, Object> data, Integer status) {
		super();
		this.data = data;
		this.status = status;
	}
	public Map<String, Object> getData() {
		return data;
	}
	public void setData(Map<String, Object> data) {
		this.data = data;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getErrorMsg() {
		return errorMsg;
	}
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	};
	
	
}
