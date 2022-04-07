package home_books_api.controller;

import home_books_api.config.ApiVersion;
import home_books_api.model.Shelf;
import home_books_api.repository.ShelfRepository;
import home_books_api.service.ShelfService;
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
@RequestMapping(value = "/api/shelves", produces = {ApiVersion.V1_HAL_JSON, MediaType.ALL_VALUE})
public class ShelfRestApiController {

    private static final String REL_SELF = "self";
    private static final String REL_BOOKS = "books";
    @Autowired
    private ShelfRepository shelfRepository;

    private ShelfService shelfService;

    public ShelfRestApiController(ShelfService shelfService) {
        this.shelfService = shelfService;
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Resource<Shelf>> getShelf(@PathVariable Integer id) {
        return this.shelfRepository.findById(id).map(this::resource)
                .map(this::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping(path = "/{id}", produces = ApiVersion.V2_FOR_ANGULAR)
    public ResponseEntity<Resource<Shelf>> getShelfForAngular(@PathVariable Integer id) {
        return this.shelfRepository.findById(id).map(this::resource)
                .map(this::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public Resources<Resource<Shelf>> getShelves() {
        Resources<Resource<Shelf>> resources = new Resources<>(
                this.shelfRepository.findAll().stream().map(this::resource)
                        .collect(Collectors.toList()));
        addShelfLink(resources, REL_SELF);
        return resources;
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping(produces = ApiVersion.V2_FOR_ANGULAR)
    public List<Shelf> getShelvesForAngular() {
        List<Shelf> resources = this.shelfRepository.findAll();
        return resources;
    }

    @GetMapping(params = "idRoom")
    public Resources<Resource<Shelf>> getShelvesByRoom(@RequestParam("idRoom") Integer idRoom) {
        Resources<Resource<Shelf>> resources = new Resources<>(
                this.shelfRepository.findByRoomId(idRoom).stream().map(this::resource)
                        .collect(Collectors.toList()));
        addShelfLink(resources, REL_SELF);
        return resources;
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping(params = "idRoom", produces = ApiVersion.V2_FOR_ANGULAR)
    public List<Resource<Shelf>> getShelvesByRoomForAngular(@RequestParam("idRoom") Integer idRoom) {
        List<Resource<Shelf>> resources = this.shelfRepository.findByRoomId(idRoom).stream().map(this::resource)
                        .collect(Collectors.toList());
        return resources;
    }

    @GetMapping(params = "letter")
    public Resources<Resource<Shelf>> findShelfByLetter(@RequestParam("letter") String letter) {
        Resources<Resource<Shelf>> resources = new Resources<>(
                this.shelfRepository.findShelfByLetter(letter).stream()
                        .map(this::resource)
                        .collect(Collectors.toList()));
        addShelfLink(resources, REL_SELF);
        return resources;
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping(params = "letter", produces = ApiVersion.V2_FOR_ANGULAR)
    public List<Resource<Shelf>> findShelfByLetterForAngular(@RequestParam("letter") String letter) {
        List<Resource<Shelf>> resources = this.shelfRepository.findShelfByLetter(letter).stream()
                        .map(this::resource)
                        .collect(Collectors.toList());
        return resources;
    }

    @GetMapping(params = "number")
    public Resources<Resource<Shelf>> findShelfByLetter(@RequestParam("letter") Integer number) {
        Resources<Resource<Shelf>> resources = new Resources<>(
                this.shelfRepository.findShelfByNumber(number).stream()
                        .map(this::resource)
                        .collect(Collectors.toList()));
        addShelfLink(resources, REL_SELF);
        return resources;
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping(params = "number", produces = ApiVersion.V2_FOR_ANGULAR)
    public List<Resource<Shelf>> findShelfByLetterForAngular(@RequestParam("letter") Integer number) {
        List<Resource<Shelf>> resources = this.shelfRepository.findShelfByNumber(number).stream()
                        .map(this::resource)
                        .collect(Collectors.toList());
        return resources;
    }

    @PostMapping
    public ResponseEntity<?> addShelf(@RequestBody Shelf shelf) {
        Shelf addedShelf = this.shelfRepository.save(shelf);
        return ResponseEntity.created(URI.create(
                resource(addedShelf).getLink(REL_SELF).getHref()))
                .build();
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping(produces = ApiVersion.V2_FOR_ANGULAR)
    public ResponseEntity<?> addShelfForAngular(@RequestBody Shelf shelf) {
        Shelf addedShelf = this.shelfService.addShelf(shelf);
        return ResponseEntity.created(URI.create(
                resource(addedShelf).getLink(REL_SELF).getHref()))
                .build();
    }

    @DeleteMapping("/{id}")
    public void deleteShelf(@PathVariable("id") Integer id) {
        this.shelfRepository.deleteById(id);
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @DeleteMapping(path = "/{id}", produces = ApiVersion.V2_FOR_ANGULAR)
    public void deleteShelfForAngular(@PathVariable("id") Integer id) {
        this.shelfRepository.deleteById(id);
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @PatchMapping(path = "/{id}", produces = ApiVersion.V2_FOR_ANGULAR)
    public void updateShelfForAngular(@PathVariable Integer id, @RequestBody Shelf newPartialShelf) {
        this.shelfService.updateShelf(id, newPartialShelf);
    }

    private ResponseEntity<String> notFound() {
        return ResponseEntity.notFound().build();
    }

    private <T> ResponseEntity<T> ok(T body) {
        return ResponseEntity.ok().body(body);
    }

    private Resource<Shelf> resource(Shelf shelf) {
        Resource<Shelf> authorResource = new Resource<>(shelf);
        authorResource.add(linkTo(
                methodOn(ShelfRestApiController.class)
                        .getShelf(shelf.getId()))
                .withSelfRel());
        addShelfBooksLink(authorResource, REL_BOOKS, shelf.getId());
        return authorResource;
    }

    private void addShelfBooksLink(Resource<Shelf> resources, String rel, Integer id) {
        resources.add(linkTo(methodOn(BookRestApiController.class)
                .getBooksByShelf(id))
                .withRel(rel));
    }

    private void addShelfLink(Resources<Resource<Shelf>> resources, String rel) {
        resources.add(linkTo(ShelfRestApiController.class)
                .withRel(rel));
    }
}
