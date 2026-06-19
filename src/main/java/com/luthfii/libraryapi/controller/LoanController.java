package com.luthfii.libraryapi.controller;

import com.luthfii.libraryapi.dto.request.LoanRequest;
import com.luthfii.libraryapi.dto.response.LoanResponse;
import com.luthfii.libraryapi.dto.response.PageResponse;
import com.luthfii.libraryapi.service.LoanService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/loans")
@RequiredArgsConstructor
public class LoanController {

    private final LoanService loanService;

    @PostMapping
    public ResponseEntity<LoanResponse> borrow(@Valid @RequestBody LoanRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(loanService.borrow(request));
    }

    @PatchMapping("/{id}/return")
    public ResponseEntity<LoanResponse> returnBook(@PathVariable Long id) {
        return ResponseEntity.ok(loanService.returnBook(id));
    }

    @GetMapping
    public ResponseEntity<PageResponse<LoanResponse>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return ResponseEntity.ok(loanService.getAllPaginated(page, size));
    }

    @GetMapping("/member/{memberId}/active")
    public ResponseEntity<List<LoanResponse>> getActiveLoansByMember(
            @PathVariable Long memberId) {

        return ResponseEntity.ok(loanService.getActiveLoansByMember(memberId));
    }
}
