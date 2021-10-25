package com.akgarg.bookreader.search;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("HttpUrlsUsage")
@Controller
public class SearchController {

    private final String COVER_IMAGE_ROOT = "http://covers.openlibrary.org/b/id/";
    private final WebClient webClient;

    public SearchController(WebClient.Builder builder) {
        ExchangeStrategies exchangeStrategies = ExchangeStrategies
                .builder().codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(16 * 1024 * 1024)).build();
        String baseUrl = "http://openlibrary.org/search.json";
        this.webClient = builder
                .exchangeStrategies(exchangeStrategies)
                .baseUrl(baseUrl)
                .build();
    }


    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public String getSearchResult(@RequestParam String query, Model model) {
        SearchResult searchResult = this.webClient
                .get()
                .uri("?q={query}", query)
                .retrieve()
                .bodyToMono(SearchResult.class)
                .block();

        if (searchResult != null) {
            List<SearchResultBook> books = searchResult
                    .getDocs()
                    .stream()
                    .limit(10)
                    .map(result -> {
                                result.setKey(result.getKey().replace("/works/", ""));
                                String coverId = result.getCover_i();
                                if (StringUtils.hasText(coverId)) {
                                    result.setCover_i(COVER_IMAGE_ROOT + coverId + "-M.jpg");
                                } else {
                                    result.setCover_i("/images/no-image.png");
                                }
                                return result;
                            }
                    )
                    .collect(Collectors.toList());
            model.addAttribute("books", books);
            return "search";
        }

        return "search_error";
    }
}
