package com.example.advertisementdemo;

import cn.bmob.v3.BmobObject;

public class Advertisement extends BmobObject {

	private Integer advType;

	private Integer advNumber;

	private String advAddress;

	private String advName;

	public Advertisement() {
		this.setTableName("Advertisement");
	}

	public int getAdvType() {
		return advType;
	}

	public void setAdvType(int advType) {
		this.advType = advType;
	}

	public int getAdvNumber() {
		return advNumber;
	}

	public void setAdvNumber(int advNumber) {
		this.advNumber = advNumber;
	}

	public String getAdvAddress() {
		return advAddress;
	}

	public void setAdvAddress(String advAddress) {
		this.advAddress = advAddress;
	}

	public String getAdvName() {
		return advName;
	}

	public void setAdvName(String advName) {
		this.advName = advName;
	}

}
