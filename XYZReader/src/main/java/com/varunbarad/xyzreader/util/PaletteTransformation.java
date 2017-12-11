package com.varunbarad.xyzreader.util;

import android.graphics.Bitmap;
import android.support.v7.graphics.Palette;

import com.squareup.picasso.Transformation;

import java.util.Map;
import java.util.WeakHashMap;

/**
 * Creator: Varun Barad
 * Date: 12-12-2017
 * Project: XYZ-Reader
 */
public class PaletteTransformation implements Transformation {
  private static final PaletteTransformation INSTANCE = new PaletteTransformation();
  private static final Map<Bitmap, Palette> CACHE = new WeakHashMap<>();
  
  private PaletteTransformation() {
  }
  
  public static PaletteTransformation instance() {
    return INSTANCE;
  }
  
  public static Palette getPalette(Bitmap bitmap) {
    return CACHE.get(bitmap);
  }
  
  @Override
  public Bitmap transform(Bitmap source) {
    Palette palette = Palette.from(source).generate();
    CACHE.put(source, palette);
    return source;
  }
  
  @Override
  public String key() {
    // Stable key for all requests. An unfortunate requirement.
    return "";
  }
}
