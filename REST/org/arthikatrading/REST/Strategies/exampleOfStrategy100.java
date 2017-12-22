package org.arthikatrading.REST.Strategies;

// import static java.lang.Math.pow;

import org.arthikatrading.Exceptions.*;
import org.arthikatrading.REST.Client.*;
import org.arthikatrading.API.*;
import java.util.logging.Level;
import java.util.logging.Logger;


public class exampleOfStrategy100 
{

    public static void main(String[] args) throws InterruptedException 
    {                   
        MapREST mapServer = new MapREST("http://localhost:8080/ArthikaRESTServer/webresources/");
        String[] user = new String[60];
        String[] password = new String[60];
        
        String 	name;
        int 	tiIndex, pbIndex;
        int[]   strategyId = new int[60];
        byte 	asset;
        byte    security, users;
        PricesToB priceToB = null;

        /***************** START OF STRATEGY CODE HERE ************************/

        for (users = 1; users <= 60; users++)
        {
            try {
                user[users-1] = "S1" + String.format ("%02d", users);
                password[users-1] = "P1" + String.format ("%02d", users);
                strategyId[users-1] = mapServer.getAUId(user[users-1], password[users-1]);
                System.out.println (user[users-1] + " " + password[users-1]);
                System.out.println("Initializing strategy Id:"+ strategyId[users-1]);
            } catch (ConfigException ex) {
                Logger.getLogger(exampleOfStrategy100.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        

        System.out.println("List of Trading interfaces available:");
        try {
            for(tiIndex = 0; tiIndex < mapServer.getNumberOfTI(); tiIndex++)
            {
                try {
                    name = mapServer.getTINameFromTIIndex(tiIndex);
                    System.out.println ("Trading Interface" + name + " has index:" + tiIndex +" Id:" + mapServer.getTIIDFromTIIndex(tiIndex));
                } catch (ConfigException ex) {
                    Logger.getLogger(exampleOfStrategy100.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (ConfigException ex) {
            Logger.getLogger(exampleOfStrategy100.class.getName()).log(Level.SEVERE, null, ex);
        }

        
        boolean sentMarket = false, sentLimit = false, modifiedLimit = false, cancelled = false;
        int 	priceBidForTrade = 0, waitStrategy = 0, p, t;
        Trade[] tradeMarket = new Trade[60], tradeLimit = new Trade[60];

        while (!cancelled)
        {
            try 
            {
                Thread.sleep(1000);                 //1000 milliseconds sleep
            } 
            catch(InterruptedException ex) 
            {
                Thread.currentThread().interrupt();
            }  

            if (!sentMarket)
            {
                try {
                    priceToB = mapServer.getTOB(StubConstants.EUR_USD);
                } catch (PricesException ex) {
                    Logger.getLogger(exampleOfStrategy100.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                for(tiIndex = 0; tiIndex < priceToB.getSize_ask(); tiIndex++)
                {
                    System.out.println("EUR/USD, Top of Book price Ask:" + priceToB.getPrices_ask(tiIndex) + " tiID:" + priceToB.getTI_ask(tiIndex));
                }
                for(tiIndex = 0; tiIndex < priceToB.getSize_bid(); tiIndex++)
                {
                    System.out.println("EUR/USD, Top of Book price Bid:" + priceToB.getPrices_bid(tiIndex) + " tiID:" + priceToB.getTI_bid(tiIndex));
                }
                
                try {
                    priceToB = mapServer.getTOB(StubConstants.GBP_USD, StubConstants.SIDE_BID);
                } catch (PricesException ex) {
                    Logger.getLogger(exampleOfStrategy100.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                if (priceBidForTrade != priceToB.getPrices_bid(0))
                {
                    try {
                        System.out.println("GBP_USD Top of Book security price:" + priceToB.getPrices_bid(0) + " Asset prices: GBP" +
                                mapServer.getPricesBookAsset(StubConstants.GBP));
                    } catch (PricesException ex) {
                        Logger.getLogger(exampleOfStrategy100.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                if (priceBidForTrade != 0) 
                {
                    PricesFB pricesFullBook = null;
                    
                    try {
                        pricesFullBook = mapServer.getFBA(StubConstants.GBP_USD, priceToB.getTI_bid(0), 0, StubConstants.SIDE_ASK);
                    } catch (PricesException ex) {
                        Logger.getLogger(exampleOfStrategy100.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                    for (p = 0; p < pricesFullBook.getSize_ask(); p++)
                        {
                        System.out.println("-> Full book GBP/USD: Quote" + pricesFullBook.getQuoteID_ask(p)+ " Price: " + 
                                            pricesFullBook.getPrices_ask(p) + " Liquidity:" + pricesFullBook.getDepth_ask(p));
                        }
                    for (users = 1; users <= 60; users++)
                    {
                        if (users <= 30)
                        {
                            try {
                                tradeMarket[users-1] = mapServer.sendMarketTrade(StubConstants.TRADE_SIDE_BUY, StubConstants.TRADE_TIMEINFORCE_FILL_OR_KILL,
                                        50000, priceToB.getTI_bid(0), StubConstants.GBP_USD, user[users-1], password[users-1]);
                            } catch (TradesException ex) {
                                Logger.getLogger(exampleOfStrategy100.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                        else
                        {
                            try {
                                tradeMarket[users-1] = mapServer.sendMarketTrade(StubConstants.TRADE_SIDE_SELL, StubConstants.TRADE_TIMEINFORCE_FILL_OR_KILL,                            
                                        50000, priceToB.getTI_bid(0), StubConstants.GBP_USD, user[users-1], password[users-1]);
                            } catch (TradesException ex) {
                                Logger.getLogger(exampleOfStrategy100.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                        if ( tradeMarket[users-1].getResult() == 0 )
                        {
                            try {
                                System.out.println("Trade sent by AU:" + strategyId[users-1] + " , GBP_USD Market Id:" + tradeMarket[users-1].getID() +
                                        " Number of trades Alive" + mapServer.getTradesAlive(user[users-1], password[users-1]));
                                
                                sentMarket = true;
                                waitStrategy = 0;
                            } catch (TradesException ex) {
                                Logger.getLogger(exampleOfStrategy100.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                        else
                        {
                            switch (tradeMarket[users-1].getResult())
                            {
                                case StubConstants.ERROR_TRADINGINTERFACE:
                                {
                                        System.out.println ("Trade rejected, Trading Interface Not OK");
                                        break;
                                }
                                case StubConstants.ERROR_ORDERTOOSMALL:
                                {
                                        System.out.println ("Trade rejected, Order too small");
                                        break;
                                }
                                case StubConstants.ERROR_ORDERTOOBIG:
                                {
                                        System.out.println ("Trade rejected, Order too big");
                                        break;
                                }
                                case StubConstants.ERROR_NOMARGIN:
                                {
                                    try {
                                        System.out.println ("Trade rejected, Prime broker ID:" + mapServer.getPBFromTIID(priceToB.getTI_bid(0)) + " No margin. Equity:" +
                                                mapServer.getEquity(strategyId[users-1], mapServer.getPBFromTIID(priceToB.getTI_bid(0)))+ " Used margin:" +
                                                mapServer.getUsedMargin(strategyId[users-1], mapServer.getPBFromTIID(priceToB.getTI_bid(0))));
                                        break;
                                    } catch (ConfigException | AccountingException ex) {
                                        Logger.getLogger(exampleOfStrategy100.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                }
                                default:
                                {
                                        System.out.println ("Trade rejected");
                                        break;
                                }
                            }
                        }
                    }
                }
                priceBidForTrade = priceToB.getPrices_bid(0);
            }
            else if ((sentMarket)&&(!sentLimit)&&(waitStrategy > 5))
            {
                try {
                    priceToB = mapServer.getTOB(StubConstants.GBP_USD, StubConstants.SIDE_ASK);
                } catch (PricesException ex) {
                    Logger.getLogger(exampleOfStrategy100.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                for (users = 1; users <= 60; users++)
                {
                    try {
                        tradeLimit[users-1] = mapServer.sendLimitTrade(StubConstants.TRADE_SIDE_SELL, StubConstants.TRADE_TIMEINFORCE_DAY,
                                priceToB.getPrices_ask(0)+1000, 50000, priceToB.getTI_ask(0), StubConstants.GBP_USD, user[users-1], password[users-1]);
                        if ( tradeLimit[users-1].getResult() == 0)
                        {
                            System.out.println ("Trade sent, AU Id:" + strategyId[users-1] + " type Limit Id:" + tradeLimit[users-1].getID() + " Number of trades Alive:" +
                                    mapServer.getTradesAlive(user[users-1], password[users-1]));
                            sentLimit = true;
                            waitStrategy = 0;
                        }
                        else
                        {
                            switch (tradeLimit[users-1].getResult())
                            {
                                case StubConstants.ERROR_TRADINGINTERFACE:
                                {
                                    System.out.println ("Trade rejected, Trading Interface Not OK");
                                    break;
                                }
                                case StubConstants.ERROR_ORDERTOOSMALL:
                                {
                                    System.out.println ("Trade rejected, Order too small");
                                    break;
                                }
                                case StubConstants.ERROR_ORDERTOOBIG:
                                {
                                    System.out.println ("Trade rejected, Order too big");
                                    break;
                                }
                                case StubConstants.ERROR_NOMARGIN:
                                {
                                    System.out.println ("Trade rejected, Prime broker ID:" + mapServer.getPBFromTIID(priceToB.getTI_bid(0)) + "No margin. Equity:" +
                                            mapServer.getEquity(strategyId[users-1], mapServer.getPBFromTIID(priceToB.getTI_bid(0)))+ " Used margin:" +
                                            mapServer.getUsedMargin(strategyId[users-1], mapServer.getPBFromTIID(priceToB.getTI_bid(0))));
                                    break;
                                }
                                default:
                                {
                                    System.out.println ("Trade rejected");
                                    break;
                                }
                            }
                        }
                    } catch (TradesException | ConfigException | AccountingException ex) {
                        Logger.getLogger(exampleOfStrategy100.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
            else if ((sentLimit)&&(!modifiedLimit)&&(waitStrategy > 5))
            {
            for (users = 1; users <= 60; users++)
                {
                    try {
                        if (mapServer.getTradesAlive(user[users-1], password[users-1]) == 0)
                        {
                            System.out.println ( "Trade limit not alive");
                            modifiedLimit = true;
                            cancelled = true;
                            waitStrategy = 0;
                        }
                        else
                        {
                            if (mapServer.getTradeAliveInfo(tradeLimit[users-1].getID(), user[users-1], password[users-1]).getID() == tradeLimit[users-1].getID())
                            {
                                mapServer.modifyLimitTrade(tradeLimit[users-1].getID(), tradeLimit[users-1].getPrice()-500, 50000, user[users-1], password[users-1]);
                                modifiedLimit = true;
                                System.out.println ("Trade modified.");
                                waitStrategy = 0;
                            }
                            else
                            {
                                System.out.println ("Trade Not modified:%d, looking for Id:" + tradeLimit[users-1].getID());
                            }
                        }
                    } catch (TradesException ex) {
                        Logger.getLogger(exampleOfStrategy100.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
            else if ((modifiedLimit)&&(!cancelled)&&(waitStrategy > 5))
            {
            for (users = 1; users <= 60; users++)
                {
                    try {
                        if (mapServer.getTradeAliveInfo(tradeLimit[users-1].getID(), user[users-1], password[users-1]).getID() == tradeLimit[users-1].getID())
                        {
                            mapServer.cancelTrade(tradeLimit[users-1], user[users-1], password[users-1]);
                            cancelled = true;
                            System.out.println ( "Trade cancell sent:" + tradeLimit[users-1].getID());
                            waitStrategy = 0;
                        }
                    } catch (TradesException ex) {
                        Logger.getLogger(exampleOfStrategy100.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
            else
            {
                waitStrategy++;
            }

        }
        for (users = 1; users <= 60; users++)
        {
            try {
                mapServer.getTradeInfo(tradeMarket[users-1], user[users-1], password[users-1]);
                System.out.println ("AU Id:" + strategyId[users-1] + " Trade Market finished status:" + StubConstants.codeStatus.get(tradeMarket[users-1].getStatus()));
                mapServer.getTradeInfo(tradeLimit[users-1], user[users-1], password[users-1]);
                System.out.println ("Trade Limit finished status:" + StubConstants.codeStatus.get(tradeLimit[users-1].getStatus()));
                AssetExposure exposure;
                SecurityExposure exposureSec;
                for(pbIndex = 0; pbIndex < mapServer.getNumberOfPB(); pbIndex++)
                {
                    for (asset = 1; asset < StubConstants.mapAsset.size(); asset++)
                    {
                        exposure = mapServer.getAssetExposure(strategyId[users-1], mapServer.getPBIDFromIndex(pbIndex), asset);
                        if (exposure.getExposure() != 0)
                        {
                            System.out.println ("AU Id:" + strategyId[users-1] + "PB:" + mapServer.getPBNameFromIndex(pbIndex) + " Asset: " + StubConstants.mapAsset_inverse.get(asset) + " " + exposure.getExposure());
                        }
                    }
                    for (security = 1; security < StubConstants.mapSecurities.size(); security++)
                    {
                        exposureSec = mapServer.getSecurityExposure(strategyId[users-1], mapServer.getPBIDFromIndex(pbIndex), security);
                        if (exposureSec.getExposure() != 0)
                        {
                            System.out.println ("AU Id:" + strategyId[users-1] + "PB:" + mapServer.getPBNameFromIndex(pbIndex) + " Asset: " + StubConstants.mapSecurities_inverse.get(security)
                                    + " " + exposureSec.getExposure() + " Price:" + exposureSec.getPrice());
                        }
                    }
                }
            } catch (TradesException | ExposuresException | ConfigException ex) {
                Logger.getLogger(exampleOfStrategy100.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}

    /*                                                                         */

        
//        mapServer.getPricesBookSecurity(StubConstants.EUR_USD);
        
        /****************** END OF STRATEGY CODE HERE *************************/
       