package org.wiki.assignment.imagesearch.fragment;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

/**
 * Created by me on 6/18/2017.
 */

public class ImageSearchFragment extends Fragment {

  /*  //private static final String TAG = GalleryFragment.class.getSimpleName();
    private static final int COLUMN = 3;
    private static final int ITEM_PER_PAGE = 50;


    private RequestQueue requestQueue;
    private RecyclerView recyclerView;
    private GridLayoutManager gridLayoutManager;
    //private CustomSwipeRefreshLayout mSwipeRefreshLayout;

     private ImageAdapter imageAdapter;

    private boolean loading = false;
    private boolean hasMore = true;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_images, container, false);
        requestQueue = Volley.newRequestQueue(getActivity());

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        gridLayoutManager = new GridLayoutManager(getActivity(), COLUMN);
        recyclerView.setLayoutManager(gridLayoutManager);
        imageAdapter = new ImageAdapter(getActivity(), new ArrayList<Image>());
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


        fetchImages();
        return view;
    }



    private void fetchImages() {
        loading = true;
        int totalItem = gridLayoutManager.getItemCount();
        final int page = totalItem / ITEM_PER_PAGE + 1;

        String query =  PreferenceManager
                            .getDefaultSharedPreferences(getActivity())
                            .getString(UrlManager.SEARCH_QUERY, null);

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


                                    result.add(new Image(imageurl));
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
                        // TODO Auto-generated method stub

                    }
                });
        requestQueue.add(jsObjRequest);
    }


    public void refresh() {
        imageAdapter.clear();
        fetchImages();
    }*/
}
