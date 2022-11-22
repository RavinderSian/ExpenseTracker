'use strict'

const registerBtn = document.querySelector('.btn-register');
const registerBox = document.querySelector('.register-box');
const registerSubmitBtn = document.querySelector('#btn-register-submit');

registerBtn.addEventListener('click', function() {
	console.log('hidden');
	registerBox.classList.toggle('hidden');
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
