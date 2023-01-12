'use strict'

export const displayExpenses = function(expenseToShow, expenseHeaders){
	expenseToShow
	.forEach(expense => {
				expenseHeaders.insertAdjacentHTML('afterend' ,
					`<div class = "budget-list">
					<p>${expense.purchaseDate}</p>
			  		<p>${expense.category}</p>
			  		<p>&pound${expense.amount}</p>
			  		<p>${expense.description}</p>
			  		<a class = "edit-expense-link"><button class = "edit-expense-btn">Edit</button></a>
			  		<a class = "delete-expense-link" href = /delete/${expense.id}><button class = "delete-expense-btn">Delete</button></a>
		  	</div>`);
		});
}