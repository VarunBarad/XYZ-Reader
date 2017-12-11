package com.varunbarad.xyzreader.articledetails;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ShareCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.varunbarad.xyzreader.R;
import com.varunbarad.xyzreader.data.model.Article;
import com.varunbarad.xyzreader.databinding.ActivityArticleDetailBinding;
import com.varunbarad.xyzreader.util.Helper;
import com.varunbarad.xyzreader.util.PaletteTransformation;

import java.util.Locale;

import io.realm.Realm;
import ru.noties.markwon.Markwon;

/**
 * An activity representing a single Article detail screen, letting you swipe between articles.
 */
public class ArticleDetailActivity extends AppCompatActivity {
  private static final String KEY_ARTICLE_ID = "article_id";
  
  private ActivityArticleDetailBinding dataBinding;
  
  private Article article;
  
  public static void start(Context context, Article article) {
    context.startActivity(ArticleDetailActivity.getStarterIntent(context, article));
  }
  
  public static Intent getStarterIntent(Context context, Article article) {
    Intent intent = new Intent(context, ArticleDetailActivity.class);
    intent.putExtra(KEY_ARTICLE_ID, article.getId());
    return intent;
  }
  
  private static String getShareableMessage(Context context, Article article) {
    return String.format(
        Locale.getDefault(),
        context.getString(R.string.message_share),
        article.getTitle(),
        article.getAuthor()
    );
  }
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    this.dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_article_detail);
  
    if ((this.getIntent().getExtras() != null) && (this.getIntent().getExtras().containsKey(KEY_ARTICLE_ID))) {
      long articleId = this.getIntent().getExtras().getLong(KEY_ARTICLE_ID);
      this.article = Realm
          .getDefaultInstance()
          .where(Article.class)
          .equalTo("id", articleId)
          .findFirst();
    
      if (this.article == null) {
        throw new IllegalArgumentException("The activity must be started with a non-null article specified.");
      }
    } else {
      throw new IllegalArgumentException("The activity must be started with a non-null article specified.");
    }
  
    this.setSupportActionBar(this.dataBinding.toolbarArticleDetail);
    this.dataBinding.collapsingToolbarArticleDetail.setTitle(this.article.getTitle());
    if (this.getSupportActionBar() != null) {
      this.getSupportActionBar().setTitle("");
      this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
  
    Picasso
        .with(this)
        .load(this.article.getPhotoUrl())
        .transform(PaletteTransformation.instance())
        .into(
            this.dataBinding.imageViewArticleDetailCover,
            new Callback() {
              @Override
              public void onSuccess() {
                Bitmap bitmap = ((BitmapDrawable) ArticleDetailActivity.this.dataBinding.imageViewArticleDetailCover.getDrawable()).getBitmap();
                Palette palette = PaletteTransformation.getPalette(bitmap);
              
                ArticleDetailActivity.this.setColorsFromPalette(palette);
              }
            
              @Override
              public void onError() {
              
              }
            }
        );
  
    this.dataBinding
        .textViewArticleDetailAuthor
        .setText(this.article.getAuthor());
  
    this.dataBinding
        .textViewArticleDetailPublishingDate
        .setText(Helper.getUserFriendlyDate(this.article.getPublicationDate()));
  
    Markwon.setText(
        this.dataBinding.textViewArticleDetailContent,
        this.article.getContent()
    );
  
    this.dataBinding
        .buttonArticleDetailShare
        .setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            ArticleDetailActivity.this.startActivity(
                Intent.createChooser(
                    ShareCompat.IntentBuilder.from(ArticleDetailActivity.this)
                        .setType("text/plain")
                        .setText(ArticleDetailActivity.getShareableMessage(ArticleDetailActivity.this, ArticleDetailActivity.this.article))
                        .getIntent(),
                    ArticleDetailActivity.this.getString(R.string.action_share)
                )
            );
          }
        });
  }
  
  private void setColorsFromPalette(Palette palette) {
    int primaryColor = ContextCompat.getColor(this, R.color.colorPrimary);
    int primaryDarkColor = ContextCompat.getColor(this, R.color.colorPrimaryDark);
    
    this
        .dataBinding
        .backgroundArticleDetailMetaDetails
        .setBackgroundColor(palette.getMutedColor(0));
    
    this
        .dataBinding
        .collapsingToolbarArticleDetail
        .setContentScrimColor(palette.getMutedColor(primaryColor));
    
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      this
          .getWindow()
          .addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
      this
          .getWindow()
          .setStatusBarColor(palette.getDarkMutedColor(primaryDarkColor));
    }
  }
  
  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == android.R.id.home) {
      this.onBackPressed();
      return true;
    } else {
      return super.onOptionsItemSelected(item);
    }
  }
}
