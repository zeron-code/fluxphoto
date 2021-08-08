package org.fluxphoto.api;

import org.fluxphoto.service.GalleryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;

@RestController("galleryController")
public class GalleryController {

	@Autowired
	private GalleryService galleryService;
	
	@GetMapping("/import")
	public Mono<String> importGallery() {
		galleryService.importGallery();
		return Mono.just("Import in process");
	}
}
