package com.example.gproject.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

//import com.astuetz.PagerSlidingTabStrip;
import com.example.gproject.Adapters.MyTabAdapter;
import com.example.gproject.Fragments.MainFragment;
import com.example.gproject.Fragments.OperationFragment;
import com.example.gproject.R;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {
    //PagerSlidingTabStrip tabs;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private Toolbar toolbar;

    MainFragment mainFragment;
    OperationFragment operationFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setSupportActionBar(toolbar);

        viewPager = findViewById(R.id.login_view_pager);
        tabLayout = findViewById(R.id.login_tab_layout);

        //tabs.setViewPager(viewPager);

        mainFragment = new MainFragment();
        operationFragment = new OperationFragment();

        tabLayout.setupWithViewPager(viewPager);

        //Adding Tabs
        MyTabAdapter myTabAdapter = new MyTabAdapter(getSupportFragmentManager(), 0);
        myTabAdapter.addFragment(mainFragment, "Main");
        myTabAdapter.addFragment(operationFragment, "Opr");
        viewPager.setAdapter(myTabAdapter);
        //Adding Icons to tabs
       // tabLayout.getTabAt(0).setIcon(R.drawable.ic_baseline_main_tab);

    }
}