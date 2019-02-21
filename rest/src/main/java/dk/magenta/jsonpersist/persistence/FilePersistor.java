/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.magenta.jsonpersist.persistence;

import dk.magenta.jsonpersist.CustomProperties;
import dk.magenta.jsonpersist.Utilities;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author martin
 */
public class FilePersistor {

    public static final String CUSTOM_FORMAT_DIR = "custom";
    public static final String STORAGE_DIR = "store";
    private final File configuredRoot;
    private static final Logger LOGGER = LogManager.getLogger();

    public FilePersistor() throws IOException {
        configuredRoot = null;
    }

    public FilePersistor(File root) {
        this.configuredRoot = root;
    }

    public Integer saveJSON(String jsonString) throws IOException {
        LOGGER.info("Saving json: "+jsonString);
        Integer generatedID = getNextID();
        File directory = getFolder(generatedID);
        LOGGER.info("Saving to "+directory.getAbsolutePath());
        directory.mkdirs();
        File target = new File(directory, generatedID + "");
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(target), StandardCharsets.UTF_8))) {
            writer.write(jsonString);
        }
        LOGGER.info("Saved to "+target);
        return generatedID;
    }

    public List<String> getIDs() throws IOException{
        List<String> ids = new ArrayList<>();
        getIds(getStore(), ids);
        return ids;
    }

    public String jsonString(Integer id) throws IOException {
        File contentDir = getFolder(id);
        File fileDir = new File(contentDir, id+"");
        String content = new Scanner(fileDir).useDelimiter("\\Z").next();
        return content;
    }

    protected void getIds(File file, List<String> ids) {
        if (file.isDirectory()) {
            getIds(file, ids);
        } else {
            ids.add(file.getName());
        }
    }

    protected synchronized Integer getNextID() throws IOException, NumberFormatException {
        File root = getRoot();
        File idFile = new File(root, "id");
        root.mkdirs();
        Integer nextID;
        if (idFile.exists()) {
            try (BufferedReader in = new BufferedReader(new FileReader(idFile))) {
                String lastIDString = in.readLine();
                Integer lastID = Integer.valueOf(lastIDString);
                nextID = lastID + 1;

            }
        } else {
            nextID = 1;
        }
        try (PrintWriter writer = new PrintWriter(new FileWriter(idFile, false))) {
            writer.println(nextID);
        }
        return nextID;
    }

    protected File getStore() throws IOException {
        File root;
        root = getRoot();
        return new File(root, STORAGE_DIR);
    }

    protected synchronized File getFolder(Integer id) throws IOException {
        Integer folder = id / 20000;
        return new File(getStore(), folder + "");
    }
    
    protected File getRoot() throws IOException{
        if(configuredRoot != null){
            return configuredRoot;
        }else{
            CustomProperties conf = Utilities.getProperties();
            return conf.getRoot();
        }
        
    }

}
