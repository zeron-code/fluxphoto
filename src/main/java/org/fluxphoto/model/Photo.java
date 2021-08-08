package org.fluxphoto.model;

import org.fluxphoto.model.provider.ProviderPhoto;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "photos")
public class Photo {

	@Id
	private String photoId;
	
	private String albumId;
	private int providerId;
	private int providerAlbumId;
	
	private String title;
	private String url;
	private String thumbnailUrl;
	
	public static Photo fromProvider(ProviderPhoto providerPhoto) {
		Photo p = new Photo();
		
		p.setProviderAlbumId(providerPhoto.getAlbumId());
		p.setProviderId(providerPhoto.getId());
		p.setThumbnailUrl(providerPhoto.getThumbnailUrl());
		p.setUrl(providerPhoto.getUrl());
		p.setTitle(providerPhoto.getTitle());
		
		return p;
	}
	
	public String getPhotoId() {
		return photoId;
	}
	
	public void setPhotoId(String photoId) {
		this.photoId = photoId;
	}
	
	public String getAlbumId() {
		return albumId;
	}
	
	public void setAlbumId(String albumId) {
		this.albumId = albumId;
	}
	
	public int getProviderId() {
		return providerId;
	}
	
	public void setProviderId(int providerId) {
		this.providerId = providerId;
	}
	
	public int getProviderAlbumId() {
		return providerAlbumId;
	}
	
	public void setProviderAlbumId(int providerAlbumId) {
		this.providerAlbumId = providerAlbumId;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getUrl() {
		return url;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}
	
	public String getThumbnailUrl() {
		return thumbnailUrl;
	}
	
	public void setThumbnailUrl(String thumbnailUrl) {
		this.thumbnailUrl = thumbnailUrl;
	}
}
