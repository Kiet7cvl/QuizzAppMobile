package com.coloful.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.coloful.R;
import com.coloful.activity.StudySetDetailsActivity;
import com.coloful.adapters.RecyclerViewQuizAdapter;
import com.coloful.dao.QuizDao;
import com.coloful.datalocal.DataLocalManager;
import com.coloful.model.Account;
import com.coloful.model.Quiz;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private RecyclerView rcQuizHome, rcYourSet;
    private List<Quiz> quizRecently = new ArrayList<>();
    private List<Quiz> yourSet = new ArrayList<>();
    private TextView tvQuizRecently, tvYourSet, tvInStruction;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        rcQuizHome = view.findViewById(R.id.rc_quiz_home);
        rcYourSet = view.findViewById(R.id.rc_your_quiz_home);
        tvQuizRecently = view.findViewById(R.id.tv_quiz_recently);
        tvYourSet = view.findViewById(R.id.tv_your_set);
        tvInStruction = view.findViewById(R.id.tv_home_instruction);

        QuizDao dao = new QuizDao();
        Account account = DataLocalManager.getAccount();
        quizRecently = dao.getQuizRecently(getContext(), account);
        yourSet = dao.getYourQuiz(getContext(), account);

        if ((quizRecently == null && yourSet == null)
                || (quizRecently.isEmpty() && yourSet.isEmpty())) {

            System.err.println("null or empty all!");

            tvQuizRecently.setVisibility(View.INVISIBLE);
            tvYourSet.setVisibility(View.INVISIBLE);
            String instruc = tvInStruction.getText().toString();
            instruc += " If you want to learn, choose search icon on navigation bar and find set you want!";
            tvInStruction.setText(instruc);
        }
        if (quizRecently == null || quizRecently.isEmpty()) {
            System.err.println("recently null or empty!");

            tvQuizRecently.setVisibility(View.INVISIBLE);
        }
        if (yourSet == null || yourSet.isEmpty()) {
            System.err.println("set null or empty!");
            tvYourSet.setVisibility(View.INVISIBLE);
        }
        if (!quizRecently.isEmpty()) {
            System.err.println("recent have data");

            LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
            mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            rcQuizHome.setLayoutManager(mLayoutManager);
            rcQuizHome.setAdapter(new RecyclerViewQuizAdapter(quizRecently, new RecyclerViewQuizAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(Quiz item) {
                            Toast.makeText(getContext(), item.getTitle() + " Clicked", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getActivity(), StudySetDetailsActivity.class);
                            intent.putExtra("screen", "home");
                            intent.putExtra("quizId", item.getId());
                            startActivity(intent);
                        }
                    })
            );
        }
        if (!yourSet.isEmpty()) {
            System.err.println("set have data");
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
            mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            rcYourSet.setLayoutManager(mLayoutManager);
            rcYourSet.setAdapter(new RecyclerViewQuizAdapter(yourSet, new RecyclerViewQuizAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(Quiz item) {
                            Toast.makeText(getContext(), item.getTitle() + " Clicked", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getActivity(), StudySetDetailsActivity.class);
                            intent.putExtra("screen", "home");
                            intent.putExtra("quizId", item.getId());
                            startActivity(intent);
                        }
                    })
            );
        }
        return view;
    }
}