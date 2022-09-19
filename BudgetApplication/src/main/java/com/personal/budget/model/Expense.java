package com.personal.budget.model;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class Expense {

	private Long id;
	private Long userId;
	private Category category;
	private BigDecimal amount;
	private String description;
}
