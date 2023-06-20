package com.weavx.web.model;

import java.util.HashMap;
import com.fasterxml.jackson.annotation.JsonView;
import com.weavx.web.jsonview.Views;

public class AjaxResponseBodyHash2 {

	@JsonView(Views.Public.class)
	String msg;
	@JsonView(Views.Public.class)
	String code;
	@JsonView(Views.Public.class)
	HashMap<String, Object> result;

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

	public HashMap<String, Object> getResult() {
		return result;
	}

	public void setResult(HashMap<String, Object> result) {
		this.result = result;
	}

	@Override
	public String toString() {
		return "AjaxResponseBody [msg=" + msg + ", code=" + code + ", result=" + result + "]";
	}



}
