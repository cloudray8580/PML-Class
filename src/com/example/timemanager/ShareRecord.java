package com.example.timemanager;

import java.util.Date;

import cn.bmob.v3.BmobObject;

public class ShareRecord extends BmobObject{
	
	String username;
	Integer availableTimeToday;
//	Date date;
//	
//	public Date getDate() {
//		return date;
//	}
//	public void setDate(Date date) {
//		this.date = date;
//	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public Integer getAvailableTimeToday() {
		return availableTimeToday;
	}
	public void setAvailableTimeToday(Integer availableTimeToday) {
		this.availableTimeToday = availableTimeToday;
	}
}
