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
//    @Column(name = "last_name")
//    private String lastName;
//    @OneToMany(mappedBy = "author", cascade = {CascadeType.MERGE, CascadeType.DETACH, CascadeType.PERSIST, CascadeType.REFRESH})
//    private List<Book> books;

//    public Author(String firstName, String lastName) {
    public Author(String name) {
        this.name = name;
//        this.firstName = firstName;
//        this.lastName = lastName;
    }
//
//    public void addBook(Book book) {
//        if (books
//            == null) {
//            books = new ArrayList<>();
//        }
//
//        books.add(book);
//        book.setAuthor(this);
//    }
}
