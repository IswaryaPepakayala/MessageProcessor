package com.jpmc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.jpmc.model.SaleMessageVO;
import com.jpmc.model.SaleVO;
import com.jpmc.service.impl.StoreSalesMessagesImpl;
import com.jpmc.utility.OperationType;

/**
 * Unit test for simple App.
 */
@RunWith(MockitoJUnitRunner.class)
public class MessageProcessorTest {
	@Mock
	private StoreSalesMessagesImpl storeSalesMessages;

	private MessageProcessor messageProcessor;

	@Before
	public void setUp() {
		messageProcessor = new MessageProcessor();
	}

	@Test
	public void processMessageTypeTrue() throws Exception{
		List<SaleMessageVO> saleMessages = new ArrayList<SaleMessageVO>();
		SaleMessageVO saleMessage = new SaleMessageVO("Message", new SaleVO("apple", BigDecimal.valueOf(10)), 0, null);
		saleMessages.add(saleMessage);
		when(storeSalesMessages.updateStore(saleMessage)).thenReturn(true);
		boolean b = messageProcessor.process(saleMessages);
		assertTrue(b);
	}

	@Test
	public void processSaleMessageTypeTrue() throws Exception{
		List<SaleMessageVO> saleMessages = new ArrayList<SaleMessageVO>();
		SaleMessageVO saleMessage = new SaleMessageVO("saleMessage", new SaleVO("apple", BigDecimal.valueOf(10)), 11,
				null);
		saleMessages.add(saleMessage);
		boolean b = messageProcessor.process(saleMessages);
		assertTrue(b);
	}

	@Test
	public void processAdjustmentMessageTypeTrue() throws Exception{
		List<SaleMessageVO> saleMessages = new ArrayList<SaleMessageVO>();
		saleMessages.add(new SaleMessageVO("Message", new SaleVO("apple", BigDecimal.valueOf(10)), 0, null));
		saleMessages.add(new SaleMessageVO("AdjustmentMessage", new SaleVO("apple", BigDecimal.valueOf(30)), 0,
				OperationType.ADD));
		boolean b = messageProcessor.process(saleMessages);
		assertTrue(b);
	}

	@Test
	public void processTenMessageTrue() throws Exception{
		List<SaleMessageVO> saleMessages = new ArrayList<SaleMessageVO>();
		saleMessages.add(new SaleMessageVO("Message", new SaleVO("apple", BigDecimal.valueOf(10)), 0, null));
		saleMessages.add(new SaleMessageVO("saleMessage", new SaleVO("apple", BigDecimal.valueOf(10)), 11, null));
		saleMessages.add(new SaleMessageVO("AdjustmentMessage", new SaleVO("apple", BigDecimal.valueOf(30)), 0,
				OperationType.ADD));
		saleMessages.add(new SaleMessageVO("Message", new SaleVO("banana", BigDecimal.valueOf(20)), 0, null));
		saleMessages.add(new SaleMessageVO("saleMessage", new SaleVO("banana", BigDecimal.valueOf(5)), 15, null));
		saleMessages.add(new SaleMessageVO("AdjustmentMessage", new SaleVO("banana", BigDecimal.valueOf(5)), 0,
				OperationType.ADD));
		saleMessages.add(new SaleMessageVO("Message", new SaleVO("mango", BigDecimal.valueOf(20)), 0, null));
		saleMessages.add(new SaleMessageVO("saleMessage", new SaleVO("mango", BigDecimal.valueOf(10)), 10, null));
		saleMessages.add(new SaleMessageVO("AdjustmentMessage", new SaleVO("mango", BigDecimal.valueOf(10)), 0,
				OperationType.MULTIPLY));
		saleMessages.add(new SaleMessageVO("Message", new SaleVO("orange", BigDecimal.valueOf(15)), 0, null));
		saleMessages.add(new SaleMessageVO("saleMessage", new SaleVO("orange", BigDecimal.valueOf(10)), 5, null));
		saleMessages.add(new SaleMessageVO("AdjustmentMessage", new SaleVO("orange", BigDecimal.valueOf(30)), 0,
				OperationType.ADD));
		boolean b = messageProcessor.process(saleMessages);
		assertTrue(b);
	}

	@Test
	public void processNullMessage() throws Exception{
		List<SaleMessageVO> saleMessages = new ArrayList<SaleMessageVO>();
		SaleMessageVO saleMessage = new SaleMessageVO(null, null, 0, null);
		saleMessages.add(saleMessage);
		boolean b = messageProcessor.process(saleMessages);
		assertTrue(b);
	}

	@Test
	public void processInvalidMessageType() throws Exception{
		List<SaleMessageVO> saleMessages = new ArrayList<SaleMessageVO>();
		SaleMessageVO saleMessage = new SaleMessageVO("invalidMessage", new SaleVO("apple", BigDecimal.ZERO), 0, null);
		saleMessages.add(saleMessage);
		messageProcessor.process(saleMessages);
	}
	
	@Test
	public void printAdjustmentLogTest() throws Exception {
		StringBuilder sb=new StringBuilder();
		SaleMessageVO saleMessage = new SaleMessageVO("AdjustmentMessage", new SaleVO("apple", BigDecimal.valueOf(30)), 0, OperationType.ADD);
		Method method=MessageProcessor.class.getDeclaredMethod("printAdjustmentLog", SaleMessageVO.class,StringBuilder.class);
		method.setAccessible(true);
		StringBuilder log=(StringBuilder) method.invoke(messageProcessor, saleMessage,sb);
		System.out.println(log);
		assertEquals(log.toString().trim(),"Product apple was adjusted operation: ADD by a value of 30.");
	}
}
