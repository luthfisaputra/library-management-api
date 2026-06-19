package com.luthfii.libraryapi.repository;

import com.luthfii.libraryapi.entity.Loan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LoanRepository extends JpaRepository<Loan, Long> {
    boolean existsByMemberIdAndBookIdAndIsReturnedFalse(Long memberId, Long bookId);

    List<Loan> findByMemberIdAndIsReturnedFalse(Long memberId);
}
