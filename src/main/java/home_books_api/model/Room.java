package home_books_api.model;

import lombok.*;

import javax.persistence.*;

@Entity(name = "Room")
@Table(name = "ROOM")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_room")
    private Integer id;

    @Column(name = "name")
    private String name;

    public Room(String name) {
        this.name = name;
    }
}
