package com.nagareddy.tabbedactivity.logic;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ListView;

import com.nagareddy.tabbedactivity.R;
import com.nagareddy.tabbedactivity.fragment.BlankFragment;
import com.nagareddy.tabbedactivity.fragment.ItemFragment;
import com.nagareddy.tabbedactivity.fragment.TwoFragment;
import com.nagareddy.tabbedactivity.fragment.dummy.DummyContent;

import java.util.ArrayList;
import java.util.List;



public class Tabs extends AppCompatActivity implements ItemFragment.OnListFragmentInteractionListener{

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private int menuLayout;
    ListView lv;
    private Menu myMenu;

    public int getMenuLayout() {
        return menuLayout;
    }

    public void setMenuLayout(int menuLayout) {
        this.menuLayout = menuLayout;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabs);
        lv = (ListView) findViewById(R.id.lv_contact) ;
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

         /* ActionBar actionBar = getSupportActionBar();
        actionBar.setLogo(R.drawable.home);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true); */

        //getSupportActionBar().setIcon(R.drawable.home);
       // getSupportActionBar().setDisplayShowTitleEnabled(true);
       // getSupportActionBar().setTitle(R.string.app_name);
      //    getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

//Below code is responsible for setting the notification bar (status bar) to the same color as the actionbar
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(this.getResources().getColor(R.color.colorPrimaryDark));
        }
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_tabs, menu);
        myMenu = menu;
        return super.onCreateOptionsMenu(menu);
    }



    @Override
    public boolean onOptionsItemSelected( MenuItem item) {

        int itemId_ = item.getItemId();
        if (itemId_ == R.id.home) {

            return true;
        }
        if (itemId_ == R.id.scan) {
            BlankFragment fragment = (BlankFragment)getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.viewpager + ":" + viewPager.getCurrentItem());
            fragment.updateList();
        }
        return false;
    }



    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new BlankFragment(), "Scan List");
        adapter.addFragment(new TwoFragment(), "Saved List");
        viewPager.setAdapter(adapter);
        viewPager.getCurrentItem();
    }

    @Override
    public void onListFragmentInteraction(DummyContent.DummyItem item) {

    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }


    public boolean onPrepareOptionsMenu (Menu menu) {

        if (menuLayout == 0) {
            //menu.removeItem(R.id.scan);
            //menu.close();
            if(menu.findItem(R.id.scan)!=null) menu.findItem(R.id.scan).setActionView(null);
            stopAnimation();
            myMenu.clear();
            getMenuInflater().inflate(R.menu.menu_scan_tab, myMenu);
            stopAnimation();
           // myMenu = menu;
        }
        else if (menuLayout == 1) {

            myMenu.clear();
            getMenuInflater().inflate(R.menu.menu_saved_tab, myMenu);

            //myMenu = menu;
        }
        else if (menuLayout == 2){
            menu.clear();
            getMenuInflater().inflate(R.menu.menu_scan_tab, myMenu);
            //myMenu = menu;
            LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            ImageView iv = (ImageView)inflater.inflate(R.layout.scan_refresh, null);
            Animation rotation = AnimationUtils.loadAnimation(this, R.anim.refresh_rotate);
            rotation.setRepeatCount(Animation.INFINITE);
            iv.startAnimation(rotation);
            menu.findItem(R.id.scan).setActionView(iv);
        }
        else {}

        return super.onPrepareOptionsMenu(myMenu);
    }

    public void stopAnimation()
    {
        // Get our refresh item from the menu
        MenuItem m = myMenu.findItem(R.id.scan);

        if(m!=null && m.getActionView()!=null)
        {
            m.getActionView().clearAnimation();
            m.setActionView(null);
        }
        //myMenu.clear();
    }

}