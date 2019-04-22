package com.example.demo.controller;


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
