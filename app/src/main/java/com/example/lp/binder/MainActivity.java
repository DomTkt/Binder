package com.example.lp.binder;

import android.app.Fragment;
import android.content.Intent;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.lp.binder.fragment.HomeFragment;
import com.example.lp.binder.fragment.ProfileDetailsFragment;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*Intent i = new Intent(this, PaymentActivity.class);
        startActivity(i);
*/
        BottomBar bottomBar = (BottomBar) findViewById(R.id.bottomBar);
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                Fragment fragment = null;
                switch(tabId) {
                    case R.id.tab_home:
                        fragment = HomeFragment.newInstance();
                        break;
                    case R.id.tab_message:

                        break;
                    case R.id.tab_setting:

                        break;
                    case R.id.tab_account:
                        fragment = ProfileDetailsFragment.newInstance();
                        break;
                }
                if(fragment != null) {
                    getFragmentManager().beginTransaction().add(R.id.contentContainer, fragment).commit();
                }

                if (tabId == R.id.tab_account) {
                    System.out.println("TAB ACCOUNT");
                    Intent i  = new Intent(MainActivity.this, ProfilCustom.class);
                    startActivity(i);
                }
            }
        });
    }
}
