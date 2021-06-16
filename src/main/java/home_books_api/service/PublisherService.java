package home_books_api.service;

import home_books_api.model.Publisher;
import home_books_api.repository.PublisherRepository;
import org.springframework.stereotype.Service;

@Service
public class PublisherService {
    private PublisherRepository publisherRepository;

    public PublisherService(PublisherRepository publisherRepository) {
        this.publisherRepository = publisherRepository;
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
