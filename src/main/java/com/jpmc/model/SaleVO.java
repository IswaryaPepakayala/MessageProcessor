package com.jpmc.model;

import java.math.BigDecimal;

public class SaleVO {
	private String productType;
    private BigDecimal sellingPrice;
    
	public SaleVO() {
		super();
	}
	
	public SaleVO(String productType, BigDecimal sellingPrice) {
		super();
		this.productType = productType;
		this.sellingPrice = sellingPrice;
	}

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
