'use strict';

const dataArray = [];

expenses.sort((a, b) => a.purchaseDate.split('-')[2] - b.purchaseDate.split('-')[2]);

console.log(expenses);

const dataReal = expenses.forEach(expense => dataArray.push({...expense}));
console.log(dataArray);



(async function() {
  const data = dataArray;

  new Chart(
    document.querySelector('.expenses-jan'),
    {
      type: 'line',
      data: {
        labels: data.map(row => row.purchaseDate),
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