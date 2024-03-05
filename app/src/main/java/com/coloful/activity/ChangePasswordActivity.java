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

public class ChangePasswordActivity extends AppCompatActivity implements View.OnClickListener {

    EditText editOldPass;
    EditText edtNewPassword;
    EditText edtPasswordCf;
    TextView msg;
    Account account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#4257b0")));

        DataLocalManager.init(this);
        account = DataLocalManager.getAccount();

        editOldPass = findViewById(R.id.edit_old_password);
        edtNewPassword = findViewById(R.id.edit_new_password);
        edtPasswordCf = findViewById(R.id.edit_cf_new_password);
        msg = findViewById(R.id.tv_msg_change_pass);

        findViewById(R.id.btn_cancel_change_pass).setOnClickListener(this::onClick);
        findViewById(R.id.btn_save_change_pass).setOnClickListener(this::onClick);
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
            case R.id.btn_cancel_change_pass:
                intent = new Intent(this, MainActivity.class);
                intent.putExtra("backScreen", "Profile");
                startActivity(intent);
                break;
            case R.id.btn_save_change_pass:
                AccountDao accountDao = new AccountDao();
                String oldPass = editOldPass.getText().toString();
                String newPass = edtNewPassword.getText().toString();
                String newPassCf = edtPasswordCf.getText().toString();
                Pattern pattern = Pattern.compile("^\\w{8,32}$");
                if (pattern.matcher(newPass).find() && pattern.matcher(newPassCf).find()) {
                    if (oldPass == null || oldPass.trim().length() == 0) {
                        msg.setText("Please check your old password!");
                    } else if (accountDao.checkAccount(this, new Account(account.getUsername(), oldPass)) == null) {
                        msg.setText("Old password is wrong, please check again!");
                    } else {
                        if (newPass == null || newPass.trim().length() == 0 || newPassCf == null || newPassCf.trim().length() == 0) {
                            msg.setText("New password or password confirm is not empty!");
                        } else if (!newPass.equals(newPassCf)) {
                            msg.setText("New password and password confirm not match!");
                        } else {
                            accountDao.updatePassword(this, newPass, DataLocalManager.getAccount().getId());
                            msg.setText("Change password success!");
                        }
                    }
                } else {
                    msg.setText("Password and Re-password must have 8 to 32 character a-zA-Z0-9");
                }
                break;
        }
    }
}