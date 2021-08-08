package org.fluxphoto.service;

import java.util.Date;

import org.fluxphoto.model.Album;
import org.fluxphoto.model.AlbumAggregate;
import org.fluxphoto.model.Photo;
import org.fluxphoto.model.User;
import org.fluxphoto.model.provider.ProviderAlbum;
import org.fluxphoto.model.provider.ProviderPhoto;
import org.fluxphoto.repository.AlbumRepository;
import org.fluxphoto.repository.PhotoRepository;
import org.fluxphoto.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.aggregation.SortOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

@Service("galleryService")
public class GalleryServiceImpl implements GalleryService {

	@Autowired
	private WebClient webClient;

	@Autowired
	private UserRepository userRepository;	
	
	@Autowired
	private AlbumRepository albumRepository;

	@Autowired
	private PhotoRepository photoRepository;
	
	@Autowired
    private ReactiveMongoTemplate mongoTemplate;

	@Override
	@Async("taskExecutor")
	public void importGallery() {
		
		// Drop any existing data
		photoRepository.deleteAll().block();
		albumRepository.deleteAll().block();
		userRepository.deleteAll().block();
		
		// Import users
		importModule("/users", User.class, userRepository);
		
		// Import albums
		Flux<ProviderAlbum> albumFlux = webClient
			.get()
			.uri("/albums")
			.retrieve()
			.bodyToFlux(ProviderAlbum.class);
	
		albumFlux
			.map(providerAlbum -> Album.fromProvider(providerAlbum))
			.flatMap(this::loadUser)
			.flatMap(this::linkUserToAlbum)
			.flatMap(albumRepository::save)
			.subscribe();
		
		// Import photos
		Flux<ProviderPhoto> photoFlux = webClient
			.get()
			.uri("/photos")
			.retrieve()
			.bodyToFlux(ProviderPhoto.class);
		
		photoFlux
			.map(providerPhoto -> Photo.fromProvider(providerPhoto))
			.flatMap(this::loadAlbum)
			.flatMap(this::linkAlbumToPhoto)
			.flatMap(photoRepository::save)
			.subscribe();
	}
	
	private Mono<Tuple2<User, Album>> loadUser(Album album) {
		return userRepository.findByProviderId(album.getProviderUserId())
			.map(dbUser -> Tuples.of(dbUser, album));
	}
	
	private Mono<Album> linkUserToAlbum(Tuple2<User, Album> t) {
		t.getT2().setUserId(t.getT1().getUserId());
		return Mono.just(t.getT2());
	}
	
	private Mono<Tuple2<Album, Photo>> loadAlbum(Photo photo) {
		return albumRepository.findByProviderId(photo.getProviderAlbumId())
			.map(dbAlbum -> Tuples.of(dbAlbum, photo));
	}
	
	private Mono<Photo> linkAlbumToPhoto(Tuple2<Album, Photo> t) {
		t.getT2().setAlbumId(t.getT1().getAlbumId());
		return Mono.just(t.getT2());
	}
	
	private <T> void importModule(String uri, Class<T> klass, ReactiveMongoRepository<T, String> repository) {
		Flux<T> flux = webClient
				.get()
				.uri(uri)
				.retrieve()
				.bodyToFlux(klass);
		
		repository.saveAll(flux).subscribe();
	}
	
	@Override
	public Mono<Photo> getPhoto(String id) {
		return photoRepository.findById(id);
	}
	
	@Override
	public Flux<Photo> getAllPhotos() {
		return photoRepository.findAll();
	}

	@Override
	public Mono<Photo> savePhoto(Photo photo) {
		photo.setUpdateDate(new Date());
		return photoRepository.save(photo);
	}
	
	@Override
	public void deletePhoto(String id) {
		photoRepository.deleteById(id).subscribe();
	}

	@Override
	public Flux<Album> getAllAlbums() {
		return albumRepository.findAll();
	}

	@Override
	public Mono<Album> getAlbum(String id) {
		return albumRepository.findById(id);
	}
	
	@Override
	public Flux<AlbumAggregate> getUserAlbumsWithPhotos(String userId) {
		LookupOperation lookupOp = LookupOperation.newLookup()
                .from("photos")
                .localField("_id")
                .foreignField("albumId")
                .as("photos");
		
		SortOperation sortAlbums = Aggregation.sort(Direction.ASC, "updateDate");
		SortOperation sortPhotos = Aggregation.sort(Direction.ASC, "photos.updateDate");
		
		Aggregation aggregation = Aggregation
				.newAggregation(Aggregation.match(Criteria.where("userId").is(userId)), 
						lookupOp, sortAlbums, sortPhotos);
		
        return mongoTemplate.aggregate(aggregation, "albums", AlbumAggregate.class);
	}
	
	@Override
	public Mono<Album> saveAlbum(Album album) {
		album.setUpdateDate(new Date());
		return albumRepository.save(album);
	}

	@Override
	public void deleteAlbum(String id) {
		albumRepository.deleteById(id).subscribe();
		photoRepository.deleteByAlbum(id).subscribe();
	}
}
