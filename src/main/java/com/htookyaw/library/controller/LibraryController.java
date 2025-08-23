package com.htookyaw.library.controller;

import com.htookyaw.library.dto.*;
import com.htookyaw.library.entity.Book;
import com.htookyaw.library.entity.BorrowHistory;
import com.htookyaw.library.entity.Borrower;
import com.htookyaw.library.service.LibraryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/library")
public class LibraryController {
    @Autowired
    private LibraryService libraryService;

    // register borrower to library system
    @PostMapping("/borrowers")
    public ResponseEntity<Borrower> registerBorrower(@RequestBody @Valid BorrowerReqDto borrower) {
        return ResponseEntity.status(HttpStatus.CREATED).body(libraryService.addBorrower(borrower));
    }

    // add books to library
    @PostMapping("/books")
    public ResponseEntity<Book> registerBook(@RequestBody @Valid BookReqDto bookDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(libraryService.addBook(bookDto));
    }

    // get all books by pagination
    @GetMapping("/books")
    public ResponseEntity<BookResDto> getBooks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(libraryService.getBooks(page, size));
    }

    // Borrow Book
    @PostMapping("/borrows")
    public ResponseEntity<BorrowHistory> borrowBook(@RequestBody @Valid BorrowerActionDto dto) {
       return ResponseEntity.ok(libraryService.borrowBook(dto.getBookId(), dto.getBorrowerId()));
    }

    // Return Book
    @PostMapping("/returns")
    public ResponseEntity<BorrowHistory> returnBook(@RequestBody @Valid BorrowerActionDto dto) {
        return ResponseEntity.ok(libraryService.returnBook(dto.getBookId(), dto.getBorrowerId()));
    }
    @GetMapping("/borrower/history/{borrowerId}")
    public ResponseEntity<BorrowHistoryResDto> getBorrowHistory(@PathVariable("borrowerId") Long borrowerId) {
        return ResponseEntity.ok(libraryService.getBorrowHistoryByBorrowerId(borrowerId));
    }
}
