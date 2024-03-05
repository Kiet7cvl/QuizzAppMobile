package com.coloful.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.coloful.R;
import com.coloful.adapters.FragmentDialogHelper;
import com.coloful.dao.AccountDao;
import com.coloful.datalocal.DataLocalManager;
import com.coloful.model.Account;

import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

public class LogInActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageButton ImgBtnBack;
    private TextView tvFgUsername;
    private TextView tvFgPassword;
    private Button btnLogin;
    private EditText edtUsername;
    private EditText edtPassword;
    private TextView tvMsg;
    private AccountDao accountDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        getSupportActionBar().hide();

        DataLocalManager.init(this);

        ImgBtnBack = findViewById(R.id.ImgBtn_back);
        tvFgUsername = findViewById(R.id.tv_fg_username);
        tvFgPassword = findViewById(R.id.tv_fg_password);
        btnLogin = findViewById(R.id.btn_login);
        edtUsername = findViewById(R.id.edt_username_login);
        edtPassword = findViewById(R.id.edt_password_login);
        tvMsg = findViewById(R.id.tvMsg);
        System.out.println(DataLocalManager.getAccount());

        accountDao = new AccountDao();

        tvFgPassword.setOnClickListener(this::onClick);
        tvFgUsername.setOnClickListener(this::onClick);
        ImgBtnBack.setOnClickListener(this::onClick);
        btnLogin.setOnClickListener(this::onClick);
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        FragmentManager fm;
        FragmentDialogHelper popup;
        switch (view.getId()) {
            case R.id.btn_login:
                checkLogin();
                break;
            case R.id.ImgBtn_back:
                intent = new Intent(this, LandingActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_fg_username:
                fm = getSupportFragmentManager();
                popup = FragmentDialogHelper.newInstance("username");
                popup.show(fm, null);
                break;
            case R.id.tv_fg_password:
                fm = getSupportFragmentManager();
                popup = FragmentDialogHelper.newInstance("password");
                popup.show(fm, null);
                break;
        }
    }

    private void checkLogin() {
        String username = edtUsername.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();

        Account account = new Account(username, password);

        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            tvMsg.setText("Please enter username and password!");
            return;
        }
        Account isLogin = accountDao.checkAccount(this, account);
        if (Objects.isNull(isLogin)) {
            tvMsg.setText("Username or password is invalid!");
        } else {
            DataLocalManager.setAccount(isLogin);
            // init some quiz
//                QuizDao.initQuizData(LogInActivity.this);

            Intent main = new Intent(this, MainActivity.class);
            startActivity(main);
        }
    }
}