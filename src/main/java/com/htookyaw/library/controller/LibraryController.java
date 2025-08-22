package com.htookyaw.library.controller;

import com.htookyaw.library.dto.BookReqDto;
import com.htookyaw.library.dto.BookResDto;
import com.htookyaw.library.dto.BorrowerReqDto;
import com.htookyaw.library.entity.Book;
import com.htookyaw.library.entity.BorrowHistory;
import com.htookyaw.library.entity.Borrower;
import com.htookyaw.library.service.LibraryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


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
    @PostMapping("/borrow/{bookId}/borrower/{borrowerId}")
    public ResponseEntity<BorrowHistory> borrowBook(@PathVariable Long bookId, @PathVariable Long borrowerId) {
        return ResponseEntity.ok(libraryService.borrowBook(bookId, borrowerId));
    }

    // Return Book
    @PostMapping("/return/{bookId}/borrower/{borrowerId}")
    public ResponseEntity<BorrowHistory> returnBook(@PathVariable Long bookId, @PathVariable Long borrowerId) {
        return ResponseEntity.ok(libraryService.returnBook(bookId, borrowerId));
    }
}
