package com.jpmc.model;

import java.math.BigDecimal;

public class SaleVO {
	private String productType;
    private BigDecimal sellingPrice;
    
    
	public String getProductType() {
		return productType;
	}
	public void setProductType(String productType) {
		this.productType = productType;
	}
	public BigDecimal getSellingPrice() {
		return sellingPrice;
	}
	public void setSellingPrice(BigDecimal sellingPrice) {
		this.sellingPrice = sellingPrice;
	}

}
