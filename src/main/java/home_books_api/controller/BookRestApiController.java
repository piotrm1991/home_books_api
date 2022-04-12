package home_books_api.controller;

import home_books_api.config.ApiVersion;
import home_books_api.model.Book;
import home_books_api.repository.BookRepository;
import home_books_api.service.BookService;
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
@RequestMapping(value = "/api/books", produces = {ApiVersion.V1_HAL_JSON, MediaType.ALL_VALUE})
public class BookRestApiController {

    private static final String REL_SELF = "self";
    @Autowired
    private BookRepository bookRepository;

    private BookService bookService;

    public BookRestApiController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Resource<Book>> getBook(@PathVariable Integer id) {
        return this.bookRepository.findById(id)
                .map(this::resource)
                .map(this::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping(path = "/{id}", produces = ApiVersion.V2_FOR_ANGULAR)
    public ResponseEntity<Resource<Book>> getBookForAngular(@PathVariable Integer id) {
        return this.bookRepository.findById(id)
                .map(this::resource)
                .map(this::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public Resources<Resource<Book>> getBooks() {
        Resources<Resource<Book>> resources = new Resources<>(
                this.bookRepository.findAll().stream().map(this::resource)
                        .collect(Collectors.toList()));
        addBookLink(resources, REL_SELF);
        return resources;
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping(produces = ApiVersion.V2_FOR_ANGULAR)
    public List<Resource<Book>> getBooksForAngular() {
        List<Resource<Book>> resources = this.bookRepository.findAll().stream().map(this::resource)
                        .collect(Collectors.toList());
        return resources;
    }

    @GetMapping(params = "idAuthor")
    public Resources<Resource<Book>> getBooksByAuthor(@RequestParam("idAuthor") Integer idAuthor) {
        Resources<Resource<Book>> resources = new Resources<>(
                this.bookRepository.findByAuthorId(idAuthor).stream().map(this::resource)
                        .collect(Collectors.toList()));
        addBookLink(resources, REL_SELF);
        return resources;
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping(params = "idAuthor", produces = ApiVersion.V2_FOR_ANGULAR)
    public List<Resource<Book>> getBooksByAuthorForAngular(@RequestParam("idAuthor") Integer idAuthor) {
        List<Resource<Book>> resources = this.bookRepository.findByAuthorId(idAuthor).stream().map(this::resource)
                        .collect(Collectors.toList());
        return resources;
    }

    @GetMapping(params = "idRoom")
    public Resources<Resource<Book>> getBooksByRoom(@RequestParam("idRoom") Integer idRoom) {
        Resources<Resource<Book>> resources = new Resources<>(
                this.bookRepository.findByShelfRoomId(idRoom).stream().map(this::resource)
                        .collect(Collectors.toList()));
        addBookLink(resources, REL_SELF);
        return resources;
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping(params = "idRoom", produces = ApiVersion.V2_FOR_ANGULAR)
    public List<Resource<Book>> getBooksByRoomForAuthor(@RequestParam("idRoom") Integer idRoom) {
        List<Resource<Book>> resources = this.bookRepository.findByShelfRoomId(idRoom).stream().map(this::resource)
                        .collect(Collectors.toList());
        return resources;
    }

    @GetMapping(params = "idPublisher")
    public Resources<Resource<Book>> getBooksByPublisher(@RequestParam("idPublisher") Integer idAuthor) {
        Resources<Resource<Book>> resources = new Resources<>(
                this.bookRepository.findByPublisherId(idAuthor).stream().map(this::resource)
                        .collect(Collectors.toList()));
        addBookLink(resources, REL_SELF);
        return resources;
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping(params = "idPublisher", produces = ApiVersion.V2_FOR_ANGULAR)
    public List<Resource<Book>> getBooksByPublisherForAngular(@RequestParam("idPublisher") Integer idAuthor) {
        List<Resource<Book>> resources = this.bookRepository.findByPublisherId(idAuthor).stream().map(this::resource)
                        .collect(Collectors.toList());
        return resources;
    }

    @GetMapping(params = "idShelf")
    public Resources<Resource<Book>> getBooksByShelf(@RequestParam("idShelf") Integer idShelf) {
        Resources<Resource<Book>> resources = new Resources<>(
                this.bookRepository.findByShelfId(idShelf).stream().map(this::resource)
                        .collect(Collectors.toList()));
        addBookLink(resources, REL_SELF);
        return resources;
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping(params = "idShelf", produces = ApiVersion.V2_FOR_ANGULAR)
    public List<Resource<Book>> getBooksByShelfForAngular(@RequestParam("idShelf") Integer idShelf) {
        List<Resource<Book>> resources = this.bookRepository.findByShelfId(idShelf).stream().map(this::resource)
                        .collect(Collectors.toList());
        return resources;
    }

    @GetMapping(params = "idStatusType")
    public Resources<Resource<Book>> getBooksByStatusType(@RequestParam("idStatusType") Integer idStatusType) {
        Resources<Resource<Book>> resources = new Resources<>(
                this.bookRepository.findByStatusStatusTypeId(idStatusType).stream().map(this::resource)
                        .collect(Collectors.toList()));
        addBookLink(resources, REL_SELF);
        return resources;
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping(params = "idStatusType", produces = ApiVersion.V2_FOR_ANGULAR)
    public List<Resource<Book>> getBooksByStatusTypeForAngular(@RequestParam("idStatusType") Integer idStatusType) {
        List<Resource<Book>> resources = this.bookRepository.findByStatusStatusTypeId(idStatusType).stream().map(this::resource)
                        .collect(Collectors.toList());
        return resources;
    }

    @GetMapping(params = "idStatus")
    public ResponseEntity<Resource<Book>> getBookByStatus(@RequestParam("idStatus") Integer idStatus) {
                return this.bookRepository.findByStatusId(idStatus)
                        .map(this::resource)
                        .map(this::ok)
                        .orElse(ResponseEntity.notFound().build());
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping(params = "idStatus", produces = ApiVersion.V2_FOR_ANGULAR)
    public ResponseEntity<Resource<Book>> getBookByStatusForAngular(@RequestParam("idStatus") Integer idStatus) {
                return this.bookRepository.findByStatusId(idStatus)
                        .map(this::resource)
                        .map(this::ok)
                        .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping(params = "name")
    public Resources<Resource<Book>> findBookByName(@RequestParam("name") String name) {
        Resources<Resource<Book>> resources = new Resources<>(
                this.bookRepository.findBookByName(name).stream()
                        .map(this::resource)
                        .collect(Collectors.toList()));
        addBookLink(resources, REL_SELF);
        return resources;
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping(params = "name", produces = ApiVersion.V2_FOR_ANGULAR)
    public List<Resource<Book>> findBookByNameForAngular(@RequestParam("name") String name) {
        List<Resource<Book>> resources = this.bookRepository.findBookByName(name).stream()
                        .map(this::resource)
                        .collect(Collectors.toList());
        return resources;
    }

    @PostMapping
    public ResponseEntity<?> addBook(@RequestBody Book book) {
        Book addedBook = this.bookService.addBook(book);
        return ResponseEntity.created(URI.create(
                resource(addedBook).getLink(REL_SELF).getHref()))
                .build();
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping(produces = ApiVersion.V2_FOR_ANGULAR)
    public ResponseEntity<?> addBookForAngular(@RequestBody Book book) {
        Book addedBook = this.bookService.addBook(book);
        return ResponseEntity.created(URI.create(
                resource(addedBook).getLink(REL_SELF).getHref()))
                .build();
    }

    @PatchMapping(path = "/{id}")
    public void updateBook(@PathVariable Integer id, @RequestBody Book newPartialBook) {
        this.bookService.updateBook(id, newPartialBook);
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @PatchMapping(path = "/{id}", produces = ApiVersion.V2_FOR_ANGULAR)
    public void updateBookForAngular(@PathVariable Integer id, @RequestBody Book newPartialBook) {
        this.bookService.updateBook(id, newPartialBook);
    }

    @DeleteMapping(path = "/{id}")
    public void deleteBook(@PathVariable("id") Integer id) {
        this.bookRepository.deleteById(id);
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @DeleteMapping(path = "/{id}", produces = ApiVersion.V2_FOR_ANGULAR)
    public void deleteBookForAngular(@PathVariable("id") Integer id) {
        this.bookRepository.deleteById(id);
    }

    private ResponseEntity<String> notFound() {
        return ResponseEntity.notFound().build();
    }

    private <T> ResponseEntity<T> ok(T body) {
        return ResponseEntity.ok().body(body);
    }

    private Resource<Book> resource(Book book) {
        Resource<Book> bookResource = new Resource<>(book);
        bookResource.add(linkTo(
                methodOn(BookRestApiController.class)
                        .getBook(book.getId()))
                .withSelfRel());
        return bookResource;
    }

    private void addBookLink(Resources<Resource<Book>> resources, String rel) {
        resources.add(linkTo(BookRestApiController.class)
                .withRel(rel));
    }
}
