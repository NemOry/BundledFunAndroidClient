package com.nemory.bundledfun.objects;

public class Administrator{

	private String 	id;
	private String 	name;
	private int 	highScore;
	private String 	idPicture;
	
	/** ------------ SETTERS AND GETTERS ------------ **/
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
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
	public String getIdPicture() {
		return idPicture;
	}
	public void setIdPicture(String idPicture) {
		this.idPicture = idPicture;
	}
	
}
