document.addEventListener("DOMContentLoaded", () => {
    const monthSelect = document.getElementById("month-select");
    const searchBox = document.getElementById("search-box");
    const transactionsTable = document.getElementById("transactions-table");
    const totalSales = document.getElementById("total-sales");
    const totalSold = document.getElementById("total-sold");
    const totalNotSold = document.getElementById("total-not-sold");
    const barChartCanvas = document.getElementById("bar-chart");

    let barChart; 

    
    const fetchData = (endpoint, params = {}) => {
        const url = new URL(endpoint, window.location.origin);
        Object.keys(params).forEach(key => url.searchParams.append(key, params[key]));
        return fetch(url).then(response => response.json());
    };

   
    const renderTransactions = async () => {
        const month = monthSelect.value;
        const search = searchBox.value;

        const transactions = await fetchData("/api/transactions", { month, search });
        transactionsTable.innerHTML = transactions.map(transaction => `
            <tr>
                <td>${transaction.id}</td>
                <td>${transaction.title}</td>
                <td>${transaction.description}</td>
                <td>${transaction.price}</td>
            </tr>
        `).join("");
    };

    
    const renderStatistics = async () => {
        const month = monthSelect.value;

        const stats = await fetchData("/api/transactions/statistics", { month });
        totalSales.textContent = stats.totalSales;
        totalSold.textContent = stats.totalSold;
        totalNotSold.textContent = stats.totalNotSold;
    };

    
    const renderBarChart = async () => {
        const month = monthSelect.value;

        const barChartData = await fetchData("/api/transactions/bar-chart", { month });
        const labels = barChartData.map(data => data.label);
        const data = barChartData.map(data => data.value);

        if (barChart) barChart.destroy(); 

        barChart = new Chart(barChartCanvas, {
            type: "bar",
            data: {
                labels,
                datasets: [{
                    label: "Price Range",
                    data,
                    backgroundColor: "rgba(75, 192, 192, 0.2)",
                    borderColor: "rgba(75, 192, 192, 1)",
                    borderWidth: 1
                }]
            },
            options: {
                responsive: true,
                scales: {
                    y: { beginAtZero: true }
                }
            }
        });
    };

   
    monthSelect.addEventListener("change", () => {
        renderTransactions();
        renderStatistics();
        renderBarChart();
    });

    searchBox.addEventListener("input", () => {
        renderTransactions();
    });

    
    renderTransactions();
    renderStatistics();
    renderBarChart();
});
