package org.fluxphoto.repository;

import org.fluxphoto.model.User;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import reactor.core.publisher.Mono;

/**
 * A MongoDB user repository
 */
public interface UserRepository extends ReactiveMongoRepository<User, String> {
	
	@Query("{'id' : ?0}")
	Mono<User> findByProviderId(Integer providerUserId);
}
