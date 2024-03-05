package com.coloful.fragments;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.coloful.R;
import com.coloful.activity.StudySetDetailsActivity;
import com.coloful.adapters.ListViewQuizAdapter;
import com.coloful.dao.QuizDao;
import com.coloful.datalocal.DataLocalManager;
import com.coloful.model.Quiz;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private final List<Quiz> quizList = new ArrayList<>();
    private EditText edtSearch;
    private TextView tvNoti;
    private ListView lvSearch;
    private ListViewQuizAdapter adapter;
    private List<Quiz> quizShow = new ArrayList<>();
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SearchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        edtSearch = view.findViewById(R.id.edt_search_set);
        tvNoti = view.findViewById(R.id.tv_search_noti);

        lvSearch = view.findViewById(R.id.lv_search);
        QuizDao dao = new QuizDao();

        quizList.addAll(dao.allQuiz(getContext(), DataLocalManager.getAccount().getId()));

        
        quizShow = getQuizSearch("");
        adapter = new ListViewQuizAdapter(getActivity(), quizShow);
        lvSearch.setAdapter(adapter);
        edtSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                tvNoti.setText("Search quiz you want study");
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                quizShow = getQuizSearch(charSequence.toString());
                adapter = new ListViewQuizAdapter(getActivity(), quizShow);
                lvSearch.setAdapter(adapter);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (quizShow.isEmpty()) {
                    tvNoti.setText("Can't find the quiz you want!");
                }
            }
        });

        lvSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), StudySetDetailsActivity.class);
                intent.putExtra("screen", "search");
                intent.putExtra("quizId", quizShow.get(i).getId());
                startActivity(intent);
            }
        });

        // Inflate the layout for this fragment
        return view;
    }

    private List<Quiz> getQuizSearch(String charSequence) {
        if (StringUtils.isEmpty(charSequence)) {
//            return new ArrayList<>();
            return  quizList;
        }
        return quizList.stream().filter(q ->
                q.getTitle().toLowerCase().contains(charSequence.toLowerCase())
        ).collect(Collectors.toList());
    }
}
