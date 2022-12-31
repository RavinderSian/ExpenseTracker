"use strict";
import { MONTHS } from './config.js';

const navBar = document.querySelector('.nav-box')
const registerBox = document.querySelector(".register-box");
const registerSubmitBtn = document.querySelector("#btn-register-submit");
const currentMonth = document.querySelector('.month-text');

let expensesOnPage = document.querySelectorAll('.budget-list');
const expenseHeaders = document.querySelector('.budget-list-header');

navBar.addEventListener("click", function(e) {
	if (e.target.classList.contains('btn-register')) {
		registerBox.classList.toggle("hidden");
	}
});

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
			  		<a class = "edit-expense-link"><button class = "edit-expense-btn">Edit</button></a>
			  		<a class = "delete-expense-link" th:href = '/delete/' + ${expense.id}><button class = "delete-expense-btn">Delete</button></a>
		  	</div>`);
		})

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

	//If the login button also triggers the hidden class to be added the box never appears
	//So a second condition is needed to ensure that does not happen
	const isClickInside =
		registerBox.contains(e.target) || e.target.classList.contains('btn-register');
	
	//If we are outside the box when it appears, hide it again 
	if (!isClickInside) {
		registerBox.classList.add("hidden");
	}
	
	if (e.target.classList.contains('edit-expense-btn')){
		e.preventDefault();
		
		const expenseToEdit = e.target.closest('.budget-list');
		
		const currentExpenseId = expenseToEdit.querySelector('.delete-expense-link').attributes.item(3).name;
		const currentExpenseContent = Array.from(expenseToEdit.children).filter(child => child.nodeName === 'P').map(paragraph => paragraph.textContent);
		
		console.log(currentExpenseContent);
		
		expenseToEdit.insertAdjacentHTML('afterend', `<form class = "budget-list-edit-form" action="/editexpense" id=expense method="post">
					<input class = "edit-expense-input" type = "hidden" name = "id" value = ${currentExpenseId}>
					<input class = "edit-expense-input" type = "date" name = "purchaseDate" value = ${currentExpenseContent[0]} placeholder=${currentExpenseContent[0]}>
					<select class = "edit-expense-input" name="category" value = ${currentExpenseContent[1]} placeholder = ${currentExpenseContent[1]}>
				        <option value="DATES">Dates</option>
				        <option value="MISC">Misc</option>
				 		<option value="FUEL">Fuel</option>
				        <option value="DATES">MISC</option>
			     	</select>
					<input class = "edit-expense-input" type = "text" name = "amount" value = ${currentExpenseContent[2].replace('£', '')} placeholder = ${currentExpenseContent[2]}>
					<input class = "edit-expense-input" type = "text" name = "description" value = ${currentExpenseContent[3]} placeholder = ${currentExpenseContent[3]}>
					<input class = "edit-expense-input" name="submit-login" type="submit" value="submit" />
				</form>`);
	}
	
});

const sendFormToRegister = function() {
	return fetch($(".register-form").attr("action"), {
		method: "POST",
		headers: {
			"Content-Type": "application/json",
		},
		body: JSON.stringify({
			username: document.querySelector("#register-username").value,
			password: document.querySelector("#register-password").value,
			email: document.querySelector("#register-email").value,
		}),
	});
};

const sendDeleteRequest = async function(url) {
	try {
		fetch(url);
	} catch(err) {
		console.error(err);
	}
};

const renderError = function(err) {

	const message = err.message;
	const fieldsToCheck = ['username', 'email', 'password'];

	if (message.includes("has already been taken")) {

		fieldsToCheck.filter(field => message.includes(field)).forEach(field => {
			document
				.querySelector(`#duplicate-${field}`).classList.remove('hidden');
		})

	} else {
		fieldsToCheck.filter(field => message.includes(field)).forEach(field => {
			document
				.querySelector(`#invalid-${field}`).classList.remove('hidden');
		})
	}

	if (message.includes('maintenance')) {
		document
			.querySelector(`.login-error`).classList.remove('hidden');
	}
};

//Added error classes to all the error labels so can clear them with ease before each request submission
const clearErrors = function() {
	const errorElements = document.querySelectorAll('.error');
	errorElements.forEach(errorElement => errorElement.classList.add('hidden'));
}

const consumeRegister = async function() {
	try {
		const registerPromise = await sendFormToRegister();
		const data = await registerPromise.text();

		//Get rid of existing errors seen by user, some may not be needed anymore eg invalid email
		//Better to have this after the requests than before, otherwise user sees input boxes move

		if (!registerPromise.ok) {
			throw new Error(data);
		} else {
			clearErrors();
			window.location.reload();
			alert("Successful registration");
		}
	} catch (err) {
		clearErrors();
		console.error(err);
		renderError(err);
	}
};

registerSubmitBtn.addEventListener("click", function(e) {
	e.preventDefault();
	consumeRegister();
});

registerBox.addEventListener("keydown", function(e) {
	if (e.key === "Enter") {
		e.preventDefault();
		consumeRegister();
	}
});
