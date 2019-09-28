package com.jpmc;

import static com.jpmc.utility.Constants.ADJUSTMENT;
import static com.jpmc.utility.Constants.MESSAGE_CAPACITY;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.jpmc.exceptions.InvalidMessageTypeException;
import com.jpmc.exceptions.InvalidOperationException;
import com.jpmc.exceptions.TechnicalFailureException;
import com.jpmc.model.SaleMessageVO;
import com.jpmc.service.StoreSalesMessage;
import com.jpmc.service.impl.StoreSalesMessagesImpl;;

public class MessageProcessor {

	private static MessageProcessor messageProcessor = new MessageProcessor();
	private StoreSalesMessage storeSalesMessages = new StoreSalesMessagesImpl();;

	private static final Logger logger = LogManager.getLogger(MessageProcessor.class);

	public MessageProcessor() {
	}

	public static MessageProcessor getMessageProcessor() {
		return messageProcessor;
	}

	/**
	 * This method will parse json and will put all the sales message in the list
	 * 
	 * @param salesMessageFile
	 * @return List<SaleMessageVO>
	 */
	public List<SaleMessageVO> parse(String salesMessageFile) {
		List<SaleMessageVO> saleMessage = null;
		ObjectMapper mapper = new ObjectMapper();
		try {
			logger.info("Converting Json to Java Objects");
			saleMessage = mapper.readValue(new File(salesMessageFile), new TypeReference<List<SaleMessageVO>>() {
			});
		} catch (InvalidFormatException e) {
			logger.error("Invalid Format in the JSON please check.");
		}catch (IOException e) {
			e.printStackTrace();
		}
		return saleMessage;
	}

	/**
	 * This method takes list of message provided in Json and will process every
	 * message by incrementing count and will also generate report for every 10th
	 * sales message and adjust log after completion of every process.
	 * 
	 * @param saleMessages
	 * @return boolean
	 */
	public boolean process(List<SaleMessageVO> saleMessages)
			throws InvalidMessageTypeException, InvalidOperationException {

		AtomicInteger counter = new AtomicInteger(0);
		StringBuilder adjustmentsLog = new StringBuilder();

		saleMessages.stream().forEach(saleMessage -> {
			try {
				storeSalesMessages.updateStore(saleMessage);
				counter.getAndIncrement();
				if (ADJUSTMENT.equals(saleMessage.getMessageType())) {
					printAdjustmentLog(saleMessage, adjustmentsLog);
				}

				if (counter.get() % 10 == 0) {
					logger.info("\n------- 10 Sales Processed Record ------");
					storeSalesMessages.printSalesReport();
				}

				if (counter.get() == MESSAGE_CAPACITY) {
					System.out.println("\nThe processing capacity is only a total of " + MESSAGE_CAPACITY
							+ " messages. Processing stopped.");
					return;
				}
			} catch (InvalidMessageTypeException | InvalidOperationException e) {
				logger.error("Invalid Message is available in json object");
			} catch (TechnicalFailureException e) {
				logger.error("Technical Error Message. Please contact adminstrator");
			}

		});

		if (adjustmentsLog.length() != 0) {
			logger.info("\n---- Adjustment Log ---");
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
