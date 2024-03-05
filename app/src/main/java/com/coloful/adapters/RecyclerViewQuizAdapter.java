package com.coloful.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.coloful.R;
import com.coloful.model.Quiz;

import java.util.List;

public class RecyclerViewQuizAdapter extends RecyclerView.Adapter<RecyclerViewQuizAdapter.MyViewHolder> {
    List<Quiz> quizList;
    private final OnItemClickListener listener;
    public interface OnItemClickListener {
        void onItemClick(Quiz item);
    }

    public RecyclerViewQuizAdapter(List<Quiz> quizList, OnItemClickListener listener) {
        this.quizList = quizList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_view_search_items, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Quiz q = quizList.get(position);
        holder.tvQuizTitle.setText(q.getTitle());
        holder.tvQuizTerm.setText(q.getQuestionList().size() + " Terms");
        holder.tvQuizAuthor.setText(q.getAuthor().getUsername());
        holder.bind(quizList.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return quizList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvQuizTitle, tvQuizTerm, tvQuizAuthor;

        MyViewHolder(View view) {
            super(view);
            View child = view.findViewById(R.id.view_search_item);
            if (child.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
                ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
                p.setMargins(15, 0, 15, 0);
                view.requestLayout();
            }
            tvQuizTitle = child.findViewById(R.id.tv_search_title);
            tvQuizTerm = child.findViewById(R.id.tv_search_term);
            tvQuizAuthor = child.findViewById(R.id.tv_search_author);
        }

        public void bind(final Quiz item, final OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }
    }

}
