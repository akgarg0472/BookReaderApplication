package com.akgarg.bookreader.userBooks;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.time.LocalDate;

@Controller
public class UserBooksController {

    private final UserBooksRepository userBooksRepository;

    @Autowired
    public UserBooksController(UserBooksRepository userBooksRepository) {
        this.userBooksRepository = userBooksRepository;
    }


    @SuppressWarnings("ConstantConditions")
    @RequestMapping(value = "/addUserBook", method = RequestMethod.POST)
    public String addBookForUser(
            @RequestBody MultiValueMap<String, String> formData,
            @AuthenticationPrincipal OAuth2User principal) {
        if (principal == null || principal.getAttribute("login") == null) {
            return "redirect:/";
        }

        UserBooks userBooks = new UserBooks();
        userBooks.setKey(new UserBooksPrimaryKey(principal.getAttribute("login"), formData.getFirst("bookId")));
        userBooks.setStartDate(LocalDate.parse(formData.getFirst("startDate")));
        userBooks.setCompletedDate(LocalDate.parse(formData.getFirst("completedDate")));
        userBooks.setRating(Integer.parseInt(formData.getFirst("rating")));
        userBooks.setReadingStatus(formData.getFirst("readingStatus"));
        this.userBooksRepository.save(userBooks);

        return "redirect:/books/" + formData.getFirst("bookId");
    }
}
