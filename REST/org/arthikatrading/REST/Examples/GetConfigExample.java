/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.arthikatrading.REST.Examples;

import org.arthikatrading.API.ConfigData;
import org.arthikatrading.API.StubConstants;
import org.arthikatrading.Exceptions.ConfigException;
import org.arthikatrading.REST.Client.MapREST;

/**
 *
 * @author gudu
 */
public class GetConfigExample {
    
    public static void main(String[] args) throws InterruptedException 
    {
        MapREST mapREST = new MapREST("http://104.218.51.2:8080/ArthikaRESTServer/webresources/");
        
        String user = "Conservative_GBPUSD";
        String password = "AlgoPlus";
        
        ConfigData configData = null;
        
        int numberOfPB = 0;
        int pbId = 0;
        int pbIndex = 0;
        String pbName;
        
        int numberOfTI = 0;
        int tiId = 0;
        int tiIndex = 0;
        String tiName;
        
        int AUId = 0;
        int decPosSecurity = 0;
        
        /**********************************************************************/
        
        System.out.println("\n/************ CONFIG API with getConfig method ************/\n");
        
        try {           
            configData = mapREST.getConfig(user, password);
        } catch (ConfigException ex) {
            System.out.println(ex.getCode() + " - " + ex.getMessage());
            System.exit(1);
        }
        
        // Get number of PBs
        numberOfPB = configData.getNumberOfPB();       
        System.out.println("mapREST.getNumberOfPB = " + numberOfPB + "\n");
        
        // Print Index, ID and PB names
        for (int i = 0; i < numberOfPB; i++)
        {
            System.out.println("Index of PB = " + configData.getPrimeBrokerIndex(i).getIndex());
            pbId = configData.getPrimeBrokerIndex(i).getID();
            System.out.println("configData.getPrimeBrokerIndex(" + i +").getID() = " + pbId);
            pbIndex = configData.getPrimeBrokerID(pbId).getIndex();
            System.out.println("configData.getPrimeBrokerID(" + pbId +").getIndex() = " + pbIndex);
            pbName = configData.getPrimeBrokerID(pbId).getName();
            System.out.println("configData.getPrimeBrokerID(" + pbId + ").getName() = " + pbName);
            pbName = configData.getPrimeBrokerIndex(i).getName();
            System.out.println("configData.getPrimeBrokerIndex(" + i +").getName() = " + pbName);
            System.out.println();
        }
        
        // Get number of TIs
        numberOfTI = configData.getNumberOfTI();
        System.out.println("mapREST.getNumberOfTI = " + numberOfTI + "\n");
        
        // Print Index, ID, TI names and PB associated
        for (int i = 0; i < numberOfTI; i++)
        {
            System.out.println("Index of TI = " + i + " (" + configData.getTradingInterfaceIndex(i).getIndex()+ ")");
            tiId = configData.getTradingInterfaceIndex(i).getID();
            System.out.println("configData.getTradingInterfaceIndex(" + i +").getID() = " + tiId);
            tiIndex = configData.getTradingInterfaceID(tiId).getIndex();
            System.out.println("configData.getTradingInterfaceID(" + tiId +").getIndex() = " + tiIndex);
            tiName = configData.getTradingInterfaceIndex(i).getName();
            System.out.println("configData.getTradingInterfaceIndex(" + i +").getName() = " + tiName);
            tiName = configData.getTradingInterfaceID(tiId).getName();
            System.out.println("configData.getTradingInterfaceID(" + tiId +").getName() = " + tiName);
            pbId = configData.getTradingInterfaceIndex(i).getPbAssociated();
            System.out.println("configData.getTradingInterfaceIndex(" + i +").getPbAssociated() = " + pbId);
            pbId = configData.getTradingInterfaceID(tiId).getPbAssociated();
            System.out.println("configData.getTradingInterfaceID(" + tiId +").getPbAssociated() = " + pbId);
            System.out.println();
        }
        
        // Get AUId
        AUId = configData.getAuID();
        System.out.println("configData.getAuID() = " + AUId + "\n");
        
        // Get number os decimal places for EUR/USD and GBP/USD
        try {           
            decPosSecurity = mapREST.getDecPosSecurity (StubConstants.mapSecurities.get("EUR_USD"));
            System.out.println("mapREST.getDecPosSecurity(EUR/USD) = " + decPosSecurity);
            decPosSecurity = mapREST.getDecPosSecurity (StubConstants.GBP_USD);
            System.out.println("mapREST.getDecPosSecurity(GBP/USD) = " + decPosSecurity);
        }
        catch (ConfigException ex)
        {
            System.out.println(ex.getCode() + " - " + ex.getMessage());
            System.exit(1);
        }
              
        System.out.println("\n/************* CONFIG API with other methods *************/\n");
                      
        // Get number of PBs            
        try {
            numberOfPB = mapREST.getNumberOfPB ();
            System.out.println("mapREST.getNumberOfPB() = " + numberOfPB + "\n");
        } catch (ConfigException ex) {
            System.out.println(ex.getCode() + " - " + ex.getMessage());
            System.exit(1);
        }
                 
        try {
            // Check PB Config methods
            for (int i = 0; i < numberOfPB; i++)
            {
                System.out.println("Index of PB = " + i);
                pbId = mapREST.getPBIDFromIndex(i);
                System.out.println("mapREST.getPBIDFromIndex = " + pbId);
                pbIndex = mapREST.getPBIndexFromID(pbId);
                System.out.println("mapREST.getPBIndexFromID = " + pbIndex);
                pbName = mapREST.getPBNameFromID(pbId);
                System.out.println("mapREST.getPBNameFromID = " + pbName);
                pbName = mapREST.getPBNameFromIndex(pbIndex);
                System.out.println("mapREST.getPBNameFromIndex = " + pbName);
                System.out.println();
            }
        }
        catch (ConfigException ex)
        {
            System.out.println(ex.getCode() + " - " + ex.getMessage());
            System.exit(1);
        }
        
        // Get number of TIs
        
        try {
            numberOfTI = mapREST.getNumberOfTI ();
            System.out.println("mapREST.getNumberOfTI = " + numberOfTI);
        } catch (ConfigException ex) {
            System.out.println(ex.getCode() + " - " + ex.getMessage());
            System.exit(1);
        }       

        try {
            // Check TI config methods
            for (int i = 0; i < numberOfTI; i++)
            {
                System.out.println("Index of TI = " + i);
                tiId = mapREST.getTIIDFromTIIndex(i);
                System.out.println("mapREST.getTIIDFromTIIndex = " + tiId);
                tiIndex = mapREST.getTIIndexFromTIID(tiId);
                System.out.println("mapREST.getTIIndexFromTIID = " + tiIndex);
                tiName = mapREST.getTINameFromTIIndex(tiIndex);
                System.out.println("mapREST.getTINameFromTIIndex = " + tiName);
                tiName = mapREST.getTINameFromTIID(tiId);
                System.out.println("mapREST.getTINameFromTIID = " + tiName);
                pbId = mapREST.getPBFromTIIndex(tiIndex);
                System.out.println("mapREST.getPBFromTIIndex = " + pbId);
                pbId = mapREST.getPBFromTIID(tiId);
                System.out.println("mapREST.getPBFromTIID = " + pbId);
                System.out.println();
            }
        }
        catch (ConfigException ex)
        {
            System.out.println(ex.getCode() + " - " + ex.getMessage());
            System.exit(1);
        }
        
        // Get Accountig Unit ID       
        try {
            AUId = mapREST.getAUId (user, password);
            System.out.println("mapREST.getAUId() = " + AUId + "\n");
        } catch (ConfigException ex) {
            System.out.println(ex.getCode() + " - " + ex.getMessage());
            System.exit(2);
        }
               
        try {
            // Get number os decimal places for EUR/USD
            decPosSecurity = mapREST.getDecPosSecurity (StubConstants.mapSecurities.get("EUR_USD"));
            System.out.println("mapREST.getDecPosSecurity(EUR/USD) = " + decPosSecurity);
            decPosSecurity = mapREST.getDecPosSecurity (StubConstants.GBP_USD);
            System.out.println("mapREST.getDecPosSecurity(GBP/USD) = " + decPosSecurity);
        }
        catch (ConfigException ex)
        {
            System.out.println(ex.getCode() + " - " + ex.getMessage());
            System.exit(1);
        }
        
        /**********************************************************************/
      
    }
}
