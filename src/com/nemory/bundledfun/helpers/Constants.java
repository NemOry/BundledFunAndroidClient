package com.nemory.bundledfun.helpers;

import java.io.File;

import android.os.Environment;

public class Constants{

	//DATABASE VARIABLES
	public static final String DATABASE_NAME 			= "db_bundledfun";
	public static final int	   DATABASE_VERSION 		= 1;
	public static final String TABLE_USERS 				= "tbl_users";
    
	//USER COLUMNS
	public static final String C_USER_ID 				= "user_id";
	public static final String C_USER_PASS 				= "password"; 
	public static final String C_USER_NAME 				= "name";
	public static final String C_USER_PICTURE 			= "picture";
	public static final String C_USER_TIME_ELAPSED 		= "time_elapsed";
	public static final String C_USER_CORRECT_ANSWERS 	= "correct_answers";
	public static final String C_USER_HIGH_SCORE 		= "high_score";

	// PREFS KEYS
	public static final String	KEY_PREFS_SYNCED 		= "key_prefs_synced";
	public static final String	KEY_PREFS_USERNAME 		= "key_prefs_username";
	public static final String	KEY_PREFS_PASSWORD 		= "key_prefs_password";
	public static final String	KEY_PREFS_CHECK_UPDATES	= "key_prefs_check_updates";
	public static final String 	KEY_PREFS_ID 			= "key_prefs_group_id";
	
	public static final String DATAPATH = "sdcard/.BundledFun/";
	public static final String BUNDLEDFUN_FOLDER = ".BundledFun";
	
	public static String HOST_NAME = "";
	
	public static void createFolders(){
		String sdcardPath = Environment.getExternalStorageDirectory() + File.separator;
		String rootPath = ".BundledFun/";
		
		File files = new File(sdcardPath + rootPath + "files");
		File users = new File(sdcardPath + rootPath + "files" + File.separator + "users");
		File questions = new File(sdcardPath + rootPath + "files" + File.separator + "questions");
		
		File[] folders = {files, questions, users};
		
		for(File f : folders){
			f.mkdirs();
		}
	}
	
	public static void deleteFiles(File folder){
		
		if (!folder.exists()){
            return;
		}
		
		for (File file1 : folder.listFiles()) { // files inside /.BundledFun/
        	if(file1.isDirectory()){
        		for (File file2 : file1.listFiles()) { // Files inside /files/
        			if(file2.isDirectory()){
	        			for (File file3 : file2.listFiles()) { // files inside /questions/ and /users/
	        				file3.delete();
	                    }
        			}
                }
        	}
        }
		
        folder.delete();
        
	}
	
	public static int countFiles(File bundledFun){
		
		int totalFiles = 0;
		
		if (!bundledFun.exists()){
            return 0;
		}
		
		boolean hasIterated = false;
		
		for (File easyMediumHardUsers : bundledFun.listFiles()) {
        	if(easyMediumHardUsers.isDirectory()){
        		for (File videosImagesAudios : easyMediumHardUsers.listFiles()) {
        			totalFiles += videosImagesAudios.listFiles().length;
                }
        		
        		if(easyMediumHardUsers.equals("users") && !hasIterated){
        			totalFiles += easyMediumHardUsers.listFiles().length;
        			hasIterated = true;
        		}
        	}
        }
		
		return totalFiles;
	}
}
