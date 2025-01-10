package com.example.TransactionManagementSystem;
public class BarChartDTO {

	private String priceRange;
    private Long itemCount;
	public BarChartDTO(String priceRange, Long itemCount) {
		super();
		this.priceRange = priceRange;
		this.itemCount = itemCount;
	}
	public String getPriceRange() {
		return priceRange;
	}
	public void setPriceRange(String priceRange) {
		this.priceRange = priceRange;
	}
	public Long getItemCount() {
		return itemCount;
	}
	public void setItemCount(Long itemCount) {
		this.itemCount = itemCount;
	}
    
    
}

