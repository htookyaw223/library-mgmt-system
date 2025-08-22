package com.htookyaw.library.repository;

import com.htookyaw.library.entity.BorrowHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BorrowHistoryRepo extends JpaRepository<BorrowHistory, Long> {

    Optional<BorrowHistory> findByBookId(Long bookId);

    Optional<BorrowHistory> findTopByBookIdAndReturnDateIsNull(Long bookId);
}
