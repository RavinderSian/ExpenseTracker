"use strict";

const navBar = document.querySelector('.nav-box')
const deleteBtn = document.querySelector('.delete-expense-btn');
const currentYear = document.querySelector('.year-text');

//We are listening on document because some elements do not exist at certain points
//This means we need event delegation to listen for them
document.addEventListener("click", (e) => {

	if (e.target.classList.contains('delete-expense-btn')) {
		console.log("deleting");
		e.preventDefault();
		const confirm = window.confirm('Are you sure you want to delete this? This operation CANNOT be undone');
		console.log(confirm);
		console.log(e.target.closest('a').href);
		
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
