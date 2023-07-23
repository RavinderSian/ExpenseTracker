package com.personal.budget.apimodels;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class ExpenseRangeRequest {
	
	@NotNull(message = "Please provide a year")
	private Integer year;
	@NotNull(message = "Please provide a month")
	private Integer month;

}
