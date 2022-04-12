package home_books_api.service;

import home_books_api.model.Room;
import home_books_api.modelDTO.RoomDTO;
import home_books_api.repository.BookRepository;
import home_books_api.repository.RoomRepository;
import home_books_api.repository.ShelfRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RoomService {

    private RoomRepository roomRepository;
    private BookRepository bookRepository;
    private ShelfRepository shelfRepository;

    public RoomService(RoomRepository roomRepository, BookRepository bookRepository, ShelfRepository shelfRepository) {
        this.roomRepository = roomRepository;
        this.bookRepository = bookRepository;
        this.shelfRepository = shelfRepository;
    }

    public List<RoomDTO> getRoomsForAngular() {
        List<Room> rooms  =  this.roomRepository.findAll();
        List<RoomDTO> roomsDTO = new ArrayList<>();
        rooms.stream().forEach(room -> {
            roomsDTO.add(RoomDTO.builder()
                    .id(room.getId())
                    .name(room.getName())
                    .noBooks((int)this.bookRepository.findByShelfRoomId(room.getId()).stream().count())
                    .noShelves((int)this.shelfRepository.findByRoomId(room.getId()).stream().count())
                    .build());
        });
        return roomsDTO;
    }

    public void updateRoom(Integer id, Room newPartialRoom) {
        this.roomRepository.findById(id).ifPresent(room -> {
            if (newPartialRoom.getName() != null) {
                room.setName(
                        newPartialRoom.getName());
            }
            this.roomRepository.save(room);
        });
    }
}