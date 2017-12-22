/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.arthikatrading.REST.Examples;

import static java.lang.Math.pow;
import org.arthikatrading.API.ConfigData;
import org.arthikatrading.API.PricesFB;
import org.arthikatrading.API.StubConstants;
import org.arthikatrading.API.Trade;
import org.arthikatrading.Exceptions.ConfigException;
import org.arthikatrading.Exceptions.PricesException;
import org.arthikatrading.Exceptions.TradesException;
import org.arthikatrading.REST.Client.MapREST;

/**
 *
 * @author gudu
 */
public class ModifyCancelLimitTradeExample {
    public static void main(String[] args) throws InterruptedException 
    {
        MapREST mapREST = new MapREST("http://104.218.51.2:8080/ArthikaRESTServer/webresources/");
        
        String user = "Conservative_GBPUSD";
        String password = "AlgoPlus";
        
        ConfigData configData = null;
        PricesFB pricesFB = null;
        
        int tiID = 0;
        int decPosSecurity = 0;
        
        /**********************************************************************/
        
        try {           
            configData = mapREST.getConfig(user, password);
        } catch (ConfigException ex) {
            System.out.println(ex.getCode() + " - " + ex.getMessage());
            System.exit(1);
        }
        
        tiID = configData.getTradingInterfaceIndex(0).getID();
        
        // Get number os decimal places for EUR/USD and GBP/USD
        try {           
            decPosSecurity = mapREST.getDecPosSecurity (StubConstants.mapSecurities.get("EUR_USD"));
        }
        catch (ConfigException ex)
        {
            System.out.println(ex.getCode() + " - " + ex.getMessage());
            System.exit(1);
        }
        
        System.out.println("/************ TRADE API ************/\n");
        
        try {
            // Get ask and bid full book agregated eith depth 0 (same as Top of Book) 
            // prices for EUR/USD from trading interface tiID
            pricesFB = mapREST.getFBA (StubConstants.mapSecurities.get("EUR_USD"), tiID, 1);
            System.out.println("mapREST.getFBA (StubConstants.mapSecurities.get(\"EUR_USD\"), " + tiID + ", 1)");
            for (int i = 0; i < pricesFB.getSize_ask(); i++)
            {
                System.out.print("Ask QuoteID = " + pricesFB.getQuoteID_ask(i) + "\t");                      
            }
            System.out.println();
            for (int i = 0; i < pricesFB.getSize_ask(); i++)
            {
                System.out.print("Ask Price = " + pricesFB.getPrices_ask(i) / pow(10,decPosSecurity) + "\t");
            }
            System.out.println();
            for (int i = 0; i < pricesFB.getSize_ask(); i++)
            {
                System.out.print("Ask Depth = " + pricesFB.getDepth_ask(i) + "\t");
            }
            System.out.println();
            System.out.println();

            for (int i = 0; i < pricesFB.getSize_bid(); i++)
            {
                System.out.print("Bid QuoteID = " + pricesFB.getQuoteID_bid(i) + "\t");                     
            }
            System.out.println();
            for (int i = 0; i < pricesFB.getSize_bid(); i++)
            {
                System.out.print("Bid Price = " + pricesFB.getPrices_bid(i) / pow(10,decPosSecurity) + "\t");
            }
            System.out.println();
            for (int i = 0; i < pricesFB.getSize_bid(); i++)
            {
                System.out.print("Bid Depth = " + pricesFB.getDepth_bid(i) + "\t");
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
            Trade trade;

            // Send limit trade
            System.out.println("Trade Parameters:");
            System.out.println("Action: Buy");
            System.out.println("TimeinForce: Day");
            System.out.println("Amount: 200000");
            System.out.println("Price: " + (pricesFB.getPrices_ask(0) - 30));
            System.out.println("Trading Interface ID: " + tiID);
            System.out.println("Security: EUR/USD");
            System.out.println();
            
            System.out.println("mapREST.sendTrade");
            trade = mapREST.sendLimitTrade (StubConstants.TRADE_SIDE_BUY, StubConstants.TRADE_TIMEINFORCE_DAY, 
                                            (pricesFB.getPrices_ask(0) - 30), 200000, tiID, StubConstants.mapSecurities.get("EUR_USD"), 
                                            user, password);
 
            // Check result of sent
            System.out.println("Trade ID = " + trade.getID());
            if (trade.getResult() < 0)
            {
                System.out.println("Trade Result = " + StubConstants.codeErrors.get(trade.getResult()));
            }
            else
            {
                System.out.println("Method Result = " + trade.getResult());
            }

            System.out.println();

            // Wait 1 second to ask for the trade result
            Thread.sleep(1000);

            // Get info of market trade sended
            System.out.println("mapREST.getTradeInfo");
            mapREST.getTradeInfo(trade, user, password);

            System.out.println("Trade ID = " + trade.getID());
            if (trade.getResult() < 0)
            {
                System.out.println("Method Result = " + StubConstants.codeErrors.get(trade.getResult()));
            }
            else
            {
                System.out.println("Method Result = " + trade.getResult());
            }

            System.out.println("Trade Status = " + StubConstants.codeStatus.get(trade.getStatus()));
            System.out.println("Trade Final Price = " + trade.getPrice());
            System.out.println("Trade Final Amount = " + trade.getAmount());
            System.out.println();
            
            // Wait 1 second to ask for the trade result
            Thread.sleep(1000);
            
            // Modify trade
            System.out.println("mapREST.modifyLimitTrade (" + trade.getID() + ", " + pricesFB.getPrices_ask(0) + ", 200000, user, password)");
            mapREST.modifyLimitTrade(trade, (pricesFB.getPrices_ask(0) - 30), 400000, user, password);

            // Get status of cancel
            if (trade.getResult() < 0)
            {
                System.out.println("Method Result = " + StubConstants.codeErrors.get(trade.getResult()));
            }
            else
            {
                System.out.println("Method Result = " + trade.getResult());
            }

            System.out.println("Trade Status = " + StubConstants.codeStatus.get(trade.getStatus()));
            System.out.println("Trade Price = " + trade.getPrice());
            System.out.println("Trade Amount = " + trade.getAmount());
            System.out.println();
            
            Thread.sleep(1000);

            // Get state of trade
            System.out.println("mapREST.getTradeInfo");
            mapREST.getTradeInfo(trade, user, password);

            System.out.println("Trade ID = " + trade.getID());
            if (trade.getResult() < 0)
            {
                System.out.println("Method Result = " + StubConstants.codeErrors.get(trade.getResult()));
            }
            else
            {
                System.out.println("Method Result = " + trade.getResult());
            }
            System.out.println("Trade Price = " + trade.getPrice());
            System.out.println("Trade Amount = " + trade.getAmount());

            System.out.println("Trade Status = " + StubConstants.codeStatus.get(trade.getStatus()));
            System.out.println();    
            
            // Cancel trade
            System.out.println("mapREST.cancelTrade (trade, user, password)");
            mapREST.cancelTrade(trade, user, password);

            // Get status of cancel
            if (trade.getResult() < 0)
            {
                System.out.println("Method Result = " + StubConstants.codeErrors.get(trade.getResult()));
            }
            else
            {
                System.out.println("Method Result = " + trade.getResult());
            }

            System.out.println("Trade Status = " + StubConstants.codeStatus.get(trade.getStatus()));
            System.out.println("Trade Price = " + trade.getPrice());
            System.out.println("Trade Amount = " + trade.getAmount());
            System.out.println();
            
            Thread.sleep(1000);

            // Get state of trade
            System.out.println("mapREST.getTradeInfo");
            mapREST.getTradeInfo(trade, user, password);

            System.out.println("Trade ID = " + trade.getID());
            if (trade.getResult() < 0)
            {
                System.out.println("Method Result = " + StubConstants.codeErrors.get(trade.getResult()));
            }
            else
            {
                System.out.println("Method Result = " + trade.getResult());
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
    }
}
