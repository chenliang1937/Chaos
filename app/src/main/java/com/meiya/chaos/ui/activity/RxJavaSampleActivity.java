package com.meiya.chaos.ui.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.meiya.chaos.R;
import com.meiya.chaos.common.base.BaseActivity;
import com.meiya.chaos.model.event.Event;
import com.meiya.chaos.ui.fragment.rxsample.CacheFragment;
import com.meiya.chaos.ui.fragment.rxsample.ElementaryFragment;
import com.meiya.chaos.ui.fragment.rxsample.MapFragment;
import com.meiya.chaos.ui.fragment.rxsample.TokenAdvancedFragment;
import com.meiya.chaos.ui.fragment.rxsample.TokenFragment;
import com.meiya.chaos.ui.fragment.rxsample.ZipFragment;

import butterknife.BindView;

/**
 * Created by chenliang3 on 2016/5/30.
 */
public class RxJavaSampleActivity extends BaseActivity {

    @BindView(R.id.sample_tabs)
    TabLayout tabLayout;
    @BindView(R.id.sample_viewpager)
    ViewPager viewPager;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_rxjavasample;
    }

    @Override
    protected TransitionMode getOverridePendingTransitionMode() {
        return TransitionMode.RIGHT;
    }

    @Override
    protected void initViews() {
        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {

            @Override
            public int getCount() {
                return 6;
            }

            @Override
            public Fragment getItem(int position) {
                switch (position){
                    case 0:
                        return new ElementaryFragment();
                    case 1:
                        return new MapFragment();
                    case 2:
                        return new ZipFragment();
                    case 3:
                        return new TokenFragment();
                    case 4:
                        return new TokenAdvancedFragment();
                    case 5:
                        return new CacheFragment();
                    default:
                        return new ElementaryFragment();
                }
            }

            @Override
            public CharSequence getPageTitle(int position) {
                switch (position){
                    case 0:
                        return "基本";
                    case 1:
                        return "转换(map)";
                    case 2:
                        return "压合(zip)";
                    case 3:
                        return "Token(flatMap)";
                    case 4:
                        return "Token_高级(retryWhen)";
                    case 5:
                        return "缓存(BehaviorSubject)";
                    default:
                        return "基本";
                }
            }
        });
        tabLayout.setupWithViewPager(viewPager);
    }

    public void onEventMainThread(Event event){

    }
}
