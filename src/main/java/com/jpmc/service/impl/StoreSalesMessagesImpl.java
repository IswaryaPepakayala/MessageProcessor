package com.jpmc.service.impl;

import static com.jpmc.utility.Constants.ADJUSTMENT;
import static com.jpmc.utility.Constants.MESSAGE;
import static com.jpmc.utility.Constants.SALE;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jpmc.model.SaleMessageVO;
import com.jpmc.model.Transaction;
import com.jpmc.service.StoreSalesMessage;
import com.jpmc.utility.OperationType;


public class StoreSalesMessagesImpl implements StoreSalesMessage{
	Map<String, List<Transaction>> productTransactionStore;
	
	public StoreSalesMessagesImpl() {
        this.productTransactionStore = new HashMap<>();
    }
	
	
	public boolean updateStore(SaleMessageVO saleMessage) {
		 if(saleMessage == null || saleMessage.getSale() == null) {
	            System.out.println("Invalid message");
	            return false;
	        }
		 List<Transaction> transactions = new ArrayList<Transaction>();
		 
		if (productTransactionStore.containsKey(saleMessage.getSale().getProductType())) {
			transactions = productTransactionStore.get(saleMessage.getSale().getProductType());
		} else {
			productTransactionStore.put(saleMessage.getSale().getProductType(), transactions);
		}
		 
		
		 switch(saleMessage.getMessageType()) {
		 		 case ADJUSTMENT:
		 			 transactions = adjustSaleTransactions(transactions, saleMessage);
		 			 break;
		 		 case SALE:
		 			 transactions = addNewSaleTransactions(transactions, saleMessage);
		 			 break;
		 		 case MESSAGE:
		 			 transactions.add(new Transaction(saleMessage.getSale().getSellingPrice()));
		 			 break;
		 		 default :
		 			System.out.println("Invalid operation");
		 			 break;
		 
		 }
		 
		 if(transactions.size() == 0) {
	            System.out.println("\nThere are no product (" + saleMessage.getSale().getProductType() +
	                    ") related sales to adjust.");
	            return false; 
	            }
		 
		 productTransactionStore.put(saleMessage.getSale().getProductType(), transactions);
		return true;
	}
	
	
	private List<Transaction> adjustSaleTransactions(List<Transaction> transactions, SaleMessageVO saleMessage) {
        OperationType operationType = saleMessage.getOperationType();

        switch(operationType) {
            case ADD:
			  for(Transaction transaction : transactions) {
			  transaction.setValue(transaction.getValue().add(saleMessage.getSale().
			  getSellingPrice()));
			   }
			   break;
            case MULTIPLY:
                for(Transaction transaction : transactions) {
                    transaction.setValue(transaction.getValue().multiply(saleMessage.getSale().getSellingPrice()));
                    
                }
                break;
            case SUBTRACT:
                for(Transaction transaction : transactions) {
                    transaction.setValue(transaction.getValue().subtract(saleMessage.getSale().getSellingPrice()));
                    
                }
                break;
            default:
                System.out.println("Invalid operation");
                break;
        }
        
        return transactions;

}
	
	private List<Transaction> addNewSaleTransactions(List<Transaction> transactions, SaleMessageVO saleMessage) {
        BigDecimal price = saleMessage.getSale().getSellingPrice();
        long salesCount = saleMessage.getSaleCount();

        if(salesCount <= 0) {
            System.out.println("Null or negative sales found.");
            return transactions;
        }

        for(long i=0; i<salesCount; i++) {
            transactions.add(new Transaction(price));
        }

        return transactions;
    }
	
	public void printSalesReport() {
        for(Map.Entry<String, List<Transaction>> productTransaction : productTransactionStore.entrySet()) {
            System.out.println("Product type: " + productTransaction.getKey() +
                    ", Total units sold: " + productTransaction.getValue().size() +
                    ", Revenue generated: " + getRevenueForProduct(productTransaction.getValue())
            );
        }
    }

    private BigDecimal getRevenueForProduct(List<Transaction> transactions) {
        BigDecimal revenueGenerated = new BigDecimal(0);

        for(Transaction transaction : transactions) {
        	revenueGenerated = revenueGenerated.add(transaction.getValue());
        }

        return revenueGenerated;
    }
	
}
