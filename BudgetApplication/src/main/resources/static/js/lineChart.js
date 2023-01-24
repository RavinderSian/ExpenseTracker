'use strict';

const dataArray = [];
console.log(expenses);

const dataReal = expenses.forEach(expense => dataArray.push({date: expense.purchaseDate, amount: expense.amount}));
console.log(dataArray);



(async function() {
  const data = dataArray;

  new Chart(
    document.getElementById('acquisitions'),
    {
      type: 'line',
      data: {
        labels: data.map(row => row.date),
        datasets: [
          {
            label: 'Acquisitions by year',
            data: data.map(row => row.amount)
          }
        ]
      }
    }
  );
})();