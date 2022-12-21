"use strict";

const registerBtn = document.querySelector(".btn-register");
const registerBox = document.querySelector(".register-box");
const registerSubmitBtn = document.querySelector("#btn-register-submit");

registerBtn.addEventListener("click", function() {
	registerBox.classList.toggle("hidden");
});

document.addEventListener("click", (e) => {
	//If the login button also triggers the hidden class to be added the box never appears
	//So a second condition is needed to ensure that does not happen
	const isClickInside =
		registerBox.contains(e.target) || registerBtn.contains(e.target);

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

const renderError = function(err) {

	const message = err.message;
	
	const fieldsToCheck = ['username', 'email', 'password'];

	if (message.includes("has already been taken")) {
		
		fieldsToCheck.filter(field => message.includes(field)).forEach( field => {
			document
				.querySelector(`#duplicate-${field}`).classList.remove('hidden');
		})

	} else {
		fieldsToCheck.filter(field => message.includes(field)).forEach( field => {
			document
				.querySelector(`#invalid-${field}`).classList.remove('hidden');
		})
	}
	
};

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

//Added error classes to all the error labels so can clear them with ease before each request submission
const clearErrors = function(){
	const errorElements = document.querySelectorAll('.error');
	errorElements.forEach(errorElement => errorElement.classList.add('hidden'));
}

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
