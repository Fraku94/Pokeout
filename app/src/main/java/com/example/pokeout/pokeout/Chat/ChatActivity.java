package com.example.pokeout.pokeout.Chat;

import android.content.ContentResolver;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.pokeout.pokeout.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mChatAdapter;
    private RecyclerView.LayoutManager mChatLayoutManager;


    private EditText mSendEditText;

    private Button mSendButton;

    private String currentUserID, ConnectId, chatId, timeH;
    private int  timeD, timeM, timeY;

    DatabaseReference mDatabaseUser, mDatabaseChat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        ConnectId = getIntent().getExtras().getString("ConnectId");

        currentUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        mDatabaseUser = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserID).child("follow").child("connect").child(ConnectId).child("ChatId");
        mDatabaseChat = FirebaseDatabase.getInstance().getReference().child("Chat");

        getData();
        getChatId();

        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setHasFixedSize(false);
//        mRecyclerView.scrollToPosition(resultsChat.size()-1);


        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ChatActivity.this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mChatAdapter = new ChatAdapter(getDataSetChat(), ChatActivity.this);
        mRecyclerView.setAdapter(mChatAdapter);

        mSendEditText = findViewById(R.id.message);
        mSendButton = findViewById(R.id.send);




        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sendMessage();
            }
        });

    }

    private void sendMessage() {
        String sendMessageText = mSendEditText.getText().toString();

        if(!sendMessageText.isEmpty()){
            DatabaseReference newMessageDb = mDatabaseChat.push();

            Map newMessage = new HashMap();
            newMessage.put("createdByUser", currentUserID);
            newMessage.put("text", sendMessageText);
            newMessage.put("timeHour", timeH);
            newMessage.put("timeDay", timeD);
            newMessage.put("timeMonth", timeM);
            newMessage.put("timeYear", timeY);
            newMessageDb.setValue(newMessage);
        }
        mSendEditText.setText(null);
    }

    private void getChatId(){
        mDatabaseUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    chatId = dataSnapshot.getValue().toString();
                    mDatabaseChat = mDatabaseChat.child(chatId);
                    getChatMessages();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getData(){


        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        Calendar calendar = Calendar.getInstance();
        Date date = Calendar.getInstance().getTime();
        String sDate = format.format(date);
        int mYear = calendar.get(Calendar.YEAR);//9999
        int mMonth = calendar.get(Calendar.MONTH);
        mMonth = mMonth + 1;//12
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hrs = calendar.get(Calendar.HOUR_OF_DAY);//24
        int min = calendar.get(Calendar.MINUTE);//59

        timeH = ((hrs)+":"+Integer.toString(min));
        timeD = day;
        timeM = mMonth;
        timeY = mYear;


    }
    private void getChatMessages() {
        mDatabaseChat.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if(dataSnapshot.exists()){
                    String message = null;
                    String timeh = null;
                    int timed = 0;
                    int timem = 0;
                    int timey = 0;
                    String createdByUser = null;

                    if(dataSnapshot.child("text").getValue()!=null){
                        message = dataSnapshot.child("text").getValue().toString();
                    }
                    if(dataSnapshot.child("timeHour").getValue()!=null){
                        timeh = dataSnapshot.child("timeHour").getValue().toString();
                    }
                    if(dataSnapshot.child("timeDay").getValue()!=null){
                        timed = Integer.valueOf(dataSnapshot.child("timeDay").getValue().toString());
                    }
                    if(dataSnapshot.child("timeMonth").getValue()!=null){
                        timem = Integer.valueOf(dataSnapshot.child("timeMonth").getValue().toString());
                    }
                    if(dataSnapshot.child("timeYear").getValue()!=null){
                        timey = Integer.valueOf(dataSnapshot.child("timeYear").getValue().toString());
                    }
                    if(dataSnapshot.child("createdByUser").getValue()!=null){
                        createdByUser = dataSnapshot.child("createdByUser").getValue().toString();
                    }

                    if(message!=null && createdByUser!=null){
                        Boolean currentUserBoolean = false;
                        if(createdByUser.equals(currentUserID)){
                            currentUserBoolean = true;
                        }
                        ChatObject newMessage = new ChatObject(message,timeh, timed, timem, timey, currentUserBoolean);
                        resultsChat.add(newMessage);
                        mChatAdapter.notifyDataSetChanged();
                    }
                }
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private ArrayList<ChatObject> resultsChat = new ArrayList<ChatObject>();
    private List<ChatObject> getDataSetChat() {
        return resultsChat;
    }
}