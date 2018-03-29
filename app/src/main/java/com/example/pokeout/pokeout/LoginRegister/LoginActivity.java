package com.example.pokeout.pokeout.LoginRegister;

import android.content.Intent;
import android.graphics.DashPathEffect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pokeout.pokeout.MainActivity;
import com.example.pokeout.pokeout.R;
import com.example.pokeout.pokeout.Services.MyFirebaseIdService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;


public class LoginActivity extends AppCompatActivity {



    private Button blogin;
    private TextView tvregister,tvForgotPassword;
    ProgressBar progressBar;
    private EditText etEmail, etPassword;

    private DatabaseReference UserDb;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;


    public LoginActivity() {
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        UserDb = FirebaseDatabase.getInstance().getReference().child("Users");
        mAuth = FirebaseAuth.getInstance();


        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {

            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                //**Jezeli uzytkownik nie istnieje w bazie to stworz i zaloguj do okna glownego **//

                if (user != null) {

                    String CurrentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    String CurrentUserToken = FirebaseInstanceId.getInstance().getToken();

                    UserDb.child(CurrentUserId).child("deviceToken").child(CurrentUserToken).setValue(true).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            Intent mainintent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(mainintent);
                            finish();
                            return;

                        }
                    });
                }
            }
        };


        blogin = (Button) findViewById(R.id.bLogin);
        final AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);
        tvregister = (TextView) findViewById(R.id.tvSign);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);
        tvForgotPassword = (TextView) findViewById(R.id.tvForgotPassword);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        //** po kliknieciiu zaloguj  **//

        blogin.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                v.startAnimation(buttonClick);
                Log.d("tag", "Login");
                if (!validate()) {
                    Toast.makeText(LoginActivity.this, "Fill empty ", Toast.LENGTH_SHORT).show();
                }else {
                    progressBar.setVisibility(View.VISIBLE);
                    final String email = etEmail.getText().toString();
                    final String password = etPassword.getText().toString();
                    mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                Toast.makeText(LoginActivity.this, "login error", Toast.LENGTH_SHORT).show();
                            }
                            progressBar.setVisibility(View.GONE);
                        }
                    });
                }
            }
        });


        tvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent linkresetinintent = new Intent(LoginActivity.this, ResetPassword.class);
                startActivity(linkresetinintent);
                finish();
                return;
            }
        });


        //** po kliknieciiu przejdz do actyvity   rjestracji  **//
        tvregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent linkloginintent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(linkloginintent);
                finish();
                return;
            }
        });
    }




    public boolean validate() {
        boolean valid = true;

        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();

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

        return valid;
    }






    @Override
    public void onBackPressed() {
        // disable going back to the MainActivity
        moveTaskToBack(true);
    }



    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(firebaseAuthListener);

    }



    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(firebaseAuthListener);
    }
}