package com.coloful.activity;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.app.NotificationCompat;

import com.coloful.R;
import com.coloful.dao.QuizDao;
import com.coloful.datalocal.DataLocalManager;
import com.coloful.model.Account;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class CreateStudySetActivity extends AppCompatActivity {
    private final String NOTIFICATION_CHANNEL_ID = "CreateSuccessStudySet";
    private final String NOTIFICATION_CHANNEL_NAME = "Create Study Set Notifications";
    private Button btnSave;
    private EditText edtCreateQuizTitle;
    private Account account;
    private ImageButton buttonAdd;
    private LinearLayout list;
    private ArrayAdapter<String> arrayAdapter;
    private Context context;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_study_set);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#4257b0")));
        context = this;

        edtCreateQuizTitle = findViewById(R.id.edt_create_quiz_title);
        list = findViewById(R.id.list);
        btnSave = findViewById(R.id.btn_save_set);

        buttonAdd = findViewById(R.id.add);
        account = DataLocalManager.getAccount();

        addItem();
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = edtCreateQuizTitle.getText().toString().trim().toUpperCase();
                if (StringUtils.isEmpty(title)) {
                    Toast.makeText(context, "Set's title can not null, please check again!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (list.getChildCount() < 1) {
                    Toast.makeText(context, "To create a set requires at least one question, please check again!", Toast.LENGTH_SHORT).show();
                    return;
                }

                QuizDao db = new QuizDao();
                if (db.isQuizExistByTitle(context, title)) {
                    Toast.makeText(context, "Title is existed", Toast.LENGTH_SHORT).show();
                    return;
                }

                Map<String, String> map = new HashMap<>();

                for (int i = 0; i < list.getChildCount(); i++) {
                    if (list.getChildAt(i) instanceof LinearLayoutCompat) {
                        LinearLayoutCompat ll = (LinearLayoutCompat) list.getChildAt(i);
                        EditText edtAnswer = ll.findViewById(R.id.edt_answer);
                        for (int j = 0; j < ll.getChildCount(); j++) {
                            if (ll.getChildAt(j) instanceof EditText) {
                                EditText et = (EditText) ll.getChildAt(j);
                                if (et.getId() == R.id.edt_question) {
                                    if (isEmptyQuestionAndAnswer(et.getText().toString(), edtAnswer.getText().toString())) {
                                        TextView tvError = ll.findViewById(R.id.tvErrorMsg);
                                        tvError.setText("Term and definition cannot be empty!!");
                                        return;
                                    }
                                    map.put(et.getText().toString().trim(), edtAnswer.getText().toString().trim());
                                }
                            }
                        }
                    }
                }
                Long qId = db.addQuiz(CreateStudySetActivity.this, account.getId().toString(), title);
                for (String ques : map.keySet()) {
                    db.addQuestion(CreateStudySetActivity.this, ques, qId, map.get(ques));
                }
                pushNotification(edtCreateQuizTitle.getText().toString(), Math.toIntExact(qId));
                Intent intent = new Intent(view.getContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        arrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line);
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addItem();
            }
        });
    }

    private boolean isEmptyQuestionAndAnswer(String question, String answer) {
        return StringUtils.isEmpty(question) || StringUtils.isEmpty(answer);
    }

    public void addItem() {
        LayoutInflater layoutInflater =
                (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View addView = layoutInflater.inflate(R.layout.list_view_create_set_item, null);
        LinearLayoutCompat.LayoutParams params = new LinearLayoutCompat.LayoutParams(
                LinearLayoutCompat.LayoutParams.MATCH_PARENT,
                LinearLayoutCompat.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, 15, 0, 15);
        addView.setLayoutParams(params);

        list.addView(addView);
        Button buttonRemove = addView.findViewById(R.id.remove);
        final View.OnClickListener thisListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((LinearLayout) addView.getParent()).removeView(addView);
            }
        };
        buttonRemove.setOnClickListener(thisListener);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                MainActivity mainActivity = new MainActivity();
                Intent intent = new Intent(this, mainActivity.getClass());
                intent.putExtra("backScreen", "Home");
                startActivity(intent);
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void pushNotification(String data, int quizId) {
        Intent openActivity = new Intent(this, StudySetDetailsActivity.class);
        openActivity.putExtra("screen", "home");
        openActivity.putExtra("quizId", quizId);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 10, openActivity,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        Notification notification = builder
                .setSmallIcon(R.drawable.ic_baseline_add_circle_24)
                .setContentTitle("Congrats! Your quiz " + edtCreateQuizTitle.getText() + " is created successfully")
                .setContentText(data)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setPriority(Notification.DEFAULT_LIGHTS)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(data))
                .build();
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    NOTIFICATION_CHANNEL_ID,
                    NOTIFICATION_CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(2, notification);
    }
}