package com.jpmc.service;

import com.jpmc.exceptions.InvalidMessageTypeException;
import com.jpmc.exceptions.InvalidOperationException;
import com.jpmc.exceptions.TechnicalFailureException;
import com.jpmc.model.SaleMessageVO;

public interface StoreSalesMessage {
	
	 boolean updateStore(SaleMessageVO saleMessage) throws InvalidMessageTypeException, InvalidOperationException, TechnicalFailureException;
	 
	 void printSalesReport();

}
