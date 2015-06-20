package com.example.advertisementdemo;

import cn.bmob.v3.BmobObject;

public class Person extends BmobObject {

	private String userIp;

	private String advID;

	private String userName;

	public Person() {
		this.setTableName("Person");
	}

	public String getUserIp() {
		return userIp;
	}

	public void setUserIp(String userIp) {
		this.userIp = userIp;
	}

	public String getAdvID() {
		return advID;
	}

	public void setAdvID(String advID) {
		this.advID = advID;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
}
