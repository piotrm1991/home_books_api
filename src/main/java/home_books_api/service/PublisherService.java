package home_books_api.service;

import home_books_api.model.Publisher;
import home_books_api.modelDTO.PublisherDTO;
import home_books_api.repository.BookRepository;
import home_books_api.repository.PublisherRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PublisherService {
    private PublisherRepository publisherRepository;
    private BookRepository bookRepository;

    public PublisherService(PublisherRepository publisherRepository, BookRepository bookRepository) {
        this.publisherRepository = publisherRepository;
        this.bookRepository = bookRepository;
    }

    public List<PublisherDTO> getPublishersForAngular() {
        List<Publisher> publishers  =  this.publisherRepository.findAll();
        List<PublisherDTO> publishersDTO = new ArrayList<>();
        publishers.stream().forEach(publisher -> {
            publishersDTO.add(PublisherDTO.builder()
                    .id(publisher.getId())
                    .name(publisher.getName())
                    .noBooks((int)this.bookRepository.findByPublisherId(publisher.getId()).stream().count())
                    .build());
        });
        return publishersDTO;
    }

    public void updatePublisher(Integer id, Publisher newPartialPublisher) {
        this.publisherRepository.findById(id).ifPresent(publisher -> {
            if (newPartialPublisher.getName() != null) {
                publisher.setName(
                        newPartialPublisher.getName());
            }
            this.publisherRepository.save(publisher);
        });
    }
}
