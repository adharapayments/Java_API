/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.arthikatrading.REST.Strategies;

import org.arthikatrading.Exceptions.*;
import org.arthikatrading.REST.Client.*;
import org.arthikatrading.API.*;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author gudu
 */
public class TestTimesAPIREST {
    
    public static void main(String[] args) throws InterruptedException 
    {
        MapREST mapREST = new MapREST("http://104.218.51.2:8080/ArthikaRESTServer/webresources/");
        
        String user = "Conservative_GBPUSD";
        String password = "AlgoPlus";
        
        Date timeExecInit;
        Date timeExecEnd;
        
        PricesToB pricesToB;
        PricesFB pricesFB;
        
        long acuTimes = 0;
        long diffTimes = 0;
        long maxTimes = 0;
        long minTimes = 1000000;
        
        int iterations = 50000;
        
        /**********************************************************************/
        
        for (int i = 0; i < iterations; i++)
        {
            timeExecInit = new Date();
            try {
                pricesToB = mapREST.getTOB (StubConstants.mapSecurities.get("EUR_USD"));
            } catch (PricesException ex) {
                Logger.getLogger(TestTimesAPIREST.class.getName()).log(Level.SEVERE, null, ex);
            }
            timeExecEnd = new Date();
            diffTimes = timeExecEnd.getTime() - timeExecInit.getTime();
            acuTimes += diffTimes;
            
            maxTimes = (maxTimes < diffTimes) ? diffTimes : maxTimes;
            minTimes = (minTimes > diffTimes) ? diffTimes : minTimes;
        }
        
        System.out.println("Average time petition = " + (acuTimes/iterations) + "ms");
        System.out.println("Max time petition = " + maxTimes + "ms");
        System.out.println("Min time petition = " + minTimes + "ms");
        System.out.println();
        
        /**********************************************************************/
        
        int tiId = 0;
        try {
            tiId = mapREST.getTIIDFromTIIndex(0);
        } catch (ConfigException ex) {
            Logger.getLogger(TestTimesAPIREST.class.getName()).log(Level.SEVERE, null, ex);
        }
        acuTimes = 0;
        maxTimes = 0;
        minTimes = 1000000;
        
        for (int i = 0; i < iterations; i++)
        {
            timeExecInit = new Date();
            try {
                pricesFB = mapREST.getFBA (StubConstants.mapSecurities.get("EUR_USD"), tiId, 0);
            } catch (PricesException ex) {
                Logger.getLogger(TestTimesAPIREST.class.getName()).log(Level.SEVERE, null, ex);
            }
            timeExecEnd = new Date();
            diffTimes = timeExecEnd.getTime() - timeExecInit.getTime();
            acuTimes += diffTimes;
            
            maxTimes = (maxTimes < diffTimes) ? diffTimes : maxTimes;
            minTimes = (minTimes > diffTimes) ? diffTimes : minTimes;
        }
        
        System.out.println("Average time petition = " + (acuTimes/iterations) + "ms");
        System.out.println("Max time petition = " + maxTimes + "ms");
        System.out.println("Min time petition = " + minTimes + "ms");
        
        /**********************************************************************/
    }
}
