package com.example.mkhade.newyorktimessearch.activity;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.example.mkhade.newyorktimessearch.R;
import com.example.mkhade.newyorktimessearch.adapters.RecyclerViewArticleAdapter;
import com.example.mkhade.newyorktimessearch.fragments.SearchFilterDialogFragment;
import com.example.mkhade.newyorktimessearch.listeners.EndlessRecyclerViewScrollListener;
import com.example.mkhade.newyorktimessearch.listeners.EndlessScrollListener;
import com.example.mkhade.newyorktimessearch.models.Article;
import com.example.mkhade.newyorktimessearch.utils.Constants;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

import static com.example.mkhade.newyorktimessearch.activity.SearchActivity.Filters.newsDesk;

public class SearchActivity extends AppCompatActivity {

    //GridView gvResults;
    public static String storedString = "headlines";
    public static final String default_query = "";
    private EndlessRecyclerViewScrollListener scrollListener;
    ArrayList<Article> articles;
    RecyclerViewArticleAdapter adapter;

    @BindView(R.id.rvArticles) RecyclerView articlesRecyclerView;
    @BindView(R.id.toolbar) Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        setupViews();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);

        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                articles.clear();
                adapter.notifyDataSetChanged();
                storedString = query;
                // perform query here
                onArticleSearch(query, 0);
                // workaround to avoid issues with some emulators and keyboard devices firing twice if a keyboard enter is used
                // see https://code.google.com/p/android/issues/detail?id=24599
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Toast.makeText(getApplicationContext(),
                "Button is clicked", Toast.LENGTH_LONG).show();
        switch (item.getItemId()) {
            case R.id.action_filter:
                openFilterWindow();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void openFilterWindow() {
        SearchFilterDialogFragment searchFilterDialog = new SearchFilterDialogFragment();
        FragmentManager fm = getSupportFragmentManager();
        searchFilterDialog.show(fm, "filter");
    }

    public void setupViews() {
        setSupportActionBar(toolbar);
        setTitle("NewYork TImes News");

        articles = new ArrayList<>();
        adapter = new RecyclerViewArticleAdapter(this, articles);

        StaggeredGridLayoutManager gridLayoutManager = new StaggeredGridLayoutManager(Constants.RESULT_COLS, StaggeredGridLayoutManager.VERTICAL);
        gridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);

        articlesRecyclerView.setLayoutManager(gridLayoutManager);

        scrollListener = new EndlessRecyclerViewScrollListener(gridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                loadNextDataFromApi(page);
            }
        };
        articlesRecyclerView.addOnScrollListener(scrollListener);
        articlesRecyclerView.setAdapter(adapter);
    }

    public void loadNextDataFromApi(int offset) {
        Toast.makeText(this, "Loading more...", Toast.LENGTH_SHORT).show();
        onArticleSearch(storedString, offset);
    }

        //eg query: https://api.nytimes.com/svc/search/v2/articlesearch.json?begin_date=20160112
    // &sort=oldest&fq=news_desk:(%22Education%22%20%22Health%22)&api-key=227c750bb7714fc39ef1559ef1bd8329
    public RequestParams generateSearchQuery(String searchStr, int page){
        Toast.makeText(this, "Searching: " + searchStr, Toast.LENGTH_LONG).show();
        RequestParams params = new RequestParams();
        params.put("api-key", Constants.getApi_key());
        params.put("page", page);
        params.put("q", searchStr);
        params.put("sort", Filters.sortOrder);

        if (!TextUtils.isEmpty(Filters.beginDate.fromattedBeginDate)) {
            params.put("begin_date", Filters.beginDate.fromattedBeginDate);
        }

        if (newsDesk.size() > 0) {
            StringBuilder ndBuilder = new StringBuilder();
            ndBuilder.append("news_desk:(");
            if (newsDesk.size() > 1) {
                for (int i = 0; i < newsDesk.size() - 1; i++) {
                    ndBuilder.append(newsDesk.get(i).getNewsDesk());
                    ndBuilder.append(" ");
                }
                ndBuilder.append(newsDesk.get(newsDesk.size() - 1).getNewsDesk());
            } else {
                ndBuilder.append(newsDesk.get(0).getNewsDesk());
            }
            ndBuilder.append(")");

            //NEWS_DESK, TODO: better way to represent
            params.put("fq", ndBuilder.toString());
        }
        return params;
    }

/*   public void onArticleSearch(MenuItem mi) {
        onArticleSearch("latest", 0); //TODO: !!!!!!!!
    } */

    public void onNewArticleSearch() {
        articles.clear();
        adapter.notifyDataSetChanged();
        onArticleSearch(storedString, 0);
        adapter.notifyDataSetChanged();
    }

    public void onArticleSearch(String query, int page) {
        RequestParams params = generateSearchQuery(query, page);
        final AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(1000 * 1000);
        Toast.makeText(this, "LOADING page no: " + page, Toast.LENGTH_LONG).show();

        client.get(Constants.getUrl(), params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("DEBUG", response.toString());
                JSONArray articleJsonResults = null;
                try {
                    articleJsonResults = response.getJSONObject("response").getJSONArray("docs");
                    articles.addAll(Article.fromJSONArray(articleJsonResults));
                    Log.wtf("FFFFFDEBUG", articles.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject jsonObject) {
                Log.wtf("MKKK: ", "HTTP Request failed " + statusCode + " and " + throwable.getMessage());

                if(statusCode == 429){
                    onArticleSearch(storedString, 0);
                }
            }
        });
    }

    //TODO: Hacky! need to fix.
    @Override
    protected void onResume() {
        super.onResume();
     /*   if(storedString != null && !storedString.equals("null") && !storedString.isEmpty())
            onArticleSearch(storedString, 0);*/
        setTitle("LATEST NEWS");
    }

    //To persist the filters
    public static class Filters{
        public static BeginDate beginDate = new BeginDate("","");
        public static SortOrder sortOrder = SortOrder.NEWEST;
        public static ArrayList<NewsDesk> newsDesk = new ArrayList<>();
    }

    public static class BeginDate {
        public static String beginDateRaw;
        public static String fromattedBeginDate;

        public BeginDate(String beginDateRaw, String formattedBginDate){
            this.beginDateRaw = beginDateRaw;
            this.fromattedBeginDate = formattedBginDate;
        }
    }

    public enum SortOrder {
        OLDEST("Oldest", 0), NEWEST("Newest", 1);
        public String sortOrder;
        public int id;

        SortOrder(String so, int id) {
            this.sortOrder = so;
            this.id = id;
        }
    }

    public enum NewsDesk {
        ARTS("Arts"), FASHION_AND_STYLR("Fashion & Style"), SPORTS("Sports");
        private String newsDesk;

        NewsDesk(String nd) {
            this.newsDesk = nd;
        }

        public String getNewsDesk() { return newsDesk; }
    }
}