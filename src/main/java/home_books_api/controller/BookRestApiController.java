package home_books_api.controller;

import home_books_api.config.ApiVersion;
import home_books_api.model.Book;
import home_books_api.model.Publisher;
import home_books_api.repository.BookRepository;
import home_books_api.service.BookService;
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
@RequestMapping(value = "/api/books", produces = {ApiVersion.V1_HAL_JSON, MediaType.ALL_VALUE})
public class BookRestApiController {

    private static final String REL_SELF = "self";
    @Autowired
    private BookRepository bookRepository;

    private BookService bookService;

    public BookRestApiController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Resource<Book>> getBook(@PathVariable Integer id) {
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

    @GetMapping(params = "idAuthor")
    public Resources<Resource<Book>> getBooksByAuthor(@RequestParam("idAuthor") Integer idAuthor) {
        Resources<Resource<Book>> resources = new Resources<>(
                this.bookRepository.findByAuthorId(idAuthor).stream().map(this::resource)
                        .collect(Collectors.toList()));
        addBookLink(resources, REL_SELF);
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

    @GetMapping(params = "idPublisher")
    public Resources<Resource<Book>> getBooksByPublisher(@RequestParam("idPublisher") Integer idAuthor) {
        Resources<Resource<Book>> resources = new Resources<>(
                this.bookRepository.findByPublisherId(idAuthor).stream().map(this::resource)
                        .collect(Collectors.toList()));
        addBookLink(resources, REL_SELF);
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

    @GetMapping(params = "idStatusType")
    public Resources<Resource<Book>> getBooksByStatusType(@RequestParam("idStatusType") Integer idStatusType) {
        Resources<Resource<Book>> resources = new Resources<>(
                this.bookRepository.findByStatusStatusTypeId(idStatusType).stream().map(this::resource)
                        .collect(Collectors.toList()));
        addBookLink(resources, REL_SELF);
        return resources;
    }

    @GetMapping(params = "idStatus")
    public ResponseEntity<Resource<Book>> getBookByStatus(@RequestParam("idStatus") Integer idStatus) {
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

    @PostMapping
    public ResponseEntity<?> addBook(@RequestBody Book book) {
        Book addedBook = this.bookService.addBook(book);
        return ResponseEntity.created(URI.create(
                resource(addedBook).getLink(REL_SELF).getHref()))
                .build();
    }

    @PatchMapping("/{id}")
    public void updateBook(@PathVariable Integer id, @RequestBody Book newPartialBook) {
        this.bookService.updateBook(id, newPartialBook);
    }

    @DeleteMapping("/{id}")
    public void deleteBook(@PathVariable("id") Integer id) {
        this.bookRepository.deleteById(id);
    }

    private ResponseEntity<String> notFound() {
        return ResponseEntity.notFound().build();
    }

    private <T> ResponseEntity<T> ok(T body) {
        return ResponseEntity.ok().body(body);
    }

    private Resource<Book> resource(Book book) {
        Resource<Book> authorResource = new Resource<>(book);
        authorResource.add(linkTo(
                methodOn(BookRestApiController.class)
                        .getBook(book.getId()))
                .withSelfRel());
        return authorResource;
    }

    private void addBookLink(Resources<Resource<Book>> resources, String rel) {
        resources.add(linkTo(BookRestApiController.class)
                .withRel(rel));
    }
}
