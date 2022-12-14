"use strict";
import { MONTHS } from './config.js';

const currentMonth = document.querySelector('.month-text');
const dateBanner = document.querySelector('.budget-date-filter');
let expensesOnPage = document.querySelectorAll('.budget-list');
const expenseForm = document.querySelector('.add-expense-form');
const expenseHeaders = document.querySelector('.budget-list-header');
const total = document.querySelector('.total');
const totalBar = document.querySelector('.budget-list-total');
const searchBar = document.querySelector('.search-bar');

const searchRequest = async function(searchQuery) {
	try {
		const res = await fetch("/search", {
			method: "POST",
			headers: {
				"Content-Type": "application/json",
			},
			body: searchQuery,
		});
		
		if (!res.ok) return;
		
		const data = await res.json();
		return data;

	} catch (err) {
		console.error(err);
	}

}

searchBar.addEventListener('keyup', async () => {
	
	if(searchBar.value.length === 0) return;
	
	expenseForm.classList.add('hidden-opacity-collapse');
	dateBanner.classList.add('hidden-opacity-collapse');
	totalBar.classList.add('hidden-opacity-collapse');

	const result = await searchRequest(searchBar.value);

	expensesOnPage.forEach(expense => expense.parentNode.removeChild(expense));

	displayExpenses(result);
})

searchBar.addEventListener('input', function(e) {
	if(searchBar.value.length === 0){ 
		expenseForm.classList.remove('hidden-opacity-collapse');
		dateBanner.classList.remove('hidden-opacity-collapse');
		totalBar.classList.remove('hidden-opacity-collapse');
	}
	displayNewExpenses();
})

const calculateTotalExpenses = function(filteredExpenses) {
	return filteredExpenses.reduce((acc, cur) => acc + cur.amount, 0);
}

const displayExpenses = function(expensesToDisplay) {

	expensesToDisplay
		.forEach(expense => {
			expenseHeaders.insertAdjacentHTML('afterend',
				`<div class = "budget-list">
						<p>${expense.purchaseDate}</p>
				  		<p>${expense.category}</p>
				  		<p>&pound${expense.amount}</p>
				  		<p>${expense.description}</p>
				  		<a class = "delete-expense-link" href = /delete/${expense.id}><button class = "delete-expense-btn">Delete</button></a>
		  			</div>`);
		});

	//This is needed so the expensesOnPage is the new set of expenses and not the old
	expensesOnPage = document.querySelectorAll('.budget-list');
}

const displayNewExpenses = function() {

	let expenseTotal;

	const filteredExpenses = expenses.filter(expense => parseInt(expense.purchaseDate.split('-')[1]) ===
		MONTHS.indexOf(currentMonth.textContent) + 1);

	//Below removes each expense element currently displayed on the page
	expensesOnPage.forEach(expense => expense.parentNode.removeChild(expense));

	if (currentMonth.textContent === 'ALL') {
		expenses
			.forEach(expense => {
				expenseHeaders.insertAdjacentHTML('afterend',
					`<div class = "budget-list">
					<p>${expense.purchaseDate}</p>
			  		<p>${expense.category}</p>
			  		<p>&pound${expense.amount}</p>
			  		<p>${expense.description}</p>
			  		<a class = "edit-expense-link"><button class = "edit-expense-btn">Edit</button></a>
			  		<a class = "delete-expense-link" href = /delete/${expense.id}><button class = "delete-expense-btn">Delete</button></a>
		  	</div>`);
			});
		expenseTotal = calculateTotalExpenses(expenses);

	} else {
		filteredExpenses
			.forEach(expense => {
				expenseHeaders.insertAdjacentHTML('afterend',
					`<div class = "budget-list">
					<p>${expense.purchaseDate}</p>
			  		<p>${expense.category}</p>
			  		<p>&pound${expense.amount}</p>
			  		<p>${expense.description}</p>
			  		<a class = "edit-expense-link"><button class = "edit-expense-btn">Edit</button></a>
			  		<a class = "delete-expense-link" href = /delete/${expense.id}><button class = "delete-expense-btn">Delete</button></a>
		  	</div>`);
			});
		expenseTotal = calculateTotalExpenses(filteredExpenses);
	}

	total.innerHTML = `Total: ??${expenseTotal}`;
	//This is needed so the expensesOnPage is the new set of expenses and not the old
	expensesOnPage = document.querySelectorAll('.budget-list');

};
displayNewExpenses();

const monthArrows = function(id) {
	const indexOfMonth = MONTHS.indexOf(currentMonth.textContent);

	if (id.includes('month-arrow-next')) {
		currentMonth.textContent = indexOfMonth === 12 ? 'JANUARY'
			: MONTHS[indexOfMonth + 1];
	} else {
		currentMonth.textContent = indexOfMonth === 0 ? 'ALL'
			: MONTHS[indexOfMonth - 1];
	}
}

const displayCorrectExpensesForMonth = function(e) {
	if (!e.target.id.includes('month-arrow')) return;

	e.preventDefault();

	monthArrows(e.target.id);

	const filteredExpenses = expenses.filter(expense => parseInt(expense.purchaseDate.split('-')[1]) ===
		MONTHS.indexOf(currentMonth.textContent) + 1);

	displayNewExpenses();
}

//We are listening on document because some elements do not exist at certain points
//This means we need event delegation to listen for them
document.addEventListener("click", (e) => {
	displayCorrectExpensesForMonth(e);

	if (e.target.classList.contains('delete-expense-btn')) {
		e.preventDefault();
		const confirm = window.confirm('Are you sure you want to delete this? This operation CANNOT be undone');

		if (confirm) {
			sendDeleteRequest(e.target.closest('a').href);
			window.location.reload();
		}
	}

	if (e.target.classList.contains('edit-expense-btn')) {
		e.preventDefault();

		const expenseToEdit = e.target.closest('.budget-list');

		const currentExpenseId = expenseToEdit.querySelector('.delete-expense-link').attributes.item(3).name;
		const currentExpenseContent = Array.from(expenseToEdit.children).filter(child => child.nodeName === 'P').map(paragraph => paragraph.textContent);

		expenseToEdit.insertAdjacentHTML('afterend', `<form class = "budget-list-edit-form" action="/editexpense" id=expense method="post">
					<input class = "edit-expense-input" type = "hidden" name = "id" value = ${currentExpenseId}>
					<input class = "edit-expense-input" type = "date" name = "purchaseDate" value = ${currentExpenseContent[0]} placeholder=${currentExpenseContent[0]}>
					<select class = "edit-expense-input" name="category" value = ${currentExpenseContent[1]} placeholder = ${currentExpenseContent[1]}>
				        <option value="DATES">Dates</option>
				        <option value="MISC">Misc</option>
				 		<option value="FUEL">Fuel</option>
				        <option value="DATES">MISC</option>
			     	</select>
					<input class = "edit-expense-input" type = "text" name = "amount" value = ${currentExpenseContent[2].replace('??', '')} placeholder = ${currentExpenseContent[2]}>
					<input class = "edit-expense-input" type = "text" name = "description" value = ${currentExpenseContent[3]} placeholder = ${currentExpenseContent[3]}>
					<input class = "edit-expense-input" name="submit-login" type="submit" value="submit" />
				</form>`);
	}
});

const sendDeleteRequest = async function(url) {
	try {
		fetch(url);
	} catch (err) {
		console.error(err);
	}
};
