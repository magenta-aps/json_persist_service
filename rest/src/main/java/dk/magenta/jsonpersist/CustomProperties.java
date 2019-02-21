/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.magenta.jsonpersist;

import java.io.File;
import java.util.Properties;

/**
 *
 * @author martin
 */
public class CustomProperties extends Properties{
    public static final String ROOTCONF = "root";
    public static final String MAILHOST = "mailhost";
    public static final String MAILFROM = "mailfrom";
    public static final String MAILTOFIELD = "mailtofield";
    public static final String LOGOPATH = "logopath";

    public CustomProperties() {
    }

    public CustomProperties(Properties defaults) {
        super(defaults);
    }
    
    public File getRoot(){
        return new File(getProperty(ROOTCONF)); 
    }
    
    public String getMailServer(){
        return getProperty(MAILHOST); 
    }
    
    public String getMailFrom(){
        return getProperty(MAILFROM); 
    }
    
    public String getToField(){
        return getProperty(MAILTOFIELD); 
    }
    
    public String getLogoPath(){
        return getProperty(LOGOPATH); 
    }
    
}
