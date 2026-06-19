package com.luthfii.libraryapi.service;

import com.luthfii.libraryapi.dto.request.BookRequest;
import com.luthfii.libraryapi.dto.response.BookResponse;
import com.luthfii.libraryapi.dto.response.PageResponse;

import java.util.List;

public interface BookService {

    BookResponse create(BookRequest request);
    BookResponse getById(Long id);
    List<BookResponse> getAll();
    BookResponse update(Long id, BookRequest request);
    void delete(Long id);
    PageResponse<BookResponse> getAllPaginated(int page, int size, String sortBy,
                                               String direction, String title, Long authorId);

}
