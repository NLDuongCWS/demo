package com.example.demo.controller;

/**
 * DTO for {@link Code} Entity
 */
public class SignDto {

	private Integer id;
	private String jsonrpc;
	private String method;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getJsonrpc() {
		return jsonrpc;
	}

	public void setJsonrpc(String jsonrpc) {
		this.jsonrpc = jsonrpc;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	@Override
	public String toString() {
		return "CodeDto [id=" + id + ", jsonrpc=" + jsonrpc + ", method=" + method + "]";
	}

	
	
}
