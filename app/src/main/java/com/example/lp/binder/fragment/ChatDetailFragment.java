package com.example.lp.binder.fragment;

import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.data.Chat;
import com.example.lp.binder.ChatActivity;
import com.example.lp.binder.MainActivity;
import com.example.lp.binder.R;


public class ChatDetailFragment extends Fragment {

    private Chat chat;
    private MainActivity mainActivity;

    public ChatDetailFragment() {
        // Required empty public constructor
    }


    public static ChatDetailFragment newInstance(Chat chat) {
        ChatDetailFragment fragment = new ChatDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable("chat",chat);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity = (MainActivity) getActivity();
        if (getArguments() != null) {
            chat = (Chat) getArguments().getSerializable("chat");

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chat_detail, container, false);
        LinearLayout llcontent = (LinearLayout) view.findViewById(R.id.ll_content);
        for(int i=0; i<chat.getConversation().size();i++){
            TextView tv = new TextView(mainActivity);
            tv.setText(chat.getConversation().get(i).getUserSender().getName() + " : " + chat.getConversation().get(i).getContent());
            llcontent.addView(tv);
        }
        return view;
    }






}
