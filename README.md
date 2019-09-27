# MessageProcessor
This project is the Implementation of a small message processing application that will processing sales notification messages.
It will generate a report about no. of sales ,type of product and revenue generated for every 10th record.
For 50 records it will stop and generate a adjustment log as well.
Prerequisite are:
1.Input data should be in Json format.
2.Add jackson-databind dependency in pom.xml

As per the probelm staement ,there are three input message types in JSON:
1.Message(type 1) which has values productType and sellingPrice.
2.saleMessage(type 2) which has values productType,sellingPrice,saleCount.
3.AdjustmentMessage(type 3) which has values operationType,productType and sellingPrice.

Two test json(Messages.json,Messages60.json) are being commited .One with 11 meesage and 50 message for which path can be changed in main.java.
