package org.fluxphoto.api;

import org.fluxphoto.model.Photo;
import org.fluxphoto.service.GalleryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController("photoController")
@RequestMapping("/photos")
public class PhotoController {

	@Autowired
	private GalleryService galleryService;
	
	@GetMapping
	public Flux<Photo> getAllPhotos() {
		return galleryService.getAllPhotos();
	}
	
	@GetMapping("/{id}")
	public Mono<Photo> getPhoto(@PathVariable("id") String id) {
		return galleryService.getPhoto(id);
	}
	
	@PostMapping
	public ResponseEntity<Mono<Photo>> createPhoto(@RequestBody Photo photo) {
		Mono<Photo> saved = galleryService.savePhoto(photo);
		return ResponseEntity.status(HttpStatus.CREATED).body(saved);
	}

	@PutMapping
	public Mono<Photo> updatePhoto(@RequestBody Photo photo) {
		return galleryService.savePhoto(photo);
	}
	
	@DeleteMapping("/{id}")
	public Mono<ResponseEntity<Void>> deletePhoto(@PathVariable("id") String id) {
		galleryService.deletePhoto(id);
		return Mono.just(ResponseEntity.noContent().build());
	}
}
