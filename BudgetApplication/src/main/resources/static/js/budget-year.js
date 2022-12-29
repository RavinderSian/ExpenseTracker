"use strict";
import { MONTHS } from './config.js';

const deleteBtn = document.querySelector('.delete-expense-btn');
const currentYear = document.querySelector('.year-text');
const currentMonth = document.querySelector('.month-text');
const months = ['JANUARY', 'FEBRUARY', 'MARCH', 'APRIL', 'MAY', 'JUNE', 
	'JULY', 'AUGUST', 'SEPTEMBER', 'OCTOBER', 'NOVEMBER', 'DECEMBER'];

let expensesOnPage = document.querySelectorAll('.budget-list');
const expenseHeaders = document.querySelector('.budget-list-header');

//This self executing function displays the current (default) months expenses
//On the page on load 
const displayNewExpenses = function() {

	const filteredExpenses = expenses.filter(expense => parseInt(expense.purchaseDate.split('-')[1]) === 
		MONTHS.indexOf(currentMonth.textContent)+1);
	
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
		});

	//This is needed so the expensesOnPage is the new set of expenses and not the old
	expensesOnPage = document.querySelectorAll('.budget-list');
};

displayNewExpenses();

const monthArrows = function(id) {
	const indexOfMonth = MONTHS.indexOf(currentMonth.textContent);
	
	if (id.includes('month-arrow-next')){
		currentMonth.textContent = indexOfMonth === 11 ? 'JANUARY' 
		: MONTHS[indexOfMonth+1];
	} else{
		currentMonth.textContent = indexOfMonth === 0 ? 'DECEMBER' 
		: MONTHS[indexOfMonth-1];
	}
}

const displayCorrectExpensesForMonth = function(e) {
	if (!e.target.id.includes('month-arrow')) return;

	e.preventDefault();
	
	monthArrows(e.target.id);
	
	const filteredExpenses = expenses.filter(expense => parseInt(expense.purchaseDate.split('-')[1]) === 
		months.indexOf(currentMonth.textContent)+1);
	
	displayNewExpenses();
}

//We are listening on document because some elements do not exist at certain points
//This means we need event delegation to listen for them
document.addEventListener("click", (e) => {
	
	displayCorrectExpensesForMonth(e);

	if (e.target.classList.contains('delete-expense-btn')) {
		e.preventDefault();
		const confirm = window.confirm('Are you sure you want to delete this? This operation CANNOT be undone');
		
		if (confirm){
			sendDeleteRequest(e.target.closest('a').href);
			window.location.reload();
		}
	}
});

const sendDeleteRequest = async function(url) {
	try {
		fetch(url);
	} catch(err) {
		console.error(err);
	}
};
