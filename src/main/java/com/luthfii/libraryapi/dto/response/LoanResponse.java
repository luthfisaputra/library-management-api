package com.luthfii.libraryapi.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoanResponse {

    private Long id;
    private String memberName;
    private String bookTitle;
    private String bookIsbn;
    private LocalDate loanDate;
    private LocalDate returnDate;
    private Boolean isReturned;

}
