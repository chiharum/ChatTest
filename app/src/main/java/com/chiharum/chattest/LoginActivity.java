package com.chiharum.chattest;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseAuth.AuthStateListener authStateListener;

    EditText mailEditText, passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mailEditText = (EditText)findViewById(R.id.mailEditText);
        passwordEditText = (EditText)findViewById(R.id.passwordEditText);

        auth = FirebaseAuth.getInstance();

//        authStateListener = new FirebaseAuth.AuthStateListener() {
//            @Override
//            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth){
//                FirebaseUser user = firebaseAuth.getCurrentUser();
//                if(user == null){
//                    Log.i("log", "user==null");
//                    Toast.makeText(LoginActivity.this, "Have to login", Toast.LENGTH_SHORT).show();
//                }else{
//                    Log.i("log", "Already Logged in, Uid=" + user.getUid());
//                    Toast.makeText(LoginActivity.this, "uid=" + user.getUid(), Toast.LENGTH_SHORT).show();
//                }
//            }
//        };
    }

    public void createAccount(View view){
        buttonPushedAction(true);
    }

    public void login(View view){
        buttonPushedAction(false);
    }

    public void buttonPushedAction(boolean isCreate){
        SpannableStringBuilder builder = (SpannableStringBuilder)mailEditText.getText();
        String email = builder.toString();

        builder.clear();

        builder = (SpannableStringBuilder)passwordEditText.getText();
        String password = builder.toString();

        if (isCreate){
            create(email, password);
        }else{
            signIn(email, password);
        }
    }

    public void create(String email, String password){

        Toast.makeText(this, "email=" + email + ", password=" + password, Toast.LENGTH_SHORT).show();

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                Log.i("log", "createUserWithEmail:onComplete:" + String.valueOf(task.isSuccessful()));

                if (!task.isSuccessful()){
                    Toast.makeText(LoginActivity.this, "Failed to auth login", Toast.LENGTH_SHORT).show();
                }else{
                    goToMainActivity();
                }
            }
        });
    }

    public void signIn (String email, String password){

        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                Log.i("log", "signInWithEmail:onComplete:" + task.isSuccessful());

                if (!task.isSuccessful()){
                    Log.w("log", "signInWithEmail:failed", task.getException());
                    Toast.makeText(LoginActivity.this, "Sign In With Email failed", Toast.LENGTH_SHORT).show();
                }else{
                    goToMainActivity();
                }
            }
        });
    }

    public void goToMainActivity(){
        Intent intent = new Intent();
        intent.setClass(LoginActivity.this, MainActivity.class);
        startActivity(intent);
    }

//    @Override
//    public void onStart(){
//        super.onStart();
//        auth.addAuthStateListener(authStateListener);
//    }
//
//    @Override
//    public void onStop(){
//        super.onStop();
//        if(authStateListener != null){
//            auth.removeAuthStateListener(authStateListener);
//        }
//    }
}
