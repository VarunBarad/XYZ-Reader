package com.varunbarad.xyzreader.articledetails;

import android.database.Cursor;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.varunbarad.xyzreader.data.ArticleLoader;

/**
 * Creator: Varun Barad
 * Date: 02-12-2017
 * Project: XYZ-Reader
 */
public class ArticlesPagerAdapter extends FragmentStatePagerAdapter {
  private Cursor cursor;
  
  public ArticlesPagerAdapter(FragmentManager fragmentManager, Cursor cursor) {
    super(fragmentManager);
    this.cursor = cursor;
  }
  
  @Override
  public Fragment getItem(int position) {
    cursor.moveToPosition(position);
    return ArticleDetailFragment.newInstance(cursor.getLong(ArticleLoader.Query._ID));
  }
  
  @Override
  public int getCount() {
    return (cursor != null) ? cursor.getCount() : 0;
  }
  
  public void setCursor(Cursor cursor) {
    this.cursor = cursor;
  }
}
