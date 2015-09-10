package com.nemory.bundledfun.helpers;


public class Constants{

	//DATABASE VARIABLES
	public static final String DATABASE_NAME 	= "dbBundledFun";
	public static final int	   DATABASE_VERSION = 1;
	
	//TABLES
	public static final String TABLE_QUESTIONS 		= "tblQuestions";
	public static final String TABLE_STUDENTS 		= "tblStudents";
	public static final String TABLE_ADMINISTRATOR 	= "tblAdministrators";

	//QUESTION COLUMNS
	public static final String C_QUESTION_ID 			= "QuestionID";
	public static final String C_QUESTION_TEXT 			= "Text";
	public static final String C_QUESTION_SCHOOL_LEVEL	= "SchoolLevel";
	public static final String C_QUESTION_YEAR_LEVEL	= "YearLevel";
	public static final String C_QUESTION_STATUS 		= "Status";
	public static final String C_QUESTION_ANSWER 		= "CorrectAnswer";
	public static final String C_QUESTION_CHOICE_A 		= "A";
	public static final String C_QUESTION_CHOICE_B 		= "B";
	public static final String C_QUESTION_CHOICE_C 		= "C"; 
	public static final String C_QUESTION_FILE 			= "File";
	public static final String C_QUESTION_FILE_TYPE 	= "FileType";
	public static final String C_QUESTION_SCORE_POINTS 	= "ScorePoints";
	public static final String C_QUESTION_TIMER_SECS 	= "TimerSecs";
    
	//STUDENT COLUMNS
	public static final String C_STUDENT_ID 				= "StudentID";
	public static final String C_STUDENT_PASS 				= "Password"; 
	public static final String C_STUDENT_NAME 				= "Name";
	public static final String C_STUDENT_SCHOOL_LEVEL 		= "SchoolLevel";
	public static final String C_STUDENT_YEAR_LEVEL 		= "YearLevel";
	public static final String C_STUDENT_ID_PICTURE 		= "IDPicture";
	public static final String C_STUDENT_TIME_ELAPSED 		= "TimeElapsed";
	public static final String C_STUDENT_CORRECT_ANSWERS 	= "CorrectAnswers";
	public static final String C_STUDENT_HIGH_SCORE 		= "HighScore";
	
	//ADMINISTRATOR COLUMNS
	public static final String C_ADMIN_ID 			= "AdminID";
	public static final String C_ADMIN_PASS 		= "Password";
	public static final String C_ADMIN_NAME 		= "Name";
	public static final String C_ADMIN_ID_PICTURE 	= "IDPicture";
	public static final String C_ADMIN_HIGH_SCORE 	= "HighScore";

	// PREFS KEYS
	public static final String	KEY_PREFS_SOUND = "key_prefs_sounds";

	// SECONDS CONSTANT
	public static final int	_10_SECONDS = 10000;
	public static final int	_20_SECONDS = 20000;
	public static final int	_30_SECONDS = 30000;
	public static final int	_40_SECONDS = 40000;
	public static final int	_50_SECONDS = 50000;
	public static final int	_60_SECONDS = 60000;
	
	// QUESTIONS LIMIT PER GAME
	public static final int	QUESTIONS_LIMIT = 100;
	
	public static final String DATAPATH = "sdcard/.BundledFun/";
}
