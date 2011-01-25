package com.example.tasker;

import android.app.ListActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SimpleCursorAdapter;

public class Tasker extends ListActivity {
	public static final int INSERT_ID = Menu.FIRST;
	
	private int mTaskNumber = 1;
	private TasksDbAdapter mDbHelper;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_list);
		mDbHelper = new TasksDbAdapter(this);
		mDbHelper.open();
		fillData();
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		boolean result = super.onCreateOptionsMenu(menu);
		menu.add(0, INSERT_ID, 0, R.string.menu_insert);
		return result;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case INSERT_ID:
				createTask();
				return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void createTask() {
		String taskName = "Task " + mTaskNumber++;
		mDbHelper.createTask(taskName, "");
		fillData();
	}

	private void fillData() {
		Cursor c = mDbHelper.fetchAllTasks();
		startManagingCursor(c);
		
		String[] from = new String[] { TasksDbAdapter.KEY_TITLE };
		int[] to = new int[] { R.id.text1 };
		
		SimpleCursorAdapter notes = 
			new SimpleCursorAdapter(this, R.layout.task_row, c, from, to);
		setListAdapter(notes);
	}
}
