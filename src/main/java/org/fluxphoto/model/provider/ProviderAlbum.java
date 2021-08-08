package org.fluxphoto.model.provider;

/**
 * Models the third party provider album
 */
public class ProviderAlbum {

	private int userId;
	private int id;
	private String title; 

	public ProviderAlbum() {}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}
