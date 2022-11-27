package com.example.novelsocial.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.novelsocial.databinding.CommentItemBinding;
import com.example.novelsocial.models.BookComments;
import com.parse.ParseException;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {

    private Context context;
    private List<BookComments> bookComments;

    public CommentAdapter(Context context, List<BookComments> bookComments){
        this.context = context;
        this.bookComments = bookComments;
    }

    @NonNull
    @Override
    public CommentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        CommentItemBinding binding = CommentItemBinding.inflate(layoutInflater, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentAdapter.ViewHolder holder, int position) {
        BookComments comment = bookComments.get(position);
        try {
            holder.bind(comment);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return bookComments.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CommentItemBinding binding;
        TextView comment;
        TextView createdAt;
        TextView user;

        public ViewHolder(@NonNull CommentItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            comment = binding.tvComment;
            createdAt = binding.tvCreatedAt;
            user = binding.tvUsername;
        }

        public void bind(BookComments bookComments) throws ParseException {
            comment.setText(bookComments.getComment());
            createdAt.setText(bookComments.getCreatedAtAsString());
            user.setText(bookComments.getUser());
        }
    }
}
