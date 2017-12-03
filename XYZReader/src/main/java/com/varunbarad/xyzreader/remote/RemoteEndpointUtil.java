package com.varunbarad.xyzreader.remote;

import android.util.Log;

import com.google.gson.Gson;
import com.varunbarad.xyzreader.data.model.Article;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RemoteEndpointUtil {
  private static final String TAG = RemoteEndpointUtil.class.getSimpleName();
  
  private RemoteEndpointUtil() {
  }
  
  public static ArrayList<Article> fetchJsonArray() {
    String itemsJson;
    try {
      itemsJson = fetchPlainText(Config.BASE_URL);
    } catch (IOException e) {
      Log.e(TAG, "Error fetching items JSON", e);
      itemsJson = "[]";
    }
    
    // Parse JSON
    Gson gson = new Gson();
    ArrayList<Article> articles = new ArrayList<>(Arrays.asList(gson.fromJson(itemsJson, Article[].class)));
    
    return articles;
  }
  
  static String fetchPlainText(URL url) throws IOException {
    OkHttpClient client = new OkHttpClient();
    
    Request request = new Request.Builder()
        .url(url)
        .build();
    
    Response response = client.newCall(request).execute();
    return response.body().string();
  }
}
