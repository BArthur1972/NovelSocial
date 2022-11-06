package com.example.novelsocial.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.novelsocial.R;
import com.example.novelsocial.interfaces.OnItemClickListener;
import com.example.novelsocial.models.HomeChildItem;

import java.util.List;

public class HomeChildItemAdapter extends RecyclerView.Adapter<HomeChildItemAdapter.ViewHolder> {

    private Context context;
    private List<HomeChildItem> homeChildItemList;
    private OnItemClickListener listener;

    public HomeChildItemAdapter(Context context, List<HomeChildItem> homeChildItemList) {
        this.context = context;
        this.homeChildItemList = homeChildItemList;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.rv_child_layout, parent, false);
        return new ViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            HomeChildItem homeChildItem = homeChildItemList.get(position);
            holder.bind(homeChildItem);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private ImageView bookCover;

        public ViewHolder(@NonNull View itemView, OnItemClickListener listener) throws NullPointerException {
            super(itemView);

            title = itemView.findViewById(R.id.tv_child_list_item_author);
            bookCover = itemView.findViewById(R.id.iv_child_list_item_cover);

            if (listener == null) {
                throw new NullPointerException("Listener is null");
            }

            itemView.setOnClickListener((View view) -> listener.onItemClick(itemView, getAdapterPosition()));
        }

        public void bind(HomeChildItem item) {
            title.setText(item.getBookTitle());
            String image = item.getBookCoverUrl();

            if (image != null) {
                Glide.with(getContext()).load(image).into(bookCover);
            }
        }
    }

    @Override
    public int getItemCount() {
        return homeChildItemList.size();
    }

    // Easy access to the context object in the Recycler View
    private Context getContext() {
        return context;
    }
}
