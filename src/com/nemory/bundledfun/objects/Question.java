package com.nemory.bundledfun.objects;

import java.util.ArrayList;
import java.util.Collections;

public class Question {

	private String 	text;
	private String 	difficulty;
	private String 	answer;
	private String 	choice_a;
	private String 	choice_b;
	private String 	choice_c;
	private String 	file_name;
	private String 	type;
	private int 	points;
	private int 	timer;
	
	public static ArrayList<Question> questions = new ArrayList<Question>();
	
	public static void shuffle(){
		Collections.shuffle(questions);
	}
	
	/** ------------ SETTERS AND GETTERS ------------ **/
	
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}

	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	public String getChoice_a() {
		return choice_a;
	}
	public void setChoice_a(String choice_a) {
		this.choice_a = choice_a;
	}
	public String getChoice_b() {
		return choice_b;
	}
	public void setChoice_b(String choice_b) {
		this.choice_b = choice_b;
	}
	public String getChoice_c() {
		return choice_c;
	}
	public void setChoice_c(String choice_c) {
		this.choice_c = choice_c;
	}
	public String getFileName() {
		return file_name;
	}
	public void setFile(String file_name) {
		this.file_name = file_name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getPoints() {
		return points;
	}
	public void setScore_points(int points) {
		this.points = points;
	}
	public int getTimer() {
		return timer * 1000;
	}
	public void setTimer(int timer) {
		this.timer = timer;
	}

	public String getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(String difficulty) {
		this.difficulty = difficulty;
	}
	
}
