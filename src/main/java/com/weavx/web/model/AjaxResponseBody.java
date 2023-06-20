package com.weavx.web.model;


import java.util.HashMap;

import com.fasterxml.jackson.annotation.JsonView;
import com.weavx.web.jsonview.Views;

public class AjaxResponseBody {

	@JsonView(Views.Public.class)
	String msg;
	@JsonView(Views.Public.class)
	String code;
	@JsonView(Views.Public.class)
	CustomerUser result;	
	@JsonView(Views.Public.class)
	String txId;
	@JsonView(Views.Public.class)
	HashMap<String, Object> resultHashMap;

	public String getMsg() {
		return msg;
	}

	public HashMap<String, Object> getResultHashMap() {
		return resultHashMap;
	}

	public void setResultHashMap(HashMap<String, Object> resultHashMap) {
		this.resultHashMap = resultHashMap;
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

	public CustomerUser getResult() {
		return result;
	}

	public void setResult(CustomerUser result) {
		this.result = result;
	}
	
	public String getTxId() {
		return txId;
	}
	public void setTxId(String txId) {
		this.txId = txId;
	}

	@Override
	public String toString() {
		return "AjaxResponseResult [msg=" + msg + ", code=" + code + ", result=" + result + "]";
	}

}
