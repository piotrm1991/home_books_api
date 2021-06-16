package home_books_api.repository;

import home_books_api.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.Collection;

@Repository
public interface RoomRepository extends JpaRepository<Room, Integer> {
    Collection<Room> findRoomByName(String name);
}
