package com.example.tasker;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.AdapterView.AdapterContextMenuInfo;

public class Tasker extends ListActivity {
	private static final int ACTIVITY_CREATE = 0;
	private static final int ACTIVITY_EDIT = 1;
	
	private static final int INSERT_ID = Menu.FIRST;
	private static final int DELETE_ID = Menu.FIRST + 1;
	
	private TasksDbAdapter mDbHelper;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_list);
		mDbHelper = new TasksDbAdapter(this);
		mDbHelper.open();
		fillData();
		registerForContextMenu(getListView());
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		boolean result = super.onCreateOptionsMenu(menu);
		menu.add(0, INSERT_ID, 0, R.string.menu_insert);
		return result;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
			case INSERT_ID:
				createTask();
				return true;
		}
		
		return super.onMenuItemSelected(featureId, item);
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.add(0, DELETE_ID, 0, R.string.menu_delete);
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case DELETE_ID:
				AdapterContextMenuInfo info = (AdapterContextMenuInfo)item.getMenuInfo();
				mDbHelper.deleteTask(info.id);
				fillData();
				return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		Intent i = new Intent(this, TaskEdit.class);
		i.putExtra(TasksDbAdapter.KEY_ROWID, id);
		startActivityForResult(i, ACTIVITY_EDIT);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		fillData();
	}
	
	private void createTask() {
		Intent i = new Intent(this, TaskEdit.class);
		startActivityForResult(i, ACTIVITY_CREATE);
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
