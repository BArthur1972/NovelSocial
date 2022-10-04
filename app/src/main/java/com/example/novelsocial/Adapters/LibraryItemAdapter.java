package com.example.novelsocial.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.novelsocial.R;
import com.example.novelsocial.models.LibraryItem;

import java.util.List;

public class LibraryItemAdapter extends RecyclerView.Adapter<LibraryItemAdapter.ViewHolder> {
    private Context context;
    private List<LibraryItem> libraryItemList;

    public LibraryItemAdapter(Context context, List<LibraryItem> libraryItemList) {
        this.context = context;
        this.libraryItemList = libraryItemList;
    }

    @NonNull
    @Override
    public LibraryItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.library_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LibraryItemAdapter.ViewHolder holder, int position) {
        LibraryItem libraryItem = libraryItemList.get(position);
        holder.bind(libraryItem);
    }

    @Override
    public int getItemCount() {
        return libraryItemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView author;
        private TextView title;
        private ImageView bookCover;
        private Button removeButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            author = itemView.findViewById(R.id.tv_library_author_name);
            title = itemView.findViewById(R.id.tv_library_book_title);
            bookCover = itemView.findViewById(R.id.iv_library_book_cover);
            removeButton = itemView.findViewById(R.id.bt_remove_from_library);
        }

        public void bind(LibraryItem item) {
            author.setText(item.getAuthorName());
            title.setText(item.getBookTitle());
            String image = item.getCoverUrl();

            if (image != null) {
                Glide.with(context).load(image).into(bookCover);
            }

            removeButton.setOnClickListener(view -> item.deleteInBackground(e -> {
                if (e != null) {
                    Toast.makeText(context, "Failed to Delete", Toast.LENGTH_SHORT).show();
                    Log.e(getClass().getSimpleName(), e.getMessage());
                } else {
                    int currentItem = getAdapterPosition();
                    libraryItemList.remove(currentItem);
                    notifyItemRemoved(currentItem);
                }
            }));
        }
    }
}
