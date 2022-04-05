package home_books_api.controller;

import home_books_api.config.ApiVersion;
import home_books_api.model.Author;
import home_books_api.repository.AuthorRepository;
import home_books_api.service.AuthorService;
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
@RequestMapping(value = "/api/authors", produces = {ApiVersion.V1_HAL_JSON, MediaType.ALL_VALUE})
public class AuthorRestApiController {

    private static final String REL_SELF = "self";
    private static final String REL_BOOKS = "books";
    @Autowired
    private AuthorRepository authorRepository;

    private AuthorService authorService;

    public AuthorRestApiController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Resource<Author>> getAuthor(@PathVariable Integer id) {
        return this.authorRepository.findById(id).map(this::resource)
                .map(this::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public Resources<Resource<Author>> getAuthors() {
        Resources<Resource<Author>> resources = new Resources<>(
                this.authorRepository.findAll().stream().map(this::resource)
                        .collect(Collectors.toList()));
        addAuthorLink(resources, REL_SELF);
        return resources;
    }

    @GetMapping(params = "firstName")
    public Resources<Resource<Author>> findAuthorsByFirstName(@RequestParam("firstName") String firstName) {
        Resources<Resource<Author>> resources = new Resources<>(
                this.authorRepository.findAuthorsByFirstName(firstName).stream()
                        .map(this::resource)
                        .collect(Collectors.toList()));
        addAuthorLink(resources, REL_SELF);
        return resources;
    }

    @GetMapping(params = "lastName")
    public Resources<Resource<Author>> findAuthorsByLastName(@RequestParam("lastName") String lastName) {
        Resources<Resource<Author>> resources = new Resources<>(
                this.authorRepository.findAuthorsByLastName(lastName).stream()
                        .map(this::resource)
                        .collect(Collectors.toList()));
        addAuthorLink(resources, REL_SELF);
        return resources;
    }

    @PatchMapping("/{id}")
    public void updateAuthor(@PathVariable Integer id, @RequestBody Author newPartialAuthor) {
        this.authorService.updateAuthor(id, newPartialAuthor);
    }

    @PostMapping
    public ResponseEntity<?> addAuthor(@RequestBody Author author) {
        Author addedAuthor = this.authorRepository.save(author);
        return ResponseEntity.created(URI.create(
                resource(addedAuthor).getLink(REL_SELF).getHref()))
                .build();
    }

    @GetMapping(params = {"firstName", "lastName"})
    public Resources<Resource<Author>> findAuthorsByFirstNameAndLastName(@RequestParam("lastName") String lastName, @RequestParam("firstName") String firstName) {
        Resources<Resource<Author>> resources = new Resources<>(
                this.authorRepository.findByFirstNameAndLastName(firstName, lastName).stream()
                        .map(this::resource)
                        .collect(Collectors.toList()));
        addAuthorLink(resources, REL_SELF);
        return resources;
    }

    @DeleteMapping("/{id}")
    public void deleteAuthor(@PathVariable("id") Integer id) {
        this.authorRepository.deleteById(id);
    }

    private ResponseEntity<String> notFound() {
        return ResponseEntity.notFound().build();
    }

    private <T> ResponseEntity<T> ok(T body) {
        return ResponseEntity.ok().body(body);
    }

    private Resource<Author> resource(Author author) {
        Resource<Author> authorResource = new Resource<>(author);
        authorResource.add(linkTo(
                methodOn(AuthorRestApiController.class)
                        .getAuthor(author.getId()))
                .withSelfRel());
        authorResource.add(linkTo(methodOn(BookRestApiController.class)
                .getBooksByAuthor(author.getId()))
                .withRel(REL_BOOKS));
        return authorResource;
    }

    private void addAuthorLink(Resources<Resource<Author>> resources, String rel) {
        resources.add(linkTo(AuthorRestApiController.class)
                .withRel(rel));
    }
}
