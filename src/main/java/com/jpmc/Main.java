package com.jpmc;

import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import com.jpmc.model.SaleMessageVO;

/**
 * Hello world!
 *
 */
public class Main 
{
	public static void main(String[] args) {

		try {
			Path path = Paths.get("D:\\Iswarya\\workspace2\\MessageProcessor\\TestData\\Messages.json");

			if (!Files.exists(path) || Files.notExists(path) || !Files.isReadable(path)) {
				System.out.println("Given file path is incorrect or inaccessible. ");
				System.exit(1);
			}

			MessageProcessor messageProcessor = MessageProcessor.getMessageProcessor();
			List<SaleMessageVO> saleMessages = messageProcessor.parse(path.toString());
			if(saleMessages == null || saleMessages.isEmpty()) {
	            System.out.println("Sale messages parsing failed.");
	            System.exit(1);
	        }
			
			boolean processed = messageProcessor.process(saleMessages);
	        if(!processed) {
	            System.out.println("Sale notifications' processing failed.");
	            System.exit(1);
	        }
			

		} catch (InvalidPathException | NullPointerException exception) {
			System.out.println("Given file path is incorrect or inaccessible. ");
			System.exit(1);
		}

	}
}
