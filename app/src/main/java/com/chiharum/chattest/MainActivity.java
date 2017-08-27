package com.chiharum.chattest;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    TextView textView;
    EditText editText;

    DatabaseReference reference;

    String text;

    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView)findViewById(R.id.textView);
        editText = (EditText)findViewById(R.id.editText);

        auth = FirebaseAuth.getInstance();

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth){
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user == null){
                    Log.i("log", "user==null");
                    Intent intent = new Intent();
                    intent.setClass(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                }else{
                    Log.i("log", "Already Logged in, Uid=" + user.getUid());
                }
            }
        };

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        reference = database.getReference("message");

        reference.setValue("Welcome!");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                Log.i("Value", "value=" + value);

                if(text != null){
                    text += value + "\n";
                }else{
                    text = value + "\n";
                }
                textView.setText(text);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("Failed", "Failed", databaseError.toException());
            }
        });
    }

    public void send(View view){
        SpannableStringBuilder builder = (SpannableStringBuilder)editText.getText();
        String input = builder.toString();
        reference.setValue(input);
        Log.i("sent", "text=" + input);
        editText.setText("");
    }

    @Override
    public void onStart(){
        super.onStart();
        auth.addAuthStateListener(authStateListener);
    }

    @Override
    public void onStop(){
        super.onStop();
        if(authStateListener != null){
            auth.removeAuthStateListener(authStateListener);
        }
    }
}
