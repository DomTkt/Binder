package com.example.lp.binder;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Debug;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.data.Chat;
import com.example.lp.binder.fragment.ChatDetailFragment;
import com.example.lp.binder.fragment.ChatListFragment;
import com.example.lp.binder.fragment.HomeFragment;
import com.example.lp.binder.fragment.ProfileDetailsFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

public class MainActivity extends AppCompatActivity {


    String name;
    public DatabaseReference databaseFirebase;
    public String userUid;
    public FirebaseUser user;
    public ChatDetailFragment chatDetailFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*Intent i = new Intent(this, PaymentActivity.class);
        startActivity(i);
*/

        databaseFirebase = FirebaseDatabase.getInstance().getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();
        userUid = user.getUid();

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
                        fragment = ChatListFragment.newInstance();
                        break;
                    case R.id.tab_setting:

                        break;
                    case R.id.tab_account:
                        fragment = ProfileDetailsFragment.newInstance();
                        break;
                }
                if(fragment != null) {
                    FragmentManager fm = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fm.beginTransaction();
                    fragmentTransaction.replace(R.id.contentContainer, fragment);
                    fragmentTransaction.commit();
                }

//                if (tabId == R.id.tab_account) {
//                    System.out.println("TAB ACCOUNT");
//                    Intent i  = new Intent(MainActivity.this, ProfilCustom.class);
//                    startActivity(i);
//                }
            }
        });
    }

    public void openChat(Chat chat){
        chatDetailFragment = ChatDetailFragment.newInstance(chat);
        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.contentContainer, chatDetailFragment,"ChatDetailFragment");
        fragmentTransaction.commit();

    }
}
