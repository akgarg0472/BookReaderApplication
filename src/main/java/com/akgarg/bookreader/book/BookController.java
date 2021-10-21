package com.akgarg.bookreader.book;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Optional;

@SuppressWarnings({"SpringJavaAutowiredFieldsWarningInspection", "FieldCanBeLocal", "HttpUrlsUsage"})
@Controller
public class BookController {

    private final String COVER_IMAGE_ROOT = "http://covers.openlibrary.org/b/id/";

    @Autowired
    private BookRepository bookRepository;

    @RequestMapping(value = "/books/{bookId}", method = RequestMethod.GET)
    public String getBook(@PathVariable("bookId") String bookId, Model model) {
        Optional<Book> optionalBook = bookRepository.findById(bookId);

        if (optionalBook.isPresent()) {
            Book book = optionalBook.get();
            model.addAttribute("book", book);

            if (book.getCoverIds() != null && book.getCoverIds().size() > 0) {
                model.addAttribute("coverImage", COVER_IMAGE_ROOT + book.getCoverIds().get(0) + "-L.jpg");
            } else {
                model.addAttribute("coverImage", "/images/no-image.png");
            }

            return "book";
        }

        return "book-not-found";
    }

}
