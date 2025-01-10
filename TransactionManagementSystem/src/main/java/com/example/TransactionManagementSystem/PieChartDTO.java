package com.example.TransactionManagementSystem;
public class PieChartDTO {

	 private String category;
	    private Long itemCount;
		public PieChartDTO(String category, Long itemCount) {
			super();
			this.category = category;
			this.itemCount = itemCount;
		}
		public String getCategory() {
			return category;
		}
		public void setCategory(String category) {
			this.category = category;
		}
		public Long getItemCount() {
			return itemCount;
		}
		public void setItemCount(Long itemCount) {
			this.itemCount = itemCount;
		}
	    
	    
}

