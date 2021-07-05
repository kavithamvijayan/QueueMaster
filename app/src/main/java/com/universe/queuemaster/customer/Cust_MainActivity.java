package com.universe.queuemaster.customer;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.universe.queuemaster.R;
import com.universe.queuemaster.misc.SectionsPagerAdapter;

public class Cust_MainActivity  extends AppCompatActivity {
    private static final String TAG = "Cust_MainActivity";
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cust_outline);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.cust_viewpager_container);
        setupViewPager(mViewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.cust_tabs);

        tabLayout.setupWithViewPager(mViewPager);

        tabLayout.getTabAt(0).setIcon(R.drawable.icon_myqueue_noncircular);
        tabLayout.getTabAt(1).setIcon(R.drawable.icon_home_noncircular);
        tabLayout.getTabAt(2).setIcon(R.drawable.icon_user_noncircular);

    }

    private void setupViewPager(ViewPager viewPager) {
        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new CustCurrentQueues());
        adapter.addFragment(new CustExplore());
        adapter.addFragment(new CustSettings());

        viewPager.setAdapter(adapter);
    }
}