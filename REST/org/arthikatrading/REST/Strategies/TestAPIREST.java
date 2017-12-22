/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.arthikatrading.REST.Strategies;

import org.arthikatrading.Exceptions.*;
import org.arthikatrading.REST.Client.*;
import org.arthikatrading.API.*;
import static java.lang.Math.pow;

/**
 *
 * @author gudu
 */
public class TestAPIREST {
    
    public static void main(String[] args) throws InterruptedException 
    {
        //MapREST mapREST = new MapREST("http://104.218.51.2:8080/ArthikaRESTServer/webresources/");
        MapREST mapREST = new MapREST("http://localhost:8080/ArthikaRESTServer/webresources/");
        
        String user = "Conservative_GBPUSD";
        String password = "AlgoPlus";
        
        /**********************************************************************/
        
        System.out.println("\n/************ CONFIG API ************/\n");
        
        ConfigData configData = null;
        
        try {           
            configData = mapREST.getConfig(user, password);
        } catch (ConfigException ex) {
            System.out.println(ex.getCode() + " - " + ex.getMessage());
            System.exit(1);
        }
        
        // Get number of PBs
        int numberOfPB = 0;               
        try {
            numberOfPB = mapREST.getNumberOfPB ();
            System.out.println("mapREST.getNumberOfPB = " + numberOfPB + " (" + configData.getNumberOfPB() + ")" + "\n");
        } catch (ConfigException ex) {
            System.out.println(ex.getCode() + " - " + ex.getMessage());
            System.exit(1);
        }
          
        int pbId = 0;
        int index;
        String pbName;
        
        try {
            // Check PB Config methods
            for (int i = 0; i < numberOfPB; i++)
            {
                System.out.println("Index of PB = " + i + " (" + configData.getPrimeBrokerIndex(i).getIndex() + ")");
                pbId = mapREST.getPBIDFromIndex(i);
                System.out.println("mapREST.getPBIDFromIndex = " + pbId + " (" + configData.getPrimeBrokerIndex(i).getID() + ")");
                index = mapREST.getPBIndexFromID(pbId);
                System.out.println("mapREST.getPBIndexFromID = " + index + " (" + configData.getPrimeBrokerID(pbId).getIndex() + ")");
                pbName = mapREST.getPBNameFromID(pbId);
                System.out.println("mapREST.getPBNameFromID = " + pbName + " (" + configData.getPrimeBrokerID(pbId).getName() + ")");
                pbName = mapREST.getPBNameFromIndex(index);
                System.out.println("mapREST.getPBNameFromIndex = " + pbName + " (" + configData.getPrimeBrokerIndex(i).getName() + ")");
                System.out.println();
            }
        }
        catch (ConfigException ex)
        {
            System.out.println(ex.getCode() + " - " + ex.getMessage());
            System.exit(1);
        }
        
        // Get number of TIs
        int numberOfTI = 0;
        try {
            numberOfTI = mapREST.getNumberOfTI ();
            System.out.println("mapREST.getNumberOfTI = " + numberOfTI + " (" + configData.getNumberOfTI() + ")" + "\n");
        } catch (ConfigException ex) {
            System.out.println(ex.getCode() + " - " + ex.getMessage());
            System.exit(1);
        }       
        
        int tiId = 0;
        String tiName;
        
        try {
            // Check TI config methods
            for (int i = 0; i < numberOfTI; i++)
            {
                System.out.println("Index of TI = " + i + " (" + configData.getTradingInterfaceIndex(i).getIndex()+ ")");
                tiId = mapREST.getTIIDFromTIIndex(i);
                System.out.println("mapREST.getTIIDFromTIIndex = " + tiId + " (" + configData.getTradingInterfaceIndex(i).getID()+ ")");
                index = mapREST.getTIIndexFromTIID(tiId);
                System.out.println("mapREST.getTIIndexFromTIID = " + index + " (" + configData.getTradingInterfaceID(tiId).getIndex()+ ")");
                tiName = mapREST.getTINameFromTIIndex(index);
                System.out.println("mapREST.getTINameFromTIIndex = " + tiName + " (" + configData.getTradingInterfaceIndex(i).getName() + ")");
                tiName = mapREST.getTINameFromTIID(tiId);
                System.out.println("mapREST.getTINameFromTIID = " + tiName + " (" + configData.getTradingInterfaceID(tiId).getName() + ")");
                pbId = mapREST.getPBFromTIIndex(index);
                System.out.println("mapREST.getPBFromTIIndex = " + pbId + " (" + configData.getTradingInterfaceIndex(i).getPbAssociated()+ ")");
                pbId = mapREST.getPBFromTIID(tiId);
                System.out.println("mapREST.getPBFromTIID = " + pbId + " (" + configData.getTradingInterfaceID(tiId).getPbAssociated() + ")");
                System.out.println();
            }
        }
        catch (ConfigException ex)
        {
            System.out.println(ex.getCode() + " - " + ex.getMessage());
            System.exit(1);
        }
        
        // Get Accountig Unit ID
        int AUId = 0;
        try {
            AUId = mapREST.getAUId (user, password);
            System.out.println("mapREST.getAUId = " + AUId + " (" + configData.getAuID() + ")");
        } catch (ConfigException ex) {
            System.out.println(ex.getCode() + " - " + ex.getMessage());
            System.exit(2);
        }
        
        
        int decPosSecurity;
        decPosSecurity = 0;
        
        try {
            // Get number os decimal places for EUR/USD
            decPosSecurity = mapREST.getDecPosSecurity (StubConstants.mapSecurities.get("EUR_USD"));
            System.out.println("mapREST.getDecPosSecurity(EUR/USD) = " + decPosSecurity + "\n");
        }
        catch (ConfigException ex)
        {
            System.out.println(ex.getCode() + " - " + ex.getMessage());
            System.exit(1);
        }
        
        /**********************************************************************/
        
        System.out.println("/************ PRICES API ************/\n");
        
        PricesToB pricesToB;
        PricesFB pricesFB;
        double price;
        
        try
        {
            // Get ask and bid top of book prices for EUR/USD
            pricesToB = mapREST.getTOB (StubConstants.mapSecurities.get("EUR_USD"));
            System.out.println("mapREST.getTOB (StubConstants.mapSecurities.get(\"EUR_USD\"))");
            for (int i = 0; i < pricesToB.getSize_ask(); i++)
            {
                System.out.println("TI = " + pricesToB.getTI_ask(i));
                System.out.println("Price = " + pricesToB.getPrices_ask(i) / pow(10,decPosSecurity));
                System.out.println("Depth = " + pricesToB.getDepth_ask(i));
            }
            System.out.println();

            for (int i = 0; i < pricesToB.getSize_bid(); i++)
            {
                System.out.println("TI = " + pricesToB.getTI_bid(i));
                System.out.println("Price = " + pricesToB.getPrices_bid(i) / pow(10,decPosSecurity));
                System.out.println("Depth = " + pricesToB.getDepth_bid(i));
            }
            System.out.println();
        } 
        catch (PricesException ex) 
        {
            System.out.println(ex.getCode() + " - " + ex.getMessage());
            System.exit(1);
        }
         
        // Get ask and bid full book agregated prices for EUR/USD from trading interface tiID
        try 
        {
            System.out.println("mapREST.getFBA (StubConstants.mapSecurities.get(\"EUR_USD\"), " + tiId + ", 0)");
            pricesFB = mapREST.getFBA (StubConstants.mapSecurities.get("EUR_USD"), tiId, 0);
                   
            for (int i = 0; i < pricesFB.getSize_ask(); i++)
            {
                System.out.printf("QuoteID = %6s\t", pricesFB.getQuoteID_ask(i));                      
            }
            System.out.println();
            for (int i = 0; i < pricesFB.getSize_ask(); i++)
            {
                System.out.printf("Price = %8s\t", pricesFB.getPrices_ask(i) / pow(10,decPosSecurity));
            }
            System.out.println();
            for (int i = 0; i < pricesFB.getSize_ask(); i++)
            {
                System.out.printf("Depth = %8s\t", pricesFB.getDepth_ask(i));
            }
            System.out.println();
            System.out.println();

            for (int i = 0; i < pricesFB.getSize_bid(); i++)
            {
                System.out.printf("QuoteID = %6s\t", pricesFB.getQuoteID_bid(i));                     
            }
            System.out.println();
            for (int i = 0; i < pricesFB.getSize_bid(); i++)
            {
                System.out.printf("Price = %8s\t", pricesFB.getPrices_bid(i) / pow(10,decPosSecurity));
            }
            System.out.println();
            for (int i = 0; i < pricesFB.getSize_bid(); i++)
            {
                System.out.printf("Depth = %8s\t", pricesFB.getDepth_bid(i));
            }
            System.out.println();
            System.out.println();

            // Get ask and bid full book agregated eith depth 0 (same as Top of Book) 
            // prices for EUR/USD from trading interface tiID
            pricesFB = mapREST.getFBA (StubConstants.mapSecurities.get("EUR_USD"), tiId, 1);
            System.out.println("mapREST.getFBA (StubConstants.mapSecurities.get(\"EUR_USD\"), tiId, 1)");
            for (int i = 0; i < pricesFB.getSize_ask(); i++)
            {
                System.out.print("QuoteID = " + pricesFB.getQuoteID_ask(i) + "\t");                      
            }
            System.out.println();
            for (int i = 0; i < pricesFB.getSize_ask(); i++)
            {
                System.out.print("Price = " + pricesFB.getPrices_ask(i) / pow(10,decPosSecurity) + "\t");
            }
            System.out.println();
            for (int i = 0; i < pricesFB.getSize_ask(); i++)
            {
                System.out.print("Depth = " + pricesFB.getDepth_ask(i) + "\t");
            }
            System.out.println();
            System.out.println();

            for (int i = 0; i < pricesFB.getSize_bid(); i++)
            {
                System.out.print("QuoteID = " + pricesFB.getQuoteID_bid(i) + "\t");                     
            }
            System.out.println();
            for (int i = 0; i < pricesFB.getSize_bid(); i++)
            {
                System.out.print("Price = " + pricesFB.getPrices_bid(i) / pow(10,decPosSecurity) + "\t");
            }
            System.out.println();
            for (int i = 0; i < pricesFB.getSize_bid(); i++)
            {
                System.out.print("Depth = " + pricesFB.getDepth_bid(i) + "\t");
            }
            System.out.println();
            System.out.println();
        } 
        catch (PricesException ex) 
        {
            System.out.println(ex.getCode() + " - " + ex.getMessage());
            System.exit(1);
        }
                
        try
        {
            // Get book prices for asset EUR
            price = mapREST.getPricesBookAsset (StubConstants.mapAsset.get("EUR"));
            System.out.println("mapREST.getPricesBookAsset (StubConstants.mapAsset.get(\"EUR\")) = " + price);        

            // Get book prices for asset USD
            price = mapREST.getPricesBookAsset (StubConstants.mapAsset.get("USD"));
            System.out.println("mapREST.getPricesBookAsset (StubConstants.mapAsset.get(\"USD\")) = " + price);

            // Get book prices for security EUR/USD
            price = mapREST.getPricesBookSecurity (StubConstants.mapSecurities.get("EUR_USD"));
            System.out.println("mapREST.getPricesBookSecurity (StubConstants.mapSecurities.get(\"EUR_USD\")) = " + price);
        }
        catch (PricesException ex)
        {
            System.out.println(ex.getCode() + " - " + ex.getMessage());
            System.exit(1);
        }
        
        System.out.println();
        
        /**********************************************************************/
        
        System.out.println("/************ TRADE API ************/\n");
        
        try
        {
            Trade trade;

            // Send market trade
            System.out.println("mapREST.sendTrade");
            trade = mapREST.sendMarketTrade (StubConstants.TRADE_SIDE_BUY, StubConstants.TRADE_TIMEINFORCE_INMED_OR_CANCEL, 
                                            100000, tiId, StubConstants.mapSecurities.get("EUR_USD"), user, password);

            // Check result of sent
            System.out.println("Trade ID = " + trade.getID());
            if (trade.getResult() < 0)
            {
                System.out.println("Trade Result = " + StubConstants.codeErrors.get(trade.getResult()));
            }
            else
            {
                System.out.println("Trade Result = " + trade.getResult());
            }

            System.out.println("Trade Status = " + StubConstants.codeStatus.get(trade.getStatus()));
            System.out.println();

            Thread.sleep(1000);

            // Get info of market trade sended
            System.out.println("mapREST.getTradeInfo");
            mapREST.getTradeInfo(trade, user, password);

            System.out.println("Trade ID = " + trade.getID());
            if (trade.getResult() < 0)
            {
                System.out.println("Trade Result = " + StubConstants.codeErrors.get(trade.getResult()));
            }
            else
            {
                System.out.println("Trade Result = " + trade.getResult());
            }

            System.out.println("Trade Status = " + StubConstants.codeStatus.get(trade.getStatus()));
            System.out.println("Trade Price = " + trade.getPrice());
            System.out.println("Trade Amount = " + trade.getAmount());
            System.out.println();

            // Send limit trade with price out of market
            System.out.println("mapREST.sendTrade Limit Day");       
            trade = mapREST.sendLimitTrade (StubConstants.TRADE_SIDE_SELL, StubConstants.TRADE_TIMEINFORCE_DAY, 
                                            200001, 200000, tiId, StubConstants.mapSecurities.get("EUR_USD"), user, password);

            // Check result of sent
            System.out.println("Trade ID = " + trade.getID());
            if (trade.getResult() < 0)
            {
                System.out.println("Trade Result = " + StubConstants.codeErrors.get(trade.getResult()));
            }
            else
            {
                System.out.println("Trade Result = " + trade.getResult());
            }

            System.out.println("Trade Status = " + StubConstants.codeStatus.get(trade.getStatus()));
            System.out.println("Trade Price = " + trade.getPrice());
            System.out.println("Trade Amount = " + trade.getAmount());
            System.out.println();

            Thread.sleep(4000);

            // Get info of limit trade sended
            System.out.println("mapREST.getTradeInfo");
            mapREST.getTradeInfo(trade, user, password);

            System.out.println("Trade ID = " + trade.getID());
            if (trade.getResult() < 0)
            {
                System.out.println("Trade Result = " + StubConstants.codeErrors.get(trade.getResult()));
            }
            else
            {
                System.out.println("Trade Result = " + trade.getResult());
            }

            System.out.println("Trade Status = " + StubConstants.codeStatus.get(trade.getStatus()));
            System.out.println("Trade Price = " + trade.getPrice());
            System.out.println("Trade Amount = " + trade.getAmount());
            System.out.println();

            // Modify price of trade to 250001 (2.50001)
            System.out.println("mapREST.modifyTrade (trade, user, password)");
            trade.setPrice(250001);
            mapREST.modifyTrade(trade, user, password);

            // Check result of modify
            if (trade.getResult() < 0)
            {
                System.out.println("Trade Result = " + StubConstants.codeErrors.get(trade.getResult()));
            }
            else
            {
                System.out.println("Trade Result = " + trade.getResult());
            }

            System.out.println("Trade Status = " + StubConstants.codeStatus.get(trade.getStatus()));
            System.out.println("Trade Price = " + trade.getPrice());
            System.out.println("Trade Amount = " + trade.getAmount());

            Thread.sleep(2000);

            // Get info of trade
            System.out.println("mapREST.getTradeInfo");
            mapREST.getTradeInfo(trade, user, password);

            System.out.println("Trade ID = " + trade.getID());
            if (trade.getResult() < 0)
            {
                System.out.println("Trade Result = " + StubConstants.codeErrors.get(trade.getResult()));
            }
            else
            {
                System.out.println("Trade Result = " + trade.getResult());
            }

            System.out.println("Trade Status = " + StubConstants.codeStatus.get(trade.getStatus()));
            System.out.println("Trade Price = " + trade.getPrice());
            System.out.println("Trade Amount = " + trade.getAmount());
            System.out.println();

            // Cancel trade
            System.out.println("mapREST.cancelTrade (trade, user, password)");
            mapREST.cancelTrade(trade, user, password);

            // Get status of cancel
            if (trade.getResult() < 0)
            {
                System.out.println("Trade Result = " + StubConstants.codeErrors.get(trade.getResult()));
            }
            else
            {
                System.out.println("Trade Result = " + trade.getResult());
            }

            System.out.println("Trade Status = " + StubConstants.codeStatus.get(trade.getStatus()));
            System.out.println("Trade Price = " + trade.getPrice());
            System.out.println("Trade Amount = " + trade.getAmount());

            Thread.sleep(1000);

            // Get state of trade
            System.out.println("mapREST.getTradeInfo");
            mapREST.getTradeInfo(trade, user, password);

            System.out.println("Trade ID = " + trade.getID());
            if (trade.getResult() < 0)
            {
                System.out.println("Trade Result = " + StubConstants.codeErrors.get(trade.getResult()));
            }
            else
            {
                System.out.println("Trade Result = " + trade.getResult());
            }
            System.out.println("Trade Price = " + trade.getPrice());
            System.out.println("Trade Amount = " + trade.getAmount());

            System.out.println("Trade Status = " + StubConstants.codeStatus.get(trade.getStatus()));
            System.out.println();          
            System.out.println("Trade Status = " + StubConstants.codeStatus.get(trade.getStatus()));
            System.out.println("Trade Price = " + trade.getPrice());
            System.out.println("Trade Amount = " + trade.getAmount());

            Thread.sleep(1000);

            // Get state of trade
            System.out.println("mapREST.getTradeInfo");
            mapREST.getTradeInfo(trade, user, password);

            System.out.println("Trade ID = " + trade.getID());
            if (trade.getResult() < 0)
            {
                System.out.println("Trade Result = " + StubConstants.codeErrors.get(trade.getResult()));
            }
            else
            {
                System.out.println("Trade Result = " + trade.getResult());
            }
            
            System.out.println("Trade Price = " + trade.getPrice());
            System.out.println("Trade Amount = " + trade.getAmount());

            System.out.println("Trade Status = " + StubConstants.codeStatus.get(trade.getStatus()));
            System.out.println();     
        }
        catch (TradesException ex)
        {
            System.out.println(ex.getCode() + " - " + ex.getMessage());
        }
        
        /**********************************************************************/
        
        System.out.println("/************ ACCOUNTING API ************/\n");
        
        try {
            AccountingData accountingData;
            accountingData = mapREST.getAccounting(AUId, pbId);
            System.out.println("mapREST.getEquity(" + AUId + ", " + pbId + ") = " + accountingData.getEquity());
            System.out.println("mapREST.getUsedMargin(" + AUId + ", " + pbId + ") = " + accountingData.getUsedMargin());
            System.out.println("mapREST.getReserveMargin(" + AUId + ", " + pbId + ") = " + accountingData.getReservedMargin());
            System.out.println("mapREST.getGlobalEquity(" + AUId + ", " + pbId + ") = " + accountingData.getGlobalEquity());
            System.out.println("mapREST.getP&L(" + AUId + ", " + pbId + ") = " + accountingData.getProfitAndLoss());
            System.out.println("mapREST.getGlobalP&L(" + AUId + ", " + pbId + ") = " + accountingData.getGlobalProfitAndLoss());
            System.out.println();
        } catch (AccountingException ex) {
            System.out.println(ex.getCode() + " - " + ex.getMessage());
            System.exit(1);
        }
        
        double equity;
        double margin;
        
        try 
        {
            // Get equity and margin of system
            equity = mapREST.getEquity (AUId, pbId);
            System.out.println("mapREST.getEquity(" + AUId + ", " + pbId + ") = " + equity);
            margin = mapREST.getUsedMargin (AUId, pbId);
            System.out.println("mapREST.getUsedMargin(" + AUId + ", " + pbId + ") = " + margin);
            equity = mapREST.getReserveMargin (AUId, pbId);
            System.out.println("mapREST.getReserveMargin(" + AUId + ", " + pbId + ") = " + equity);
            equity = mapREST.getGlobalEquity (AUId, pbId);
            System.out.println("mapREST.getGlobalEquity(" + AUId + ", " + pbId + ") = " + equity);
            equity = mapREST.getProfitAndLoss (AUId, pbId);
            System.out.println("mapREST.getP&L(" + AUId + ", " + pbId + ") = " + equity);
            equity = mapREST.getGlobalProfitAndLoss(AUId, pbId);
            System.out.println("mapREST.getGlobalP&L(" + AUId + ", " + pbId + ") = " + equity);
            System.out.println();
        }
        catch (AccountingException ex)
        {
            System.out.println(ex.getCode() + " - " + ex.getMessage());
            System.exit(1);
        }
        
        /**********************************************************************/
        
        System.out.println("/************ EXPOSURE API ************/\n");
        
        AssetExposure assetExposure;
        SecurityExposure securityExposure;
        
        try 
        {
            // Get exposures of system
            assetExposure = mapREST.getAssetExposure (AUId, pbId, StubConstants.mapAsset.get("EUR"));
            System.out.println("mapREST.getAssetExposure(" + AUId + ", " + pbId + ", EUR) = " + assetExposure.getExposure());

            assetExposure = mapREST.getAssetExposure (AUId, pbId, StubConstants.mapAsset.get("USD"));
            System.out.println("mapREST.getAssetExposure(" + AUId + ", " + pbId + ", USD) = " + assetExposure.getExposure());

            securityExposure = mapREST.getSecurityExposure (AUId, pbId, StubConstants.mapSecurities.get("EUR_USD"));
            System.out.println("mapREST.getSecurityExposure(" + AUId + ", " + pbId + ", EUR/USD) = " + securityExposure.getExposure());
            System.out.println("mapREST.getSecurityExposure(" + AUId + ", " + pbId + ", EUR/USD) = " + securityExposure.getPrice());

            assetExposure = mapREST.getTotalAssetExposure (AUId, pbId, StubConstants.mapAsset.get("EUR"));
            System.out.println("mapREST.getTotalAssetExposure(" + AUId + ", " + pbId + ", EUR) = " + assetExposure.getExposure());

            assetExposure = mapREST.getTotalAssetExposure (AUId, pbId, StubConstants.mapAsset.get("USD"));
            System.out.println("mapREST.getTotalAssetExposure(" + AUId + ", " + pbId + ", USD) = " + assetExposure.getExposure());

            assetExposure = mapREST.getGlobalAssetExposure (AUId, StubConstants.mapAsset.get("EUR"));
            System.out.println("mapREST.getGlobalAssetExposure(" + AUId + ", EUR) = " + assetExposure.getExposure());

            assetExposure = mapREST.getGlobalAssetExposure (AUId, StubConstants.mapAsset.get("USD"));
            System.out.println("mapREST.getGlobalAssetExposure(" + AUId + ", USD) = " + assetExposure.getExposure());

            securityExposure = mapREST.getGlobalSecurityExposure (AUId, StubConstants.mapSecurities.get("EUR_USD"));
            System.out.println("mapREST.getGlobalSecurityExposure(" + AUId + ", EUR/USD) = " + securityExposure.getExposure());
            System.out.println("mapREST.getGlobalSecurityExposure(" + AUId + ", EUR/USD) = " + securityExposure.getPrice());

            assetExposure = mapREST.getGlobalTotalAssetExposure (AUId, StubConstants.mapAsset.get("EUR"));
            System.out.println("mapREST.getGlobalTotalAssetExposure(" + AUId + ", EUR) = " + assetExposure.getExposure());

            assetExposure = mapREST.getGlobalTotalAssetExposure (AUId, StubConstants.mapAsset.get("USD"));
            System.out.println("mapREST.getGlobalTotalAssetExposure(" + AUId + ", USD) = " + assetExposure.getExposure());
            System.out.println();
        }
        catch (ExposuresException ex)
        {
            System.out.println(ex.getCode() + " - " + ex.getMessage());
            System.exit(1);
        }
        
        /**********************************************************************/
    }
}
