package home_books_api.repository;

import com.querydsl.core.types.Predicate;
import home_books_api.model.Author;
import net.bytebuddy.jar.asm.commons.Remapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.Collection;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Integer> {

    Collection<Author> findAuthorsByFirstName(String firstName);

    Collection<Author> findAuthorsByLastName(String lastName);

    Collection<Author> findByFirstNameAndLastName(String firstName, String lastName);
}
