package com.varunbarad.xyzreader.articlelist;

import android.content.Context;
import android.content.Intent;
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
import com.varunbarad.xyzreader.data.ItemsContract;
import com.varunbarad.xyzreader.data.model.Article;
import com.varunbarad.xyzreader.util.Helper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Creator: Varun Barad
 * Date: 03-12-2017
 * Project: XYZ-Reader
 */
public final class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ViewHolder> {
  private ArrayList<Article> articles;
  private Context context;
  
  public ArticleAdapter(ArrayList<Article> articles, Context context) {
    this.articles = articles;
    this.context = context;
  }
  
  @Override
  public long getItemId(int position) {
    return this.articles.get(position).getId();
  }
  
  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater
        .from(parent.getContext())
        .inflate(R.layout.list_item_article, parent, false);
  
    return new ArticleAdapter.ViewHolder(view);
  }
  
  @Override
  public void onBindViewHolder(ViewHolder holder, int position) {
    holder
        .titleView
        .setText(this.articles.get(position).getTitle());
  
    Date publishedDate = Helper.parsePublishedDate(this.articles.get(position).getPublicationDate());
    if (!publishedDate.before((new GregorianCalendar(2, 1, 1)).getTime())) { //ToDo: Clean the code
      
      holder.subtitleView.setText(Html.fromHtml(
          DateUtils.getRelativeTimeSpanString(
              publishedDate.getTime(),
              System.currentTimeMillis(), DateUtils.HOUR_IN_MILLIS,
              DateUtils.FORMAT_ABBREV_ALL).toString()
              + "<br/>" + " by "
              + this.articles.get(position).getAuthor()));
    } else {
      holder.subtitleView.setText(Html.fromHtml(
          (new SimpleDateFormat()).format(publishedDate) //ToDo: Clean the code
              + "<br/>" + " by "
              + this.articles.get(position).getAuthor()));
    }
    Picasso
        .with(this.context)
        .load(this.articles.get(position).getThumbnailUrl())
        .into(holder.thumbnailView);
  }
  
  @Override
  public int getItemCount() {
    if (this.articles != null) {
      return this.articles.size();
    } else {
      return 0;
    }
  }
  
  public void setArticles(ArrayList<Article> articles) {
    this.articles = articles;
    this.notifyDataSetChanged();
  }
  
  protected class ViewHolder extends RecyclerView.ViewHolder {
    private ImageView thumbnailView;
    private TextView titleView;
    private TextView subtitleView;
    
    private ViewHolder(View itemView) {
      super(itemView);
      
      thumbnailView = itemView.findViewById(R.id.thumbnail);
      titleView = itemView.findViewById(R.id.article_title);
      subtitleView = itemView.findViewById(R.id.article_subtitle);
      
      itemView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          context.startActivity(new Intent(
              Intent.ACTION_VIEW,
              ItemsContract.Items.buildItemUri(ArticleAdapter.this.getItemId(getAdapterPosition()))
          ));
        }
      });
    }
  }
}
