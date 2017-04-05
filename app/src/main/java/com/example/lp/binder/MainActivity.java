package com.example.lp.binder;

import android.content.Intent;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

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
                if (tabId == R.id.tab_home) {
                    System.out.println("TAB HOME");

                }

                if (tabId == R.id.tab_message) {
                    System.out.println("TAB MESSAGE");
                }

                if (tabId == R.id.tab_account) {
                    System.out.println("TAB account");
                    Intent i = new Intent(MainActivity.this, ProfilCustom.class);
                    startActivity(i);

                }
            }
        });
    }
}
