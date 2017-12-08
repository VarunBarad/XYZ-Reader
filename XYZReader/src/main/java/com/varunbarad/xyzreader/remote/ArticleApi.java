package com.varunbarad.xyzreader.remote;

import com.varunbarad.xyzreader.data.model.Article;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Creator: Varun Barad
 * Date: 07-12-2017
 * Project: XYZ-Reader
 */
public interface ArticleApi {
  String baseUrl = "https://go.udacity.com/";
  
  @GET("/xyz-reader-json")
  Call<ArrayList<Article>> getAllArticles();
}
