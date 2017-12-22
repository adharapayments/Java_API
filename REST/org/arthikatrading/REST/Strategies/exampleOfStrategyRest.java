package org.arthikatrading.REST.Strategies;
// import static java.lang.Math.pow;

import org.arthikatrading.Exceptions.*;
import org.arthikatrading.REST.Client.*;
import org.arthikatrading.API.*;
import java.util.logging.Level;
import java.util.logging.Logger;


public class exampleOfStrategyRest {

    public static void main(String[] args) throws InterruptedException 
    {                   
        MapREST mapServer = new MapREST("http://104.218.51.2:8080/ArthikaRESTServer/webresources/");
        String user = "S101";
        String password = "P101";
        
        String 	name;
        int 	tiIndex, pbIndex;
        byte 	asset;
        byte    security;
        SecurityExposure    securityExposure;
        AssetExposure       assetExposure;
        PricesToB priceToB = null;

        /***************** START OF STRATEGY CODE HERE ************************/

        int strategyId = 0;
        try {
            strategyId = mapServer.getAUId(user, password);
        } catch (ConfigException ex) {
            Logger.getLogger(exampleOfStrategyRest.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Initializing strategy Id:"+ strategyId);

        System.out.println("List of Trading interfaces available:");
        try {
            for(tiIndex = 0; tiIndex < mapServer.getNumberOfTI(); tiIndex++)
            {
                try {
                    name = mapServer.getTINameFromTIIndex(tiIndex);
                    System.out.println ("Trading Interface" + name + " has index:" + tiIndex +" Id:" + mapServer.getTIIDFromTIIndex(tiIndex));
                } catch (ConfigException ex) {
                    Logger.getLogger(exampleOfStrategyRest.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (ConfigException ex) {
            Logger.getLogger(exampleOfStrategyRest.class.getName()).log(Level.SEVERE, null, ex);
        }

        System.out.println("List of Prime Brokers available:");
        try {
            for(pbIndex = 0; pbIndex < mapServer.getNumberOfPB(); pbIndex++)
            {
                try {
                    name = mapServer.getPBNameFromIndex(pbIndex);
                    System.out.println("Prime Broker:" + name + " has index:" + pbIndex + " Id:" + mapServer.getPBIDFromIndex(pbIndex));
                    for (security = 1; security < StubConstants.mapSecurities.size(); security++)
                    {
                        securityExposure = mapServer.getSecurityExposure(strategyId, mapServer.getPBIDFromIndex(pbIndex), security);
                        if (securityExposure.getExposure() != 0)
                        {
                            name = StubConstants.mapSecurities_inverse.get(security);
                            System.out.println(name + " security exposure:"+ securityExposure.getExposure() + ",price:" +
                                    securityExposure.getPrice() + " decimal places:" + mapServer.getDecPosSecurity(security));
                        }
                    }
                    for (asset = 1; asset < StubConstants.mapAsset.size(); asset++)
                    {
                        assetExposure = mapServer.getAssetExposure(strategyId, mapServer.getPBIDFromIndex(pbIndex), asset);
                        if (assetExposure.getExposure() != 0)
                        {
                            name = StubConstants.mapAsset_inverse.get(asset);
                            System.out.println(name + " asset exposure:" + assetExposure.getExposure());
                        }
                        
                    }
                } catch (ConfigException | ExposuresException ex) {
                    Logger.getLogger(exampleOfStrategyRest.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (ConfigException ex) {
            Logger.getLogger(exampleOfStrategyRest.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        boolean 	sentMarket = false, sentLimit = false, modifiedLimit = false, cancelled = false;
        int 	priceBidForTrade = 0, waitStrategy = 0, p, t;
        Trade       tradeMarket, tradeLimit;
        tradeMarket = new Trade();
        tradeLimit  = new Trade();

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
                    Logger.getLogger(exampleOfStrategyRest.class.getName()).log(Level.SEVERE, null, ex);
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
                    Logger.getLogger(exampleOfStrategyRest.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                if (priceBidForTrade != priceToB.getPrices_bid(0))
                {
                    try {
                        System.out.println("GBP_USD Top of Book security price:" + priceToB.getPrices_bid(0) + " Asset prices: GBP" +
                                mapServer.getPricesBookAsset(StubConstants.GBP)+ " Global Equity:" +
                                mapServer.getGlobalEquity(strategyId, mapServer.getPBFromTIID(priceToB.getTI_bid(0))));
                    } catch (PricesException | AccountingException | ConfigException ex) {
                        Logger.getLogger(exampleOfStrategyRest.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                if (priceBidForTrade > priceToB.getPrices_bid(0) + 1) // If price goes down 0.1 PIP then BUY!!
                {
                    try {
                        tradeMarket = mapServer.sendMarketTrade(StubConstants.TRADE_SIDE_BUY, StubConstants.TRADE_TIMEINFORCE_FILL_OR_KILL,
                                50000, priceToB.getTI_bid(0), StubConstants.GBP_USD, user, password);
                    } catch (TradesException ex) {
                        Logger.getLogger(exampleOfStrategyRest.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                    if ( tradeMarket.getResult() == 0 )
                    {
                        
                        try {
                            System.out.println("Trade sent, GBP_USD Market Id:" + tradeMarket.getID() + " Number of trades Alive" +
                                    mapServer.getTradesAlive(user, password));
                            PricesFB pricesFullBook = mapServer.getFBA(StubConstants.GBP_USD, priceToB.getTI_bid(0), 0, StubConstants.SIDE_ASK);
                            for (p = 0; p < pricesFullBook.getSize_ask(); p++)
                            {
                                System.out.println("-> Full book GBP/USD: Quote" + pricesFullBook.getQuoteID_ask(p)+ " Price: " +
                                        pricesFullBook.getPrices_ask(p) + " Liquidity:" + pricesFullBook.getDepth_ask(p));
                            }
                            sentMarket = true;
                            waitStrategy = 0;
                        } catch (TradesException | PricesException ex) {
                            Logger.getLogger(exampleOfStrategyRest.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        
                    }
                    else
                    {
                        switch (tradeMarket.getResult())
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
                                    System.out.println ("Trade rejected, Prime broker ID:" + mapServer.getPBFromTIID(priceToB.getTI_bid(0)) + "No margin. Equity:" +
                                            mapServer.getEquity(strategyId, mapServer.getPBFromTIID(priceToB.getTI_bid(0)))+ " Used margin:" +
                                            mapServer.getUsedMargin(strategyId, mapServer.getPBFromTIID(priceToB.getTI_bid(0))));
                                    break;
                                } catch (ConfigException | AccountingException ex) {
                                    Logger.getLogger(exampleOfStrategyRest.class.getName()).log(Level.SEVERE, null, ex);
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
                priceBidForTrade = priceToB.getPrices_bid(0);
            }
            else if ((sentMarket)&&(!sentLimit)&&(waitStrategy > 5))
            {
                try {
                    priceToB = mapServer.getTOB(StubConstants.GBP_USD, StubConstants.SIDE_ASK);
                    tradeLimit = mapServer.sendLimitTrade(StubConstants.TRADE_SIDE_SELL, StubConstants.TRADE_TIMEINFORCE_DAY,
                            priceToB.getPrices_ask(0)+1000, 50000, priceToB.getTI_ask(0), StubConstants.GBP_USD, user, password);
                    if ( tradeLimit.getResult() == 0)
                    {
                        System.out.println ("Trade sent, type Limit Id:" + tradeLimit.getID() + " Number of trades Alive:" + mapServer.getTradesAlive(user, password));
                        sentLimit = true;
                        waitStrategy = 0;
                    }
                    else
                    {
                        switch (tradeLimit.getResult())
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
                                        mapServer.getEquity(strategyId, mapServer.getPBFromTIID(priceToB.getTI_bid(0)))+ " Used margin:" +
                                        mapServer.getUsedMargin(strategyId, mapServer.getPBFromTIID(priceToB.getTI_bid(0))));
                                break;
                            }
                            default:
                            {
                                System.out.println ("Trade rejected");
                                break;
                            }
                        }
                    }
                } catch (PricesException | TradesException | ConfigException | AccountingException ex) {
                    Logger.getLogger(exampleOfStrategyRest.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            else if ((sentLimit)&&(!modifiedLimit)&&(waitStrategy > 5))
            {
                try {
                    if (mapServer.getTradesAlive(user, password) == 0)
                    {
                        System.out.println ( "Trade limit not alive");
                        modifiedLimit = true;
                        cancelled = true;
                        waitStrategy = 0;
                    }
                    else
                    {
                        if (mapServer.getTradeAliveInfo(tradeLimit.getID(), user, password).getID() == tradeLimit.getID())
                        {
                            mapServer.modifyLimitTrade(tradeLimit.getID(), tradeLimit.getPrice()-500, 50000, user, password);
                            modifiedLimit = true;
                            System.out.println ("Trade modified.");
                            waitStrategy = 0;
                        }
                        else
                        {
                            System.out.println ("Trade Not modified:%d, looking for Id:" + tradeLimit.getID());
                        }
                    }
                } catch (TradesException ex) {
                    Logger.getLogger(exampleOfStrategyRest.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            else if ((modifiedLimit)&&(!cancelled)&&(waitStrategy > 5))
            {
                try {
                    if (mapServer.getTradeAliveInfo(tradeLimit.getID(), user, password).getID() == tradeLimit.getID())
                    {
                        mapServer.cancelTrade(tradeLimit, user, password);
                        cancelled = true;
                        System.out.println ( "Trade cancell sent:" + tradeLimit.getID());
                        waitStrategy = 0;
                    }
                } catch (TradesException ex) {
                    Logger.getLogger(exampleOfStrategyRest.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            else if ((cancelled)&&(waitStrategy > 20))
            {
                try {
                    mapServer.getTradeHistoricInfo(tradeMarket, user, password);
                    System.out.println ("Trade Market finished status:" + StubConstants.codeStatus.get(tradeMarket.getStatus()));
                    mapServer.getTradeHistoricInfo(tradeLimit, user, password);
                    System.out.println ("Trade Limit finished status:" + StubConstants.codeStatus.get(tradeLimit.getStatus()));
                    waitStrategy = 0;
                    AssetExposure exposure;
                    SecurityExposure exposureSec;
                    for(pbIndex = 0; pbIndex < mapServer.getNumberOfPB(); pbIndex++)
                    {
                        exposure = mapServer.getAssetExposure(strategyId, pbIndex, StubConstants.USD);
                        if (exposure.getExposure() != 0)
                        {
                            System.out.println ("PB:" + mapServer.getPBNameFromIndex(pbIndex) + "USD asset exposure:" + exposure.getExposure());
                        }
                        exposureSec = mapServer.getSecurityExposure(strategyId, pbIndex, StubConstants.GBP_USD);
                        if (exposureSec.getExposure() != 0)
                        {
                            System.out.println ("PB:" + mapServer.getPBNameFromIndex(pbIndex) + "GBP/USD security exposure:" 
                                    + exposureSec.getExposure() + " Price:" + exposureSec.getPrice());
                        }
                    }
                } catch (TradesException | ExposuresException | ConfigException ex) {
                    Logger.getLogger(exampleOfStrategyRest.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            else
            {
                waitStrategy++;
            }

        }
    }
}

    /*                                                                         */

        
//        mapServer.getPricesBookSecurity(StubConstants.EUR_USD);
        
        /****************** END OF STRATEGY CODE HERE *************************/
       