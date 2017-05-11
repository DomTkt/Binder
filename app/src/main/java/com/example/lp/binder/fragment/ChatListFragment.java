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
import android.widget.Toast;

import com.example.data.Chat;
import com.example.data.Message;
import com.example.data.User;
import com.example.lp.binder.MainActivity;
import com.example.lp.binder.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;


public class ChatListFragment extends Fragment {

    private ArrayList<Chat> listChat;
    private ListView lvChat;
    private StableArrayAdapter adapter;
    private MainActivity mainActivity;
    private User connectedUser;
    private ArrayList<String> listConvID;
    private ArrayList<User> listUserConv;



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
                Message mess = new Message(Integer.toString(j+(i*20)),date,us2,"blablabla"+(j+(i*20)));
                Message mess2 = new Message(Integer.toString(j+(i*20)),date,us1,"blablabla"+(j+(i*20)));
                list.add(mess);
                list.add(mess2);
            }
            Chat chat = new Chat(Integer.toString(i),us1,us2,list);
            listChat.add(chat);
        }

        convertDatas();
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

    public void convertDatas(){

        connectedUser = new User(null,null,-1,-1,null);
        listChat = new ArrayList<>();
        listConvID = new ArrayList<>();
        listUserConv = new ArrayList<>();
        connectedUser.setId(mainActivity.userUid);

        mainActivity.databaseFirebase.child("users").child(mainActivity.userUid).getRef().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //User
                connectedUser.setNickname(dataSnapshot.child("nickname").getValue(String.class));
                connectedUser.setAge(dataSnapshot.child("age").getValue(Integer.class));
                Log.d(ChatListFragment.class.getName(),"userid = "+mainActivity.userUid);

                //Conversation
                for(int i = 0; i<dataSnapshot.child("conversations").getChildrenCount(); i++){
                    String id = dataSnapshot.child("conversations").child(Integer.toString(i)).getValue(String.class);
                    listConvID.add(id);
                }
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mainActivity.databaseFirebase.child("users").getRef().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(listConvID != null ){
                    if(listConvID.size()>0){
                        for(int i=0 ; i<listConvID.size();i++){
                            for (Iterator<DataSnapshot> usersIter = dataSnapshot.getChildren().iterator(); usersIter.hasNext();){
                                DataSnapshot ds = usersIter.next();
                                if(ds.hasChild("conversations")){
                                    for (Iterator<DataSnapshot> convsIter = ds.child("conversations").getChildren().iterator(); convsIter.hasNext();){
                                        DataSnapshot convds = convsIter.next();
                                        if(convds.getValue(String.class).equals(listConvID.get(i)) && !ds.getKey().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                                            User other = new User();
                                            other.setNickname(ds.child("nickname").getValue(String.class));
                                            other.setId(ds.getKey());
                                            listUserConv.add(other);
                                        }
                                    }

                                }
                            }

                        }

                    }else{
                        Toast.makeText(mainActivity,"Vous n'avez pas de conversations", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(mainActivity,"Vous n'avez pas de conversations", Toast.LENGTH_SHORT).show();
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        mainActivity.databaseFirebase.child("conversations").getRef().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(listConvID != null ){
                    if(listConvID.size()>0){
                        listChat.clear();
                        for(int i=0 ; i<listConvID.size();i++){
                            ArrayList<Message> mess = new ArrayList<>();
                            for (Iterator<DataSnapshot> messIter = dataSnapshot.child(listConvID.get(i)).child("messages").getChildren().iterator(); messIter.hasNext();){
                                DataSnapshot ds = messIter.next();
                                User sender;

                                if(ds.child("userId").exists() && ds.child("userId").getValue(String.class).equals(connectedUser.getId())){
                                    sender = connectedUser;
                                }else{
                                    sender = listUserConv.get(i);
                                }
                                if(ds.child("timestamp").exists() && ds.child("content").exists()){
                                    Message message = new Message(ds.getKey(),new Date(Long.valueOf(ds.child("timestamp").getValue(String.class))),sender,ds.child("content").getValue(String.class));
                                    mess.add(message);
                                }

                            }
                            Chat chat = new Chat(listConvID.get(i),connectedUser,listUserConv.get(i),mess);
                            listChat.add(chat);

                        }

                    }else{
                        Toast.makeText(mainActivity,"Vous n'avez pas de conversations", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(mainActivity,"Vous n'avez pas de conversations", Toast.LENGTH_SHORT).show();
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


}
