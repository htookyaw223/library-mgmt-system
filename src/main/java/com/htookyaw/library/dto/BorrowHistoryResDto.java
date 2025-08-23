package com.htookyaw.library.dto;

import com.htookyaw.library.entity.Book;
import com.htookyaw.library.entity.Borrower;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class BorrowHistoryResDto {
    private Borrower borrower;
    private List<RecordHistoryDto> borrowHistory;
}
