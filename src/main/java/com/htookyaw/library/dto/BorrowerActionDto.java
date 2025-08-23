package com.htookyaw.library.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BorrowerActionDto {
 @NotNull(message = "BookId is required")
 private Long bookId;
 @NotNull(message = "borrowerId is required")
 private Long borrowerId;
}
