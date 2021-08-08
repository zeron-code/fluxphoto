package org.fluxphoto.service;

import org.fluxphoto.model.Album;
import org.fluxphoto.model.AlbumAggregate;
import org.fluxphoto.model.Photo;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Central gallery service
 */
public interface GalleryService {

	/**
	 * Import the gallery from the remote 3rd party provider
	 */
	void importGallery();

	/**
	 * Get all albums
	 * @return {@link Flux} of {@link Album}s
	 */
	Flux<Album> getAllAlbums();
	
	/**
	 * Get a single album
	 * @param id Album ID
	 * @return {@link Mono} of {@link Album}
	 */
	Mono<Album> getAlbum(String id);
	
	/**
	 * Get user albums and photos
	 * 
	 * @param userId The user ID
	 * @return {@link Flux} of {@link AlbumAggregate}s
	 */
	Flux<AlbumAggregate> getUserAlbumsWithPhotos(String userId);
	
	/**
	 * Save a single album
	 * @param album The album to save
	 * @return {@link Mono} of {@link Album}
	 */
	Mono<Album> saveAlbum(Album album);

	/**
	 * Delete an album and associated photos
	 * @param id The album ID
	 */
	void deleteAlbum(String id);
	
	/**
	 * Get all photos
	 * @return {@link Flux} of {@link Photo}s
	 */
	Flux<Photo> getAllPhotos();
	
	/**
	 * Get a single photo
	 * @param id photo ID
	 * @return {@link Mono} of {@link Photo}
	 */
	Mono<Photo> getPhoto(String id);
	
	/**
	 * Save a single photo
	 * @param photo The photo to save
	 * @return {@link Mono} of {@link Photo}
	 */
	Mono<Photo> savePhoto(Photo photo);

	/**
	 * Delete a photo
	 * @param id The photo ID
	 */
	void deletePhoto(String id);

}
