package home_books_api.controller;

import home_books_api.config.ApiVersion;
import home_books_api.model.Book;
import home_books_api.model.Room;
import home_books_api.model.Shelf;
import home_books_api.repository.ShelfRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
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

    @GetMapping("/{id}")
    public ResponseEntity<Resource<Shelf>> getShelf(@PathVariable Integer id) {
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

    @GetMapping(params = "idRoom")
    public Resources<Resource<Shelf>> getShelvesByRoom(@RequestParam("idRoom") Integer idRoom) {
        Resources<Resource<Shelf>> resources = new Resources<>(
                this.shelfRepository.findByRoomId(idRoom).stream().map(this::resource)
                        .collect(Collectors.toList()));
        addShelfLink(resources, REL_SELF);
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

    @GetMapping(params = "number")
    public Resources<Resource<Shelf>> findShelfByLetter(@RequestParam("letter") Integer number) {
        Resources<Resource<Shelf>> resources = new Resources<>(
                this.shelfRepository.findShelfByNumber(number).stream()
                        .map(this::resource)
                        .collect(Collectors.toList()));
        addShelfLink(resources, REL_SELF);
        return resources;
    }

    @PostMapping
    public ResponseEntity<?> addShelf(@RequestBody Shelf shelf) {
        Shelf addedShelf = this.shelfRepository.save(shelf);
        return ResponseEntity.created(URI.create(
                resource(addedShelf).getLink(REL_SELF).getHref()))
                .build();
    }

    @DeleteMapping("/{id}")
    public void deleteShelf(@PathVariable("id") Integer id) {
        this.shelfRepository.deleteById(id);
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
