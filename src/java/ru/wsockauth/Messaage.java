/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.wsockauth;

import java.io.StringReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;

/**
 *
 * @author alex
 */
public class Messaage {

    private String type;
    private String sequenceId;
    private String data;
    private JsonObject jsonData;

    public Messaage() {
        type = "";
        sequenceId = "";
        data = "";
        jsonData = Json.createObjectBuilder().build();
    }

    public Messaage(String type, String sequenceId, String text) {
        this.type = type;
        this.sequenceId = sequenceId;
        
        JsonObjectBuilder dataBuilder = Json.createObjectBuilder();
        dataBuilder.add("text", text);
        this.jsonData = dataBuilder.build();

        this.data = this.jsonData.toString();
    }
    
    public void addData(String key, String value) {
        JsonObjectBuilder retVal = Json.createObjectBuilder();
        Set<String> keys = jsonData.keySet();
        for (String k : keys) {
            retVal.add(k, jsonData.get(k));
        }
        retVal.add(key, value);
        jsonData = retVal.build();
        this.data = this.jsonData.toString();
    }
    
    public void parseData() {
        if ( !data.equals("")) {
            try {
                JsonReader jsonReader = Json.createReader(new StringReader(data));
                jsonData = jsonReader.readObject();
                jsonReader.close();
            }
            catch (Exception er) {
                System.out.println("Message Data parser error ! " + er.getMessage());
            }
        }
    }

    public String getType() {
        return type;
    }

    public String getSequenceId() {
        return sequenceId;
    }

    public String getData() {
        return data;
    }

    public String getData(String name) {
        return jsonData.getString(name);
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setSequenceId(String sequenceId) {
        this.sequenceId = sequenceId;
    }

    public void setData(String data) {
        this.data = data;
    }
}
