package com.example.tasker;

import android.database.Cursor;

import java.util.ArrayList;

public class TaskService {
	
	private TasksDbAdapter mDbHelper;
	private ArrayList<Task> mTasksList;
	
	public TaskService(TasksDbAdapter dbHelper) {
		mDbHelper = dbHelper;
	}
	
	public ArrayList<Task> fetchAllTasks() {
		if (mTasksList == null) {
			mTasksList = new ArrayList<Task>();
			Cursor tasks = mDbHelper.fetchAllTasks();
			if (tasks.getCount() > 0) {
				tasks.moveToFirst();
				do {
					Long taskId = tasks.getLong(tasks.getColumnIndexOrThrow(TasksDbAdapter.KEY_ROWID));
					String taskTitle = tasks.getString(tasks.getColumnIndexOrThrow(TasksDbAdapter.KEY_TITLE));
					String taskBody = tasks.getString(tasks.getColumnIndexOrThrow(TasksDbAdapter.KEY_BODY));

					Task task = new Task(taskId, taskTitle, taskBody);
					mTasksList.add(task);
					tasks.moveToNext();
				} while (!tasks.isLast());
			}
		}
		
		return mTasksList;
	}
	
	public TasksDbAdapter getDbHelper() {
		return mDbHelper;
	}
	
}