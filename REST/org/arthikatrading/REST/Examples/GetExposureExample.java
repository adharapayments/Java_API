/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.arthikatrading.REST.Examples;

import org.arthikatrading.API.AssetExposure;
import org.arthikatrading.API.ConfigData;
import org.arthikatrading.API.SecurityExposure;
import org.arthikatrading.API.StubConstants;
import org.arthikatrading.Exceptions.ConfigException;
import org.arthikatrading.Exceptions.ExposuresException;
import org.arthikatrading.REST.Client.MapREST;

/**
 *
 * @author gudu
 */
public class GetExposureExample {
    
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
