package com.example.tasker;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class Task {
	
	private Long mId;
	private String mTitle;
	private String mBody;
	private Calendar mDueDate;
	
	public Task() {
		mDueDate = new GregorianCalendar();
	}
	
	public Task(String title) {
		this();
		mTitle = title;
	}
	
	public Task(String title, String body) {
		this(title);
		mBody = body;
	}
	
	public Task(String title, String body, Calendar dueDate) {
		mTitle = title;
		mBody = body;
		mDueDate = dueDate;
	}
	
	public Task(Long id, String title, String body) {
		this(title, body);
		mId = id;
	}
	
	public Task(Long id, String title, String body, Calendar dueDate) {
		this(id, title, body);
		mDueDate = dueDate;
	}
	
	public Long getId() {
		return mId;
	}
	
	public String getTitle() {
		return mTitle;
	}
	
	public void setTitle(String title) {
		mTitle = title;
	}
	
	public String getBody() {
		return mBody;
	}
	
	public void setBody(String body) {
		mBody = body;
	}
	
	public Calendar getDueDate() {
		return mDueDate;
	}
	
	public void setDueDate(Calendar dueDate) {
		mDueDate = dueDate;
	}
	
	public void setDueDate(int year, int month, int day) {
		mDueDate.set(year, month, day);
	}
	
}