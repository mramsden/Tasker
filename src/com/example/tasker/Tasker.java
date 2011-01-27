package com.example.tasker;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.lang.Runnable;
import java.lang.Thread;

public class Tasker extends ListActivity {
	private static final int ACTIVITY_CREATE = 0;
	private static final int ACTIVITY_EDIT = 1;
	
	private static final int INSERT_ID = Menu.FIRST;
	private static final int DELETE_ID = Menu.FIRST + 1;
	
	private static final String TAG = "Tasker";
	
	private TasksDbAdapter mDbHelper;
	private TaskService mTaskService;
	private ArrayList<Task> mTasks;
	
	private TaskAdapter mListAdapter;
	private Runnable viewTasks;
	
	private ProgressDialog mProgressDialog;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_list);
		mDbHelper = new TasksDbAdapter(this);
		mDbHelper.open();
		
		mTaskService = new TaskService(mDbHelper);
		
		mListAdapter = new TaskAdapter(this, R.layout.task_row, mTaskService.fetchAllTasks());
		
		registerForContextMenu(getListView());
		
		// Create the view tasks thread which is responsible
		// for loading tasks from the datasource.
		viewTasks = new Runnable() {
			@Override
			public void run() {
				getTasks();
			}
		};
		Thread thread = new Thread(null, viewTasks, "BackgroundTaskLoader");
		thread.start();
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
		Thread thread = new Thread(null, viewTasks, "BackgroundTaskLoader");
		thread.start();
	}
	
	private void createTask() {
		Intent i = new Intent(this, TaskEdit.class);
		startActivityForResult(i, ACTIVITY_CREATE);
	}
	
	private void getTasks() {
		Log.i(TAG, "Loading tasks");
		runOnUiThread(displayLoadingMessage);
		mTasks = mTaskService.fetchAllTasks();
		runOnUiThread(returnTasks);
	}
	
	// This is used to respond to a call from getTasks.
	private Runnable returnTasks = new Runnable() {
		@Override
		public void run() {
			Log.i(TAG, "Tasks loaded");
			mProgressDialog.dismiss();
			if (mTasks != null && mTasks.size() > 0) {
				mListAdapter.notifyDataSetChanged();
				for (Task task : mTasks) {
					mListAdapter.add(task);
				}
				mListAdapter.notifyDataSetChanged();
			}
		}
	};
	
	private Runnable displayLoadingMessage = new Runnable() {
		@Override
		public void run() {
			Log.i(TAG, "Display loading message");
			mProgressDialog = null;
			mProgressDialog = ProgressDialog.show(Tasker.this, getString(R.string.loading_title), getString(R.string.loading_tasks_message), true);
		}
	};
	
	private class TaskAdapter extends ArrayAdapter<Task> {
		private ArrayList<Task> mItems;
		
		public TaskAdapter(Context context, int textViewResourceId, ArrayList<Task> items) {
			super(context, textViewResourceId, items);
			mItems = items;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
			if (v == null) {
				LayoutInflater vi = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(R.layout.task_row, null);
			}
			
			Task t = mItems.get(position);
			if (t != null) {
				TextView tt = (TextView)v.findViewById(R.id.text1);
				if (tt != null) {
					tt.setText(t.getTitle());
				}
			}
			
			return v;
		}
	}
}
