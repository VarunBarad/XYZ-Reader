package com.varunbarad.xyzreader.articlelist;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.varunbarad.xyzreader.R;
import com.varunbarad.xyzreader.articledetails.ArticleDetailActivity;
import com.varunbarad.xyzreader.data.ArticleLoader;
import com.varunbarad.xyzreader.data.model.Article;
import com.varunbarad.xyzreader.databinding.ActivityArticleListBinding;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * An activity representing a list of Articles. This activity has different presentations for
 * handset and tablet-size devices. On handsets, the activity presents a list of items, which when
 * touched, lead to a {@link ArticleDetailActivity} representing item details. On tablets, the
 * activity presents a grid of items as cards.
 */
public class ArticleListActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<Article>> {
  private static final int LOADER_ID_ARTICLES = 1;
  
  private ActivityArticleListBinding dataBinding;
  
  private ArticleAdapter articleAdapter;
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    this.dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_article_list);
  
    int columnCount = getResources().getInteger(R.integer.columns_articleList);
    StaggeredGridLayoutManager layoutManager =
        new StaggeredGridLayoutManager(columnCount, GridLayoutManager.VERTICAL);
  
    this.dataBinding
        .recyclerViewArticleListArticles
        .setLayoutManager(layoutManager);
  
    if (savedInstanceState == null) {
      this.refresh();
    } else {
      this.showArticles(this.retrieveArticles());
    }
  
    this.dataBinding
        .swipeRefreshLayoutArticleListArticles
        .setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
          @Override
          public void onRefresh() {
            ArticleListActivity.this.refresh();
          }
        });
    this.dataBinding
        .swipeRefreshLayoutArticleListPlaceholderError
        .setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
          @Override
          public void onRefresh() {
            ArticleListActivity.this.refresh();
          }
        });
  }
  
  private void refresh() {
    this.showProgress();
  
    Loader<ArrayList<Article>> articleLoader = this.getSupportLoaderManager()
        .getLoader(ArticleListActivity.LOADER_ID_ARTICLES);
    if (articleLoader != null) {
      articleLoader.forceLoad();
    } else {
      this.getSupportLoaderManager()
          .initLoader(ArticleListActivity.LOADER_ID_ARTICLES, null, this);
    }
  }
  
  @Override
  public Loader<ArrayList<Article>> onCreateLoader(int id, Bundle bundle) {
    return ArticleLoader.getInstance(this);
  }
  
  @Override
  public void onLoadFinished(Loader<ArrayList<Article>> loader, ArrayList<Article> data) {
    this.showArticles(data);
  }
  
  @Override
  public void onLoaderReset(Loader<ArrayList<Article>> loader) {
  
  }
  
  private ArrayList<Article> retrieveArticles() {
    Realm realm = Realm.getDefaultInstance();
    
    RealmResults<Article> recipeRealmResults = realm.where(Article.class).findAllSorted("id");
    
    return new ArrayList<>(realm.copyFromRealm(recipeRealmResults));
  }
  
  private void showProgress() {
    this.dataBinding
        .swipeRefreshLayoutArticleListPlaceholderError
        .setVisibility(View.GONE);
    this.dataBinding
        .swipeRefreshLayoutArticleListPlaceholderError
        .setRefreshing(false);
    
    this.dataBinding
        .swipeRefreshLayoutArticleListArticles
        .setRefreshing(true);
    this.dataBinding
        .swipeRefreshLayoutArticleListArticles
        .setVisibility(View.VISIBLE);
  }
  
  private void showArticles(ArrayList<Article> articles) {
    if ((articles == null) || (articles.size() < 1)) {
      this.showError();
    } else {
      if (this.articleAdapter != null) {
        this.articleAdapter.setArticles(articles);
      } else {
        this.articleAdapter = new ArticleAdapter(articles, this);
        this.dataBinding
            .recyclerViewArticleListArticles
            .setAdapter(this.articleAdapter);
      }
      
      this.dataBinding
          .swipeRefreshLayoutArticleListPlaceholderError
          .setRefreshing(false);
      this.dataBinding
          .swipeRefreshLayoutArticleListPlaceholderError
          .setVisibility(View.GONE);
      
      this.dataBinding
          .swipeRefreshLayoutArticleListArticles
          .setRefreshing(false);
      this.dataBinding
          .swipeRefreshLayoutArticleListArticles
          .setVisibility(View.VISIBLE);
    }
  }
  
  private void showError() {
    this.dataBinding
        .swipeRefreshLayoutArticleListPlaceholderError
        .setVisibility(View.VISIBLE);
    this.dataBinding
        .swipeRefreshLayoutArticleListPlaceholderError
        .setRefreshing(false);
    
    this.dataBinding
        .swipeRefreshLayoutArticleListArticles
        .setVisibility(View.GONE);
    
    this.dataBinding
        .swipeRefreshLayoutArticleListArticles
        .setRefreshing(false);
  }
}
