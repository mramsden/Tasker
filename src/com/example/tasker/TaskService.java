package com.example.tasker;

import android.database.Cursor;

import java.util.ArrayList;

public class TaskService {
	
	private TasksDbAdapter mDbHelper;
	
	public TaskService(TasksDbAdapter dbHelper) {
		mDbHelper = dbHelper;
	}
	
	public ArrayList<Task> fetchAllTasks() {
		ArrayList<Task> tasksList = new ArrayList<Task>();
		Cursor tasks = mDbHelper.fetchAllTasks();
		tasks.moveToFirst();
		do {
			Long taskId = tasks.getLong(tasks.getColumnIndexOrThrow(TasksDbAdapter.KEY_ROWID));
			String taskTitle = tasks.getString(tasks.getColumnIndexOrThrow(TasksDbAdapter.KEY_TITLE));
			String taskBody = tasks.getString(tasks.getColumnIndexOrThrow(TasksDbAdapter.KEY_BODY));
			
			Task task = new Task(taskId, taskTitle, taskBody);
			tasksList.add(task);
			tasks.moveToNext();
		} while (!tasks.isLast());
		
		return tasksList;
	}
	
	public TasksDbAdapter getDbHelper() {
		return mDbHelper;
	}
	
}