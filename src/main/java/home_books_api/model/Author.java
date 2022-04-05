package home_books_api.model;

import lombok.*;

import javax.persistence.*;

@Entity(name = "Author")
@Table(name = "AUTHOR")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_author")
    private Integer id;
    @Column(name = "name")
    private String name;

    public Author(String name) {
        this.name = name;
    }
}