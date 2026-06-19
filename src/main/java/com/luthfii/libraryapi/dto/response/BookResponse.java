package com.luthfii.libraryapi.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookResponse {

    private Long id;
    private String title;
    private String isbn;
    private Integer yearPublished;
    private Integer stock;
    private String authorName;
    private Long authorId;

}
