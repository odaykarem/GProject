package com.example.gproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.example.gproject.Adapters.LoginAdapter;
import com.example.gproject.LogIn.LoginTabFragment;
import com.example.gproject.LogIn.RegisterTabFragment;
import com.google.android.material.tabs.TabLayout;

public class test extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);


        tabLayout = findViewById(R.id.login_tab_layout);
        viewPager = findViewById(R.id.login_view_pager);

        LoginTabFragment loginTabFragment = new LoginTabFragment();
        RegisterTabFragment registerTabFragment = new RegisterTabFragment();



        final LoginAdapter loginAdapter = new LoginAdapter(getSupportFragmentManager(), 0);

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setupWithViewPager(viewPager);

        loginAdapter.addFragment(loginTabFragment, "Log In");
        loginAdapter.addFragment(registerTabFragment, "Register");
        viewPager.setAdapter(loginAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

    }
}