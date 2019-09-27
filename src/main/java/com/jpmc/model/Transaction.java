package com.jpmc.model;

import java.math.BigDecimal;
import java.util.Date;

public class Transaction {
    private BigDecimal value;
    private Date transactionDate;

    public Transaction() {
        this.transactionDate = new Date();
    }

    public Transaction(BigDecimal value) {
        this.value = value;
        this.transactionDate = new Date();
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }

    
}
