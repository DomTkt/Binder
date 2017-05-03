package com.example.lp.binder.fragment;

import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.data.Chat;
import com.example.data.Message;
import com.example.data.User;
import com.example.lp.binder.MainActivity;
import com.example.lp.binder.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class ChatListFragment extends Fragment {

    private ArrayList<Chat> listChat;
    private ListView lvChat;
    private StableArrayAdapter adapter;
    private MainActivity mainActivity;



    public ChatListFragment() {
        // Required empty public constructor
    }


    public static ChatListFragment newInstance() {
        ChatListFragment fragment = new ChatListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mainActivity = (MainActivity) getActivity();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        listChat = new ArrayList<Chat>();
        Log.d(this.getTag(),"create test datas");
        //create test datas
        for(int i=0; i<5 ; i++){
            User us1 = new User("jean");
            User us2 = new User("pierre"+i);
            ArrayList<Message> list = new ArrayList<>();
            for(int j=0; j<10;j++){
                Date date = new Date();
                Message mess = new Message(j+(i*20),date,us2,"blablabla"+(j+(i*20)));
                Message mess2 = new Message(j+(i*20),date,us1,"blablabla"+(j+(i*20)));
                list.add(mess);
                list.add(mess2);
            }
            Chat chat = new Chat(i,us1,us2,list);
            listChat.add(chat);
        }

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chat_list, container, false);
        lvChat = (ListView) view.findViewById(R.id.lv_chat);
        adapter = new StableArrayAdapter(mainActivity);
        Log.d(this.getTag(),"add adapter");
        lvChat.setAdapter(adapter);

        lvChat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mainActivity.openChat(listChat.get(position));
            }
        });

        return view;

    }

    private class StableArrayAdapter extends BaseAdapter {


        private Context context;

        private LayoutInflater inflater;

        class ViewHolder{
            TextView name;
        }

        public StableArrayAdapter(Context context) {

            super();
            this.context = context;
            this.inflater = LayoutInflater.from(context);
        }


        @Override
        public int getCount() {

            return listChat.size();
        }

        @Override
        public Object getItem(int position) {

            return listChat.get(position);
        }

        @Override
        public long getItemId(int position) {

            return position;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            // reuse views
            ViewHolder holder;
            if (convertView == null) {

                convertView = inflater.inflate(R.layout.row_chat_layout, null);
                holder = new ViewHolder();
                holder.name = (TextView) convertView.findViewById(R.id.name);


                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.name.setText(listChat.get(position).getUser2().getNickname());


            return convertView;
        }

    }



}
