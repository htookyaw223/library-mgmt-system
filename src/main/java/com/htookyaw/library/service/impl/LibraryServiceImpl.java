package com.htookyaw.library.service.impl;

import com.htookyaw.library.dto.BookReqDto;
import com.htookyaw.library.dto.BookResDto;
import com.htookyaw.library.dto.BorrowerReqDto;
import com.htookyaw.library.entity.Book;
import com.htookyaw.library.entity.BorrowHistory;
import com.htookyaw.library.entity.Borrower;
import com.htookyaw.library.exception.InvalidRequestException;
import com.htookyaw.library.repository.BookRepo;
import com.htookyaw.library.repository.BorrowHistoryRepo;
import com.htookyaw.library.repository.BorrowerRepo;
import com.htookyaw.library.service.LibraryService;
import com.htookyaw.library.util.ISBNValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class LibraryServiceImpl implements LibraryService {
    @Autowired
    private BorrowerRepo borrowerRepo;
    @Autowired
    private BookRepo bookRepo;
    @Autowired
    private BorrowHistoryRepo historyRepo;

    @Override
    public Borrower addBorrower(BorrowerReqDto borrowerDto) {
        Optional<Borrower> borrowerOptional = borrowerRepo.findByEmail(borrowerDto.getEmail());
        // if borrower is already exist in system , will not add anymore and return existing one. otherwise will store in database
        return borrowerOptional.orElseGet(() -> borrowerRepo.save(new Borrower(borrowerDto)));
    }

    @Override
    public Book addBook(BookReqDto bookDto) {
        if(!ISBNValidator.isValidISBN(bookDto.getIsbn())) {
            throw new InvalidRequestException("Invalid ISBN!");
        }
        List<Book> existing = bookRepo.findByIsbn(bookDto.getIsbn());
        if (!existing.isEmpty()) {
            Book first = existing.get(0);
            if (!first.getTitle().equals(bookDto.getTitle()) ||
                    !first.getAuthor().equals(bookDto.getAuthor())) {
                throw new InvalidRequestException("ISBN conflict: same ISBN must have same title and author");
            }
        }
        Book book = new Book(bookDto.getIsbn(), bookDto.getTitle(), bookDto.getAuthor());
        return bookRepo.save(book);
    }

    @Override
    public BorrowHistory borrowBook(Long bookId, Long borrowerId) {
        Book book = bookRepo.findById(bookId).orElseThrow();
        Borrower borrower = borrowerRepo.findById(borrowerId).orElseThrow(()-> new NoSuchElementException("Borrower has not been registered yet"));

        // Check if already borrowed and not yet returned
        Optional<BorrowHistory> borrowHistory = historyRepo.findTopByBookIdAndReturnDateIsNull(bookId);
        boolean alreadyBorrowed = borrowHistory.isPresent();
        if (alreadyBorrowed) throw new InvalidRequestException("This book already borrowed!");

        BorrowHistory history = new BorrowHistory(null, borrower, book, LocalDateTime.now(), null);
        return historyRepo.save(history);
    }

    public BorrowHistory returnBook(Long bookId, Long borrowerId) {
        BorrowHistory history = historyRepo.findByBookId(bookId).stream()
                .filter(h -> h.getBorrower().getId().equals(borrowerId) && h.getReturnDate() == null)
                .findFirst()
                .orElseThrow(() -> new InvalidRequestException("No active borrow record found!"));

        history.setReturnDate(LocalDateTime.now());
        return historyRepo.save(history);
    }


    // Get paginated list of books
    @Override
    public BookResDto getBooks(int page, int size) {
        Page<Book> bookPage = bookRepo.findAll(PageRequest.of(page, size));
        return new BookResDto(bookPage.getTotalElements(), bookPage.getContent());
    }

}
