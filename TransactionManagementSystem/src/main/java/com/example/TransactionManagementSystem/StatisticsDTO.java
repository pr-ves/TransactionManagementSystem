package com.example.TransactionManagementSystem;
public class StatisticsDTO {
	 private Double totalSales;
	 private Long soldItems;
	 private Long unsoldItems;
	public StatisticsDTO(Double totalSales, Long soldItems, Long unsoldItems) {
		super();
		this.totalSales = totalSales;
		this.soldItems = soldItems;
		this.unsoldItems = unsoldItems;
	}
	public Double getTotalSales() {
		return totalSales;
	}
	public void setTotalSales(Double totalSales) {
		this.totalSales = totalSales;
	}
	public Long getSoldItems() {
		return soldItems;
	}
	public void setSoldItems(Long soldItems) {
		this.soldItems = soldItems;
	}
	public Long getUnsoldItems() {
		return unsoldItems;
	}
	public void setUnsoldItems(Long unsoldItems) {
		this.unsoldItems = unsoldItems;
	}

	 
}
