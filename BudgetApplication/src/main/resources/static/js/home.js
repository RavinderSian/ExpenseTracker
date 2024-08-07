"use strict";
import { MONTHS, MONTHS_DICTIONARY } from "./config.js";
import {
  calculateTotalExpenses,
  displayExpenses,
  monthArrows,
} from "./helpers.js";
import { searchRequest, sendDeleteRequest } from "./requests.js";

const navBar = document.querySelector(".nav-box");
const currentMonth = document.querySelector(".month-text");

let expensesOnPage = document.querySelectorAll(".budget-list");
const expenseHeaders = document.querySelector(".budget-list-header");
const total = document.querySelector(".total");

const totalBar = document.querySelector(".budget-list-total");
const searchBar = document.querySelector(".search-bar");
const expenseForm = document.querySelector(".add-expense-form");
const dateBanner = document.querySelector(".budget-date-filter");
const categoryFilter = document.querySelector("#category-filter-input");
const sortingArrows = document.querySelector(".sorting-arrows");

if (categoryFilter) {
  categoryFilter.onchange = function () {
    displayExpensesBasedOnCategory(categoryFilter.value.toLowerCase());
  };
}

const expensesForMonth = async function(month) {
	const res = await fetch("/expenseformonth", {
	 method: "POST",
	 headers: {
	   "Content-Type": "application/json",
	 },
	  body: JSON.stringify({
	  year: new Date().getFullYear(),
	  month: month
   }),
});
    const expensesForMonthData = await res.json();
    
	return expensesForMonthData;

  }

const displayExpensesBasedOnCategory = function (category) {
  if (category.toLowerCase() === "all") {
    displayExpensesBasedOnMonth();
  } else {
    const filteredExpenses = expenses
      .filter(
        (expense) =>
          parseInt(expense.purchaseDate.split("-")[1]) ===
          MONTHS.indexOf(currentMonth.textContent) + 1
      )
      .filter(
        (expense) => expense.category.toLowerCase() === category.toLowerCase()
      );
    expensesOnPage.forEach((expense) =>
      expense.parentNode.removeChild(expense)
    );
    displayExpenses(filteredExpenses, expenseHeaders);
    expensesOnPage = document.querySelectorAll(".budget-list");
    total.innerHTML = `Total: £${parseFloat(
      calculateTotalExpenses(filteredExpenses)
    ).toFixed(2)}`;
  }
};

navBar.addEventListener("input", async (e) => {
  if (!e.target.classList.contains("search-bar")) return;
  if (searchBar.value.length === 0) {
    expenseForm.classList.remove("hidden-opacity-collapse");
    dateBanner.classList.remove("hidden-opacity-collapse");
    totalBar.classList.remove("hidden-opacity-collapse");
    displayExpensesBasedOnMonth();
  } else {
    expenseForm.classList.add("hidden-opacity-collapse");
    dateBanner.classList.add("hidden-opacity-collapse");
    totalBar.classList.add("hidden-opacity-collapse");

    const result = await searchRequest(searchBar.value);

    expensesOnPage.forEach((expense) =>
      expense.parentNode.removeChild(expense)
    );

    displayExpenses(result, expenseHeaders);
    expensesOnPage = document.querySelectorAll(".budget-list");
  }
});

document.addEventListener("submit", async (e) => {
  if (e.target.name === "addExpenseForm") {
    e.preventDefault();

    const addExpenseInputFields = ["amount", "purchaseDate", "description"];

    const response = await addExpense();

    if (response.ok) {
      window.location.reload();
    } else if (response.status === 503) {
      document
        .querySelector("#add-expense-error-maintenance")
        .classList.toggle("hidden");
    } else {
      document.querySelector("#add-expense-error").classList.remove("hidden");

      const responseText = await response.text();

      const invalidFields = addExpenseInputFields.filter((field) =>
        responseText.includes(field)
      );

      addExpenseInputFields
        .filter((field) => !invalidFields.includes(field))
        .forEach((validField) => {
          if (validField === "purchaseDate") {
            document.querySelector(
              "#add-expense-purchase-date"
            ).style.backgroundColor = "white";
          } else {
            document.querySelector(
              `#add-expense-${validField}`
            ).style.backgroundColor = "white";
          }
        });

      invalidFields.forEach((invalidField) => {
        if (invalidField === "purchaseDate") {
          document.querySelector(
            "#add-expense-purchase-date"
          ).style.backgroundColor = "red";
        } else {
          document.querySelector(
            `#add-expense-${invalidField}`
          ).style.backgroundColor = "red";
        }
      });
    }
  }
});

const addExpense = function () {
  return fetch($('form[name="addExpenseForm"]').attr("action"), {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify({
      purchaseDate: document.querySelector("#add-expense-purchase-date").value,
      amount: document.querySelector("#add-expense-amount").value,
      category: document.querySelector("#add-expense-category").value,
      description: document.querySelector("#add-expense-description").value,
    }),
  });
};

//This self executing function displays the current (default) months expenses
//On the page on load
const displayExpensesBasedOnMonth = (async function displayMonthlyExpenses() {
  if (!currentMonth) return;
  
  let expenseTotal;
  
  const expenses = await expensesForMonth(MONTHS_DICTIONARY[currentMonth.innerHTML])

  //Below removes each expense element currently displayed on the page
  expensesOnPage.forEach((expense) => expense.parentNode.removeChild(expense));

  displayExpenses(expenses, expenseHeaders);
  expenseTotal = calculateTotalExpenses(expenses);

  total.innerHTML = `Total: £${parseFloat(expenseTotal).toFixed(2)}`;
  //This is needed so the expensesOnPage is the new set of expenses and not the old
  expensesOnPage = document.querySelectorAll(".budget-list");
  return displayMonthlyExpenses;
})();

const displayExpensesBasedOnMonthNew = async function displayMonthlyExpenses() {
  if (!currentMonth) return;
  
  let expenseTotal;
  
  const expensesResult = await expensesForMonth(MONTHS_DICTIONARY[currentMonth.innerHTML])

  //Below removes each expense element currently displayed on the page
  expensesOnPage.forEach((expense) => expense.parentNode.removeChild(expense));

  displayExpenses(expensesResult, expenseHeaders);
  expenseTotal = calculateTotalExpenses(expensesResult);

  total.innerHTML = `Total: £${parseFloat(expenseTotal).toFixed(2)}`;
  //This is needed so the expensesOnPage is the new set of expenses and not the old
  expensesOnPage = document.querySelectorAll(".budget-list");
  
  expenses = expensesResult;
  
  return displayMonthlyExpenses;
};

const displayCorrectExpensesForMonth = function (e) {
  if (e.target.id === null || !e.target.id.includes("month-arrow")) return;

  e.preventDefault();
  const newMonth = monthArrows(e.target.id, currentMonth);
  currentMonth.textContent = newMonth;
  
  displayExpensesBasedOnMonthNew();
};

//Listens for ignore checkbox
document.addEventListener("change", (e) => {
  if (e.target.id !== "ignore-expense-checkbox") return;

  //Best way to get parent
  const expenseElement = e.target.parentElement.parentElement;

  const currentTotal = parseFloat(
    total.innerHTML.substring(total.innerHTML.indexOf("£") + 1)
  );

  if (e.target.checked) {
    expenseElement.style.opacity = "0.5";
    const blurredExpenseAmount = +parseFloat(
      expenseElement.children[2].innerHTML.replace("£", "")
    );

    total.innerHTML = `Total: £${(currentTotal - blurredExpenseAmount).toFixed(
      2
    )}`;
  } else {
    const blurredExpenseAmount = +parseFloat(
      expenseElement.children[2].innerHTML.replace("£", "")
    );
    expenseElement.style.opacity = "1";
    total.innerHTML = `Total: £${(currentTotal + blurredExpenseAmount).toFixed(
      2
    )}`;
  }
});

//We are listening on document because some elements do not exist at certain points
//This means we need event delegation to listen for them
document.addEventListener("click", (e) => {
  displayCorrectExpensesForMonth(e);

  if (e.target.classList.contains("sorting-arrows")) {

    if (currentMonth.textContent === "ALL") {
      filteredExpenses = expenses;
    }

    if (e.target.textContent === "↓") {
      let asc;

      e.target.innerHTML = "↑";

      if (e.target.dataset.toSort === "cost") {
        asc = expenses.slice().sort((a, b) => a.amount - b.amount);
      }

      if (e.target.dataset.toSort === "date") {
        asc = expenses.sort(
          (a, b) =>
            parseInt(a.purchaseDate.split("-")[2]) -
            parseInt(b.purchaseDate.split("-")[2])
        );
      }

      expensesOnPage.forEach((expense) =>
        expense.parentNode.removeChild(expense)
      );
      displayExpenses(asc, expenseHeaders);
      expensesOnPage = document.querySelectorAll(".budget-list");
    } else if (e.target.textContent === "↑") {
      e.target.innerHTML = "↑↓";
      expensesOnPage.forEach((expense) =>
        expense.parentNode.removeChild(expense)
      );
      displayExpenses(expenses, expenseHeaders);
      expensesOnPage = document.querySelectorAll(".budget-list");
    } else if (e.target.textContent === "↑↓") {
      let desc;
      e.target.innerHTML = "↓";

      if (e.target.dataset.toSort === "cost") {
        desc = expenses.slice().sort((a, b) => b.amount - a.amount);
      }

      if (e.target.dataset.toSort === "date") {
        desc = expenses.sort(
          (a, b) =>
            parseInt(b.purchaseDate.split("-")[2]) -
            parseInt(a.purchaseDate.split("-")[2])
        );
      }

      expensesOnPage.forEach((expense) =>
        expense.parentNode.removeChild(expense)
      );
      displayExpenses(desc, expenseHeaders);
      expensesOnPage = document.querySelectorAll(".budget-list");
    }
  }

  if (e.target.classList.contains("delete-expense-btn")) {
    e.preventDefault();
    const confirm = window.confirm(
      "Are you sure you want to delete this? This operation CANNOT be undone"
    );

    if (confirm) {
      sendDeleteRequest(e.target.closest("a").href);
      window.location.reload();
    }
  }

  if (e.target.classList.contains("edit-expense-btn")) {
    e.preventDefault();

    const expenseToEdit = e.target.closest(".budget-list");

    if (expenseToEdit.dataset.editing) return;

    expenseToEdit.dataset.editing = "true";

    const splitURL = expenseToEdit
      .querySelector(".delete-expense-link")
      .href.split("/");
    const currentExpenseId = splitURL[splitURL.length - 1];
    const currentExpenseContent = Array.from(expenseToEdit.children)
      .filter((child) => child.nodeName === "P")
      .map((paragraph) => paragraph.textContent);

    expenseToEdit.insertAdjacentHTML(
      "afterend",
      `<form class = "budget-list-edit-form" action="/editexpense" id=expense method="post">
					<input class = "edit-expense-input" type = "hidden" name = "id" value = ${currentExpenseId}>
					<input class = "edit-expense-input" type = "date" name = "purchaseDate" value = ${
            currentExpenseContent[0]
          } placeholder=${currentExpenseContent[0]}>
					<select class = "edit-expense-input" name="category" value = ${
            currentExpenseContent[1]
          } placeholder = ${currentExpenseContent[1]}>
				        <option value="" disabled selected>Select something...</option>
				        <option value="DATES">DATES</option>
				        <option value="MISC">MISC</option>
				        <option value="MORTGAGE">Mortgage</option>
              			<option value="DIRECT_DEBITS">Direct Debits</option>
				 		<option value="FUEL">FUEL</option>
						<option value="SHOPS">SHOPS</option>
			     	</select>
					<input class = "edit-expense-input" type = "text" name = "amount" value = ${currentExpenseContent[2].replace(
            "£",
            ""
          )} placeholder = ${currentExpenseContent[2]}>
					<input class = "edit-expense-input" type = "text" name = "description" value = "${
            currentExpenseContent[3]
          }" placeholder = "${currentExpenseContent[3]}">
					<input class = "edit-expense-input" name="submit-login" type="submit" value="submit" />
			 </form>`
    );
  }
});