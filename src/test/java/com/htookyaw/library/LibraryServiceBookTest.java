package com.htookyaw.library;

import com.htookyaw.library.dto.BookReqDto;
import com.htookyaw.library.entity.Book;
import com.htookyaw.library.exception.InvalidRequestException;
import com.htookyaw.library.repository.BookRepo;
import com.htookyaw.library.service.impl.LibraryServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LibraryServiceBookTest {

    @Mock
    private BookRepo bookRepo;

    @InjectMocks
    private LibraryServiceImpl libraryService;

    @Test
    public void testAddBook_Success() {
        // Arrange
        BookReqDto reqDto = new BookReqDto("9781234567897", "Clean Code", "Robert Martin");
        Book savedBook = new Book("9781234567897", "Clean Code", "Robert Martin");

        when(bookRepo.findByIsbn("9781234567897")).thenReturn(Collections.emptyList());
        when(bookRepo.save(any(Book.class))).thenReturn(savedBook);

        // Act
        Book result = libraryService.addBook(reqDto);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getIsbn()).isEqualTo("9781234567897");
        assertThat(result.getTitle()).isEqualTo("Clean Code");
        verify(bookRepo, times(1)).findByIsbn("9781234567897");
        verify(bookRepo, times(1)).save(any(Book.class));
    }

    @Test
    public void testAddBook_InvalidISBN() {
        // Arrange
        BookReqDto reqDto = new BookReqDto("INVALID", "Some Title", "Some Author");

        // Act + Assert
        assertThrows(InvalidRequestException.class, () -> libraryService.addBook(reqDto));

        verify(bookRepo, never()).save(any(Book.class));
    }

    @Test
    public void testAddBook_IsbnConflict() {
        // Arrange
        BookReqDto reqDto = new BookReqDto("9781234567897", "Different Title", "Different Author");
        Book existingBook = new Book("9781234567897", "Clean Code", "Robert Martin");

        when(bookRepo.findByIsbn("9781234567897")).thenReturn(List.of(existingBook));

        // Act + Assert
        assertThrows(InvalidRequestException.class, () -> libraryService.addBook(reqDto));

        verify(bookRepo, never()).save(any(Book.class));
    }

}
