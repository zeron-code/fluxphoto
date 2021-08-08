package org.fluxphoto.repository;

import org.fluxphoto.model.Album;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import reactor.core.publisher.Mono;

/**
 * A MongoDB album repository
 */
public interface AlbumRepository extends ReactiveMongoRepository<Album, String> {

	@Query("{'providerId' : ?0}")
	Mono<Album> findByProviderId(Integer providerAlbumId);
}
