package com.varunbarad.xyzreader.data;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.varunbarad.xyzreader.data.model.Article;
import com.varunbarad.xyzreader.remote.ArticleApi;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Creator: Varun Barad
 * Date: 07-12-2017
 * Project: XYZ-Reader
 */
public final class ArticleLoader extends AsyncTaskLoader<ArrayList<Article>> {
  private static final long ACCEPTABLE_DELAY = 10 * 60 * 1000;
  
  private ArrayList<Article> articles;
  
  private long lastLoadTime = 0;
  
  private ArticleLoader(Context context) {
    super(context);
  }
  
  public static ArticleLoader getInstance(Context context) {
    return new ArticleLoader(context);
  }
  
  @Override
  protected void onStartLoading() {
    if ((this.articles != null) && (!this.isDataRefreshNeeded())) {
      this.deliverResult(this.articles);
    } else {
      this.forceLoad();
    }
  }
  
  @Override
  public ArrayList<Article> loadInBackground() {
    if ((this.articles != null) && (!this.isDataRefreshNeeded())) {
      return this.articles;
    } else {
      Retrofit retrofit = new Retrofit.Builder()
          .baseUrl(ArticleApi.baseUrl)
          .addConverterFactory(GsonConverterFactory.create())
          .build();
      
      ArticleApi articleApi = retrofit.create(ArticleApi.class);
      
      Response<ArrayList<Article>> response;
      try {
        response = articleApi
            .getAllArticles()
            .execute();
      } catch (IOException e) {
        e.printStackTrace();
        response = null;
      }
      
      ArrayList<Article> articles;
      if ((response != null) && (response.isSuccessful())) {
        articles = response.body();
      } else {
        articles = new ArrayList<>(0);
      }
      
      this.lastLoadTime = System.currentTimeMillis();
      return articles;
    }
  }
  
  @Override
  public void deliverResult(ArrayList<Article> data) {
    this.articles = data;
    
    // Need to return a new object every time or else it won't call
    // onLoadFinished if it finds the same reference being returned
    super.deliverResult(new ArrayList<>(this.articles));
  }
  
  private boolean isDataRefreshNeeded() {
    return ((System.currentTimeMillis() - this.lastLoadTime) > ArticleLoader.ACCEPTABLE_DELAY);
  }
}
