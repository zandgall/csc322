package com.zandgall.csc322.session1.exercises;

public class Time {
	private int hour, minute, second;

	public Time() {
		// Initiate with current milliseconds since unix epoch
		this(System.currentTimeMillis());
	}

	public Time(long elapseTimeMillis) {
		// Call setTime method to set time in ms
		setTime(elapseTimeMillis);
	}

	public Time(int hour, int minute, int second) {
		this.hour = hour;
		this.minute = minute;
		this.second = second;
	}

	public void setTime(long elapseTimeMillis) {
		// Convert milliseconds to seconds mod 60, minutes mod 60, and hours mod 24
		second = (int)(elapseTimeMillis / 1000) % 60;
		minute = (int)(elapseTimeMillis / 60000) % 60;
		hour = (int)(elapseTimeMillis / 3600000) % 24;
	}

	// Getters and setters
	public int getHour() {return hour;}
	public int getMinute() {return minute;}
	public int getSecond() {return second;}
	public void setHour(int hour) {this.hour = hour;}
	public void setMinute(int minute) {this.minute = minute;}
	public void setSecond(int second) {this.second = second;}

};
