package com.htookyaw.library.repository;

import com.htookyaw.library.dto.BookResDto;
import com.htookyaw.library.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepo extends JpaRepository<Book, Long>, PagingAndSortingRepository<Book, Long> {
    public List<Book> findByIsbn(String isbn);
}
