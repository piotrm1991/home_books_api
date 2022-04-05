package home_books_api.repository;

import home_books_api.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Integer> {

    Collection<Author> findAuthorsByName(String name);
}
