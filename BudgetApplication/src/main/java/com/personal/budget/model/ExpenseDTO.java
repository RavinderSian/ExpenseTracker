package com.personal.budget.model;

import lombok.Data;

@Data
public class ExpenseDTO {

	private String id;
	private String userId;
	private String category;
	private String amount;
	private String description;
	private String purchaseDate;
	
}
