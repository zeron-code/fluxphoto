package org.fluxphoto.api;

import org.fluxphoto.model.AlbumAggregate;
import org.fluxphoto.service.GalleryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Flux;

@RestController("userAlbumController")
public class UserAlbumController {

	@Autowired
	private GalleryService galleryService;
	
	@GetMapping("/users/{userId}/albums")
	public Flux<AlbumAggregate> getUserAlbum(@PathVariable("userId") String userId) {
		return galleryService.getUserAlbumsWithPhotos(userId);
	}
}
