/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.arthikatrading.REST.Examples;

import static java.lang.Math.pow;
import org.arthikatrading.API.ConfigData;
import org.arthikatrading.API.PricesFB;
import org.arthikatrading.API.PricesToB;
import org.arthikatrading.API.StubConstants;
import org.arthikatrading.Exceptions.ConfigException;
import org.arthikatrading.Exceptions.PricesException;
import org.arthikatrading.REST.Client.MapREST;

/**
 *
 * @author gudu
 */
public class GetPricesExample {
    public static void main(String[] args) throws InterruptedException 
    {
        MapREST mapREST = new MapREST("http://104.218.51.2:8080/ArthikaRESTServer/webresources/");
        
        String user = "Conservative_GBPUSD";
        String password = "AlgoPlus";
        
        ConfigData configData = null;
        
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
        
        System.out.println("/************ PRICES API ************/\n");
        
        PricesToB pricesToB;
        PricesFB pricesFB;
        double price;
        
        // Get ask and bid top of book prices for EUR/USD
        try
        {
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
            System.out.println("mapREST.getFBA (StubConstants.mapSecurities.get(\"EUR_USD\"), " + tiID + ", 0)");
            pricesFB = mapREST.getFBA (StubConstants.mapSecurities.get("EUR_USD"), tiID, 0);
                   
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
            pricesFB = mapREST.getFBA (StubConstants.mapSecurities.get("EUR_USD"), tiID, 1);
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
    }
}
