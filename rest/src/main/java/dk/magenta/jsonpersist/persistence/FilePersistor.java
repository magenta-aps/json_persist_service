/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.magenta.jsonpersist.persistence;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author martin
 */
public class FilePersistor {
    public static final String CONF_LOCATION = "fpSettings";
    
    public static final String CUSTOM_FORMAT_DIR = "custom";
    public static final String STORAGE_DIR = "store";
    private final File root;

    public FilePersistor() throws IOException{
        Properties conf = new Properties();
        
        String param = System.getenv(CONF_LOCATION);
        if(param != null && !param.isEmpty()){
            conf.load(new FileInputStream(new File(param)));
        }else{
            conf.load(this.getClass().getResourceAsStream("/dk/magenta/jsonpersist/properties.txt"));
        }
        root = new File(conf.getProperty("root"));
    }
    
    public FilePersistor(File root) {
        this.root = root;
    }
    
    public Integer saveJSON(String jsonString) throws IOException {
        Integer generatedID = getNextID();
        File directory = getFolder(generatedID);
        directory.mkdirs();
        File target = new File(directory, generatedID+"");
        try(BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(target), StandardCharsets.UTF_8))){
            writer.write(jsonString);
        }
        return generatedID;
    }
    
    public List<String> getIDs(){
        List<String> ids = new ArrayList<>();
        getIds(getStore(), ids);
        return ids;
    }
    

    public String jsonString(Integer id) {
        return "";
    }
    
    
    protected void getIds(File file, List<String> ids){
        if(file.isDirectory()){
            getIds(file, ids);
        }else{
            ids.add(file.getName());
        }
    }

    protected synchronized Integer getNextID() throws IOException, NumberFormatException{
        File idFile = new File(root, "id");
        root.mkdirs();
        Integer nextID;
        if(idFile.exists()){
            try(BufferedReader in = new BufferedReader(new FileReader(idFile))){
                String lastIDString = in.readLine();
                Integer lastID = Integer.valueOf(lastIDString);
                nextID = lastID+1;
                
            }
        }else{
            nextID = 1;
        }
        try(PrintWriter writer = new PrintWriter(new FileWriter(idFile, false))){
            writer.println(nextID);
        }
        return nextID;
    }
    
    protected File getStore(){
        return new File(root, STORAGE_DIR);
    }

    protected synchronized File getFolder(Integer id) {
            Integer folder = id/20000;
            return new File(getStore(), folder+"");
    }
    

}
