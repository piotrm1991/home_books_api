package home_books_api.controller;

import home_books_api.config.ApiVersion;
import home_books_api.model.Status;
import home_books_api.repository.StatusRepository;
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
@RequestMapping(value = "/api/statuses", produces = {ApiVersion.V1_HAL_JSON, MediaType.ALL_VALUE})
public class StatusRestApiController {

    private static final String REL_SELF = "self";
    private static final String REL_BOOK = "book";
    @Autowired
    private StatusRepository statusRepository;

    @GetMapping(path = "/{id}")
    public ResponseEntity<Resource<Status>> getStatus(@PathVariable Integer id) {
        return this.statusRepository.findById(id).map(this::resource)
                .map(this::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping(path = "/{id}", produces = ApiVersion.V2_FOR_ANGULAR)
    public ResponseEntity<Resource<Status>> getStatusForAngular(@PathVariable Integer id) {
        return this.statusRepository.findById(id).map(this::resource)
                .map(this::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public Resources<Resource<Status>> getStatuses() {
        Resources<Resource<Status>> resources = new Resources<>(
                this.statusRepository.findAll().stream().map(this::resource)
                        .collect(Collectors.toList()));
        addStatusLink(resources, REL_SELF);
        return resources;
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping(produces = ApiVersion.V2_FOR_ANGULAR)
    public List<Resource<Status>> getStatusesForAngular() {
        List<Resource<Status>> resources = this.statusRepository.findAll().stream().map(this::resource)
                        .collect(Collectors.toList());
        return resources;
    }

    @PostMapping
    public ResponseEntity<?> addStatus(@RequestBody Status status) {
        Status addedStatus = this.statusRepository.save(status);
        return ResponseEntity.created(URI.create(
                resource(addedStatus).getLink(REL_SELF).getHref()))
                .build();
    }

    @PostMapping(produces = ApiVersion.V2_FOR_ANGULAR)
    public ResponseEntity<?> addStatusForAngular(@RequestBody Status status) {
        Status addedStatus = this.statusRepository.save(status);
        return ResponseEntity.created(URI.create(
                resource(addedStatus).getLink(REL_SELF).getHref()))
                .build();
    }

    @DeleteMapping(path = "/{id}")
    public void deleteStatus(@PathVariable("id") Integer id) {
        this.statusRepository.deleteById(id);
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @DeleteMapping(path = "/{id}", produces = ApiVersion.V2_FOR_ANGULAR)
    public void deleteStatusForAngular(@PathVariable("id") Integer id) {
        this.statusRepository.deleteById(id);
    }

    private ResponseEntity<String> notFound() {
        return ResponseEntity.notFound().build();
    }

    private <T> ResponseEntity<T> ok(T body) {
        return ResponseEntity.ok().body(body);
    }

    private Resource<Status> resource(Status status) {
        Resource<Status> statusResource = new Resource<>(status);
        statusResource.add(linkTo(
                methodOn(StatusRestApiController.class)
                        .getStatus(status.getId()))
                .withSelfRel());
        statusResource.add(linkTo(methodOn(BookRestApiController.class)
                        .getBookByStatus(status.getId()))
                .withRel(REL_BOOK));
        return statusResource;
    }

    private void addStatusLink(Resources<Resource<Status>> resources, String rel) {
        resources.add(linkTo(StatusRestApiController.class)
                .withRel(rel));
    }
}
