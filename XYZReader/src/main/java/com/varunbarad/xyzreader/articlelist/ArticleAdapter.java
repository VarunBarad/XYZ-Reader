package com.varunbarad.xyzreader.articlelist;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.varunbarad.xyzreader.R;
import com.varunbarad.xyzreader.data.ArticleLoader;
import com.varunbarad.xyzreader.data.ItemsContract;
import com.varunbarad.xyzreader.util.Helper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Creator: Varun Barad
 * Date: 03-12-2017
 * Project: XYZ-Reader
 */
public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ViewHolder> {
  private Cursor cursor;
  private Context context;
  
  public ArticleAdapter(Cursor cursor, Context context) {
    this.cursor = cursor;
    this.context = context;
  }
  
  @Override
  public long getItemId(int position) {
    this.cursor.moveToPosition(position);
    return this.cursor.getLong(ArticleLoader.Query._ID);
  }
  
  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater
        .from(parent.getContext())
        .inflate(R.layout.list_item_article, parent, false);
    
    final ArticleAdapter.ViewHolder holder = new ArticleAdapter.ViewHolder(view);
    view.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        context.startActivity(new Intent(Intent.ACTION_VIEW,
            ItemsContract.Items.buildItemUri(getItemId(holder.getAdapterPosition()))));
      }
    });
    return holder;
  }
  
  @Override
  public void onBindViewHolder(ViewHolder holder, int position) {
    this.cursor.moveToPosition(position);
    holder.titleView.setText(this.cursor.getString(ArticleLoader.Query.TITLE));
    Date publishedDate = Helper.parsePublishedDate(this.cursor.getString(ArticleLoader.Query.PUBLISHED_DATE));
    if (!publishedDate.before((new GregorianCalendar(2, 1, 1)).getTime())) { //ToDo: Clean the code
      
      holder.subtitleView.setText(Html.fromHtml(
          DateUtils.getRelativeTimeSpanString(
              publishedDate.getTime(),
              System.currentTimeMillis(), DateUtils.HOUR_IN_MILLIS,
              DateUtils.FORMAT_ABBREV_ALL).toString()
              + "<br/>" + " by "
              + this.cursor.getString(ArticleLoader.Query.AUTHOR)));
    } else {
      holder.subtitleView.setText(Html.fromHtml(
          (new SimpleDateFormat()).format(publishedDate) //ToDo: Clean the code
              + "<br/>" + " by "
              + this.cursor.getString(ArticleLoader.Query.AUTHOR)));
    }
    Picasso
        .with(this.context)
        .load(this.cursor.getString(ArticleLoader.Query.THUMB_URL))
        .into(holder.thumbnailView);
  }
  
  @Override
  public int getItemCount() {
    return this.cursor.getCount();
  }
  
  public class ViewHolder extends RecyclerView.ViewHolder {
    public ImageView thumbnailView;
    public TextView titleView;
    public TextView subtitleView;
    
    public ViewHolder(View itemView) {
      super(itemView);
      
      thumbnailView = itemView.findViewById(R.id.thumbnail);
      titleView = itemView.findViewById(R.id.article_title);
      subtitleView = itemView.findViewById(R.id.article_subtitle);
    }
  }
}
