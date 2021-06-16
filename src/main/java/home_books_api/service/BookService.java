package home_books_api.service;

import home_books_api.config.PlaceholderNames;
import home_books_api.model.Author;
import home_books_api.model.Book;
import home_books_api.model.Publisher;
import home_books_api.model.Status;
import home_books_api.repository.AuthorRepository;
import home_books_api.repository.BookRepository;
import home_books_api.repository.PublisherRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class BookService {
    private BookRepository bookRepository;
    private AuthorRepository authorRepository;
    private PublisherRepository publisherRepository;

    public BookService(BookRepository bookRepository, AuthorRepository authorRepository, PublisherRepository publisherRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.publisherRepository = publisherRepository;
    }


    public Book addBook(Book book) {
        Optional<Author> authorAdded = Optional.ofNullable(book.getAuthor());
        if (book.getAuthor() == null) {
            authorAdded = this.authorRepository.findByFirstNameAndLastName(PlaceholderNames.AUTHOR_FIRST_NAME, PlaceholderNames.AUTHOR_LAST_NAME).stream().findFirst();
            if (authorAdded.get() == null) {
                authorAdded = Optional.of(Author.builder().firstName(PlaceholderNames.AUTHOR_FIRST_NAME).lastName(PlaceholderNames.AUTHOR_LAST_NAME).build());
            }
        }
        Optional<Publisher> publisherAdded = Optional.ofNullable(book.getPublisher());
        if (book.getPublisher() == null) {
            publisherAdded = this.publisherRepository.findPublisherByName(PlaceholderNames.PUBLISHER_NAME).stream().findFirst();
            if (publisherAdded.get() == null) {
                publisherAdded = Optional.of(Publisher.builder().name(PlaceholderNames.PUBLISHER_NAME).build());
            }
        }
        Status addedStatus = book.getStatus();
        if (addedStatus == null) {
            addedStatus = Status.builder().build();
        }
        addedStatus.setDateUp(new Date(System.currentTimeMillis()));
        Book addedBook = Book.builder()
                .name(book.getName())
                .author(authorAdded.get())
                .publisher(publisherAdded.get())
                .status(addedStatus)
                .shelf(book.getShelf())
                .build();

        return this.bookRepository.save(addedBook);
    }

    public void updateBook(Integer id, Book newPartialBook) {
        this.bookRepository.findById(id).ifPresent(book -> {
            if (newPartialBook.getName() != null) {
                book.setName(
                        newPartialBook.getName());
            }
            this.bookRepository.save(book);
        });
    }
}
