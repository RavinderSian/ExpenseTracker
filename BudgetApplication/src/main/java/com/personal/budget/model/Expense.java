package com.personal.budget.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

import lombok.Data;

@Data
public class Expense {

	private Long id;
	private Long userId;
	private String category;
	private BigDecimal amount;
	private String description;
	private Timestamp purchaseDate;
	
}
