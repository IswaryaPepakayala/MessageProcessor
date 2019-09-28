package com.jpmc.service.impl;

import static org.junit.Assert.assertTrue;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.jpmc.exceptions.InvalidMessageTypeException;
import com.jpmc.exceptions.InvalidOperationException;
import com.jpmc.exceptions.TechnicalFailureException;
import com.jpmc.model.SaleMessageVO;
import com.jpmc.model.SaleVO;
import com.jpmc.model.Transaction;
import com.jpmc.utility.OperationType;

@RunWith(MockitoJUnitRunner.class)
public class StoreSalesMessagesImplTest {
	@Mock
	private Transaction transaction;
	private StoreSalesMessagesImpl storeSalesMessages;

	@Before
	public void setUp() {
		storeSalesMessages = new StoreSalesMessagesImpl();
	}
	
	@Test
	public void updateMessageForAddOperation() throws Exception {
		SaleMessageVO saleMessage = new SaleMessageVO("AdjustmentMessage", new SaleVO("apple", BigDecimal.valueOf(40)), 0, OperationType.ADD);
		List<Transaction> trans=new ArrayList<Transaction>();
		trans.add(new Transaction(new BigDecimal(10)));
		Map<String, List<Transaction>> stores=new HashMap<String, List<Transaction>>();
		stores.put("apple",trans);
		storeSalesMessages.setStrore(stores);
		boolean b = storeSalesMessages.updateStore(saleMessage);
		assertTrue(b);
	}
	@Test
	public void updateMessageForSubtractOperation() throws Exception{
		SaleMessageVO saleMessage = new SaleMessageVO("AdjustmentMessage", new SaleVO("apple", BigDecimal.valueOf(40)), 0, OperationType.SUBTRACT);
		List<Transaction> trans=new ArrayList<Transaction>();
		trans.add(new Transaction(new BigDecimal(10)));
		Map<String, List<Transaction>> stores=new HashMap<String, List<Transaction>>();
		stores.put("apple",trans);
		storeSalesMessages.setStrore(stores);
		boolean b = storeSalesMessages.updateStore(saleMessage);
		assertTrue(b);
	}
	
	@Test
	public void updateMessageForMultiplyOperation() throws Exception{
		SaleMessageVO saleMessage = new SaleMessageVO("AdjustmentMessage", new SaleVO("apple", BigDecimal.valueOf(40)), 0, OperationType.MULTIPLY);
		List<Transaction> trans=new ArrayList<Transaction>();
		trans.add(new Transaction(new BigDecimal(10)));
		Map<String, List<Transaction>> stores=new HashMap<String, List<Transaction>>();
		stores.put("apple",trans);
		storeSalesMessages.setStrore(stores);
		boolean b = storeSalesMessages.updateStore(saleMessage);
		assertTrue(b);
	}
	
	@Test
	public void shouldAddNewSaleTransactions() throws Exception {
		SaleMessageVO saleMessage = new SaleMessageVO("saleMessage", new SaleVO("apple", BigDecimal.valueOf(30)), 10, null);
		List<Transaction> trans=new ArrayList<Transaction>();
		Method method=StoreSalesMessagesImpl.class.getDeclaredMethod("addNewSaleTransactions", List.class,SaleMessageVO.class);
		method.setAccessible(true);
		List<Transaction> list=(List<Transaction>) method.invoke(storeSalesMessages, trans,saleMessage);
	}
	
	@Test
	public void shouldGetRevenueForProduct() throws Exception {
		List<Transaction> trans=new ArrayList<Transaction>();
		trans.add(new Transaction(new BigDecimal(10)));
		trans.add(new Transaction(new BigDecimal(30)));
		trans.add(new Transaction(new BigDecimal(50)));
		Method method=StoreSalesMessagesImpl.class.getDeclaredMethod("getRevenueForProduct", List.class);
		method.setAccessible(true);
		BigDecimal list=(BigDecimal) method.invoke(storeSalesMessages, trans);
	}
	
    @Test(expected = InvalidMessageTypeException.class)
	public void shouldThrowInvalidMessageException() throws Exception{
    	SaleMessageVO saleMessage = new SaleMessageVO("invalidMessage", new SaleVO("apple", BigDecimal.ZERO), 0, null);
    	storeSalesMessages.updateStore(saleMessage);
	}
	
	
}
