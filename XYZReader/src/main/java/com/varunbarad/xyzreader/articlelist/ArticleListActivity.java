package com.varunbarad.xyzreader.articlelist;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.varunbarad.xyzreader.R;
import com.varunbarad.xyzreader.articledetails.ArticleDetailActivity;
import com.varunbarad.xyzreader.data.ArticleLoader;
import com.varunbarad.xyzreader.data.UpdaterService;
import com.varunbarad.xyzreader.databinding.ActivityArticleListBinding;

/**
 * An activity representing a list of Articles. This activity has different presentations for
 * handset and tablet-size devices. On handsets, the activity presents a list of items, which when
 * touched, lead to a {@link ArticleDetailActivity} representing item details. On tablets, the
 * activity presents a grid of items as cards.
 */
public class ArticleListActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
  private ActivityArticleListBinding dataBinding;
  
  private boolean mIsRefreshing = false;
  
  private BroadcastReceiver mRefreshingReceiver = new BroadcastReceiver() {
    @Override
    public void onReceive(Context context, Intent intent) {
      if (UpdaterService.BROADCAST_ACTION_STATE_CHANGE.equals(intent.getAction())) {
        mIsRefreshing = intent.getBooleanExtra(UpdaterService.EXTRA_REFRESHING, false);
        updateRefreshingUI();
      }
    }
  };
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    this.dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_article_list);
  
    this.dataBinding.swipeRefreshLayoutArticleList.setRefreshing(true);
    getSupportLoaderManager().initLoader(0, null, this);

    if (savedInstanceState == null) {
      refresh();
    }
  }
  
  private void refresh() {
    startService(new Intent(this, UpdaterService.class));
  }
  
  @Override
  protected void onStart() {
    super.onStart();
    registerReceiver(mRefreshingReceiver,
        new IntentFilter(UpdaterService.BROADCAST_ACTION_STATE_CHANGE));
  }
  
  @Override
  protected void onStop() {
    super.onStop();
    unregisterReceiver(mRefreshingReceiver);
  }
  
  private void updateRefreshingUI() {
    this
        .dataBinding
        .swipeRefreshLayoutArticleList
        .setRefreshing(mIsRefreshing);
  }
  
  @Override
  public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
    return ArticleLoader.newAllArticlesInstance(this);
  }
  
  @Override
  public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
    ArticleAdapter adapter = new ArticleAdapter(cursor, this);
    adapter.setHasStableIds(true);
    this
        .dataBinding
        .recyclerViewArticleListArticles
        .setAdapter(adapter);
    
    int columnCount = getResources().getInteger(R.integer.list_column_count);
    StaggeredGridLayoutManager layoutManager =
        new StaggeredGridLayoutManager(columnCount, StaggeredGridLayoutManager.VERTICAL);
  
    this
        .dataBinding
        .recyclerViewArticleListArticles
        .setLayoutManager(layoutManager);
  }
  
  @Override
  public void onLoaderReset(Loader<Cursor> loader) {
    this
        .dataBinding
        .recyclerViewArticleListArticles
        .setAdapter(null);
  }
}
