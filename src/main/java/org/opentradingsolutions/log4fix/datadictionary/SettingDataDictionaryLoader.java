/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.opentradingsolutions.log4fix.datadictionary;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import quickfix.ConfigError;
import quickfix.DataDictionary;
import quickfix.SessionID;
import quickfix.SessionSettings;

/**
 *
 * @author riskamerica
 */
public class SettingDataDictionaryLoader implements DataDictionaryLoader{
    SessionSettings sessionSetting;
    Map<SessionID,DataDictionary> cached=new HashMap<>();

    public SettingDataDictionaryLoader(SessionSettings sessionSetting) {
        this.sessionSetting = sessionSetting;
    }
    public DataDictionary loadDictionary(SessionID sessionId) {
        
        if(!this.cached.containsKey(sessionId))
        {
            try {
                Properties properties= sessionSetting.getSessionProperties(sessionId, true);
                
                String path = properties.getProperty("DataDictionary");                      
                File file = new File(path);
                this.cached.put(sessionId,new DataDictionary(new FileInputStream(file)));
            } catch (FileNotFoundException | ConfigError ex) {
                Logger.getLogger(SettingDataDictionaryLoader.class.getName()).log(Level.SEVERE, null, ex);
            }
        }       
        
        if (this.cached.get(sessionId)==null) {
            throw new IllegalArgumentException("Unable to locate Session object for " + sessionId + ".");
        }

        return this.cached.get(sessionId);
    }
}
