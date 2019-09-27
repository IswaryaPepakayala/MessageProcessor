package com.jpmc.model;

import com.jpmc.utility.OperationType;

public class SaleMessageVO {

	private String messageType;
	private SaleVO sale;
    private int saleCount;
    private OperationType operationType;
    
    
	public String getMessageType() {
		return messageType;
	}
	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}
	public SaleVO getSale() {
		return sale;
	}
	public void setSaleVO(SaleVO sale) {
		this.sale = sale;
	}
	public int getSaleCount() {
		return saleCount;
	}
	public void setSaleCount(int saleCount) {
		this.saleCount = saleCount;
	}
	public OperationType getOperationType() {
		return operationType;
	}
	public void setOperationType(OperationType operationType) {
		this.operationType = operationType;
	}
    
}
