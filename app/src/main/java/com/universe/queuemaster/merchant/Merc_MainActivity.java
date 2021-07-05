package com.universe.queuemaster.merchant;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.universe.queuemaster.R;
import com.universe.queuemaster.misc.SectionsPagerAdapter;

public class Merc_MainActivity extends AppCompatActivity {

    private static final String TAG = "Merc_MainActivity";

    private SectionsPagerAdapter mSectionsPagerAdapter;

    private ViewPager mViewPager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        setContentView(R.layout.merc_outline);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mSectionsPagerAdapter.notifyDataSetChanged();

        mViewPager = (ViewPager) findViewById(R.id.merc_viewpager_container);
        setupViewPager(mViewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.merc_tabs);

        tabLayout.setupWithViewPager(mViewPager);

        tabLayout.getTabAt(0).setIcon(R.drawable.cog);
        tabLayout.getTabAt(1).setIcon(R.drawable.icon_food);
        tabLayout.getTabAt(2).setIcon(R.drawable.helpicon26);
        //tabLayout.getTabAt(3).setIcon(R.drawable.help);

        mViewPager.setCurrentItem(1);



    }

    private void setupViewPager(ViewPager viewPager) {
        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new MercQueueConfiguration());
        adapter.addFragment(new MercMainOverview());
        adapter.addFragment(new Merc_HelpPage());
        viewPager.setAdapter(adapter);
    }


}
