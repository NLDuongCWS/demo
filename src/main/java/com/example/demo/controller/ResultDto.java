package com.example.demo.controller;

import java.util.Map;

public class ResultDto{

	
	private Object result;

	public Object getResult() {
		return result;
	}

	public void setResult(Object result) {
		this.result = result;
	}
	
	public ResultDto() {}
	
	public ResultDto(Object result) {
		super();
		this.result = result;
	}
}
