package com.coloful.adapters;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.coloful.R;
import com.coloful.activity.CreateStudySetActivity;
import com.coloful.dao.AccountDao;
import com.coloful.model.Account;

import java.util.Random;

public class FragmentDialogHelper extends DialogFragment implements View.OnClickListener {

    String layoutName;
    Button btnCancel;
    Button btnOk;
    AccountDao accountDao;
    EditText editText;
    TextView tvCreateSet, tvInstruc;


    //Được dùng khi khởi tạo dialog mục đích nhận giá trị
    public static FragmentDialogHelper newInstance(String layoutName) {
        FragmentDialogHelper dialog = new FragmentDialogHelper();
        Bundle args = new Bundle();
        args.putString("idPopUpLayout", layoutName);
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        layoutName = getArguments().getString("idPopUpLayout");
        View view = null;
        if (layoutName.equals("username")) {
            view = inflater.inflate(R.layout.popup_forgot_username, container);
        } else if (layoutName.equals("password")) {
            view = inflater.inflate(R.layout.popup_forgot_password, container);
        } else if (layoutName.equals("create quiz")) {
            view = inflater.inflate(R.layout.popup_create_quiz, container);
        }

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        accountDao = new AccountDao();

        // lấy giá trị tự bundle
        if (layoutName.equals("password")) {
            btnOk = (Button) view.findViewById(R.id.btn_fg_pass_ok);
            btnCancel = (Button) view.findViewById(R.id.btn_fg_pass_cancle);
            editText = view.findViewById(R.id.edt_username);
            tvInstruc = view.findViewById(R.id.tv_fg_pass_instruc);
        } else if (layoutName.equals("username")) {
            btnOk = (Button) view.findViewById(R.id.btn_fg_username_ok);
            btnCancel = (Button) view.findViewById(R.id.btn_fg_username_cancle);
            editText = view.findViewById(R.id.edt_email);
            tvInstruc = view.findViewById(R.id.tv_fgu_instruction);
        } else if (layoutName.equals("create quiz")) {
            btnCancel = (Button) view.findViewById(R.id.btn_cancel_create_quiz);
            tvCreateSet = (TextView) view.findViewById(R.id.tv_create_quiz);
            btnOk = null;
        }

        btnCancel.setOnClickListener(this::onClick);
        if (btnOk != null) {
            btnOk.setOnClickListener(this::onClick);
        }

        if (tvCreateSet != null) {
            tvCreateSet.setOnClickListener(this::onClick);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_fg_pass_ok:
                String username_p = editText.getText().toString();
                if (username_p == null || username_p.isEmpty()) {
                    tvInstruc.setText("Please enter your username to find password!");
                } else {
                    Account account = accountDao.checkUsernameExisted(getContext(), username_p);
                    if (account != null) {
                        int leftLimit = 48; // numeral '0'
                        int rightLimit = 122; // letter 'z'
                        int targetStringLength = 8;
                        Random random = new Random();

                        String newPass = random.ints(leftLimit, rightLimit + 1)
                                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                                .limit(targetStringLength)
                                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                                .toString();
                        accountDao.updatePassword(getContext(), newPass, account.getId());
                        editText.setText(null);
                        tvInstruc.setText("Your new password is: " + newPass + ". Please change your password after login!");
                    } else {
                        tvInstruc.setText("Not found username in system!");
                    }
                }
                tvInstruc.setTextColor(Color.parseColor("#FF0000"));
                break;
            case R.id.btn_fg_username_ok:
                String email = editText.getText().toString();
                if (accountDao.checkEmailExist(getContext(), email.trim())) {
                    String username = accountDao.getUsernameByEmail(getContext(), email);
                    if (username != null) {
//                        String messageToSend = "We send you your username: " + username;
//                        // TODO Auto-generated method stub
//                        new Thread(new Runnable() {
//                            public void run() {
//                                try {
//                                    GMailSender sender = new GMailSender(
//                                            "luongnthhe151453@fpt.edu.vn",
//                                            "Hienluong0541");
////                                    sender.addAttachment(Environment.getExternalStorageDirectory().getPath() + "/image.jpg");
//                                    sender.sendMail("Test mail", "This mail has been sent from android app along with attachment",
//                                            "conmuanngangqua200x@gmail.com",
//                                            "conmuanngangqua200x@gmail.com");
//                                    tvInstruc.setText("Please check your email, we sent an email for you!");
//                                } catch (Exception e) {
//                                    tvInstruc.setText("System error, please try again!");
//                                }
//                            }
//
//                        }).start();
                        editText.setVisibility(View.INVISIBLE);
                        tvInstruc.setText("Your username is: " + username);
                    }
                } else {
                    tvInstruc.setText("Not found email in system!");
                }
                tvInstruc.setTextColor(Color.parseColor("#FF0000"));
                break;
            case R.id.tv_create_quiz:
                Intent intent = new Intent(getContext(), CreateStudySetActivity.class);
                startActivity(intent);
                break;
            default:
                getDialog().dismiss();
                break;
        }
    }
}
