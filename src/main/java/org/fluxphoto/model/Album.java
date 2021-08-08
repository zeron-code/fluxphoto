package org.fluxphoto.model;

import org.fluxphoto.model.provider.ProviderAlbum;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "albums")
public class Album {

	@Id
	private String albumId;
	
	private String userId;
	private int providerId;
	private int providerUserId;	
	private String title;

	public static Album fromProvider(ProviderAlbum providerAlbum) {
		Album a = new Album();
		
		a.setProviderId(providerAlbum.getId());
		a.setProviderUserId(providerAlbum.getUserId());
		a.setTitle(providerAlbum.getTitle());
		return a;
	}

	public String getAlbumId() {
		return albumId;
	}

	public void setAlbumId(String albumId) {
		this.albumId = albumId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public int getProviderId() {
		return providerId;
	}

	public void setProviderId(int providerId) {
		this.providerId = providerId;
	}

	public int getProviderUserId() {
		return providerUserId;
	}

	public void setProviderUserId(int providerUserId) {
		this.providerUserId = providerUserId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}
