package com.coloful.activity;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.coloful.R;
import com.coloful.dao.QuizDao;
import com.coloful.model.Question;
import com.coloful.model.Quiz;

import java.util.ArrayList;
import java.util.List;

public class FlashcardActivity extends AppCompatActivity implements View.OnClickListener {

    ImageButton imgExist, imgPrevious, imgNext;
    TextView tvFlashcardNumber, tvBack, tvFront;
    List<Question> questionList = new ArrayList<>();
    Question currentQues = new Question();
    int quizId;
    int posPre, posNext;
    String screen;

    private AnimatorSet mSetRightOut;
    private AnimatorSet mSetLeftIn;
    private boolean mIsBackVisible = false;
    private View mCardFrontLayout;
    private View mCardBackLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flashcard);
        getSupportActionBar().hide();

        findViews();
        loadAnimations();
        changeCameraDistance();

        quizId = getIntent().getIntExtra("quizId", 0);
        screen = getIntent().getStringExtra("screen");

        QuizDao dao = new QuizDao();
        Quiz q = dao.getQuizById(this, quizId);
        questionList = q.getQuestionList();
        currentQues = questionList.get(0);
        posPre = 0;
        posNext = 1;

        imgExist = findViewById(R.id.img_exist);
        imgPrevious = findViewById(R.id.img_flash_pre);
        imgNext = findViewById(R.id.img_flash_next);
        tvFlashcardNumber = findViewById(R.id.tv_flash_card_number);

        tvBack = findViewById(R.id.tv_back);
        tvFront = findViewById(R.id.tv_front);
        tvFront.setText(currentQues.getContent());
        tvBack.setText(currentQues.getAnswer());

        tvFlashcardNumber.setText(posNext + "/" + questionList.size());
        imgPrevious.setVisibility(View.INVISIBLE);

        imgExist.setOnClickListener(this::onClick);
        imgPrevious.setOnClickListener(this::onClick);
        imgNext.setOnClickListener(this::onClick);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_exist:
                backScreen(screen);
                break;
            case R.id.img_flash_pre:
                imgNext.setVisibility(View.VISIBLE);
                currentQues = questionList.get(posPre - 1);
                tvFlashcardNumber.setText(posPre + "/" + questionList.size());
                tvFront.setText(currentQues.getContent());
                tvBack.setText(currentQues.getAnswer());
                posNext = posPre;
                posPre--;
                if (posPre == 0) {
                    imgPrevious.setVisibility(View.INVISIBLE);
                }
                break;
            case R.id.img_flash_next:
                imgPrevious.setVisibility(View.VISIBLE);
                currentQues = questionList.get(posNext);
                tvFront.setText(currentQues.getContent());
                tvBack.setText(currentQues.getAnswer());
                posNext++;
                tvFlashcardNumber.setText(posNext + "/" + questionList.size());
                posPre = posNext - 1;
                if (posNext == questionList.size()) {
                    imgNext.setVisibility(View.INVISIBLE);
                }
                break;
        }
    }

    private void backScreen(String screen) {
        Intent intent;
        switch (screen) {
            case "study details":
                intent = new Intent(this, StudySetDetailsActivity.class);
                intent.putExtra("screen", "home");
                intent.putExtra("quizId", quizId);
                startActivity(intent);
                break;
            case "option practice":
                intent = new Intent(this, OptionPracticeActivity.class);
                intent.putExtra("quizId", quizId);
                startActivity(intent);
                break;

        }
    }

    public void flipCard(View view) {
        if (!mIsBackVisible) {
            mCardBackLayout.setVisibility(View.VISIBLE);
            mSetRightOut.setTarget(mCardFrontLayout);
            mSetLeftIn.setTarget(mCardBackLayout);
            mSetRightOut.start();
            mSetLeftIn.start();
            mIsBackVisible = true;
        } else {
            mSetRightOut.setTarget(mCardBackLayout);
            mSetLeftIn.setTarget(mCardFrontLayout);
            mSetRightOut.start();
            mSetLeftIn.start();
            mIsBackVisible = false;
        }
    }

    private void changeCameraDistance() {
        int distance = 8000;
        float scale = getResources().getDisplayMetrics().density * distance;
        mCardFrontLayout.setCameraDistance(scale);
        mCardBackLayout.setCameraDistance(scale);
    }

    private void loadAnimations() {
        mSetRightOut = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.animation_out);
        mSetLeftIn = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.animation_in);
    }

    private void findViews() {
        mCardBackLayout = findViewById(R.id.card_back);
        mCardBackLayout.setVisibility(View.INVISIBLE);
        mCardFrontLayout = findViewById(R.id.card_front);
    }
}