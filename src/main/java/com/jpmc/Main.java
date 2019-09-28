package com.jpmc;

import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.jpmc.model.SaleMessageVO;

public class Main {
	
	private static final Logger logger = LogManager.getLogger(Main.class);
	public static void main(String[] args) throws Exception {

		try {
			Path path = Paths.get("TestData/Messages.json");

			if (!Files.exists(path) || Files.notExists(path) || !Files.isReadable(path)) {
				logger.info("Given file path is incorrect or inaccessible. ");
				System.exit(1);
			}

			MessageProcessor messageProcessor = MessageProcessor.getMessageProcessor();
			// Parse JSON by using ObjectMapper
			List<SaleMessageVO> saleMessages = messageProcessor.parse(path.toString());
			if (saleMessages == null || saleMessages.isEmpty()) {
				logger.info("Sale messages parsing failed.");
				System.exit(1);
			}
			// Process every message that is read from JSON
			boolean processed = messageProcessor.process(saleMessages);
			if (!processed) {
				logger.info("Sale notifications processing failed.");
				System.exit(1);
			}

		} catch (InvalidPathException | NullPointerException exception) {
			logger.error("Given file path is incorrect or inaccessible. ");
			System.exit(1);
		}

	}
}
