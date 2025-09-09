package com.htookyaw.library;


import com.htookyaw.library.entity.Book;
import com.htookyaw.library.entity.BorrowHistory;
import com.htookyaw.library.entity.Borrower;
import com.htookyaw.library.exception.InvalidRequestException;
import com.htookyaw.library.repository.BookRepo;
import com.htookyaw.library.repository.BorrowHistoryRepo;
import com.htookyaw.library.repository.BorrowerRepo;
import com.htookyaw.library.service.impl.LibraryServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LibraryServiceBorrowBookTest {

    @Mock
    private BookRepo bookRepo;

    @Mock
    private BorrowerRepo borrowerRepo;

    @Mock
    private BorrowHistoryRepo historyRepo;

    @InjectMocks
    private LibraryServiceImpl libraryService;

    private Book createBook(Long id) {
        Book book = new Book("9781234567897", "Clean Code", "Robert Martin");
        book.setId(id);
        return book;
    }

    private Borrower createBorrower(Long id, String name, String email) {
        Borrower borrower = new Borrower();
        borrower.setId(id);
        borrower.setName(name);
        borrower.setEmail(email);
        return borrower;
    }

    private BorrowHistory createBorrowHistory(Long id, Borrower borrower, Book book, boolean returned) {
        return new BorrowHistory(
                id,
                borrower,
                book,
                LocalDateTime.now().minusDays(1),
                returned ? LocalDateTime.now() : null
        );
    }

    // ---------------- BORROW TESTS ----------------

    @Test
    void borrowBook_Success() {
        Book book = createBook(1L);
        Borrower borrower = createBorrower(2L, "Borrower 1", "borrower1@gmail.com");
        BorrowHistory savedHistory = createBorrowHistory(1L, borrower, book, false);

        when(bookRepo.findById(1L)).thenReturn(Optional.of(book));
        when(borrowerRepo.findById(2L)).thenReturn(Optional.of(borrower));
        when(historyRepo.findTopByBookIdAndReturnDateIsNull(1L)).thenReturn(Optional.empty());
        when(historyRepo.save(any())).thenReturn(savedHistory);

        BorrowHistory result = libraryService.borrowBook(1L, 2L);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getBook().getTitle()).isEqualTo("Clean Code");
        assertThat(result.getBorrower().getEmail()).isEqualTo("borrower1@gmail.com");
        assertThat(result.getReturnDate()).isNull();
    }

    @Test
    void borrowBook_BookNotFound() {
        when(bookRepo.findById(1L)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> libraryService.borrowBook(1L, 2L));
        verifyNoInteractions(borrowerRepo, historyRepo);
    }

    @Test
    void borrowBook_BorrowerNotFound() {
        Book book = createBook(1L);
        when(bookRepo.findById(1L)).thenReturn(Optional.of(book));
        when(borrowerRepo.findById(2L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> libraryService.borrowBook(1L, 2L));
        verifyNoInteractions(historyRepo);
    }

    @Test
    void borrowBook_AlreadyBorrowed() {
        Book book = createBook(1L);
        Borrower borrower = createBorrower(2L, "Jane", "jane@gmail.com");
        BorrowHistory ongoing = createBorrowHistory(1L, borrower, book, false);

        when(bookRepo.findById(1L)).thenReturn(Optional.of(book));
        when(borrowerRepo.findById(2L)).thenReturn(Optional.of(borrower));
        when(historyRepo.findTopByBookIdAndReturnDateIsNull(1L)).thenReturn(Optional.of(ongoing));

        assertThrows(InvalidRequestException.class, () -> libraryService.borrowBook(1L, 2L));
        verify(historyRepo, never()).save(any());
    }

    // ---------------- RETURN TESTS ----------------

    @Test
    void returnBook_Success() {
        Book book = createBook(1L);
        Borrower borrower = createBorrower(2L, "Borrower 1", "borrower1@gmail.com");
        BorrowHistory ongoing = createBorrowHistory(1L, borrower, book, false);

        when(historyRepo.findByBookId(1L)).thenReturn(Optional.of(ongoing));
        when(historyRepo.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        BorrowHistory result = libraryService.returnBook(1L, 2L);

        assertThat(result.getBorrower().getId()).isEqualTo(2L);
        assertThat(result.getReturnDate()).isNotNull();
    }

    @Test
    void returnBook_NoActiveBorrowRecord() {
        Book book = createBook(1L);
        Borrower borrower = createBorrower(2L, "Borrower 1", "borrower1@gmail.com");
        BorrowHistory returned = createBorrowHistory(1L, borrower, book, true);

        when(historyRepo.findByBookId(1L)).thenReturn(Optional.of(returned));

        assertThrows(InvalidRequestException.class, () -> libraryService.returnBook(1L, 2L));
        verify(historyRepo, never()).save(any());
    }

    @Test
    void returnBook_WrongBorrower() {
        Book book = createBook(1L);
        Borrower otherBorrower = createBorrower(99L, "Other Person", "other@gmail.com");
        BorrowHistory ongoing = createBorrowHistory(1L, otherBorrower, book, false);

        when(historyRepo.findByBookId(1L)).thenReturn(Optional.of(ongoing));

        assertThrows(InvalidRequestException.class, () -> libraryService.returnBook(1L, 2L));
        verify(historyRepo, never()).save(any());
    }
}

