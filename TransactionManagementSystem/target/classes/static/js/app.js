// Fetch and display transactions
function getTransactions() {
    const search = document.getElementById("search").value;
    const month = document.getElementById("month").value;

    fetch(`/api/transactions?search=${search}&month=${month}&page=0&size=10`)
        .then(response => response.json())
        .then(data => {
            const tableBody = document.querySelector("#transactionsTable tbody");
            tableBody.innerHTML = ""; // Clear previous data

            data.forEach(transaction => {
                const row = document.createElement("tr");
                row.innerHTML = `
                    <td>${transaction.productTitle}</td>
                    <td>${transaction.price}</td>
                    <td>${transaction.category}</td>
                    <td>${transaction.dateOfSale}</td>
                    <td>${transaction.isSold ? 'Yes' : 'No'}</td>
                `;
                tableBody.appendChild(row);
            });
        })
        .catch(error => console.error('Error fetching transactions:', error));
}

// Fetch and display statistics
function getStatistics(month) {
    fetch(`/api/transactions/statistics?month=${month}`)
        .then(response => response.json())
        .then(data => {
            document.getElementById("totalSales").textContent = data.totalSales;
            document.getElementById("soldItems").textContent = data.soldItems;
            document.getElementById("unsoldItems").textContent = data.unsoldItems;
        })
        .catch(error => console.error('Error fetching statistics:', error));
}

// Fetch and display bar chart
function getBarChart(month) {
    fetch(`/api/transactions/bar-chart?month=${month}`)
        .then(response => response.json())
        .then(data => {
            const labels = data.map(item => item.priceRange);
            const values = data.map(item => item.count);

            const ctx = document.getElementById('barChartCanvas').getContext('2d');
            new Chart(ctx, {
                type: 'bar',
                data: {
                    labels: labels,
                    datasets: [{
                        label: 'Transactions by Price Range',
                        data: values,
                        backgroundColor: 'rgba(54, 162, 235, 0.2)',
                        borderColor: 'rgba(54, 162, 235, 1)',
                        borderWidth: 1
                    }]
                },
                options: {
                    scales: {
                        y: {
                            beginAtZero: true
                        }
                    }
                }
            });
        })
        .catch(error => console.error('Error fetching bar chart data:', error));
}

// Fetch and display pie chart
function getPieChart(month) {
    fetch(`/api/transactions/pie-chart?month=${month}`)
        .then(response => response.json())
        .then(data => {
            const labels = data.map(item => item.category);
            const values = data.map(item => item.count);

            const ctx = document.getElementById('pieChartCanvas').getContext('2d');
            new Chart(ctx, {
                type: 'pie',
                data: {
                    labels: labels,
                    datasets: [{
                        label: 'Transactions by Category',
                        data: values,
                        backgroundColor: [
                            'rgba(255, 99, 132, 0.2)',
                            'rgba(54, 162, 235, 0.2)',
                            'rgba(255, 205, 86, 0.2)',
                            'rgba(75, 192, 192, 0.2)',
                            'rgba(153, 102, 255, 0.2)',
                            'rgba(255, 159, 64, 0.2)'
                        ],
                        borderColor: [
                            'rgba(255, 99, 132, 1)',
                            'rgba(54, 162, 235, 1)',
                            'rgba(255, 205, 86, 1)',
                            'rgba(75, 192, 192, 1)',
                            'rgba(153, 102, 255, 1)',
                            'rgba(255, 159, 64, 1)'
                        ],
                        borderWidth: 1
                    }]
                }
            });
        })
        .catch(error => console.error('Error fetching pie chart data:', error));
}

// Fetch data when page loads
document.addEventListener('DOMContentLoaded', function () {
    const month = 1; // Default month (January)
    getTransactions();
    getStatistics(month);
    getBarChart(month);
    getPieChart(month);
});

// Event listener for search and month filter
document.getElementById("search").addEventListener("input", function() {
    getTransactions(); // Update the transaction list when search input changes
});
document.getElementById("month").addEventListener("change", function() {
    const month = this.value; // Get the selected month
    getStatistics(month);
    getBarChart(month);
    getPieChart(month);
});
