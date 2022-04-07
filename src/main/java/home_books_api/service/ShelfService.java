package home_books_api.service;

import home_books_api.model.Room;
import home_books_api.model.Shelf;
import home_books_api.repository.RoomRepository;
import home_books_api.repository.ShelfRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ShelfService {

    private ShelfRepository shelfRepository;
    private RoomRepository roomRepository;

    public ShelfService(ShelfRepository shelfRepository, RoomRepository roomRepository) {
        this.shelfRepository = shelfRepository;
        this.roomRepository = roomRepository;
    }

    public void updateShelf(Integer id, Shelf newPartialShelf) {
        this.shelfRepository.findById(id).ifPresent(shelf -> {
            if (newPartialShelf.getLetter() != null) {
                shelf.setLetter(
                        newPartialShelf.getLetter());
            }
            if (newPartialShelf.getNumber() != null) {
                shelf.setNumber(
                        newPartialShelf.getNumber());
            }
            if (newPartialShelf.getRoom() != null) {
                shelf.setRoom(
                        newPartialShelf.getRoom());
            }
            this.shelfRepository.save(shelf);
        });
    }

    public Shelf addShelf(Shelf shelf) {
        Optional<Room> roomAdded = Optional.ofNullable(shelf.getRoom());
        if (shelf.getRoom() != null) {
            if (shelf.getRoom().getId() != null) {
                roomAdded = this.roomRepository.findById(shelf.getRoom().getId());
            } else if (shelf.getRoom().getName() != "") {
                roomAdded = this.roomRepository.findRoomByName(shelf.getRoom().getName()).stream().findFirst();
            }
        }

        Shelf addedShelf = Shelf.builder()
                .letter(shelf.getLetter())
                .number(shelf.getNumber())
                .room(roomAdded.get())
                .build();
        return this.shelfRepository.save(addedShelf);
    }
}
