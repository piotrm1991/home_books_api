package home_books_api.modelDTO;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class StatusTypeDTO {
    private Integer id;
    private String name;
    private Integer noBooks;

    public StatusTypeDTO(String name) {
        this.name = name;
    }
}