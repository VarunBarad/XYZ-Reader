package com.varunbarad.xyzreader;

import android.app.Application;

import io.realm.Realm;

/**
 * Creator: Varun Barad
 * Date: 11-12-2017
 * Project: XYZ-Reader
 */
public class ReaderApplication extends Application {
  @Override
  public void onCreate() {
    super.onCreate();
    Realm.init(this);
  }
}
