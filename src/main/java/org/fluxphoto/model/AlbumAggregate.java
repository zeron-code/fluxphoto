package org.fluxphoto.model;

import java.util.List;

import org.springframework.data.annotation.Id;

public class AlbumAggregate {

	@Id
	private String albumId;

	private String title;
	private List<Photo> photos;

	public AlbumAggregate() {}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<Photo> getPhotos() {
		return photos;
	}

	public void setPhotos(List<Photo> photos) {
		this.photos = photos;
	}
}
