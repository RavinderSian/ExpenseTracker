'use strict'

const exportLink = document.querySelector('.export-link');


exportLink.addEventListener('click', event => {
	
	const [userId] =  window.location.href.split('/').slice(-1);
	
	const urlToPostTo = `${exportLink.href}${userId}`;
	
	console.log(urlToPostTo);
	
	return fetch(urlToPostTo, {
		method: "POST",
		headers: {
			"Content-Type": "application/json",
		},
		body: JSON.stringify({
		}),
	});
})