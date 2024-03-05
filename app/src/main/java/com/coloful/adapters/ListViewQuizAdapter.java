package com.coloful.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.coloful.R;
import com.coloful.model.Quiz;

import java.util.List;

public class ListViewQuizAdapter extends BaseAdapter {
    private List<Quiz> itemList;
    private Activity activity;

    public ListViewQuizAdapter(Activity activity, List<Quiz> itemList) {
        this.activity = activity;
        this.itemList = itemList;
    }

    @Override
    public int getCount() {
        return itemList.size();
    }

    @Override
    public Object getItem(int i) {
        return itemList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = activity.getLayoutInflater();
        view = inflater.inflate(R.layout.list_view_search_items, null);
        View child = view.findViewById(R.id.view_search_item);

        TextView tvQuizTitle = child.findViewById(R.id.tv_search_title);
        tvQuizTitle.setText(itemList.get(i).getTitle());

        TextView tvQuizTerm = child.findViewById(R.id.tv_search_term);
        tvQuizTerm.setText(itemList.get(i).getQuestionList().size() + " Terms");

        TextView tvQuizAuthor = child.findViewById(R.id.tv_search_author);
        tvQuizAuthor.setText(itemList.get(i).getAuthor().getUsername());
        return view;
    }
}
