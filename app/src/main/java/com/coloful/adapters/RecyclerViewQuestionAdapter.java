package com.coloful.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.coloful.R;
import com.coloful.model.Question;

import java.util.List;

public class RecyclerViewQuestionAdapter extends RecyclerView.Adapter<RecyclerViewQuestionAdapter.MyViewHolder> {
    List<Question> questionList;

    public RecyclerViewQuestionAdapter(List<Question> questionList) {
        this.questionList = questionList;
    }

    @NonNull
    @Override
    public RecyclerViewQuestionAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.listview_study_set_item, parent, false);
        return new RecyclerViewQuestionAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewQuestionAdapter.MyViewHolder holder, int position) {
        Question q = questionList.get(position);
        holder.tvQuestion.setText(q.getContent() + "\n" + q.getAnswer());
    }

    @Override
    public int getItemCount() {
        return questionList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvQuestion;

        MyViewHolder(View view) {
            super(view);
            tvQuestion = view.findViewById(R.id.tvQuestion);
        }
    }
}
