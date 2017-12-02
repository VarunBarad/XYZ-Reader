package com.varunbarad.xyzreader.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Creator: Varun Barad
 * Date: 02-12-2017
 * Project: XYZ-Reader
 */
public final class Helper {
  public static Date parsePublishedDate(String dateString) {
    try {
      return (new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.sss", Locale.getDefault())).parse(dateString);
    } catch (ParseException ex) {
      return new Date();
    }
  }
}
