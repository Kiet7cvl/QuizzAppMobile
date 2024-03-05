package com.coloful.activity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.coloful.R;
import com.coloful.adapters.FragmentDialogHelper;
import com.coloful.datalocal.DataLocalManager;
import com.coloful.fragments.HomeFragment;
import com.coloful.fragments.ProfileFragment;
import com.coloful.fragments.SearchFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    TextView actionBarTitle;
    private ActionBar toolbar;
    private BottomNavigationView navigation;

    @SuppressLint({"ResourceAsColor", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = getSupportActionBar();
        // change color actionBar
        toolbar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#4257b0")));
        // Customer actionBar
        toolbar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        toolbar.setDisplayShowCustomEnabled(true);
        toolbar.setCustomView(R.layout.activity_action_bar_layout);
        toolbar.setDisplayShowTitleEnabled(false);
        // setTitle of actionBar with screen
        actionBarTitle = (TextView) findViewById(R.id.action_bar_title);
        actionBarTitle.setText("Home");

        DataLocalManager.init(this);
        System.out.println(DataLocalManager.getAccount().toString());

        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(this::onNavigationItemSelected);
        getDataFromOtherActivity(getIntent().getStringExtra("backScreen"));
    }

    private void getDataFromOtherActivity(String backScreen) {
        if (backScreen == null) {
            backScreen = "";
        }
        System.out.println(backScreen);
        switch (backScreen) {
            case "Profile":
                loadFragment(new ProfileFragment(), "Profile");
                navigation.getMenu().getItem(3).setChecked(true);
                break;
            case "search":
                loadFragment(new SearchFragment(), "Search");
                navigation.getMenu().getItem(1).setChecked(true);
                break;
            default:
                loadFragment(new HomeFragment(), "Home");
                navigation.getMenu().getItem(0).setChecked(true);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment;
        FragmentManager fm;
        FragmentDialogHelper popup;
        switch (item.getItemId()) {
            case R.id.navigation_home:
                fragment = new HomeFragment();
                loadFragment(fragment, "Home");
                return true;
            case R.id.navigation_search:
                fragment = new SearchFragment();
                loadFragment(fragment, "Search");
                return true;
            case R.id.navigation_new_set:
                fm = getSupportFragmentManager();
                popup = FragmentDialogHelper.newInstance("create quiz");
                popup.show(fm, null);
                return true;
            case R.id.navigation_profile:
                fragment = new ProfileFragment();
                loadFragment(fragment, "Profile");
                return true;
        }
        return false;
    }

    private void loadFragment(Fragment fragment, String title) {
        if (!title.equalsIgnoreCase("Home")) {
            toolbar.hide();
        } else {
            toolbar.show();
        }

        toolbar.setTitle(title);
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}