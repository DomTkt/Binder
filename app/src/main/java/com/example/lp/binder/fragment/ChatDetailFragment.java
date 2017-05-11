package com.example.lp.binder.fragment;

import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.data.Chat;
import com.example.data.Message;
import com.example.lp.binder.MainActivity;
import com.example.lp.binder.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.UUID;


public class ChatDetailFragment extends Fragment {

    private Chat chat;
    private MainActivity mainActivity;
    private ImageView ivSendMess;
    private EditText etMess;
    private LinearLayout llcontent;


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
        llcontent = (LinearLayout) view.findViewById(R.id.ll_content);
        ivSendMess = (ImageView) view.findViewById(R.id.sendmess);
        etMess = (EditText) view.findViewById(R.id.etmess);

        ivSendMess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etMess.getText().toString().isEmpty()){
                    Toast.makeText(mainActivity,"Vous n'avez rien écris", Toast.LENGTH_SHORT).show();
                }else{

                    sendMess(etMess.getText().toString());
                    etMess.setText("");
                }

            }
        });

        loadData();
        return view;
    }



    public boolean isMyMess(Message mess){
        return mess.getUserSender().getId().equals(mainActivity.userUid);
    }

    public void sendMess(String content){
        String newId = UUID.randomUUID().toString();
        DatabaseReference n = mainActivity.databaseFirebase.child("conversations")
                .child(chat.getId())
                .child("messages")
                .child(newId).getRef();


        n.child("content").getRef().setValue(content);
        n.child("timestamp").getRef().setValue(Long.toString(new Date().getTime()));
        n.child("userId").getRef().setValue(mainActivity.userUid);
        loadData();
    }

    public void loadData(){
        sortMessByTmp();
        llcontent.removeAllViewsInLayout();
        DisplayMetrics displayMetrics = mainActivity.getResources().getDisplayMetrics();
        float width = displayMetrics.widthPixels;

        Log.d("tag", Integer.toString(Math.round(width)));

        for(int i=0; i<chat.getConversation().size();i++){
            LinearLayout llmess;
            LinearLayout.LayoutParams layoutParams;

            if(isMyMess(chat.getConversation().get(i))){
                llmess = (LinearLayout) mainActivity.getLayoutInflater().inflate(R.layout.my_message_layout,null);
                layoutParams = new LinearLayout.LayoutParams(Math.round(width/2), LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(30, 30, 30, 30);
                layoutParams.gravity = Gravity.RIGHT;
            }else{

                llmess = (LinearLayout) mainActivity.getLayoutInflater().inflate(R.layout.message_layout,null);
                layoutParams = new LinearLayout.LayoutParams(Math.round(width/2), LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(30, 30, 30, 30);
            }

            TextView tv = (TextView) llmess.findViewById(R.id.mess_content);
            tv.setText(chat.getConversation().get(i).getContent());

            TextView tv2 = (TextView) llmess.findViewById(R.id.mess_hour);
            DateFormat df = new DateFormat();
            tv2.setText(df.format("dd/MM/yyyy hh:mm:ss",chat.getConversation().get(i).getTime()));

            llcontent.addView(llmess, layoutParams);
        }
    }

    public void sortMessByTmp(){
        Collections.sort(chat.getConversation(), new Comparator<Message>() {
            @Override
            public int compare(Message m1, Message m2) {
                return m1.getTime().compareTo(m2.getTime());
            }
        });

    }
}
