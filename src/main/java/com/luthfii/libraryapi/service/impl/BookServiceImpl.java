package com.luthfii.libraryapi.service.impl;

import com.luthfii.libraryapi.dto.request.BookRequest;
import com.luthfii.libraryapi.dto.response.BookResponse;
import com.luthfii.libraryapi.dto.response.PageResponse;
import com.luthfii.libraryapi.entity.Author;
import com.luthfii.libraryapi.entity.Book;
import com.luthfii.libraryapi.exception.DuplicateResourceException;
import com.luthfii.libraryapi.exception.ResourceNotFoundException;
import com.luthfii.libraryapi.repository.AuthorRepository;
import com.luthfii.libraryapi.repository.BookRepository;
import com.luthfii.libraryapi.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    @Override
    public BookResponse create(BookRequest request) {
        Author author = authorRepository.findById(request.getAuthorId())
                .orElseThrow(() -> new ResourceNotFoundException("Author tidak ditemukan"));

        if (bookRepository.findByIsbn(request.getIsbn()).isPresent()) {
            throw new DuplicateResourceException("ISBN sudah terdaftar");
        }

        Book book = Book.builder()
                .title(request.getTitle())
                .isbn(request.getIsbn())
                .yearPublished(request.getYearPublished())
                .stock(request.getStock())
                .author(author)
                .build();

        Book saved = bookRepository.save(book);
        return toResponse(saved);
    }

    @Override
    public BookResponse getById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Buku tidak ditemukan"));
        return toResponse(book);
    }

    @Override
    public List<BookResponse> getAll() {
        return bookRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public BookResponse update(Long id, BookRequest request) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Buku tidak ditemukan"));

        Author author = authorRepository.findById(request.getAuthorId())
                .orElseThrow(() -> new ResourceNotFoundException("Author tidak ditemukan"));

        book.setTitle(request.getTitle());
        book.setIsbn(request.getIsbn());
        book.setYearPublished(request.getYearPublished());
        book.setStock(request.getStock());
        book.setAuthor(author);

        return toResponse(bookRepository.save(book));
    }

    @Override
    public void delete(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Buku tidak ditemukan"));
        bookRepository.delete(book);
    }

    @Override
    public PageResponse<BookResponse> getAllPaginated(int page, int size, String sortBy,
                                                      String direction, String title, Long authorId) {
        // Sort direction
        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        // Pageable object
        Pageable pageable = PageRequest.of(page, size, sort);

        // Query berdasarkan filter
        Page<Book> bookPage;

        if (title != null && !title.isEmpty()) {
            bookPage = bookRepository.findByTitleContainingIgnoreCase(title, pageable);
        } else if (authorId != null) {
            bookPage = bookRepository.findByAuthorId(authorId, pageable);
        } else {
            bookPage = bookRepository.findAll(pageable);
        }

        List<BookResponse> content = bookPage.getContent()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());

        return PageResponse.<BookResponse>builder()
                .content(content)
                .currentPage(bookPage.getNumber())
                .totalPages(bookPage.getTotalPages())
                .totalElements(bookPage.getTotalElements())
                .hasNext(bookPage.hasNext())
                .hasPrevious(bookPage.hasPrevious())
                .build();
    }

    private BookResponse toResponse(Book book) {
        return BookResponse.builder()
                .id(book.getId())
                .title(book.getTitle())
                .isbn(book.getIsbn())
                .yearPublished(book.getYearPublished())
                .stock(book.getStock())
                .authorName(book.getAuthor().getName())
                .authorId(book.getAuthor().getId())
                .build();
    }
}
