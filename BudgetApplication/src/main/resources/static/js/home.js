'use strict'

const registerBtn = document.querySelector('.btn-register');
const registerBox = document.querySelector('.register-box');
const registerSubmitBtn = document.querySelector('#btn-register-submit');

registerBtn.addEventListener('click', function() {
	registerBox.classList.toggle('hidden');
	//registerBtn.classList.toggle('hidden');
});

document.addEventListener("click", (e) => {
  //If the login button also triggers the hidden class to be added the box never appears
  //So a second condition is needed to ensure that does not happen
  const isClickInside = registerBox.contains(e.target) || registerBtn.contains(e.target);

  if (!isClickInside) {
	registerBox.classList.add('hidden');
  }
});


const sendFormToRegister = function() {
	return fetch($('.register-form').attr('action'),
		{
			method: 'POST',
			headers: {
				'Content-Type': 'application/json'
			},
			body: JSON.stringify(
				{
					username: document.querySelector('#register-username').value,
					password: document.querySelector('#register-password').value,
					email: document.querySelector('#register-email').value
				})
		})
}

const handleError = function(err){
	console.error(err);
}

const consumeRegister = async function() {
	try {
		const registerPromise = await sendFormToRegister();
		const data = await registerPromise.text();
		
		if (!registerPromise.ok) {
			throw new Error(data);
		}else {
			window.location.reload();
			alert("Successful registration");
		}
	} catch (err) {
		handleError(err);
	}
	
}

registerSubmitBtn.addEventListener('click', function(e) {
	e.preventDefault();
	consumeRegister();
});

registerBox.addEventListener("keydown", function(e) {
	if (e.key === 'Enter'){
		e.preventDefault();
		consumeRegister();
	}
});