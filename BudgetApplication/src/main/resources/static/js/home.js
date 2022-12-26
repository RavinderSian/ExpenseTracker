"use strict";

const navBar = document.querySelector('.nav-box')
const registerBtn = document.querySelector(".btn-register");
const registerBox = document.querySelector(".register-box");
const registerSubmitBtn = document.querySelector("#btn-register-submit");
const deleteBtn = document.querySelector('.delete-expense-btn');
const currentYear = document.querySelector('.year-text');
const currentMonth = document.querySelector('.month-text');
const months = ['JANUARY', 'FEBRUARY', 'MARCH', 'APRIL', 'MAY', 'JUNE', 
	'JULY', 'AUGUST', 'SEPTEMBER', 'OCTOBER', 'NOVEMBER', 'DECEMBER'];


navBar.addEventListener("click", function(e) {
	if (e.target.classList.contains('btn-register')) {
		registerBox.classList.toggle("hidden");
	}
});

//We are listening on document because some elements do not exist at certain points
//This means we need event delegation to listen for them
document.addEventListener("click", (e) => {
	console.log(expenses);
	if (e.target.id.includes('month-arrow')) {
		e.preventDefault();
		console.log("month click");
		
		if (e.target.id.includes('month-arrow-next')){
			const indexOfMonth = months.indexOf(currentMonth.textContent);
			
			currentMonth.textContent =  indexOfMonth === 11 ? 'JANUARY' 
			: months[indexOfMonth+1];
			
		}
		
		if (e.target.id.includes('month-arrow-back')){
			const indexOfMonth = months.indexOf(currentMonth.textContent);
			
			currentMonth.textContent =  indexOfMonth === 0 ? 'DECEMBER' 
			: months[indexOfMonth-1];
			
		}
		
	}

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
		e.target.classList.contains('register-box') || e.target.classList.contains('btn-register')

	//If we are outside the box when it appears, hide it again 
	if (!isClickInside) {
		registerBox.classList.add("hidden");
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
