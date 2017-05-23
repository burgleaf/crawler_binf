package me.binf.crawler;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by burgl on 2017/5/23.
 */
public class ResultItems {


    private Map<String,Object> fields = new LinkedHashMap<>();

    private boolean skip;


    public <T> T get(String key){
        Object o = fields.get(key);
        if(o==null){
            return null;
        }
        return (T)fields.get(key);
    }

    public Map<String,Object> getAll(){
        return fields;
    }

    public <T> ResultItems put(String key,T value){
        fields.put(key,value);
        return this;
    }

    public boolean isSkip() {
        return skip;
    }

    public ResultItems setSkip(boolean skip) {
        this.skip = skip;
        return this;
    }


}
