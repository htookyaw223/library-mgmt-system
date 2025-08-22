package com.htookyaw.library.service;

import com.htookyaw.library.dto.BookReqDto;
import com.htookyaw.library.dto.BookResDto;
import com.htookyaw.library.dto.BorrowerReqDto;
import com.htookyaw.library.entity.Book;
import com.htookyaw.library.entity.BorrowHistory;
import com.htookyaw.library.entity.Borrower;
import org.springframework.data.domain.Page;

public interface LibraryService {
    public Borrower addBorrower(BorrowerReqDto borrower);

    public Book addBook(BookReqDto book);

    public BorrowHistory borrowBook(Long bookId, Long borrowerId);

    public BorrowHistory returnBook(Long bookId, Long borrowerId);

    BookResDto getBooks(int page, int size);
}
