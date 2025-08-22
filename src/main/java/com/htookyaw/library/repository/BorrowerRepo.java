package com.htookyaw.library.repository;

import com.htookyaw.library.entity.Borrower;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BorrowerRepo extends JpaRepository<Borrower, Long> {
    public Optional<Borrower> findByEmail(String email);
}
