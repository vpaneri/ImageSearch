package org.wiki.assignment.imagesearch.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.wiki.assignment.imagesearch.R;
import org.wiki.assignment.imagesearch.model.Image;
import org.wiki.assignment.imagesearch.util.ImageAdapter;
import org.wiki.assignment.imagesearch.util.UrlManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ImageSearchActivity extends AppCompatActivity {
    private  EditText editText;
    private  SharedPreferences sharePref;
    private static final String TAG = ImageSearchActivity.class.getSimpleName();
    private Toolbar toolbar;
    private static final int COLUMN = 3;
    private static final int ITEM_PER_PAGE = 50;


    private RequestQueue requestQueue;
    private RecyclerView recyclerView;
    private GridLayoutManager gridLayoutManager;
    private Timer timer;


    private ImageAdapter imageAdapter;

    private boolean loading = false;
    private boolean hasMore = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_image_search);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        sharePref = PreferenceManager.getDefaultSharedPreferences(this);
        editText = (EditText)findViewById(R.id.search_image);

        requestQueue = Volley.newRequestQueue(this);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        gridLayoutManager = new GridLayoutManager(this, COLUMN);
        recyclerView.setLayoutManager(gridLayoutManager);
        imageAdapter = new ImageAdapter(this, new ArrayList<Image>());
        recyclerView.setAdapter(imageAdapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int totalItem = gridLayoutManager.getItemCount();
                int lastItemPos = gridLayoutManager.findLastVisibleItemPosition();
                if (hasMore && !loading && totalItem - 1 != lastItemPos) {
                    fetchImages();
                }
            }
        });


        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (timer != null) {
                    timer.cancel();
                    requestQueue.cancelAll(TAG);
                    imageAdapter.clear();
                }
            }

            @Override
            public void afterTextChanged(final Editable editable) {

                timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {

                        String query = editable.toString();
                        imageAdapter.clear();
                        fetchImages();
                    }
                }, 900);

            }
        });
    }





    private void fetchImages() {
        loading = true;
        int totalItem = gridLayoutManager.getItemCount();
        final int page = totalItem / ITEM_PER_PAGE + 1;

        String query  = editText.getText().toString();
        String url =  UrlManager.getInstance().getItemUrl(query);
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        List<Image> result = new ArrayList<Image>();
                        try {
                            JSONObject queryResult = response.getJSONObject("query");
                            JSONObject pages = queryResult.getJSONObject("pages");
                            JSONArray pagesArr = pages.names();
                            for (int i = 0; i < pagesArr.length(); i++) {
                                JSONObject itemObj = pages.getJSONObject((String) pagesArr.get(i));
                                if(itemObj.has("thumbnail")) {
                                    JSONObject thumbnail = itemObj.getJSONObject("thumbnail");
                                    String imageurl = (String) thumbnail.get("source");
                                    result.add(new Image(imageurl, (String)pagesArr.get(i)));
                                }
                            }

                        } catch (JSONException e) {
                            System.out.print(e.getStackTrace());
                        }
                        imageAdapter.addAll(result);
                        imageAdapter.notifyDataSetChanged();
                        loading = false;
                        //mSwipeRefreshLayout.refreshComplete();
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String json = null;
                        NetworkResponse response = error.networkResponse;
                        if(response != null && response.data != null){
                            switch(response.statusCode){
                                case 400:
                                    json = new String(response.data);
                                    json = trimMessage(json, "message");
                                    if(json != null) displayMessage(json);
                                    break;
                            }
                            //TODO: Handle additional error cases
                        }

                    }
                });
        jsObjRequest.setTag(TAG);
        requestQueue.add(jsObjRequest);
    }


    public String trimMessage(String json, String key){
        String trimmedString = null;

        try{
            JSONObject obj = new JSONObject(json);
            trimmedString = obj.getString(key);
        } catch(JSONException e){
            e.printStackTrace();
            return null;
        }

        return trimmedString;
    }


    public void displayMessage(String toastString){
        Toast.makeText(this.getBaseContext(), toastString, Toast.LENGTH_LONG).show();
    }
}



