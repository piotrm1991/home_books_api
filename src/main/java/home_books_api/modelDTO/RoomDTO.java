package home_books_api.modelDTO;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RoomDTO {
    private Integer id;
    private String name;
    private Integer noBooks;
    private Integer noShelves;

    public RoomDTO(String name) {
        this.name = name;
    }
}