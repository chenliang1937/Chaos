package com.meiya.chaos.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.gigamole.library.NavigationTabBar;
import com.meiya.chaos.R;
import com.meiya.chaos.common.AppManager;
import com.meiya.chaos.common.AppService;
import com.meiya.chaos.common.Constant;
import com.meiya.chaos.common.base.BaseActivity;
import com.meiya.chaos.model.event.StartImgEvent;
import com.meiya.chaos.ui.fragment.MeFragment;
import com.meiya.chaos.ui.fragment.RecommendFragment;
import com.meiya.chaos.ui.fragment.TopicsFragment;
import com.meiya.chaos.ui.fragment.WikiFragment;
import com.meiya.materialsearchviewlib.MaterialSearchView;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;

public class MainActivity extends BaseActivity {

    @BindView(R.id.vp_ntb)
    ViewPager viewPager;
    @BindView(R.id.ntb_horizontal)
    NavigationTabBar navigationTabBar;
    @BindView(R.id.bg_ntb_horizontal)
    View bgNavigationTabBar;
    @BindView(R.id.main_toolbar)
    Toolbar toolbar;
    @BindView(R.id.search_view)
    MaterialSearchView searchView;

    private List<Fragment> fragments = new ArrayList<>();
    private boolean isExit = false;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initViews() {
        toolbar.setTitle("推荐");
        toolbar.setTitleTextColor(getResources().getColor(R.color.toolbar_title_color));
        setSupportActionBar(toolbar);

        initViewPager();
        initSearchView();

        String[] colors = getResources().getStringArray(R.array.default_preview);
        ArrayList<NavigationTabBar.Model> models = new ArrayList<>();
        models.add(new NavigationTabBar.Model(
                getResources().getDrawable(R.mipmap.ic_recommend), Color.parseColor(colors[0]), "推荐"));
        models.add(new NavigationTabBar.Model(
                getResources().getDrawable(R.mipmap.ic_normal), Color.parseColor(colors[1]), "发现"));
        models.add(new NavigationTabBar.Model(
                getResources().getDrawable(R.mipmap.ic_wiki), Color.parseColor(colors[2]), "社区"));
        models.add(new NavigationTabBar.Model(
                getResources().getDrawable(R.mipmap.ic_me), Color.parseColor(colors[3]), "我"));
        navigationTabBar.setModels(models);
        navigationTabBar.setViewPager(viewPager, 0);

        navigationTabBar.setOnTabBarSelectedIndexListener(new NavigationTabBar.OnTabBarSelectedIndexListener() {
            @Override
            public void onStartTabSelected(NavigationTabBar.Model model, int index) {

            }

            @Override
            public void onEndTabSelected(NavigationTabBar.Model model, int index) {
                model.hideBadge();
                switch (index) {
                    case 0:
                        toolbar.setTitle("推荐");
                        break;
                    case 1:
                        toolbar.setTitle("发现");
                        break;
                    case 2:
                        toolbar.setTitle("社区");
                        break;
                    case 3:
                        toolbar.setTitle("我");
                        break;
                }
            }
        });

        navigationTabBar.post(new Runnable() {
            @Override
            public void run() {
                bgNavigationTabBar.getLayoutParams().height = (int) navigationTabBar.getBarHeight();
                bgNavigationTabBar.requestLayout();
            }
        });

        //设置badge
//        navigationTabBar.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                for (int i = 1; i < navigationTabBar.getModels().size(); i++) {
//                    final NavigationTabBar.Model model = navigationTabBar.getModels().get(i);
//                    switch (i) {
//                        case 1:
//                            model.setBadgeTitle("NIB");
//                            break;
//                        case 2:
//                            model.setBadgeTitle("4");
//                            break;
//                        case 3:
//                            model.setBadgeTitle("NEW");
//                            break;
//                    }
//                    navigationTabBar.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            model.showBadge();
//                        }
//                    }, i * 100);
//                }
//            }
//        }, 500);
        loadSplashImage();
    }

    @Override
    protected TransitionMode getOverridePendingTransitionMode() {
        return TransitionMode.FADE;
    }

    private void initSearchView(){
        searchView.setVoiceSearch(true);
        searchView.setVoiceIcon(getResources().getDrawable(R.drawable.ic_action_voice_search));
        searchView.setCursorDrawable(R.drawable.searchview_cursor);
        searchView.setEllipsize(true);
        searchView.setSuggestions(getResources().getStringArray(R.array.query_suggestions));
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                intent.putExtra("QUERY", query);
                startActivity(intent);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        searchView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }

    private void initViewPager() {
        RecommendFragment recommendFragment = new RecommendFragment();
        WikiFragment wikiFragment = new WikiFragment();
        TopicsFragment topicFragment = new TopicsFragment();
        MeFragment meFragment = new MeFragment();
        fragments.add(recommendFragment);//知乎新闻
        fragments.add(topicFragment);//网易视屏
        fragments.add(wikiFragment);
        fragments.add(meFragment);

        viewPager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager()));
    }

    private class MyFragmentPagerAdapter extends FragmentPagerAdapter{

        public MyFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position % fragments.size());
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            //将super注释掉，以解决viewpager+fragment出现的重复加载数据的情况
//            super.destroyItem(container, position, object);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            if (searchView.isSearchOpen()){
                searchView.closeSearch();
            }
            exitBy2Click();
        }
        return false;
    }

    private void loadSplashImage(){
        AppService.getInstance().getStartImage(getTaskId());
    }

    public void onEventMainThread(StartImgEvent event){
        if (event.getEventResult() == Constant.Result.SUCCESS){
            ImageView imageView = new ImageView(this);
            AppService.getPrefser().put(Constant.PRE_SPLASH_IMAGEURL, event.getmStartImage().getImg());
            Glide.with(this)
                    .load(event.getmStartImage().getImg())
                    .into(imageView);
        }
    }

    private void exitBy2Click(){
        Timer timer = null;
        if (!isExit){
            isExit = true;
            Snackbar.make(viewPager, "再按一次退出", Snackbar.LENGTH_SHORT).show();
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    isExit = false;
                }
            }, 2000);
        }else {
            AppManager.getInstance().clear();
        }
    }
}
