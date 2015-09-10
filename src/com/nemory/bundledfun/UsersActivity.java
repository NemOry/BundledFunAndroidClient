package com.nemory.bundledfun;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import com.nemory.bundledfun.R;
import com.nemory.bundledfun.helpers.ListViewAdapter;
import com.nemory.bundledfun.objects.User;

public class UsersActivity extends Activity {

	private ListView listStudents;
	private EditText etSearchStudent;
	private ArrayAdapter<String> adapter;
	private ArrayAdapter<CharSequence> spAdapter;
	private Spinner spSchoolLevel, spYearLevel;
	private ActionMode mActionMode;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.users);
		this.initialize();
		this.bindUsers();
	}

	private void bindUsers() {
		int totalUsers = User.users.size();
		String[] pics = new String[totalUsers];
		String[] names = new String[totalUsers];
		String[] ids = new String[totalUsers];
		String[] pass = new String[totalUsers];
		int[] scores = new int[totalUsers];

		for (int i = 0; i < totalUsers; i++) {
			pics[i] = User.users.get(i).getPicture();
			names[i] = User.users.get(i).getName();
			ids[i] = User.users.get(i).getId();
			pass[i] = User.users.get(i).getPass();
			scores[i] = User.users.get(i).getHighScore();
		}
		
		adapter = new ListViewAdapter(this, pics, names, ids, pass, scores);
		listStudents.setAdapter(adapter);
	}

	private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {
		public boolean onCreateActionMode(ActionMode mode, Menu menu) {
			MenuInflater inflater = mode.getMenuInflater();
			inflater.inflate(R.menu.nav_bar, menu);
			return true;
		}

		public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
			return false;
		}

		public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
			switch (item.getItemId()) {
			case R.id.menu_share:

				mode.finish();
				return true;
			default:
				return false;
			}
		}

		public void onDestroyActionMode(ActionMode mode) {
			mActionMode = null;
		}
	};

	private void initialize() {
		
		/** REFERENCING VIEWS **/
		
		listStudents = (ListView) findViewById(R.id.listStudents);
		etSearchStudent = (EditText) findViewById(R.id.etSearchStudent);

		spSchoolLevel = (Spinner) findViewById(R.id.spSchoolLevel);
		spYearLevel = (Spinner) findViewById(R.id.spYearLevel);

		spAdapter = ArrayAdapter.createFromResource(this, R.array.school_level,android.R.layout.simple_spinner_item);
		spAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spSchoolLevel.setAdapter(spAdapter);

		spAdapter = ArrayAdapter.createFromResource(this, R.array.year_level,android.R.layout.simple_spinner_item);
		spAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spYearLevel.setAdapter(spAdapter);
		
		/** LISTENERS **/
		
		listStudents.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int pos,long id) {
				
			}
		});

		listStudents.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				if (mActionMode != null) {
					return false;
				}
				mActionMode = startActionMode(mActionModeCallback);
				view.setSelected(true);
				return true;
			}
		});
		
		etSearchStudent.addTextChangedListener(new TextWatcher() {
			public void onTextChanged(CharSequence s, int start, int before,int count) {
				adapter.getFilter().filter(s.toString());
			}

			public void beforeTextChanged(CharSequence s, int start, int count,int after) {
			}

			public void afterTextChanged(Editable s) {
			}
		});
	}
}
