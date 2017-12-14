package com.varunbarad.xyzreader.data.model;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.varunbarad.xyzreader.util.Helper;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Creator: Varun Barad
 * Date: 03-12-2017
 * Project: XYZ-Reader
 */
public class Article extends RealmObject {
  @Expose
  @SerializedName("id")
  @PrimaryKey
  private long id;
  @Expose
  @SerializedName("title")
  private String title;
  @Expose
  @SerializedName("author")
  private String author;
  @Expose
  @SerializedName("body")
  private String content;
  @Expose
  @SerializedName("thumb")
  private String thumbnailUrl;
  @Expose
  @SerializedName("photo")
  private String photoUrl;
  @Expose
  @SerializedName("aspect_ratio")
  private String aspectRatio;
  @Expose
  @SerializedName("published_date")
  private String publicationDate;
  
  /**
   * This is a no-arg constructor meant for use by Gson and not intended to be used directly.
   */
  public Article() {
  }
  
  public Article(long id, String title, String author, String content, String thumbnailUrl, String photoUrl, String aspectRatio, String publicationDate) {
    this.id = id;
    this.title = title;
    this.author = author;
    this.content = content;
    this.thumbnailUrl = thumbnailUrl;
    this.photoUrl = photoUrl;
    this.aspectRatio = aspectRatio;
    this.publicationDate = publicationDate;
  }
  
  public static Article getInstance(String articleJson) {
    return (new Gson()).fromJson(articleJson, Article.class);
  }
  
  public long getId() {
    return id;
  }
  
  public String getTitle() {
    return title;
  }
  
  public String getAuthor() {
    return author;
  }
  
  public String getContent() {
    return content;
  }
  
  public String getThumbnailUrl() {
    return thumbnailUrl;
  }
  
  public String getPhotoUrl() {
    return photoUrl;
  }
  
  public String getAspectRatio() {
    return aspectRatio;
  }
  
  public String getPublicationDate() {
    return publicationDate;
  }
  
  @Override
  public String toString() {
    return this.toJson();
  }
  
  public String toJson() {
    return Helper.getGsonInstance().toJson(this);
  }
}
