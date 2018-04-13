package com.example.pokeout.pokeout.LoginRegister;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pokeout.pokeout.MainActivity;
import com.example.pokeout.pokeout.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private Button bregister;
    private TextView linklogin;
    private EditText etEmail,etPassword,etName;
    private RadioGroup radioGroup;

    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        auth =FirebaseAuth.getInstance();
        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {

            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                //**Jezeli uzytkownik nie istnieje w bazie to stworz i zaloguj do okna glownego **//

                if (user !=null){
                    Intent mainintent = new Intent(RegisterActivity.this,MainActivity.class);
                    startActivity(mainintent);
                    finish();
                    return;
                }
            }
        };



        bregister = findViewById(R.id.bRegister);
        linklogin= findViewById(R.id.linkLogin);
        etEmail= findViewById(R.id.etEmail);
        etPassword= findViewById(R.id.etPassword);
        etName= findViewById(R.id.etName);

        radioGroup = findViewById(R.id.radioGroup);



        //** po kliknieciiu przejdz do actyvity rejestracji **//
        bregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int selectId = radioGroup.getCheckedRadioButtonId();

                final RadioButton radioButton = findViewById(selectId);




                if (!validate()) {
                    Toast.makeText(RegisterActivity.this, "Fill empty ", Toast.LENGTH_SHORT).show();

                }else {


                Log.d("tag", "Register");


                final String email = etEmail.getText().toString();
                final String password = etPassword.getText().toString();
                final String name = etName.getText().toString();
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful()){
                            Toast.makeText(RegisterActivity.this, "sign up error", Toast.LENGTH_SHORT).show();
                        }else{
                            String userId = auth.getCurrentUser().getUid();
                            DatabaseReference currentUserDb = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);
                            Map userInfo = new HashMap<>();
                            userInfo.put("name", name);
                            userInfo.put("sex", radioButton.getText().toString());
                            userInfo.put("profileImageUrl", "default");
                            currentUserDb.updateChildren(userInfo);
                        }
                    }
                });
                }
            }
        });

        //** po kliknieciiu przejdz do actyvity okno główne **//
        linklogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent linkloginintent = new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(linkloginintent);
                finish();
                return;
            }
        });
    }



    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

    }



    public boolean validate() {
        boolean valid = true;

        String name = etName.getText().toString();
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            etName.setError("at least 3 characters");
            valid = false;
        } else {
            etName.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("enter a valid email address");
            valid = false;
        } else {
            etEmail.setError(null);
        }

        if (password.isEmpty() || password.length() < 6 || password.length() > 10) {
            etPassword.setError("between 6 and 10 alphanumeric characters");
            valid = false;
        } else {
            etPassword.setError(null);
        }
        if (radioGroup.getCheckedRadioButtonId() == -1)
        {
            Toast.makeText(RegisterActivity.this, "Select your gender ", Toast.LENGTH_SHORT).show();
            valid = false;
        }

        return valid;
    }




    @Override
    protected void onStart() {
        super.onStart();
        auth.addAuthStateListener(firebaseAuthListener);

    }

    @Override
    protected void onStop() {
        super.onStop();
        auth.removeAuthStateListener(firebaseAuthListener);
    }
}