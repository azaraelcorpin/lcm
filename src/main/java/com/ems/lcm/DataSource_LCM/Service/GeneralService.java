package com.ems.lcm.DataSource_LCM.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.ems.lcm.LcmApplication;
import com.google.gson.Gson;

@Service
public class GeneralService {

    public String renderJsonResponse(String statusCode,String message){
        JSONObject resp = new JSONObject();
        try {
            resp.put("statusCode", statusCode);
            resp.put("message", message);
            return resp.toString();
        } catch (JSONException e) {
            // TODO Auto-generated catch block
           
           return null;
        }
    }


    public String renderJsonResponse(String statusCode,String message,String ObjectName,String entityIdentifier) throws JSONException{
        JSONObject resp = new JSONObject();

            resp.put("statusCode", statusCode);
            resp.put("message", message);
            resp.put(ObjectName,entityIdentifier);
            return resp.toString();

    }

    public String renderJsonResponse(String statusCode,String message,Object obj) throws JSONException{
        JSONObject resp = new JSONObject();

            resp.put("statusCode", statusCode);
            resp.put("message", message);
            resp.put(obj.getClass().getSimpleName(),new JSONObject(new Gson().toJson(obj)));
            return resp.toString();

    }

    public String renderJsonResponse(String statusCode,String message,String itemsName,List iterable) throws JSONException{
        JSONObject resp = new JSONObject();
            
            resp.put("statusCode", statusCode);
            resp.put("message", message);
            resp.put(itemsName,toJSONArray(iterable));
 
            return resp.toString();

    }
    public String renderJsonResponse(String statusCode,String message,String itemsName,Page page) throws JSONException{
        JSONObject resp = new JSONObject();
            
            resp.put("statusCode", statusCode);
            resp.put("message", message);
            resp.put(itemsName,toJSONArray(page.toList()));
            resp.put("totalPages",page.getTotalPages());
            resp.put("numberOfElements",page.getNumberOfElements());
            resp.put("totalElements",page.getTotalElements());

            return resp.toString();

    }
    public JSONArray toJSONArray(List iterable) throws JSONException{
        JSONArray list = new JSONArray();
        for (Object o : iterable) {
               list.put(new JSONObject(new Gson().toJson(o)));
        }
        return list;
    }

    public JSONObject toJSONObject(Object obj) throws JSONException{
        return new JSONObject(new Gson().toJson(obj));
    } 
    
    public String getString(Object value){
        return value!=null?value.toString().isEmpty()?null:value.toString():null;
    }

    public Integer toInteger(String value){
        return value!=null?value.isEmpty()?null:(Integer.parseInt(value)):null;
    }
}
