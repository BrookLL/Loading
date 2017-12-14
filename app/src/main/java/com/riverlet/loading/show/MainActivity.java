package com.riverlet.loading.show;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private int[] layoutIds = new int[]{
            R.layout.loading_2_threebodyloadingview,
            R.layout.loading_1_circelloadingview
    };
    private Fragment[] fragments = new Fragment[layoutIds.length];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }


    private void initView() {
        for (int i = 0; i < fragments.length; i++) {
            fragments[i] = LoadingFragment.newInstance(layoutIds[i]);
        }
        final ViewPager viewpager = findViewById(R.id.viewpager);
        viewpager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragments[position];
            }

            @Override
            public int getCount() {
                return fragments.length;
            }
        });
    }
}
