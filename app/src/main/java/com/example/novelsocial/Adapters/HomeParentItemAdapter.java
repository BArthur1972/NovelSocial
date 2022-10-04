package com.example.novelsocial.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.novelsocial.R;
import com.example.novelsocial.models.HomeParentItem;

import java.util.List;

public class HomeParentItemAdapter extends RecyclerView.Adapter<HomeParentItemAdapter.ViewHolder> {

    List<HomeParentItem> homeParentItemList;
    Context context;

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
        }
    }
}
