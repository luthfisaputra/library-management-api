package com.luthfii.libraryapi.service.impl;

import com.luthfii.libraryapi.dto.request.LoanRequest;
import com.luthfii.libraryapi.dto.response.LoanResponse;
import com.luthfii.libraryapi.dto.response.PageResponse;
import com.luthfii.libraryapi.entity.Book;
import com.luthfii.libraryapi.entity.Loan;
import com.luthfii.libraryapi.entity.Member;
import com.luthfii.libraryapi.exception.BadRequestException;
import com.luthfii.libraryapi.exception.DuplicateResourceException;
import com.luthfii.libraryapi.exception.ResourceNotFoundException;
import com.luthfii.libraryapi.repository.BookRepository;
import com.luthfii.libraryapi.repository.LoanRepository;
import com.luthfii.libraryapi.repository.MemberRepository;
import com.luthfii.libraryapi.service.LoanService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LoanServiceImpl implements LoanService {

    private final LoanRepository loanRepository;
    private final BookRepository bookRepository;
    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public LoanResponse borrow(LoanRequest request) {
        // Validasi Member
        Member member = memberRepository.findById(request.getMemberId())
                .orElseThrow(() -> new ResourceNotFoundException("Member tidak ditemukan"));

        // Validasi Buku
        Book book = bookRepository.findById(request.getBookId())
                .orElseThrow(() -> new ResourceNotFoundException("Buku tidak ditemukan"));

        // Cek Stok
        if (book.getStock() <= 0) {
             throw new BadRequestException("Stok buku habis");
        }

        // Cek
        // Peminjam Aktif
        if (loanRepository.existsByMemberIdAndBookIdAndIsReturnedFalse(
                request.getMemberId(), request.getBookId())) {
            throw new DuplicateResourceException("Member masih meminjam buku ini");
        }

        //
        // Kurangi Stok
        book.setStock(book.getStock() - 1);
        bookRepository.save(book);

        // Buat Peminjaman
        Loan loan = Loan.builder()
                .member(member)
                .book(book)
                .loanDate(LocalDate.now())
                .isReturned(false)
                .build();

        return toResponse(loanRepository.save(loan));
    }

    @Override
    @Transactional
    public LoanResponse returnBook(Long loanId) {
        // Validasi Loan
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new ResourceNotFoundException("Peminjaman tidak ditemukan"));

        // Cek Returned Loan
        if (loan.getIsReturned()) {
            throw new BadRequestException("Buku sudah dikembalikan");
        }

        // Kembalikan Stok
        Book book = loan.getBook();
        book.setStock(book.getStock() + 1);
        bookRepository.save(book);

        // Update loan
        loan.setIsReturned(true);
        loan.setReturnDate(LocalDate.now());

        return toResponse(loanRepository.save(loan));
    }

    @Override
    public List<LoanResponse> getActiveLoansByMember(Long memberId) {
        memberRepository.findById(memberId)
                .orElseThrow(() -> new ResourceNotFoundException("Member tidak ditemukan"));

        return loanRepository.findByMemberIdAndIsReturnedFalse(memberId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public PageResponse<LoanResponse> getAllPaginated(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("loanDate").descending());
        Page<Loan> loanPage = loanRepository.findAll(pageable);

        List<LoanResponse> content = loanPage.getContent()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());

        return PageResponse.<LoanResponse>builder()
                .content(content)
                .currentPage(loanPage.getNumber())
                .totalPages(loanPage.getTotalPages())
                .totalElements(loanPage.getTotalElements())
                .hasNext(loanPage.hasNext())
                .hasPrevious(loanPage.hasPrevious())
                .build();
    }

    private LoanResponse toResponse(Loan loan) {
        return LoanResponse.builder()
                .id(loan.getId())
                .memberName(loan.getMember().getName())
                .bookTitle(loan.getBook().getTitle())
                .bookIsbn(loan.getBook().getIsbn())
                .loanDate(loan.getLoanDate())
                .returnDate(loan.getReturnDate())
                .isReturned(loan.getIsReturned())
                .build();
    }
}
