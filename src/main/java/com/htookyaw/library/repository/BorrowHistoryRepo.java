package com.htookyaw.library.repository;

import com.htookyaw.library.dto.BorrowHistoryResDto;
import com.htookyaw.library.dto.RecordHistoryDto;
import com.htookyaw.library.entity.BorrowHistory;
import com.htookyaw.library.entity.Borrower;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BorrowHistoryRepo extends JpaRepository<BorrowHistory, Long> {

    Optional<BorrowHistory> findByBookId(Long bookId);

    Optional<BorrowHistory> findTopByBookIdAndReturnDateIsNull(Long bookId);

    @Query("""
                SELECT new com.htookyaw.library.dto.RecordHistoryDto(bh.book, bh.borrowDate, bh.returnDate)
                FROM BorrowHistory AS bh WHERE bh.borrower=:borrower
            """)
    List<RecordHistoryDto> findAllByBorrower(Borrower borrower);
}
