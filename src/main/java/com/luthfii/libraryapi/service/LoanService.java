package com.luthfii.libraryapi.service;

import com.luthfii.libraryapi.dto.request.LoanRequest;
import com.luthfii.libraryapi.dto.response.LoanResponse;
import com.luthfii.libraryapi.dto.response.PageResponse;
import com.luthfii.libraryapi.entity.Loan;

import java.util.List;

public interface LoanService {

    LoanResponse borrow(LoanRequest request);
    LoanResponse returnBook(Long loanId);
    List<LoanResponse> getActiveLoansByMember(Long memberId);
    PageResponse<LoanResponse> getAllPaginated(int page, int size);

}
