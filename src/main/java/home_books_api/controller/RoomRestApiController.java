package home_books_api.controller;

import home_books_api.config.ApiVersion;
import home_books_api.model.Room;
import home_books_api.repository.RoomRepository;
import home_books_api.service.RoomService;
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
@RequestMapping(value = "/api/rooms", produces = {ApiVersion.V1_HAL_JSON, MediaType.ALL_VALUE})
public class RoomRestApiController {

    private static final String REL_SELF = "self";
    private static final String REL_BOOKS = "books";
    private static final String REL_SHELVES = "shelves";
    @Autowired
    private RoomRepository roomRepository;

    private RoomService roomService;

    public RoomRestApiController(RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Resource<Room>> getRoom(@PathVariable Integer id) {
        return this.roomRepository.findById(id).map(this::resource)
                .map(this::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping(path = "/{id}", produces = ApiVersion.V2_FOR_ANGULAR)
    public ResponseEntity<Resource<Room>> getRoomForAngular(@PathVariable Integer id) {
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

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping(produces = ApiVersion.V2_FOR_ANGULAR)
    public List<Resource<Room>> getRoomsForAngular() {
        return this.roomRepository.findAll().stream().map(this::resource)
                .collect(Collectors.toList());
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping(params = "name", produces = ApiVersion.V2_FOR_ANGULAR)
    public Resources<Resource<Room>> findRoomByNameForAngular(@RequestParam("name") String name) {
        Resources<Resource<Room>> resources = new Resources<>(
                this.roomRepository.findRoomByName(name).stream()
                        .map(this::resource)
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

    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping(produces = ApiVersion.V2_FOR_ANGULAR)
    public ResponseEntity<?> addRoomForAngular(@RequestBody Room room) {
        Room addedRoom = this.roomRepository.save(room);
        return ResponseEntity.created(URI.create(
                resource(addedRoom).getLink(REL_SELF).getHref()))
                .build();
    }

    @PostMapping
    public ResponseEntity<?> addRoom(@RequestBody Room room) {
        Room addedRoom = this.roomRepository.save(room);
        return ResponseEntity.created(URI.create(
                resource(addedRoom).getLink(REL_SELF).getHref()))
                .build();
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @DeleteMapping(path = "/{id}", produces = ApiVersion.V2_FOR_ANGULAR)
    public void deleteRoomForAngular(@PathVariable("id") Integer id) {
        this.roomRepository.deleteById(id);
    }

    @DeleteMapping(path = "/{id}")
    public void deleteRoom(@PathVariable("id") Integer id) {
        this.roomRepository.deleteById(id);
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @PatchMapping(path = "/{id}", produces = ApiVersion.V2_FOR_ANGULAR)
    public void updateRoomForAngular(@PathVariable Integer id, @RequestBody Room newPartialRoom) {
        this.roomService.updateRoom(id, newPartialRoom);
    }

    @PatchMapping(path = "/{id}")
    public void updateRoom(@PathVariable Integer id, @RequestBody Room newPartialRoom) {
        this.roomService.updateRoom(id, newPartialRoom);
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
