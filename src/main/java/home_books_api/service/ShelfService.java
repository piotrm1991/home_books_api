package home_books_api.service;

import home_books_api.model.Shelf;
import home_books_api.repository.ShelfRepository;
import org.springframework.stereotype.Service;

@Service
public class ShelfService {

    private ShelfRepository shelfRepository;

    public ShelfService(ShelfRepository shelfRepository) {
        this.shelfRepository = shelfRepository;
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
}
