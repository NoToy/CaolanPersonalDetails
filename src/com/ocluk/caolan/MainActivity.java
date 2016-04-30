package com.ocluk.caolan;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.SimpleCursorAdapter;


/*
 * Change Log
 * 27-Dec-13 Tony     created GetAllRows to get all the rows from our DB
 * 28-Aug-13 Tony     Created the first hard coded list of items
 */


/**
 * Main entry point for our App
 * 
 * input parameters NONE
 * @return NONE
 */

public class MainActivity extends ListActivity {
	private static final int ACTIVITY_CREATE=0;
	private static final int ACTIVITY_EDIT=1;
	private DetailsDbFunctions mDbHelper;
	
	private void createDetails() {
		Intent i = new Intent(this, EditScreenActivity.class);
		startActivityForResult(i, ACTIVITY_CREATE);
	}
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		String[] texts = new String[] { "First Line", "Second Line", "Third Line", "Fourth Line", "Fifth Line",
										"Sixth Line", "Seventh Line", "Eigth Line", "Nineth Line"};
		mDbHelper = new DetailsDbFunctions(this);
		mDbHelper.open();
		GetAllRows();
/*
		ArrayAdapter<String> adapter =
			new ArrayAdapter<String>(this, R.layout.details_row, R.id.text1, texts);
		setListAdapter(adapter);
		*/
		setContentView(R.layout.activity_main);
		registerForContextMenu(getListView());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		
		return true;
	}

	@Override
	 public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo
	     menuInfo) {
	  super.onCreateContextMenu(menu, v, menuInfo);
	  MenuInflater mi = getMenuInflater();
	  mi.inflate(R.menu.oktodelete_longpress, menu);
	 }
	
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch(item.getItemId()) {
		case R.id.menu_insert:
			createDetails();
			return true;
		case R.id.menu_settings:
		//	Intent i = new Intent(this, TaskPreferences.class);
		//	startActivity(i);
			return true;
		case R.id.menu_delete:
		
			return true;
		}
	return super.onMenuItemSelected(featureId, item);
	}
	
/**
    * The contenxt menu (popup) has been choosen
    * 
    * input parameters MenuItem item
    * 
    * @return .onContextItemSelected(item)
    */
 
 @Override
 public boolean onContextItemSelected(MenuItem item) {
  switch(item.getItemId()) {
   case R.id.oktodelete:
    // Figure out which row on the screen is selected
	   AdapterContextMenuInfo info =
     (AdapterContextMenuInfo) item.getMenuInfo();
    // Delete that row
	   mDbHelper.deleteDetailRow(info.id);
    // Now refresh the screen but reading all the rows again
	   GetAllRows();
    return true;
  }
  return super.onContextItemSelected(item);
 }
	
	private void GetAllRows() {
		Cursor DetailsCursor = mDbHelper.fetchAllDetails(); 
		startManagingCursor(DetailsCursor);
		// Cursor c = mDbHelper.rawQuery("select * from your_table_name",null);
		Log.i("Number of Records"," :: "+DetailsCursor.getCount());
	
		String[] from = new String[]{DetailsDbFunctions.KEY_Name};
	
		int[] to = new int[]{R.id.text1};
	
		SimpleCursorAdapter details =
				new SimpleCursorAdapter(this, R.layout.detail_row,
						DetailsCursor, from, to);
		setListAdapter(details);
	}
}
