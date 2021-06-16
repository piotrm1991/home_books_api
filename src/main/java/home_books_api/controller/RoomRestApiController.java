package home_books_api.controller;

import home_books_api.config.ApiVersion;
import home_books_api.model.Publisher;
import home_books_api.model.Room;
import home_books_api.repository.BookRepository;
import home_books_api.repository.RoomRepository;
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
@RequestMapping(value = "/api/rooms", produces = {ApiVersion.V1_HAL_JSON, MediaType.ALL_VALUE})
public class RoomRestApiController {

    private static final String REL_SELF = "self";
    private static final String REL_BOOKS = "books";
    private static final String REL_SHELVES = "shelves";
    @Autowired
    private RoomRepository roomRepository;

    @GetMapping("/{id}")
    public ResponseEntity<Resource<Room>> getRoom(@PathVariable Integer id) {
        return this.roomRepository.findById(id).map(this::resource)
                .map(this::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public Resources<Resource<Room>> getRooms() {
        Resources<Resource<Room>> resources = new Resources<>(
                this.roomRepository.findAll().stream().map(this::resource)
                        .collect(Collectors.toList()));
        addRoomLink(resources, REL_SELF);
        return resources;
    }

    @GetMapping(params = "name")
    public Resources<Resource<Room>> findRoomByName(@RequestParam("name") String name) {
        Resources<Resource<Room>> resources = new Resources<>(
                this.roomRepository.findRoomByName(name).stream()
                        .map(this::resource)
                        .collect(Collectors.toList()));
        addRoomLink(resources, REL_SELF);
        return resources;
    }

    @PostMapping
    public ResponseEntity<?> addRoom(@RequestBody Room room) {
        Room addedRoom = this.roomRepository.save(room);
        return ResponseEntity.created(URI.create(
                resource(addedRoom).getLink(REL_SELF).getHref()))
                .build();
    }

    @DeleteMapping("/{id}")
    public void deleteRoom(@PathVariable("id") Integer id) {
        this.roomRepository.deleteById(id);
    }

    private ResponseEntity<String> notFound() {
        return ResponseEntity.notFound().build();
    }

    private <T> ResponseEntity<T> ok(T body) {
        return ResponseEntity.ok().body(body);
    }

    private Resource<Room> resource(Room room) {
        Resource<Room> authorResource = new Resource<>(room);
        authorResource.add(linkTo(
                methodOn(RoomRestApiController.class)
                        .getRoom(room.getId()))
                .withSelfRel());
        this.addRoomBooksLink(authorResource, REL_BOOKS, room.getId());
        this.addRoomShelvesLink(authorResource, REL_SHELVES, room.getId());
        return authorResource;
    }

    private void addRoomBooksLink(Resource<Room> resources, String rel, Integer id) {
        resources.add(linkTo(methodOn(BookRestApiController.class)
                .getBooksByRoom(id))
                .withRel(rel));
    }

    private void addRoomShelvesLink(Resource<Room> resources, String rel, Integer id) {
        resources.add(linkTo(methodOn(ShelfRestApiController.class)
                .getShelvesByRoom(id))
                .withRel(rel));
    }

    private void addRoomLink(Resources<Resource<Room>> resources, String rel) {
        resources.add(linkTo(RoomRestApiController.class)
                .withRel(rel));
    }
}
