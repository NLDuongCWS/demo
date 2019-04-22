package com.example.demo.controller;

/**
 * DTO for {@link Code} Entity
 */
public class CodeDto {

	private Object params;
	private Integer decimals;
	private String mintBatches;
	private String name;
	private String symbol;
	private String tokenOwner;

	public Object getParams() {
		return params;
	}

	public void setParams(Object params) {
		this.params = params;
	}

	public Integer getDecimals() {
		return decimals;
	}

	public void setDecimals(Integer decimals) {
		this.decimals = decimals;
	}

	public String getMintBatches() {
		return mintBatches;
	}

	public void setMintBatches(String mintBatches) {
		this.mintBatches = mintBatches;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public String getTokenOwner() {
		return tokenOwner;
	}

	public void setTokenOwner(String tokenOwner) {
		this.tokenOwner = tokenOwner;
	}

	@Override
	public String toString() {
		return "CodeDto [params=" + params + ", decimals=" + decimals + ", mintBatches=" + mintBatches + ", name="
				+ name + ", symbol=" + symbol + ", tokenOwner=" + tokenOwner + "]";
	}

}
