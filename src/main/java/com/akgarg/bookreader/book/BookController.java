package com.akgarg.bookreader.book;

import com.akgarg.bookreader.userBooks.UserBooks;
import com.akgarg.bookreader.userBooks.UserBooksPrimaryKey;
import com.akgarg.bookreader.userBooks.UserBooksRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Optional;

// OL10005999W

@SuppressWarnings({"FieldCanBeLocal", "HttpUrlsUsage"})
@Controller
public class BookController {

    private final String COVER_IMAGE_ROOT;
    private final BookRepository bookRepository;
    private final UserBooksRepository userBooksRepository;

    @Autowired
    public BookController(BookRepository bookRepository,
                          UserBooksRepository userBooksRepository) {
        this.bookRepository = bookRepository;
        this.userBooksRepository = userBooksRepository;
        this.COVER_IMAGE_ROOT = "http://covers.openlibrary.org/b/id/";
    }

    @RequestMapping(value = "/books/{bookId}", method = RequestMethod.GET)
    public String getBook(@PathVariable("bookId") String bookId,
                          Model model,
                          @AuthenticationPrincipal OAuth2User principal) {
        Optional<Book> optionalBook = bookRepository.findById(bookId);

        if (optionalBook.isPresent()) {
            Book book = optionalBook.get();
            model.addAttribute("book", book);

            if (principal != null && principal.getAttribute("login") != null) {
                model.addAttribute("loginId", principal.getAttribute("login"));
                Optional<UserBooks> userBooksOptional = this.userBooksRepository.findById(
                        new UserBooksPrimaryKey(principal.getAttribute("login"), book.getId())
                );

                if (userBooksOptional.isPresent()) {
                    model.addAttribute("userBook", userBooksOptional.get());
                } else {
                    model.addAttribute("userBook", new UserBooks());
                }
            }

            if (book.getCoverIds() != null && book.getCoverIds().size() > 0) {
                model.addAttribute("coverImage", COVER_IMAGE_ROOT + book.getCoverIds().get(0) + "-L.jpg");
            } else {
                model.addAttribute("coverImage", "/images/no-image.png");
            }

            if (principal != null && principal.getAttribute("login") != null) {
                model.addAttribute("loginId", principal.getAttribute("login"));
            }

            return "book";
        }

        return "book-not-found";
    }

}
