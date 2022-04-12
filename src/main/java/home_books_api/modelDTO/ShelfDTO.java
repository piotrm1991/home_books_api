package home_books_api.modelDTO;

import home_books_api.model.Room;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ShelfDTO {
    private Integer id;
    private String letter;
    private Integer number;
    private Room room;
    private Integer noBooks;

    public ShelfDTO(String letter, Integer number) {
        this.letter = letter;
        this.number = number;
    }
}