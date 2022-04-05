package home_books_api.repository;

import home_books_api.model.Shelf;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.Collection;

@Repository
public interface ShelfRepository extends JpaRepository<Shelf, Integer> {
    Collection<Shelf> findByRoomId(Integer idRoom);

    Collection<Shelf> findShelfByLetter(String letter);

    Collection<Shelf> findShelfByNumber(Integer number);
}
