package com.example.novelsocial.Adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.novelsocial.R;
import com.example.novelsocial.interfaces.OnItemClickListener;
import com.example.novelsocial.models.Book;

import java.util.ArrayList;
import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.ViewHolder> {
    private List<Book> books;
    private final Context context;
    private OnItemClickListener listener;

    // Constructor for the BookAdapter class
    public BookAdapter(Context context, ArrayList<Book> allBooks) {
        books = allBooks;
        this.context = context;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public CardView cardView;
        public ImageView ivCover;
        public TextView tvTitle;
        public TextView tvAuthor;

        public ViewHolder(final View itemView, final OnItemClickListener clickListener) {
            super(itemView);

            cardView = itemView.findViewById(R.id.cv_list_item);
            ivCover = itemView.findViewById(R.id.iv_book_cover);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvAuthor = itemView.findViewById(R.id.tv_author);

            itemView.setOnClickListener((View view) -> clickListener.onItemClick(itemView, getAdapterPosition()));
        }
    }

    @NonNull
    @Override
    public BookAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View bookView = inflater.inflate(R.layout.book_list_item, parent, false);

        return new ViewHolder(bookView, listener);
    }

    @Override
    public void onBindViewHolder(BookAdapter.ViewHolder viewHolder, int position) {
        Book book = books.get(position);

        viewHolder.tvTitle.setText(book.getTitle());
        viewHolder.tvAuthor.setText(book.getAuthor());

        Glide.with(getContext())
                .load(Uri.parse(book.getBookCoverUrl()))
                .apply(new RequestOptions()
                        .placeholder(R.drawable.ic_nocover))
                .into(viewHolder.ivCover);

        viewHolder.cardView.startAnimation(AnimationUtils.loadAnimation(viewHolder.cardView.getContext(), R.anim.anim_sliding));
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    // Easy access to the context object in the Recycler View
    private Context getContext() {
        return context;
    }
}
