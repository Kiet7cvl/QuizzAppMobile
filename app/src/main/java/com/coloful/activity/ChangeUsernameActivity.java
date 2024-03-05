package com.coloful.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.coloful.R;
import com.coloful.dao.AccountDao;
import com.coloful.datalocal.DataLocalManager;
import com.coloful.model.Account;

import java.util.regex.Pattern;

public class ChangeUsernameActivity extends AppCompatActivity implements View.OnClickListener {

    EditText edUsername;
    Account account;
    TextView msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_username);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#4257b0")));

        DataLocalManager.init(this);
        account = DataLocalManager.getAccount();

        msg = findViewById(R.id.tv_msg_change_username);
        edUsername = findViewById(R.id.edit_username);
        edUsername.setText(account.getUsername());
        findViewById(R.id.btn_cancel_change_username).setOnClickListener(this::onClick);
        findViewById(R.id.btn_save_change_username).setOnClickListener(this::onClick);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                MainActivity mainActivity = new MainActivity();
                Intent intent = new Intent(this, mainActivity.getClass());
                intent.putExtra("backScreen", "Profile");
                startActivity(intent);
                return true;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.btn_cancel_change_username:
                intent = new Intent(this, MainActivity.class);
                intent.putExtra("backScreen", "Profile");
                startActivity(intent);
                break;
            case R.id.btn_save_change_username:
                String newUsername = edUsername.getText().toString();
                Pattern pattern = Pattern.compile("^\\w{8,32}$");
                if (pattern.matcher(newUsername).find()) {
                    if (newUsername.equalsIgnoreCase(account.getUsername())) {
                        msg.setText("Username not change, please enter new username if user want to change!");
                    } else {
                        AccountDao accountDao = new AccountDao();
                        if (accountDao.checkUsernameExisted(ChangeUsernameActivity.this, newUsername) != null) {
                            msg.setText("Username existed. Choose other username!");
                        } else {
                            if (accountDao.updateUsername(ChangeUsernameActivity.this, newUsername, account.getId())) {
                                msg.setText("Change username success!");
                                account.setUsername(newUsername);
                                DataLocalManager.setAccount(account);
                            } else {
                                msg.setText("Change username failed, please try again!");
                            }
                        }
                    }
                } else {
                    msg.setText("Username must have 8 to 32 character a-zA-Z0-9");
                }
                break;
        }
    }
}