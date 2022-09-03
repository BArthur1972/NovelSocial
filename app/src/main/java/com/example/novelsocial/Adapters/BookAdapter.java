package com.example.novelsocial.Adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.novelsocial.R;
import com.example.novelsocial.models.Book;

import java.util.ArrayList;
import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.ViewHolder> {
    private List<Book> mBooks;
    private Context mContext;
    private OnItemClickListener mListener;

    // Constructor for the BookAdapter class
    public BookAdapter(Context context, ArrayList<Book> allBooks) {
        mBooks = allBooks;
        mContext = context;
    }

    public void setFilteredList(List<Book> filteredList) {
        this.mBooks = filteredList;
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
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

            itemView.setOnClickListener((View view) -> {
                clickListener.onItemClick(itemView, getAdapterPosition());
            });
        }
    }


    @Override
    public BookAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View bookView = inflater.inflate(R.layout.book_list_item, parent, false);

        BookAdapter.ViewHolder viewHolder = new BookAdapter.ViewHolder(bookView, mListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(BookAdapter.ViewHolder viewHolder, int position) {
        Book book = mBooks.get(position);

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
        return mBooks.size();
    }

    // Easy access to the context object in the recyclerview
    private Context getContext() {
        return mContext;
    }
}
