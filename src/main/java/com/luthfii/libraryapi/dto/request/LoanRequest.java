package com.luthfii.libraryapi.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoanRequest {

    @NotNull(message = "Member ID tidak boleh kosong")
    private Long memberId;

    @NotNull(message = "Book ID tidak boleh kosong")
    private Long bookId;
}
