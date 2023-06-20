package com.weavx.web.model;


import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonView;
import com.weavx.web.jsonview.Views;

public class AjaxResponseReport<T> {

	@JsonView(Views.Public.class)
	String msg;
	@JsonView(Views.Public.class)
	String code;
	@JsonView(Views.Public.class)
	ArrayList<T> result;

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public ArrayList<T> getResult() {
		return result;
	}

	public void setResult(ArrayList<T> result) {
		this.result = result;
	}

	@Override
	public String toString() {
		return "AjaxResponseResult [msg=" + msg + ", code=" + code + ", result=" + result + "]";
	}

}
