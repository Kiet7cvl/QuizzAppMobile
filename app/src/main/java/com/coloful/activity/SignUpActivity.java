package com.coloful.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.coloful.R;
import com.coloful.dao.AccountDao;
import com.coloful.dao.DBHelper;
import com.coloful.model.Account;

import java.util.Calendar;
import java.util.regex.Pattern;


public class SignUpActivity extends AppCompatActivity {

    ImageButton back;
    Button signup;
    EditText ed_date, ed_email, ed_username, ed_password;
    DBHelper db;

    AccountDao accountDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        back = (ImageButton) findViewById(R.id.bt_back);
        accountDao = new AccountDao();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent landing = new Intent(SignUpActivity.this, LandingActivity.class);
                startActivity(landing);
            }
        });

        ed_date = (EditText) findViewById(R.id.ed_date);

        ed_date.addTextChangedListener(new TextWatcher() {
            private final String ddmmyyyy = "DDMMYYYY";
            private final Calendar cal = Calendar.getInstance();
            private String current = "";

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().equals(current)) {
                    String clean = s.toString().replaceAll("[^\\d.]", "");
                    String cleanC = current.replaceAll("[^\\d.]", "");

                    int cl = clean.length();
                    int sel = cl;
                    for (int i = 2; i <= cl && i < 6; i += 2) {
                        sel++;
                    }
                    //Fix for pressing delete next to a forward slash
                    if (clean.equals(cleanC)) sel--;

                    if (clean.length() < 8) {
                        clean = clean + ddmmyyyy.substring(clean.length());
                    } else {
                        //This part makes sure that when we finish entering numbers
                        //the date is correct, fixing it otherwise
                        int day = Integer.parseInt(clean.substring(0, 2));
                        int mon = Integer.parseInt(clean.substring(2, 4));
                        int year = Integer.parseInt(clean.substring(4, 8));

                        if (mon > 12) mon = 12;
                        cal.set(Calendar.MONTH, mon - 1);

                        year = (year < 1900) ? 1900 : (year > 2100) ? 2100 : year;
                        cal.set(Calendar.YEAR, year);
                        // ^ first set year for the line below to work correctly
                        //with leap years - otherwise, date e.g. 29/02/2012
                        //would be automatically corrected to 28/02/2012

                        day = (day > cal.getActualMaximum(Calendar.DATE)) ? cal.getActualMaximum(Calendar.DATE) : day;
                        clean = String.format("%02d%02d%02d", day, mon, year);
                    }

                    clean = String.format("%s/%s/%s", clean.substring(0, 2),
                            clean.substring(2, 4),
                            clean.substring(4, 8));

                    sel = sel < 0 ? 0 : sel;
                    current = clean;
                    ed_date.setText(current);
                    ed_date.setSelection(sel < current.length() ? sel : current.length());


                }
            }


            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


        ed_email = (EditText) findViewById(R.id.ed_email);
        ed_username = (EditText) findViewById(R.id.ed_username);
        ed_password = (EditText) findViewById(R.id.ed_password);
        db = new DBHelper(this);

        signup = (Button) findViewById(R.id.bt_signup);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String dob = ed_date.getText().toString().trim();
                String email = ed_email.getText().toString().trim();
                String username = ed_username.getText().toString().trim();
                String password = ed_password.getText().toString().trim();

                Pattern patternAccAndPass = Pattern.compile("^\\w{8,32}$");
                Pattern patternEmail = Pattern.compile("^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$");

                Account account = new Account(null, username, password, email, dob);

                if (dob.equals("") || email.equals("") || username.equals("") || password.equals("")) {
                    Toast.makeText(SignUpActivity.this, "Please enter all the fields", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (accountDao.checkUsernameAndEmail(SignUpActivity.this, account)) {
                    Toast.makeText(SignUpActivity.this, "Username or email was used", Toast.LENGTH_SHORT).show();
                } else {
                    if (!patternAccAndPass.matcher(username).find()) {
                        Toast.makeText(SignUpActivity.this, "Username not valid", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (!patternAccAndPass.matcher(password).find()) {
                        Toast.makeText(SignUpActivity.this, "Password not valid", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (!patternEmail.matcher(email).find()) {
                        Toast.makeText(SignUpActivity.this, "Email not valid", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (accountDao.insertAccount(SignUpActivity.this, account)) {
                        Toast.makeText(SignUpActivity.this, "Registered successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(SignUpActivity.this, LogInActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(SignUpActivity.this, "Registered fail", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}