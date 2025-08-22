package com.htookyaw.library.dto;

import com.htookyaw.library.entity.Book;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class BookResDto {
    private long totalElements;
    private List<Book> books;
}