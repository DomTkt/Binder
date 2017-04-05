package com.example.lp.binder;

import android.app.Fragment;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.lp.binder.fragment.HomeFragment;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomBar bottomBar = (BottomBar) findViewById(R.id.bottomBar);
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                Fragment fragment = null;
                switch(tabId) {
                    case R.id.tab_home:
                        fragment = new HomeFragment();
                        break;
                    case R.id.tab_message:

                        break;
                    case R.id.tab_setting:

                        break;
                    case R.id.tab_account:

                        break;
                }
                if(fragment != null) {
                    getFragmentManager().beginTransaction().add(R.id.contentContainer, fragment).commit();
                }
            }
        });
        getFragmentManager().beginTransaction().add(R.id.contentContainer, new HomeFragment()).commit();
    }
}
