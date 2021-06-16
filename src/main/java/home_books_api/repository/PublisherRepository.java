package home_books_api.repository;

import home_books_api.model.Publisher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.Collection;

@Repository
public interface PublisherRepository extends JpaRepository<Publisher, Integer> {
    Collection<Publisher> findPublisherByName(String name);
}
