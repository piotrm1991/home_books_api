package home_books_api.service;

import home_books_api.config.PlaceholderNames;
import home_books_api.model.*;
import home_books_api.repository.*;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class BookService {
    private BookRepository bookRepository;
    private AuthorRepository authorRepository;
    private PublisherRepository publisherRepository;
    private ShelfRepository shelfRepository;
    private StatusTypeRepository statusTypeRepository;

    public BookService(BookRepository bookRepository, AuthorRepository authorRepository,
                       PublisherRepository publisherRepository, ShelfRepository shelfRepository,
                       StatusTypeRepository statusTypeRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.publisherRepository = publisherRepository;
        this.shelfRepository = shelfRepository;
        this.statusTypeRepository = statusTypeRepository;
    }

//    public Book addBook(Book book) {
//        Optional<Author> authorAdded = Optional.ofNullable(book.getAuthor());
//        if (book.getAuthor() == null) {
//            authorAdded = this.authorRepository.findAuthorsByName(PlaceholderNames.AUTHOR_NAME).stream().findFirst();
//            if (authorAdded.get() == null) {
//                authorAdded = Optional.of(Author.builder().name(PlaceholderNames.AUTHOR_NAME).build());
//            }
//        }
//        Optional<Publisher> publisherAdded = Optional.ofNullable(book.getPublisher());
//        if (book.getPublisher() == null) {
//            publisherAdded = this.publisherRepository.findPublisherByName(PlaceholderNames.PUBLISHER_NAME).stream().findFirst();
//            if (publisherAdded.get() == null) {
//                publisherAdded = Optional.of(Publisher.builder().name(PlaceholderNames.PUBLISHER_NAME).build());
//            }
//        }
//        Status addedStatus = book.getStatus();
//        if (addedStatus == null) {
//            addedStatus = Status.builder().build();
//        }
//        addedStatus.setDateUp(new Date(System.currentTimeMillis()));
//        Book addedBook = Book.builder()
//                .name(book.getName())
//                .author(authorAdded.get())
//                .publisher(publisherAdded.get())
//                .status(addedStatus)
//                .shelf(book.getShelf())
//                .build();
//
//        return this.bookRepository.save(addedBook);
//    }


    public Book addBook(Book book) {
        Optional<Author> authorAdded = Optional.ofNullable(book.getAuthor());
        if (book.getAuthor() == null) {
            authorAdded = this.authorRepository.findAuthorsByName(PlaceholderNames.AUTHOR_NAME).stream().findFirst();
            if (authorAdded.get() == null) {
                authorAdded = Optional.of(Author.builder().name(PlaceholderNames.AUTHOR_NAME).build());
            }
        } else {
            if (book.getAuthor().getId() != null) {
                authorAdded = this.authorRepository.findById(book.getAuthor().getId());
            } else {
                authorAdded = this.authorRepository.findAuthorsByName(book.getAuthor().getName()).stream().findFirst();
            }
        }
        Optional<Publisher> publisherAdded = Optional.ofNullable(book.getPublisher());
        if (book.getPublisher() == null) {
            publisherAdded = this.publisherRepository.findPublisherByName(PlaceholderNames.PUBLISHER_NAME).stream().findFirst();
            if (publisherAdded.get() == null) {
                publisherAdded = Optional.of(Publisher.builder().name(PlaceholderNames.PUBLISHER_NAME).build());
            }
        } else {
            if (book.getPublisher().getId() != null) {
                publisherAdded = this.publisherRepository.findById(book.getPublisher().getId());
            } else {
                publisherAdded = this.publisherRepository.findPublisherByName(book.getPublisher().getName()).stream().findFirst();
            }
        }
//        Status addedStatus = book.getStatus();
//        if (addedStatus != null) {
//            addedStatus = Status.builder()
//                    .comment(book.getStatus().getComment())
//                    .statusType(this.statusTypeRepository.findById(book.getStatus().getStatusType().getId()).get())
//                    .build();
//        }
//        addedStatus.setDateUp(new Date(System.currentTimeMillis()));
        StatusType addedStatusType = this.statusTypeRepository.findById(book.getStatus().getStatusType().getId()).get();
        Status addedStatus = book.getStatus();
        addedStatus.setStatusType(addedStatusType);
        addedStatus.setDateUp(new Date(System.currentTimeMillis()));

        Optional<Shelf> addedShelf = Optional.ofNullable(book.getShelf());
        if (book.getShelf() != null) {
            addedShelf = this.shelfRepository.findById(book.getShelf().getId());
        }
        Book addedBook = Book.builder()
                .name(book.getName())
                .author(authorAdded.get())
                .publisher(publisherAdded.get())
                .status(addedStatus)
                .shelf(addedShelf.get())
                .build();

        return this.bookRepository.save(addedBook);
    }

    public void updateBook(Integer id, Book newPartialBook) {
        this.bookRepository.findById(id).ifPresent(book -> {
            if (newPartialBook.getName() != null) {
                book.setName(
                        newPartialBook.getName());
            }
            if (newPartialBook.getAuthor() != null) {
                book.setAuthor(
                        newPartialBook.getAuthor());
            }
            if (newPartialBook.getPublisher() != null) {
                book.setPublisher(
                        newPartialBook.getPublisher());
            }
            if (newPartialBook.getShelf() != null) {
                book.setShelf(
                        newPartialBook.getShelf());
            }
            if (newPartialBook.getStatus() != null) {
                book.setStatus(
                        newPartialBook.getStatus());
            }
            this.bookRepository.save(book);
        });
    }
}