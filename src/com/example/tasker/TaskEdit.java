package com.example.tasker;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.util.Log;

public class TaskEdit extends Activity {
	
	private EditText mTitleText;
	private EditText mBodyText;
	private Long mRowId;
	
	private TasksDbAdapter mDbHelper;
	
	private static final String TAG = "TaskEdit";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mDbHelper = new TasksDbAdapter(this);
		mDbHelper.open();
		
		setContentView(R.layout.task_edit);
		setTitle(R.string.edit_task);
		
		mTitleText = (EditText)findViewById(R.id.title);
		mBodyText = (EditText)findViewById(R.id.body);
		
		Button confirmButton = (Button)findViewById(R.id.confirm);
		
		mRowId = (savedInstanceState == null) ? null : (Long)savedInstanceState.getSerializable(TasksDbAdapter.KEY_ROWID);
		if (mRowId == null) {
			Bundle extras = getIntent().getExtras();
			mRowId = (extras != null) ? extras.getLong(TasksDbAdapter.KEY_ROWID) : null;
		}
		
		populateFields();
		
		confirmButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				setResult(RESULT_OK);
				finish();
			}
		});
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		saveState();
		outState.putSerializable(TasksDbAdapter.KEY_ROWID, mRowId);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		saveState();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		populateFields();
	}
	
	private void populateFields() {
		if (!isNewTask()) {
			Cursor task = mDbHelper.fetchTask(mRowId);
			startManagingCursor(task);
			mTitleText.setText(task.getString(task.getColumnIndexOrThrow(TasksDbAdapter.KEY_TITLE)));
			mBodyText.setText(task.getString(task.getColumnIndexOrThrow(TasksDbAdapter.KEY_BODY)));
		}
	}	
	
	private void saveState() {
		String title = mTitleText.getText().toString();
		String body = mBodyText.getText().toString();
		
		if (mRowId == null) {
			long id = mDbHelper.createTask(title, body);
			if (id > 0) {
				mRowId = id;
			}
		} else {
			mDbHelper.updateTask(mRowId, title, body);
		}
	}
	
	private boolean isNewTask() {
		return mRowId == null;
	}
}