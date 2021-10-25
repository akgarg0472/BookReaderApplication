package com.akgarg.bookreader.search;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class SearchResultBook {

    private String key;
    private String title;
    private List<String> author_name;
    private String cover_i;
    private int first_publish_year;

}
