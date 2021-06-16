package home_books_api.model;

import lombok.*;

import javax.persistence.*;

@Entity(name = "Book")
@Table(name = "BOOK")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_book")
    private Integer id;

    @Column(name = "name")
    private String name;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "id_author")
    private Author author;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_status")
    private Status status;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "id_publisher")
    private Publisher publisher;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "id_shelf")
    private Shelf shelf;

    public Book(String name, Status status) {
        this.name = name;
        this.status = status;
    }
}