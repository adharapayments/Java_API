/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.arthikatrading.REST.Examples;

import org.arthikatrading.API.AccountingData;
import org.arthikatrading.API.ConfigData;
import org.arthikatrading.Exceptions.AccountingException;
import org.arthikatrading.Exceptions.ConfigException;
import org.arthikatrading.REST.Client.MapREST;

/**
 *
 * @author gudu
 */
public class GetAccountingExample {
    
    public static void main(String[] args) throws InterruptedException 
    {
        MapREST mapREST = new MapREST("http://104.218.51.2:8080/ArthikaRESTServer/webresources/");
        
        String user = "Conservative_GBPUSD";
        String password = "AlgoPlus";
        
        ConfigData configData = null;
        
        int pbId = 0;      
        int AUId = 0;
        
        /**********************************************************************/
        
        try {           
            configData = mapREST.getConfig(user, password);
        } catch (ConfigException ex) {
            System.out.println(ex.getCode() + " - " + ex.getMessage());
            System.exit(1);
        }
        
        // Get AU ID and PB ID
        AUId = configData.getAuID();
        pbId = configData.getPrimeBrokerIndex(0).getID();
        
        System.out.println("/************ ACCOUNTING API with getAccounting method ************/\n");
        
        // Print accounting
        try {
            AccountingData accountingData;
            accountingData = mapREST.getAccounting(AUId, pbId);
            System.out.println("accountingData.getEquity() = " + accountingData.getEquity());
            System.out.println("accountingData.getUsedMargin() = " + accountingData.getUsedMargin());
            System.out.println("accountingData.getReservedMargin() = " + accountingData.getReservedMargin());
            System.out.println("accountingData.getGlobalEquity() = " + accountingData.getGlobalEquity());
            System.out.println("accountingData.getProfitAndLoss() = " + accountingData.getProfitAndLoss());
            System.out.println("accountingData.getGlobalProfitAndLoss() = " + accountingData.getGlobalProfitAndLoss());
            System.out.println();
        } catch (AccountingException ex) {
            System.out.println(ex.getCode() + " - " + ex.getMessage());
            System.exit(1);
        }
        
        System.out.println("/************ ACCOUNTING API with other methods ************/\n");
        
        double equity;
        double margin;
        
        // Print accounting
        try 
        {
            // Get equity and margin of system
            equity = mapREST.getEquity (AUId, pbId);
            System.out.println("mapREST.getEquity(" + AUId + ", " + pbId + ") = " + equity);
            margin = mapREST.getUsedMargin (AUId, pbId);
            System.out.println("mapREST.getUsedMargin(" + AUId + ", " + pbId + ") = " + margin);
            margin = mapREST.getReserveMargin (AUId, pbId);
            System.out.println("mapREST.getReserveMargin(" + AUId + ", " + pbId + ") = " + margin);
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
    }
    
}
