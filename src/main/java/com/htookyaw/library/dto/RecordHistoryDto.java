package com.htookyaw.library.dto;

import com.htookyaw.library.entity.Book;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@AllArgsConstructor
@Data
public class RecordHistoryDto {
    private Book book;
    private LocalDateTime borrowDate;
    private LocalDateTime returnDate;
}
