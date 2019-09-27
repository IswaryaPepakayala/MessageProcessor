package com.jpmc;

import static com.jpmc.utility.Constants.ADJUSTMENT;
import static com.jpmc.utility.Constants.MESSAGE_CAPACITY;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jpmc.model.SaleMessageVO;
import com.jpmc.service.impl.StoreSalesMessagesImpl;;

public class MessageProcessor {
	
	private static MessageProcessor messageProcessor = new MessageProcessor();
	private StoreSalesMessagesImpl storeSalesMessages;
	
	
	 private MessageProcessor() {
	        this.storeSalesMessages = new StoreSalesMessagesImpl();
	    }
	
	 public static MessageProcessor getMessageProcessor() {
	        return messageProcessor;
	    }
	 
	 
	 public List<SaleMessageVO> parse(String salesMessageFile) {
	        List<SaleMessageVO> saleMessage = null;
	        ObjectMapper mapper = new ObjectMapper();

	        try {
	        	saleMessage = mapper.readValue(new File(salesMessageFile), new TypeReference<List<SaleMessageVO>>(){});
	        } catch (IOException e) {
	            e.printStackTrace();
	        }

	        return saleMessage;
	    }
	 
	 
	 public boolean process(List<SaleMessageVO> saleMessages) {
		 AtomicInteger counter = new AtomicInteger(0);
		 StringBuilder adjustmentsLog = new StringBuilder();
		 
		 saleMessages.stream().forEach(saleMessage -> {
			 
			 storeSalesMessages.updateStore(saleMessage);
			 counter.getAndIncrement();
			 
			 if(ADJUSTMENT.equals(saleMessage.getMessageType())) {
				 printAdjustmentLog(saleMessage, adjustmentsLog);
			 }
			 
			 if(counter.get() % 10 == 0) {
	                System.out.println("\n------- 10 Sales Processed Record ------");
	                storeSalesMessages.printSalesReport();
	            }
			 
			 if(counter.get() == MESSAGE_CAPACITY) {
	                System.out.println("\nThe processing capacity is only a total of "
	                        + MESSAGE_CAPACITY + " messages. Processing stopped.");
	                return;
	            }
			 
		 }); 
		 
		 if(adjustmentsLog.length() != 0) {
	            System.out.println("\n---- Adjustment Log ---");
	            System.out.println(adjustmentsLog.toString());
	        }
		 
		 return true;
	 }
	 
	 private StringBuilder printAdjustmentLog(SaleMessageVO saleMessage, StringBuilder adjustmentsLog) {
		
			 adjustmentsLog.append("Product ");
             adjustmentsLog.append(saleMessage.getSale().getProductType());
             adjustmentsLog.append(" was adjusted operation: ");
             adjustmentsLog.append(saleMessage.getOperationType());
             adjustmentsLog.append(" by a value of ");
             adjustmentsLog.append(saleMessage.getSale().getSellingPrice());	             
             adjustmentsLog.append(".\n");
		
		 return adjustmentsLog;
		 
	 }
}
