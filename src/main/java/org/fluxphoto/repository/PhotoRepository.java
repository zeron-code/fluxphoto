package org.fluxphoto.repository;

import org.fluxphoto.model.Photo;
import org.springframework.data.mongodb.repository.DeleteQuery;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import reactor.core.publisher.Mono;

/**
 * A MongoDB photo repository
 */
public interface PhotoRepository extends ReactiveMongoRepository<Photo, String> {

	@DeleteQuery("{'albumId' : ?0}")
	Mono<Void> deleteByAlbum(String albumId);
}
