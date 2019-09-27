package com.jpmc.service;

import com.jpmc.model.SaleMessageVO;

public interface StoreSalesMessage {
	
	 boolean updateStore(SaleMessageVO saleMessage);
	 
	 void printSalesReport();

}
