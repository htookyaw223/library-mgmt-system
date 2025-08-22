package com.htookyaw.library.entity;

import com.htookyaw.library.dto.BorrowerReqDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Borrower {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    @Column(unique = true)
    private String email;

    public Borrower(BorrowerReqDto reqDto) {
        this.name = reqDto.getName();
        this.email = reqDto.getEmail();
    }
}
