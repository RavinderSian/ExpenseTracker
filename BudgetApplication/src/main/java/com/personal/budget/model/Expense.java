package com.personal.budget.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class Expense {

	private Long id;
	private Long userId;
	private String category;
	private BigDecimal amount;
	private String description;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate purchaseDate;
	
}
