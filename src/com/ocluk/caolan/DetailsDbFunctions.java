package com.ocluk.caolan;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/*
 * This Class will hold the majority if not all the functions
 *      which will be used for accessing our data base
 *      
 * Change Log
 * 06-Nov-13 Tony     Initial version, this may be all the DB functions we need.
 */

public class DetailsDbFunctions {

	private static final String DATABASE_NAME = "OCLDB";  // Our DB will be called OCLDB
	private static final String DATABASE_TABLE = "Details"; // our table inside the db is called Details
	private static final int DATABASE_VERSION = 1; // initial DB Version
	
	// These are the names of the columns in our table
	public static final String KEY_ROWID = "_id";
	public static final String KEY_Name = "Name";
	public static final String KEY_Address = "Address";
	public static final String KEY_DOB = "DOB";
	public static final String KEY_Telephone = "Telephone";
	

	private DatabaseHelper mDbHelper;
	private SQLiteDatabase mDb;
	
	private static final String DATABASE_CREATE =
			"create table " + DATABASE_TABLE + " ("
					+ KEY_ROWID + " integer primary key autoincrement, "
					+ KEY_Name + " text not null, "
					+ KEY_Address + " text not null, "
					+ KEY_DOB + " text not null, "
					+ KEY_Telephone + " text not null);";

	// above SQL statment translates to
	// create table Details ( _id integer primary key autoincrement,
	// 							Name text not null,
	//							Address text not null,
	//                          DOB text not null,
	//							Telephone text not null);
	/*
	The table would look like this (column names on top)
	   _id	Name		Address			DOB			Telephone
	   ===	===========	===============	=========== =========
		1	Tony		51 Papworth		02-Jan-1962	077xx 123456
		2	Caolam		12 Street		02-Feb-1962	077xx 123456
		3	Another		45 Road			02-Apr-1962	077xx 123456

	 */

	private final Context mCtx;

	private static class DatabaseHelper extends SQLiteOpenHelper {
			DatabaseHelper(Context context) {
				super(context, DATABASE_NAME, null, DATABASE_VERSION);
			}
			
			@Override
			public void onCreate(SQLiteDatabase db) {
				db.execSQL(DATABASE_CREATE);
			}
			
			@Override
			public void onUpgrade(SQLiteDatabase db, int oldVersion,
						int newVersion) {
				// Not used, but you could upgrade the database with ALTER
					// Scripts
			}
	}

	 /**
	   * info for database
	   * >>> Tony get more information on what this actually does <<<<<<<<<<<<
	   * input parameters Context ctx - interface to global information about an application environment
	   * 
	   * @return .mCtx
	   */
	
	public DetailsDbFunctions(Context ctx) {
		this.mCtx = ctx;
	}

	 /**
	   * opens the database
	   * 
	   * input parameters NONE
	   * 
	   * @return this(DB adaptor)
	   * throws SQLException - if it fails to open a DB it returns an exception.
	   */
	
	public DetailsDbFunctions open() throws SQLException {
		mDbHelper = new DatabaseHelper(mCtx);
		mDb = mDbHelper.getWritableDatabase();
			return this;
	}

     /**
	   * Closes the database
	   * 
	   * input parameters NONE
	   * 
	   * @return NONE
	   */
	
	public void close() {
		mDbHelper.close();
	}

   /**
     * Create a new Detail record using the supplied details (name, address etc...) 
     *  
     * If the row is successfully created then the .insert will return the new rowId
     * for that row, otherwise return a -1 to indicate failure.
     * 
     * @param name = name
     * @param address = address
     * @param DOB = date of birth 
     * @param telephone = telephone number
     * 
     * @return rowId (if successful) or -1 if failed
     */
	
	public long createDetailRow(String name,
			                   String address,
			                   String DOB,
			                   String telephone) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_Name, name);
		initialValues.put(KEY_Address, address);
		initialValues.put(KEY_DOB, DOB);
		initialValues.put(KEY_Telephone, telephone);

		return mDb.insert(DATABASE_TABLE, null, initialValues);
	}

	 /**
	   * deletes selected Details row
	   * 
	   * input parameters rowId - the Details to delete (which row it is on)
	   * 
	   * @return rowId 
	   */
	
	public boolean deleteDetailRow(long rowId) {
		return
			mDb.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
	}

	 /**
	   * fetches ALL the Details from the table
	   * 
	   * input parameters NONE
	   * 
	   * @return string[] - a list of all the reminders in our table
	   */
	
	public Cursor fetchAllDetails() {
		return mDb.query(DATABASE_TABLE, new String[] {KEY_ROWID, KEY_Name,
				KEY_Address, KEY_DOB, KEY_Telephone}, null, null, null, null, null);
	}
	
	 /**
	   * Retrieves a SINGLE row of details based on the rowid
	   * 
	   * input parameters rowId - description not found
	   * 
	   * @return mCursor - a cursor pointing to the required Details Row
	   */
	
	public Cursor fetchDetailRow(long rowId) throws SQLException {
		Cursor mCursor =
				mDb.query(true, DATABASE_TABLE, new String[] {KEY_ROWID,
						KEY_Name, KEY_Address, KEY_DOB, KEY_Address}, KEY_ROWID + "=" + rowId,
						null, null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}
	
	 /**
	   * saves changes to the detail row
	   * 
	   * input parameters rowId - the id of the row we want to update
	   * 				  title - the text of the new title
	   * 				  body - the text of the main body of the new updated reminder
	   * 				  reminderDateTime - the time and date of the new updated reminder
	   * 
	   * @return true if update was successful
	   */
	
	public boolean updateDetailRow(long rowId, 
								  String name, 
								  String address, 
								  String DOB,
								  String telephone) {
		ContentValues args = new ContentValues();
		args.put(KEY_Name, name);
		args.put(KEY_Address, address);
		args.put(KEY_DOB, DOB);
		args.put(KEY_Telephone, telephone);
		
	return
		mDb.update(DATABASE_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
	}

}
