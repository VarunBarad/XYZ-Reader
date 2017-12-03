package com.varunbarad.xyzreader.data;

import android.app.IntentService;
import android.content.ContentProviderOperation;
import android.content.ContentValues;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.RemoteException;
import android.util.Log;

import com.varunbarad.xyzreader.data.model.Article;
import com.varunbarad.xyzreader.remote.RemoteEndpointUtil;

import org.json.JSONException;

import java.util.ArrayList;

public class UpdaterService extends IntentService {
  public static final String BROADCAST_ACTION_STATE_CHANGE
      = "com.varunbarad.xyzreader.intent.action.STATE_CHANGE";
  public static final String EXTRA_REFRESHING
      = "com.varunbarad.xyzreader.intent.extra.REFRESHING";
  private static final String TAG = "UpdaterService";
  
  public UpdaterService() {
    super(TAG);
  }
  
  @Override
  protected void onHandleIntent(Intent intent) {
    ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
    NetworkInfo ni = cm.getActiveNetworkInfo();
    if (ni == null || !ni.isConnected()) {
      Log.w(TAG, "Not online, not refreshing.");
      return;
    }
    
    sendStickyBroadcast(
        new Intent(BROADCAST_ACTION_STATE_CHANGE).putExtra(EXTRA_REFRESHING, true));
    
    // Don't even inspect the intent, we only do one thing, and that's fetch content.
    ArrayList<ContentProviderOperation> cpo = new ArrayList<ContentProviderOperation>();
    
    Uri dirUri = ItemsContract.Items.buildDirUri();
    
    // Delete all items
    cpo.add(ContentProviderOperation.newDelete(dirUri).build());
    
    try {
      ArrayList<Article> articles = RemoteEndpointUtil.fetchJsonArray();
      if (articles == null) {
        throw new JSONException("Invalid parsed item array");
      }
  
      for (Article article : articles) {
        ContentValues values = new ContentValues();
    
        values.put(ItemsContract.Items.SERVER_ID, article.getId());
        values.put(ItemsContract.Items.AUTHOR, article.getAuthor());
        values.put(ItemsContract.Items.TITLE, article.getTitle());
        values.put(ItemsContract.Items.BODY, article.getContent());
        values.put(ItemsContract.Items.THUMB_URL, article.getThumbnailUrl());
        values.put(ItemsContract.Items.PHOTO_URL, article.getPhotoUrl());
        values.put(ItemsContract.Items.ASPECT_RATIO, article.getAspectRatio());
        values.put(ItemsContract.Items.PUBLISHED_DATE, article.getPublicationDate());
        
        cpo.add(ContentProviderOperation.newInsert(dirUri).withValues(values).build());
      }
      
      getContentResolver().applyBatch(ItemsContract.CONTENT_AUTHORITY, cpo);
      
    } catch (JSONException | RemoteException | OperationApplicationException e) {
      Log.e(TAG, "Error updating content.", e);
    }
    
    sendStickyBroadcast(
        new Intent(BROADCAST_ACTION_STATE_CHANGE).putExtra(EXTRA_REFRESHING, false));
  }
}
