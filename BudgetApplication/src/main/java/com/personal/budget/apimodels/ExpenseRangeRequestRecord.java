package com.personal.budget.apimodels;

import javax.validation.constraints.NotNull;

public record ExpenseRangeRequestRecord(@NotNull(message = "Please provide a year") Integer year, 
		@NotNull(message = "Please provide a month") Integer month) {

}
