let currentPage = 1;
const monthSelect = document.getElementById('monthSelect');
const searchBox = document.getElementById('searchBox');
const transactionTableBody = document.getElementById('transactionData');
const totalSale = document.getElementById('totalSale');
const soldItems = document.getElementById('soldItems');
const notSoldItems = document.getElementById('notSoldItems');
const barChartCanvas = document.getElementById('barChart');

// Function to fetch transactions data
function fetchTransactions(month, page = 1, search = '') {
    fetch(`/api/transactions?month=${month}&page=${page}&search=${search}`)
        .then(response => response.json())
        .then(data => {
            // Clear existing table data
            transactionTableBody.innerHTML = '';

            // Populate table
            data.transactions.forEach(transaction => {
                const row = document.createElement('tr');
                row.innerHTML = `
                    <td>${transaction.id}</td>
                    <td>${transaction.date}</td>
                    <td>${transaction.amount}</td>
                    <td>${transaction.type}</td>
                    <td>${transaction.description}</td>
                `;
                transactionTableBody.appendChild(row);
            });

            // Update Statistics
            totalSale.textContent = data.statistics.totalSale;
            soldItems.textContent = data.statistics.soldItems;
            notSoldItems.textContent = data.statistics.notSoldItems;

            // Load Bar Chart
            loadBarChart(data.chartData);
        })
        .catch(error => console.error('Error fetching transactions:', error));
}

// Function to load Bar Chart
function loadBarChart(chartData) {
    const ctx = barChartCanvas.getContext('2d');
    new Chart(ctx, {
        type: 'bar',
        data: {
            labels: chartData.labels,
            datasets: [{
                label: 'Price Range vs Number of Items',
                data: chartData.data,
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
}

// Event listeners for dropdown and search box
monthSelect.addEventListener('change', function() {
    fetchTransactions(monthSelect.value, currentPage, searchBox.value);
});

searchBox.addEventListener('input', function() {
    fetchTransactions(monthSelect.value, currentPage, searchBox.value);
});

// Paging
document.getElementById('nextPageBtn').addEventListener('click', function() {
    currentPage++;
    fetchTransactions(monthSelect.value, currentPage, searchBox.value);
});

document.getElementById('prevPageBtn').addEventListener('click', function() {
    if (currentPage > 1) {
        currentPage--;
        fetchTransactions(monthSelect.value, currentPage, searchBox.value);
    }
});

// Initial fetch for March
fetchTransactions('03', currentPage);
