package org.fluxphoto.repository;

import org.fluxphoto.model.Photo;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

/**
 * A MongoDB photo repository
 */
public interface PhotoRepository extends ReactiveMongoRepository<Photo, String> {
	
	//@Aggregation()
	//void g();
}
