package org.fluxphoto.api;

import org.fluxphoto.model.Album;
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

@RestController("albumController")
@RequestMapping("/albums")
public class AlbumController {

	@Autowired
	private GalleryService galleryService;
	
	@GetMapping
	public Flux<Album> getAllAlbums() {
		return galleryService.getAllAlbums();
	}
	
	@GetMapping("/{id}")
	public Mono<Album> getAlbum(@PathVariable("id") String id) {
		return galleryService.getAlbum(id);
	}
	
	@PostMapping
	public ResponseEntity<Mono<Album>> createAlbum(@RequestBody Album album) {
		Mono<Album> saved = galleryService.saveAlbum(album);
		return ResponseEntity.status(HttpStatus.CREATED).body(saved);
	}

	@PutMapping
	public Mono<Album> updateAlbum(@RequestBody Album album) {
		return galleryService.saveAlbum(album);
	}

	@DeleteMapping("/{id}")
	public Mono<ResponseEntity<Void>> deleteAlbum(@PathVariable("id") String id) {
		galleryService.deleteAlbum(id);
		return Mono.just(ResponseEntity.noContent().build());
	}
}
