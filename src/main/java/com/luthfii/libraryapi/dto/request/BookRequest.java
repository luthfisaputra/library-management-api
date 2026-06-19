package com.luthfii.libraryapi.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookRequest {

    @NotNull(message = "Judul tidak boleh kosong")
    private String title;

    @NotNull(message = "ISBN tidak boleh kosong")
    private String isbn;

    @NotNull(message = "Author ID tidak boleh kosong")
    private Long authorId;

    @NotNull(message = "Tahun terbit tidak boleh kosong")
    private Integer yearPublished;

    @NotNull(message = "Stok tidak boleh kosong")
    @Min(value = 0, message = "Stok tidak boleh negatif")
    private Integer stock;
}
