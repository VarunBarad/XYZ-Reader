package com.varunbarad.xyzreader.util;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import io.realm.RealmObject;

/**
 * Creator: Varun Barad
 * Date: 02-12-2017
 * Project: XYZ-Reader
 */
public final class Helper {
  public static Date parsePublishedDate(String dateString) {
    try {
      return (new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.sss", Locale.getDefault())).parse(dateString);
    } catch (ParseException e) {
      e.printStackTrace();
      return new Date();
    }
  }
  
  public static Gson getGsonInstance() {
    return new GsonBuilder()
        .setExclusionStrategies(new ExclusionStrategy() {
          @Override
          public boolean shouldSkipField(FieldAttributes f) {
            return f.getDeclaringClass().equals(RealmObject.class);
          }
          
          @Override
          public boolean shouldSkipClass(Class<?> clazz) {
            return false;
          }
        })
        .create();
  }
  
  public static String getUserFriendlyDate(String standardizedDate) {
    Date date = Helper.parsePublishedDate(standardizedDate);
    SimpleDateFormat userFriendlyFormat = new SimpleDateFormat("MMM d, YYYY", Locale.getDefault());
    
    return userFriendlyFormat.format(date);
  }
}
