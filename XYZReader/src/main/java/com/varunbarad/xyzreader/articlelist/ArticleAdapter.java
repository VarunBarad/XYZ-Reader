package com.varunbarad.xyzreader.articlelist;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.varunbarad.xyzreader.R;
import com.varunbarad.xyzreader.articledetails.ArticleDetailActivity;
import com.varunbarad.xyzreader.data.model.Article;
import com.varunbarad.xyzreader.databinding.ListItemArticleBinding;
import com.varunbarad.xyzreader.util.Helper;
import com.varunbarad.xyzreader.util.PaletteTransformation;

import java.util.ArrayList;
import java.util.Locale;

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
    LayoutInflater inflater =
        LayoutInflater.from(parent.getContext());
    ListItemArticleBinding itemBinding =
        ListItemArticleBinding.inflate(inflater, parent, false);
  
    return new ArticleAdapter.ViewHolder(itemBinding);
  }
  
  @Override
  public void onBindViewHolder(ViewHolder holder, int position) {
    holder.bind(this.articles.get(position));
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
  
  protected class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private ListItemArticleBinding itemBinding;
  
    private ViewHolder(ListItemArticleBinding itemBinding) {
      super(itemBinding.getRoot());
    
      this.itemBinding = itemBinding;
  
      itemView.setOnClickListener(this);
    }
  
    private void bind(Article article) {
      this.itemBinding
          .textViewDate
          .setText(Helper.getUserFriendlyDate(article.getPublicationDate()));
      
      this.itemBinding
          .textViewTitle
          .setText(article.getTitle());
    
      this.itemBinding
          .textViewAuthor
          .setText(String.format(
              Locale.getDefault(),
              "by %s",
              article.getAuthor()
          ));
    
      Picasso
          .with(this.itemBinding.imageViewThumbnail.getContext())
          .load(article.getThumbnailUrl())
          .transform(PaletteTransformation.instance())
          .into(
              this.itemBinding.imageViewThumbnail,
              new Callback() {
                @Override
                public void onSuccess() {
                  Bitmap bitmap = ((BitmapDrawable) ViewHolder.this.itemBinding.imageViewThumbnail.getDrawable()).getBitmap();
                  Palette palette = PaletteTransformation.getPalette(bitmap);
            
                  ViewHolder.this.setColorsFromPalette(palette);
                }
          
                @Override
                public void onError() {
            
                }
              }
          );
    }
  
    private void setColorsFromPalette(Palette palette) {
      int primaryColor = ContextCompat.getColor(this.itemBinding.getRoot().getContext(), R.color.colorPrimary);
    
      this.itemBinding
          .backgroundMetaDetails
          .setBackgroundColor(palette.getMutedColor(primaryColor));
    }
  
    @Override
    public void onClick(View view) {
      ArticleDetailActivity.start(context, articles.get(getAdapterPosition()));
    }
  }
}
