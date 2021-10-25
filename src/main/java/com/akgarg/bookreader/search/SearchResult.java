package com.akgarg.bookreader.search;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class SearchResult {

    private int numFound;
    private List<SearchResultBook> docs;
}
