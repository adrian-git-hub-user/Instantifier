package com.instantifier.controllers;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.Date;
import java.util.List;

@Controller
public class SaveUrlController {

	private static boolean isFirst = true;
	private static final String KEY_NAME = "FSDFA";
	
  @RequestMapping("/saveurl")
  public ModelAndView helloWorld() {
    return new ModelAndView("saveurl", "message", "");
  }
  
  @RequestMapping(value = "/time", method = RequestMethod.GET)
  public @ResponseBody String getTime(@RequestParam String name) {
    String result = "Time for " + name + " is " + new Date().toString();
    return result;
  }
  
  @RequestMapping(value = "/getallurls", method = RequestMethod.GET)
  public @ResponseBody String getUrls() throws EntityNotFoundException {		
    return this.getUrlJson();
  }
   
  @RequestMapping(value = "/addUrl", method = RequestMethod.GET)
  public @ResponseBody String getUrl(@RequestParam String urlVal) throws EntityNotFoundException {		
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

    if(isFirst){
    	Entity urlEntity = new Entity("json" , KEY_NAME);
    	urlEntity.setProperty("urlVal", "{ nodes:[ ] }");
    	datastore.put(urlEntity);   
    	isFirst = false;
  }
  
    String currentJson = this.getUrlJson();
    Container container = new Gson().fromJson(currentJson, Container.class);
    UrlNodeDetails u = new UrlNodeDetails(urlVal , "");
    container.nodes.add(u);

    String updatedJson = new Gson().toJson(container);
    Entity urlEntity = new Entity("json" , KEY_NAME);
    urlEntity.setProperty("urlVal", updatedJson);
  
    datastore.put(urlEntity);   
    
    return urlVal;
  }
  
  private String getUrlJson() throws EntityNotFoundException{
	    Key key = KeyFactory.createKey("json", KEY_NAME);
	    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	    
	    Entity entity = datastore.get(key);
	    String urlValInDB = (String)entity.getProperty("urlVal");
	    
	    return urlValInDB;
  }
  
}

