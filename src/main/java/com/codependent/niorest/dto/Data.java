package com.codependent.niorest.dto;

import java.io.Serializable;

public class Data implements Serializable{

	private static final long serialVersionUID = 1049438747605741485L;
	private String key;
	private String value;
	
	public Data(){}
	
	public Data(String key, String value){
		this.key=key;
		this.value=value;
	}
	
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
}
