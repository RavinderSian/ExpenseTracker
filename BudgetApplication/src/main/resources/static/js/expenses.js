let expensesOnPage = document.querySelectorAll('.budget-list');
const expenseHeaders = document.querySelector('.budget-list-header');
const months = ['JANUARY', 'FEBRUARY', 'MARCH', 'APRIL', 'MAY', 'JUNE', 
	'JULY', 'AUGUST', 'SEPTEMBER', 'OCTOBER', 'NOVEMBER', 'DECEMBER'];
const navBar = document.querySelector('.nav-box')
const registerBtn = document.querySelector(".btn-register");
const registerBox = document.querySelector(".register-box");
const registerSubmitBtn = document.querySelector("#btn-register-submit");
const deleteBtn = document.querySelector('.delete-expense-btn');
const currentYear = document.querySelector('.year-text');
const currentMonth = document.querySelector('.month-text');

(function() {

	const filteredExpenses = expenses.filter(expense => parseInt(expense.purchaseDate.split('-')[1]) === 
		months.indexOf(currentMonth.textContent)+1);
	
	//Below removes each expense element currently displayed on the page
	expensesOnPage.forEach(expense => expense.parentNode.removeChild(expense));
	
	filteredExpenses
	.forEach(expense => {
				expenseHeaders.insertAdjacentHTML('afterend' ,
					`<div class = "budget-list">
					<p>${expense.purchaseDate}</p>
			  		<p>${expense.category}</p>
			  		<p>&pound${expense.amount}</p>
			  		<p>${expense.description}</p>
			  		<a class = "delete-expense-link" th:href = '/delete/' + ${expense.id}><button class = "delete-expense-btn">Delete</button></a>
		  	</div>`);
		})

	//This is needed so the expensesOnPage is the new set of expenses and not the old
	expensesOnPage = document.querySelectorAll('.budget-list');
	
}());

const monthArrows = function(id) {
	const indexOfMonth = months.indexOf(currentMonth.textContent);
	
	if (id.includes('month-arrow-next')){
		currentMonth.textContent = indexOfMonth === 11 ? 'JANUARY' 
		: months[indexOfMonth+1];
	} else{
		currentMonth.textContent = indexOfMonth === 0 ? 'DECEMBER' 
		: months[indexOfMonth-1];
	}
}

export const displayCorrectExpensesForMonth = function(e) {
	if (!e.target.id.includes('month-arrow')) return;

	e.preventDefault();
	
	monthArrows(e.target.id);
	
	const filteredExpenses = expenses.filter(expense => parseInt(expense.purchaseDate.split('-')[1]) === 
		months.indexOf(currentMonth.textContent)+1);
	
	//Below removes each expense element currently displayed on the page
	expensesOnPage.forEach(expense => expense.parentNode.removeChild(expense));
	
	filteredExpenses
	.forEach(expense => {
				expenseHeaders.insertAdjacentHTML('afterend' ,
					`<div class = "budget-list">
					<p>${expense.purchaseDate}</p>
			  		<p>${expense.category}</p>
			  		<p>&pound${expense.amount}</p>
			  		<p>${expense.description}</p>
			  		<a class = "delete-expense-link" th:href = '/delete/' + ${expense.id}><button class = "delete-expense-btn">Delete</button></a>
		  	</div>`);
		})

	//This is needed so the expensesOnPage is the new set of expenses and not the old
	expensesOnPage = document.querySelectorAll('.budget-list');
}
