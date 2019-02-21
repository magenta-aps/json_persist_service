/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.magenta.jsonpersist;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author martin
 */
public final class Utilities {

    private static final Logger LOGGER = LogManager.getLogger();
    public static final String CONF_LOCATION = "jpsConf";

    private Utilities() {
    }

    public static CustomProperties getProperties() throws IOException {
        LOGGER.info("Getting config");
        CustomProperties conf = new CustomProperties();

        String configLocation = System.getenv(CONF_LOCATION);
        if (configLocation != null && !configLocation.isEmpty()) {
            conf.load(new FileInputStream(new File(configLocation)));
            return conf;
        }
        String catalinaBase = System.getProperty("catalina.base");
        LOGGER.info("catalina base prop: "+catalinaBase);
        if (catalinaBase == null || catalinaBase.isEmpty()) {
            catalinaBase = System.getenv("catalina.base");
            LOGGER.info("Catalina base from env: "+catalinaBase);
        }
       
        
        if (catalinaBase != null && !catalinaBase.isEmpty()) {
            LOGGER.info("Found catalina base: "+catalinaBase);
            File globalConfFile = new File(catalinaBase + "/conf/" + CONF_LOCATION + ".cfg");
            if (globalConfFile.exists()) {
                LOGGER.info("Found global conf: "+globalConfFile);
                conf.load(new FileInputStream(globalConfFile));
                return conf;
            }
        }
        
        conf.load(Utilities.class.getResourceAsStream("/dk/magenta/jsonpersist/properties.txt"));
        return conf;
    }

}
