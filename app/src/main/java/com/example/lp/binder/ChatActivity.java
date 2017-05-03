package com.example.lp.binder;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.data.Chat;
import com.example.lp.binder.fragment.ChatDetailFragment;
import com.example.lp.binder.fragment.ChatListFragment;

public class ChatActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.add(R.id.fragmentcontainer,ChatListFragment.newInstance(),"ChatListFragment");
        fragmentTransaction.commit();
    }


}
