package home_books_api.controller;

import home_books_api.config.ApiVersion;
import home_books_api.model.Publisher;
import home_books_api.modelDTO.PublisherDTO;
import home_books_api.repository.PublisherRepository;
import home_books_api.service.PublisherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
@RequestMapping(value = "/api/publishers", produces = {ApiVersion.V1_HAL_JSON, MediaType.ALL_VALUE})
public class PublisherRestApiController {

    private static final String REL_SELF = "self";
    private static final String REL_BOOKS = "books";
    @Autowired
    private PublisherRepository publisherRepository;

    private PublisherService publisherService;

    public PublisherRestApiController(PublisherService publisherService) {
        this.publisherService = publisherService;
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Resource<Publisher>> getPublisher(@PathVariable Integer id) {
        return this.publisherRepository.findById(id).map(this::resource)
                .map(this::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping(path = "/{id}", produces = ApiVersion.V2_FOR_ANGULAR)
    public ResponseEntity<Resource<Publisher>> getPublisherForAngular(@PathVariable Integer id) {
        return this.publisherRepository.findById(id).map(this::resource)
                .map(this::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public Resources<Resource<Publisher>> getPublishers() {
        Resources<Resource<Publisher>> resources = new Resources<>(
                this.publisherRepository.findAll().stream().map(this::resource)
                        .collect(Collectors.toList()));
        addPublisherLink(resources, REL_SELF);
        return resources;
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping(produces = ApiVersion.V2_FOR_ANGULAR)
    public List<Resource<PublisherDTO>> getPublishersForAngular() {
        return this.publisherService.getPublishersForAngular().stream().map(this::resourceAngular)
                .collect(Collectors.toList());
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @PatchMapping(path = "/{id}", produces = ApiVersion.V2_FOR_ANGULAR)
    public void updatePublisherForAngular(@PathVariable Integer id, @RequestBody Publisher newPartialPublisher) {
        this.publisherService.updatePublisher(id, newPartialPublisher);
    }

    @PatchMapping(path = "/{id}")
    public void updatePublisher(@PathVariable Integer id, @RequestBody Publisher newPartialPublisher) {
        this.publisherService.updatePublisher(id, newPartialPublisher);
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping(params = "name", produces = ApiVersion.V2_FOR_ANGULAR)
    public Resources<Resource<Publisher>> findPublisherByNameForAngular(@RequestParam("name") String name) {
        Resources<Resource<Publisher>> resources = new Resources<>(
                this.publisherRepository.findPublisherByName(name).stream()
                        .map(this::resource)
                        .collect(Collectors.toList()));
        addPublisherLink(resources, REL_SELF);
        return resources;
    }

    @GetMapping(params = "name")
    public Resources<Resource<Publisher>> findPublisherByName(@RequestParam("name") String name) {
        Resources<Resource<Publisher>> resources = new Resources<>(
                this.publisherRepository.findPublisherByName(name).stream()
                        .map(this::resource)
                        .collect(Collectors.toList()));
        addPublisherLink(resources, REL_SELF);
        return resources;
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping(produces = ApiVersion.V2_FOR_ANGULAR)
    public ResponseEntity<?> addPublisherForAngular(@RequestBody Publisher publisher) {
        Publisher addedPublisher = this.publisherRepository.save(publisher);
        return ResponseEntity.created(URI.create(
                        resource(addedPublisher).getLink(REL_SELF).getHref()))
                .build();
    }

    @PostMapping
    public ResponseEntity<?> addPublisher(@RequestBody Publisher publisher) {
        Publisher addedPublisher = this.publisherRepository.save(publisher);
        return ResponseEntity.created(URI.create(
                        resource(addedPublisher).getLink(REL_SELF).getHref()))
                .build();
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @DeleteMapping(path = "/{id}", produces = ApiVersion.V2_FOR_ANGULAR)
    public void deletePublisherForAngular(@PathVariable("id") Integer id) {
        this.publisherRepository.deleteById(id);
    }

    @DeleteMapping(path = "/{id}")
    public void deletePublisher(@PathVariable("id") Integer id) {
        this.publisherRepository.deleteById(id);
    }

    private ResponseEntity<String> notFound() {
        return ResponseEntity.notFound().build();
    }

    private <T> ResponseEntity<T> ok(T body) {
        return ResponseEntity.ok().body(body);
    }

    private Resource<Publisher> resource(Publisher publisher) {
        Resource<Publisher> authorResource = new Resource<>(publisher);
        authorResource.add(linkTo(
                methodOn(PublisherRestApiController.class)
                        .getPublisher(publisher.getId()))
                .withSelfRel());
        authorResource.add(linkTo(methodOn(BookRestApiController.class)
                .getBooksByPublisher(publisher.getId()))
                .withRel(REL_BOOKS));
        return authorResource;
    }

    private Resource<PublisherDTO> resourceAngular(PublisherDTO publisher) {
        Resource<PublisherDTO> authorResource = new Resource<>(publisher);
        authorResource.add(linkTo(
                methodOn(PublisherRestApiController.class)
                        .getPublisher(publisher.getId()))
                .withSelfRel());
        authorResource.add(linkTo(methodOn(BookRestApiController.class)
                .getBooksByPublisher(publisher.getId()))
                .withRel(REL_BOOKS));
        return authorResource;
    }

    private void addPublisherLink(Resources<Resource<Publisher>> resources, String rel) {
        resources.add(linkTo(PublisherRestApiController.class)
                .withRel(rel));
    }
}
