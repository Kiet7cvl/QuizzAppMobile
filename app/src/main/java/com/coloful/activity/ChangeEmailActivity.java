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

public class ChangeEmailActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText edtEmailChange;
    private Account account;
    private TextView msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_email);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#4257b0")));

        DataLocalManager.init(this);
        account = DataLocalManager.getAccount();

        edtEmailChange = findViewById(R.id.edit_email);
        msg = findViewById(R.id.tv_msg_change_email);
        edtEmailChange.setText(account.getEmail());
        findViewById(R.id.btn_cancel_change_email).setOnClickListener(this::onClick);
        findViewById(R.id.btn_save_change_email).setOnClickListener(this::onClick);
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
            case R.id.btn_cancel_change_email:
                intent = new Intent(this, MainActivity.class);
                intent.putExtra("backScreen", "Profile");
                startActivity(intent);
                break;
            case R.id.btn_save_change_email:

                String newEmail = edtEmailChange.getText().toString();

                Pattern pattern = Pattern.compile("^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$");
                if (pattern.matcher(newEmail).find()) {
                    if (newEmail.equalsIgnoreCase(account.getEmail())) {
                        msg.setText("Email not change, Please enter new email if you want to change email!");
                    } else {
                        AccountDao accountDao = new AccountDao();
                        if (accountDao.checkEmailExist(ChangeEmailActivity.this, newEmail)) {
                            msg.setText("Email already used!");
                        } else {
                            if (accountDao.updateEmail(ChangeEmailActivity.this, newEmail, account.getId())) {
                                msg.setText("Change email success!");
                                account.setEmail(newEmail);
                                DataLocalManager.setAccount(account);
                            } else {
                                msg.setText("Change email failed, please try again!");
                            }
                        }
                    }
                } else {
                    msg.setText("Email not valid");
                }
                break;

        }
    }
}