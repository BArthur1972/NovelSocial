package com.example.novelsocial.Adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.novelsocial.BookDetailsActivity;
import com.example.novelsocial.R;
import com.example.novelsocial.SearchActivity;
import com.example.novelsocial.clients.BookClient;
import com.example.novelsocial.models.Book;
import com.example.novelsocial.models.HomeChildItem;
import com.example.novelsocial.models.HomeParentItem;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.List;

import okhttp3.Headers;

public class HomeParentItemAdapter extends RecyclerView.Adapter<HomeParentItemAdapter.ViewHolder> {

    List<HomeParentItem> homeParentItemList;
    Context context;
    Book bookObject;

    public HomeParentItemAdapter(Context context, List<HomeParentItem> homeParentItemList) {
        this.context = context;
        this.homeParentItemList = homeParentItemList;
    }

    @NonNull
    @Override
    public HomeParentItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(context).inflate(R.layout.rv_parent_layout, null, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeParentItemAdapter.ViewHolder holder, int position) {
        HomeParentItem homeParentItem = homeParentItemList.get(position);
        holder.bind(homeParentItem);
    }

    @Override
    public int getItemCount() {
        return homeParentItemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RecyclerView rvChild;
        TextView tvParentTitle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            rvChild = itemView.findViewById(R.id.rv_child);
            tvParentTitle = itemView.findViewById(R.id.tv_parent);
        }

        public void bind(HomeParentItem item) {
            tvParentTitle.setText(item.getGenreTitle());

            HomeChildItemAdapter childItemAdapter;
            childItemAdapter = new HomeChildItemAdapter(context, item.getHomeChildItemList());

            // Set adapter on RecyclerView
            rvChild.setAdapter(childItemAdapter);

            // Set layout manager on RecyclerView
            rvChild.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));

            // Set OnItemClickListener on the child items(books) in the home fragment
            // When clicked, the BookDetailsActivity for that book should come up.
            childItemAdapter.setOnItemClickListener(((itemView, position) -> {
                HomeChildItem selectedBook = item.getHomeChildItemList().get(position);
                fetchBook(selectedBook.getOpenLibraryId());
            }));
        }
    }

    public void fetchBook(String bookId) {

        // Create BookSubjectClient object and call the getBooks method which makes the API call
        BookClient client = new BookClient();
        client.getBook(bookId, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON jsonResponse) {
                try {
                    JSONObject bookData;
                    if (jsonResponse != null) {
                        // Store JSON object representing a book
                        bookData = jsonResponse.jsonObject.getJSONObject("OLID:" + bookId);

                        // Parse the book JSON object and store the Book Object
                        bookObject = Book.fromJSONWithOpenLibraryId(bookData);

                        goToBookDetailsActivity();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.e(SearchActivity.class.getSimpleName(),
                        "Request failed with code: " + statusCode + " , Response message: " + response);
            }
        });
    }

    public void goToBookDetailsActivity() {
        Intent intent = new Intent(context, BookDetailsActivity.class);
        intent.putExtra("BookObject", Parcels.wrap(bookObject));
        context.startActivity(intent);
    }
}
