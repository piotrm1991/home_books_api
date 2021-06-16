package home_books_api.model;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "Shelf")
@Table(name = "SHELF")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Shelf{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_shelf")
    private Integer id;

    @Column(name = "letter")
    private String letter;

    @Column(name = "number")
    private Integer number;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.DETACH, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "id_room")
    private Room room;

    public Shelf(String letter, Integer number) {
        this.letter = letter;
        this.number = number;
    }
}
