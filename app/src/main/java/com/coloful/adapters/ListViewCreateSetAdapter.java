package com.coloful.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;

import com.coloful.R;
import com.coloful.model.Question;

import java.util.List;

public class ListViewCreateSetAdapter extends BaseAdapter {
    private final List<Question> itemList;
    private final Activity activity;

    public ListViewCreateSetAdapter(Activity activity, List<Question> itemList) {
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
        view = inflater.inflate(R.layout.list_view_create_set_item, null);
        EditText edt_term = (EditText) view.findViewById(R.id.txt_question);
        edt_term.setText(itemList.get(i).getContent());
        EditText edt_definition = (EditText) view.findViewById(R.id.txt_answer);
        edt_definition.setText(itemList.get(i).getAnswer());

        return view;
    }
}
