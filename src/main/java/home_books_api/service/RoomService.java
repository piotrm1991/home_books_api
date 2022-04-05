package home_books_api.service;

import home_books_api.model.Room;
import home_books_api.repository.RoomRepository;
import org.springframework.stereotype.Service;

@Service
public class RoomService {

    private RoomRepository roomRepository;

    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
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