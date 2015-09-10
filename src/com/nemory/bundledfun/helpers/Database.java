package com.nemory.bundledfun.helpers;
//package com.nemory.bundledfun;
//
//import java.util.ArrayList;
//
//import android.content.ContentValues;
//import android.content.Context;
//import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
//import android.database.sqlite.SQLiteOpenHelper;
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
//			db.execSQL("DROP TABLE IF EXISTS " + TABLE_STUDENTS);
//			db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUESTIONS);
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
//			sql = "CREATE TABLE IF NOT EXISTS " + 
//	        		TABLE_STUDENTS 			+ " (" + 
//	        		C_STUDENT_ID 	 		+ " TEXT PRIMARY KEY, " +
//	        		C_STUDENT_PASS 	 		+ " TEXT, " +
//	        		C_STUDENT_NAME 	 		+ " TEXT, " 	+ 
//	        		C_STUDENT_SCHOOL_LEVEL 	+ " TEXT, " 	+ 
//	        		C_STUDENT_YEAR_LEVEL 	+ " INTEGER, " 	+ 
//	        		C_STUDENT_ID_PICTURE 	+ " TEXT, " 	+ 
//	        		C_STUDENT_HIGH_SCORE  	+ " INTEGER);";
//			
//			db.execSQL(sql);
//			
//			sql = "CREATE TABLE IF NOT EXISTS " + 
//	        		TABLE_QUESTIONS + " (" 			+ 
//	        		C_QUESTION_ID   				+ " INTEGER PRIMARY KEY AUTOINCREMENT, " +
//	        		C_QUESTION_TEXT		 			+ " TEXT, " 	+ 
//	        		C_QUESTION_SCHOOL_LEVEL 	  	+ " TEXT, " 	+ 
//	        		C_QUESTION_YEAR_LEVEL  			+ " INTEGER, " 	+ 
//	        		C_QUESTION_STATUS 		  		+ " TEXT, " 	+ 
//	        		C_QUESTION_ANSWER 		  		+ " TEXT, " 	+
//	        		C_QUESTION_CHOICE_A 			+ " TEXT, " 	+ 
//	        		C_QUESTION_CHOICE_B 			+ " TEXT, " 	+ 
//	        		C_QUESTION_CHOICE_C 			+ " TEXT, " 	+
//	        		C_QUESTION_FILE 		  		+ " TEXT, " 	+
//	        		C_QUESTION_FILE_TYPE 		  	+ " TEXT, " 	+
//	        		C_QUESTION_SCORE_POINTS  		+ " INTEGER, " 	+
//	        		C_QUESTION_TIMER_SECS    		+ " INTEGER);";
//			
//			db.execSQL(sql);
//			
//			sql = "CREATE TABLE IF NOT EXISTS " + 
//	        		TABLE_ADMINISTRATOR 	+ " (" + 
//	        		C_ADMIN_ID 	 			+ " TEXT PRIMARY KEY, " +
//	        		C_ADMIN_PASS 	 		+ " TEXT, " +
//	        		C_ADMIN_NAME 	 		+ " TEXT, " 	+ 
//	        		C_ADMIN_ID_PICTURE 		+ " TEXT, " 	+ 
//	        		C_ADMIN_HIGH_SCORE  	+ " INTEGER);";
//			
//			db.execSQL(sql);
//		}
//
//		@Override
//		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//			db.execSQL("DROP TABLE IF EXISTS " + TABLE_STUDENTS);
//			db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUESTIONS);
//			onCreate(db);
//		}
//	}
//	
//	public void addStudent(User student){
//		ContentValues content = new ContentValues();
//		content.put(C_STUDENT_ID, student.getId());
//		content.put(C_STUDENT_PASS, student.getPass());
//		content.put(C_STUDENT_NAME, student.getName());
//		content.put(C_STUDENT_SCHOOL_LEVEL, student.getSchoolLevel());
//		content.put(C_STUDENT_YEAR_LEVEL, student.getYearLevel());
//		content.put(C_STUDENT_ID_PICTURE, student.getPicture());
//		content.put(C_STUDENT_HIGH_SCORE, student.getHighScore());
//		db.insert(TABLE_STUDENTS, null, content);
//	}
//	
//	public void addQuestion(Question question){
//		ContentValues content = new ContentValues();
//		content.put(C_QUESTION_TEXT, question.getText());
//		content.put(C_QUESTION_SCHOOL_LEVEL, question.getSchoolLevel());
//		content.put(C_QUESTION_YEAR_LEVEL, question.getYearLevel());
//		content.put(C_QUESTION_ANSWER, question.getAnswer());
//		content.put(C_QUESTION_CHOICE_A, question.getChoice_a());
//		content.put(C_QUESTION_CHOICE_B, question.getChoice_b());
//		content.put(C_QUESTION_CHOICE_C, question.getChoice_c());
//		content.put(C_QUESTION_SCORE_POINTS, question.getPoints());
//		content.put(C_QUESTION_TIMER_SECS, question.getTimer());
//		db.insert(TABLE_QUESTIONS, null, content);
//	}
//	
//	public User getSingleStudent(String id){
//	    Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_STUDENTS + " WHERE " + C_STUDENT_ID + "='" + id + "'", null);
//		cursor.moveToFirst();
//		User student = new User();
//		student.setId(cursor.getString(cursor.getColumnIndex(C_STUDENT_ID)));
//		student.setName(cursor.getString(cursor.getColumnIndex(C_STUDENT_NAME)));
//		student.setSchoolLevel(cursor.getString(cursor.getColumnIndex(C_STUDENT_SCHOOL_LEVEL)));
//		student.setYearLevel(cursor.getInt(cursor.getColumnIndex(C_STUDENT_YEAR_LEVEL)));
//		student.setPicture(cursor.getString(cursor.getColumnIndex(C_STUDENT_ID_PICTURE)));
//		student.setHighScore(cursor.getInt(cursor.getColumnIndex(C_STUDENT_HIGH_SCORE)));
//		cursor.close();
//		return student;
//	}
//	
//	public ArrayList<User> getMultipleStudents(String sql){
//		ArrayList<User> students = new ArrayList<User>();
//	    Cursor cursor = db.rawQuery(sql, null);
//	    
//    	 while(cursor.moveToNext()){
// 			User student = new User();
// 			student.setId(cursor.getString(cursor.getColumnIndex(C_STUDENT_ID)));
// 			student.setPass(cursor.getString(cursor.getColumnIndex(C_STUDENT_PASS)));
// 			student.setName(cursor.getString(cursor.getColumnIndex(C_STUDENT_NAME)));
// 			student.setSchoolLevel(cursor.getString(cursor.getColumnIndex(C_STUDENT_SCHOOL_LEVEL)));
// 			student.setYearLevel(cursor.getInt(cursor.getColumnIndex(C_STUDENT_YEAR_LEVEL)));
// 			student.setPicture(cursor.getString(cursor.getColumnIndex(C_STUDENT_ID_PICTURE)));
// 			student.setTimeElapsed(cursor.getString(cursor.getColumnIndex(C_STUDENT_TIME_ELAPSED)));
// 			student.setCorrectAnswers(cursor.getInt(cursor.getColumnIndex(C_STUDENT_CORRECT_ANSWERS)));
// 			student.setHighScore(cursor.getInt(cursor.getColumnIndex(C_STUDENT_HIGH_SCORE)));
// 			students.add(student);
// 	    }
//    	
//	    cursor.close();
//		return students;
//	}
//	
//	public ArrayList<Question> getMultipleQuestions(String sql){
//		ArrayList<Question> questions = new ArrayList<Question>();
//	    Cursor cursor = db.rawQuery(sql, null);
//	    
//    	 while(cursor.moveToNext()){
//    		Question question = new Question();
//    		question.setText(cursor.getString(cursor.getColumnIndex(C_QUESTION_TEXT)));
// 			question.setAnswer(cursor.getString(cursor.getColumnIndex(C_QUESTION_ANSWER)));
//    		question.setChoice_a(cursor.getString(cursor.getColumnIndex(C_QUESTION_CHOICE_A)));
//    		question.setChoice_b(cursor.getString(cursor.getColumnIndex(C_QUESTION_CHOICE_B)));
//    		question.setChoice_c(cursor.getString(cursor.getColumnIndex(C_QUESTION_CHOICE_C)));
//    		question.setFile(cursor.getString(cursor.getColumnIndex(C_QUESTION_FILE)));
//    		question.setType(cursor.getString(cursor.getColumnIndex(C_QUESTION_FILE_TYPE)));
//    		question.setScore_points(cursor.getInt(cursor.getColumnIndex(C_QUESTION_SCORE_POINTS)));
//    		question.setTimer(cursor.getInt(cursor.getColumnIndex(C_QUESTION_TIMER_SECS)));
// 			questions.add(question);
// 	    }
//    	
//	    cursor.close();
//		return questions;
//	}
//	
//	public boolean authenticate(String id, String pass){
//	    Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_STUDENTS + " WHERE " + C_STUDENT_ID + "='" + id + "' AND " + C_STUDENT_PASS + "='" + pass + "'", null);
//		int count = cursor.getCount();
//		cursor.close();
//		return (count > 0) ? true : false;
//	}
//
//}
