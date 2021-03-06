package com.akgarg.bookreader.home;

import com.akgarg.bookreader.user.BooksByUser;
import com.akgarg.bookreader.user.BooksByUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.query.CassandraPageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings({"HttpUrlsUsage", "SimplifyStreamApiCallChains"})
@Controller
public class HomeController {

    private final BooksByUserRepository booksByUserRepository;
    private final String COVER_IMAGE_ROOT;

    @Autowired
    public HomeController(BooksByUserRepository booksByUserRepository) {
        this.booksByUserRepository = booksByUserRepository;
        this.COVER_IMAGE_ROOT = "http://covers.openlibrary.org/b/id/";
    }


    @RequestMapping(value = "", method = RequestMethod.GET)
    public String home(@AuthenticationPrincipal OAuth2User principal, Model model) {
        if (principal == null || principal.getAttribute("login") == null) {
            return "index";
        }

        String userId = principal.getAttribute("login");
        Slice<BooksByUser> booksSlice = this.booksByUserRepository.findAllById(userId, CassandraPageRequest.of(0, 10));
        List<BooksByUser> userBooksByIdList = booksSlice.getContent().stream().distinct().map(book -> {
            String coverImageUrl = "/images/no-image.png";
            if (book.getCoverIds() != null && book.getCoverIds().size() > 0) {
                coverImageUrl = this.COVER_IMAGE_ROOT + book.getCoverIds().get(0) + "-M.jpg";
            }
            book.setCoverUrl(coverImageUrl);
            return book;
        }).collect(Collectors.toList());

        model.addAttribute("books", userBooksByIdList);
        return "home";
    }
}
