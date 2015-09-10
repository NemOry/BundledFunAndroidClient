//package com.nemory.bundledfun.helpers;
//
//import java.util.ArrayList;
//
//import android.content.ContentValues;
//import android.content.Context;
//import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
//import android.database.sqlite.SQLiteOpenHelper;
//
//import com.nemory.bundledfun.objects.User;
//
//public class Database extends Constants{
//
//	private final Context context; 
//	private databaseHelper dbHelper;
//	public SQLiteDatabase db;
//	
//	public Database (Context c){
//		context = c;
//	}
//
//	public Database open(){
//		dbHelper = new databaseHelper(context);
//		db = dbHelper.getWritableDatabase();
//		return Database.this;
//	}
//
//	public void close(){
//		dbHelper.close();
//	}
//	
//	public void createTables(){
//		dbHelper.onCreate(db);
//	}
//	
//	public void dropTables(){
//		try{
//			db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
//		}catch (Exception e) {}
//	}
//
//	private class databaseHelper extends SQLiteOpenHelper{
//		
//		public databaseHelper(Context context) {
//			super(context, DATABASE_NAME, null, DATABASE_VERSION);
//		}
//		
//		@Override
//		public void onCreate(SQLiteDatabase db) {
//			String sql;
//			sql = "CREATE TABLE IF NOT EXISTS " 	+ 
//	        		TABLE_USERS 					+ " (" + 
//	        		C_USER_ID 	 					+ " TEXT PRIMARY KEY, " +
//	        		C_USER_PASS 	 				+ " TEXT, " +
//	        		C_USER_NAME 	 				+ " TEXT, " 	+ 
//	        		C_USER_PICTURE 					+ " TEXT, " 	+ 
//	        		C_USER_TIME_ELAPSED 	 		+ " TEXT, " 	+ 
//	        		C_USER_CORRECT_ANSWERS 			+ " TEXT, " 	+ 
//	        		C_USER_HIGH_SCORE  				+ " INTEGER);";
//			
//			db.execSQL(sql);
//		}
//
//		@Override
//		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//			db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
//			onCreate(db);
//		}
//	}
//	
//	public void addUser(User user){
//		ContentValues content = new ContentValues();
//		content.put(C_USER_ID, 					user.getId());
//		content.put(C_USER_PASS, 				user.getPass());
//		content.put(C_USER_NAME, 				user.getName());
//		content.put(C_USER_PICTURE, 			user.getPicture());
//		content.put(C_USER_TIME_ELAPSED, 		user.getTimeElapsed());
//		content.put(C_USER_CORRECT_ANSWERS, 	user.getCorrectAnswers());
//		content.put(C_USER_HIGH_SCORE, 			user.getHighScore());
//		db.insert(TABLE_USERS, null, content);
//	}
//	
//	public User getSingleUser(String id){
//	    Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE " + C_USER_ID + "='" + id + "'", null);
//		cursor.moveToFirst();
//		User user = new User();
//		user.setId(cursor.getString(cursor.getColumnIndex(C_USER_ID)));
//		user.setName(cursor.getString(cursor.getColumnIndex(C_USER_NAME)));
//		user.setPicture(cursor.getString(cursor.getColumnIndex(C_USER_PICTURE)));
//		user.setTimeElapsed(cursor.getString(cursor.getColumnIndex(C_USER_TIME_ELAPSED)));
//		user.setCorrectAnswers(cursor.getInt(cursor.getColumnIndex(C_USER_CORRECT_ANSWERS)));
//		user.setHighScore(cursor.getInt(cursor.getColumnIndex(C_USER_HIGH_SCORE)));
//		cursor.close();
//		return user;
//	}
//	
//	public ArrayList<User> getMultipleUsers(String sql){
//		ArrayList<User> users = new ArrayList<User>();
//	    Cursor cursor = db.rawQuery(sql, null);
//	    
//    	 while(cursor.moveToNext()){
// 			User user = new User();
// 			user.setId(cursor.getString(cursor.getColumnIndex(C_USER_ID)));
// 			user.setPass(cursor.getString(cursor.getColumnIndex(C_USER_PASS)));
// 			user.setName(cursor.getString(cursor.getColumnIndex(C_USER_NAME)));
// 			user.setPicture(cursor.getString(cursor.getColumnIndex(C_USER_PICTURE)));
// 			user.setTimeElapsed(cursor.getString(cursor.getColumnIndex(C_USER_TIME_ELAPSED)));
// 			user.setCorrectAnswers(cursor.getInt(cursor.getColumnIndex(C_USER_CORRECT_ANSWERS)));
// 			user.setHighScore(cursor.getInt(cursor.getColumnIndex(C_USER_HIGH_SCORE)));
// 			users.add(user);
// 	    }
//    	
//	    cursor.close();
//		return users;
//	}
//
//	public boolean authenticate(String id, String pass){
//	    Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE " + C_USER_ID + "='" + id + "' AND " + C_USER_PASS + "='" + pass + "'", null);
//		int count = cursor.getCount();
//		cursor.close();
//		return (count > 0) ? true : false;
//	}
//}
