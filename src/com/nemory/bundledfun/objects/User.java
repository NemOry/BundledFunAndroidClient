package com.nemory.bundledfun.objects;

import java.util.ArrayList;

public class User {
	
	private String 	id;
	private String 	pass;
	private String 	name;
	private String 	picture;
	private int 	highScore;
	private String 	timeElapsed;
	private int 	correctAnswers;
	
	public static ArrayList<User> users = new ArrayList<User>();
	
	public static User getSingle(String id) {
		for(User student : users){
			if(student.getId().equals(id)){
				return student;
			}
		}
		return null;
	}
	
	public static boolean exists(String id) {
		for(User student : users){
			if(student.getId().equals(id)){
				return true;
			}
		}
		return false;
	}
	
	public static boolean authenticate(String id, String pass) {
		for(User s : users){
			if(s.getId().equals(id) && s.getPass().equals(pass)){
				return true;
			}
		}
		return false;
	}

	/** ------------ SETTERS AND GETTERS ------------ **/
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getHighScore() {
		return highScore;
	}

	public void setHighScore(int highScore) {
		this.highScore = highScore;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public String getTimeElapsed() {
		return timeElapsed;
	}

	public void setTimeElapsed(String timeElapsed) {
		this.timeElapsed = timeElapsed;
	}

	public int getCorrectAnswers() {
		return correctAnswers;
	}

	public void setCorrectAnswers(int correctAnswers) {
		this.correctAnswers = correctAnswers;
	}
}