package com.coloful.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.coloful.R;
import com.coloful.activity.AboutAppActivity;
import com.coloful.activity.ChangeEmailActivity;
import com.coloful.activity.ChangePasswordActivity;
import com.coloful.activity.ChangeUsernameActivity;
import com.coloful.activity.LogInActivity;
import com.coloful.activity.MainActivity;
import com.coloful.datalocal.DataLocalManager;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment implements View.OnClickListener {

    private MainActivity mainActivity;
    private View fragView;
    private TextView tvUserNameProfile;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mainActivity = (MainActivity) getActivity();
        DataLocalManager.init(getContext());
        fragView = inflater.inflate(R.layout.fragment_profile, container, false);
        tvUserNameProfile = fragView.findViewById(R.id.tv_username_profile);
        tvUserNameProfile.setText("Hello , " + DataLocalManager.getAccount().getUsername());
        onClickView();
        return fragView;
    }

    private void onClickView() {
        fragView.findViewById(R.id.tv_change_username).setOnClickListener(this::onClick);
        fragView.findViewById(R.id.tv_change_email).setOnClickListener(this::onClick);
        fragView.findViewById(R.id.tv_change_password).setOnClickListener(this::onClick);
        fragView.findViewById(R.id.tv_aboutApp).setOnClickListener(this::onClick);
        fragView.findViewById(R.id.tv_log_out).setOnClickListener(this::onClick);
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.tv_change_username:
                intent = new Intent(mainActivity, ChangeUsernameActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_change_email:
                intent = new Intent(mainActivity, ChangeEmailActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_aboutApp:
                intent = new Intent(mainActivity, AboutAppActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_change_password:
                intent = new Intent(mainActivity, ChangePasswordActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_log_out:
                DataLocalManager.removeSession();
                intent = new Intent(mainActivity, LogInActivity.class);
                startActivity(intent);
                break;
        }
    }
}