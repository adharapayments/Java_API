/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.arthikatrading.REST.Examples;

import org.arthikatrading.API.ConfigData;
import org.arthikatrading.API.StubConstants;
import org.arthikatrading.API.Trade;
import org.arthikatrading.Exceptions.ConfigException;
import org.arthikatrading.Exceptions.TradesException;
import org.arthikatrading.REST.Client.MapREST;

/**
 *
 * @author gudu
 */
public class SendMarketTradeExample {
    
    public static void main(String[] args) throws InterruptedException 
    {
        MapREST mapREST = new MapREST("http://104.218.51.2:8080/ArthikaRESTServer/webresources/");
        
        String user = "Conservative_GBPUSD";
        String password = "AlgoPlus";
        
        ConfigData configData = null;
        
        int tiID = 0;
        
        /**********************************************************************/
        
        try {           
            configData = mapREST.getConfig(user, password);
        } catch (ConfigException ex) {
            System.out.println(ex.getCode() + " - " + ex.getMessage());
            System.exit(1);
        }
        
        tiID = configData.getTradingInterfaceIndex(0).getID();
        
        System.out.println("/************ TRADE API ************/\n");
        
        try
        {
            Trade trade;

            // Send market trade
            System.out.println("Trade Parameters:");
            System.out.println("Action: Buy");
            System.out.println("TimeinForce: Inmediately or Cancel");
            System.out.println("Amount: 100000");
            System.out.println("Trading Interface ID: " + tiID);
            System.out.println("Security: EUR/USD");
            System.out.println();
            
            System.out.println("mapREST.sendTrade");
            trade = mapREST.sendMarketTrade (StubConstants.TRADE_SIDE_BUY, StubConstants.TRADE_TIMEINFORCE_INMED_OR_CANCEL, 
                                            100000, tiID, StubConstants.mapSecurities.get("EUR_USD"), user, password);
 
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
            
        }
        catch (TradesException ex)
        {
            System.out.println(ex.getCode() + " - " + ex.getMessage());
        }
        
        /**********************************************************************/   
    }
}
