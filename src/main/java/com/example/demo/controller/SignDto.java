package com.example.demo.controller;

/**
 * DTO for {@link Code} Entity
 */
public class SignDto {

	private Integer id;
	private String jsonrpc;
	private String method;
	private CodeDto params;

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

	public CodeDto getParams() {
		return params;
	}

	public void setParams(CodeDto params) {
		this.params = params;
	}

	@Override
	public String toString() {
		return "SignDto [id=" + id + ", jsonrpc=" + jsonrpc + ", method=" + method + ", params=" + params + "]";
	}

}
