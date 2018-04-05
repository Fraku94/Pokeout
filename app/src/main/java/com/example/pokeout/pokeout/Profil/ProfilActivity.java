package com.example.pokeout.pokeout.Profil;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.pokeout.pokeout.LoginRegister.LoginActivity;
import com.example.pokeout.pokeout.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class ProfilActivity extends AppCompatActivity {

    android.widget.ProgressBar fff;

    private TextView mBrithProfil, mRadiusProfil, mCityProfil, mSexUserProfil;
    View dialogView;
    int yearx, monthx, dayx;

    private ImageView mImageProfil;

    private SeekBar mRadiusSeekBarProfil;

    private FirebaseAuth mAuth;
    private DatabaseReference mUserDatabase;
    private EditText mNameProfil, mPhoneProfil, mDescriptionProfil, reauth, passwordInput, userInput;
    private String userId, username, userphone, userprofileImageUrl, userSex, userbrith, userDescription, userRadius, userCity;

    private Uri resultUri;
    private Button mConfirmProfil, mBackProfil, mdelete;
    private FirebaseUser user;
    static final int DIALOG_ID = 0;
    private DatePickerDialog.OnDateSetListener dpickerListner = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            yearx = year;
            monthx = month + 1;
            dayx = dayOfMonth;
            mBrithProfil.setText(Integer.toString(yearx) + "/" + Integer.toString(monthx) + "/" + Integer.toString(dayx));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);

        fff = findViewById(R.id.progressBar1);
        //EditText
        mNameProfil = findViewById(R.id.nameProfil);
        mPhoneProfil = findViewById(R.id.phoneProfil);
        mDescriptionProfil = findViewById(R.id.descriptionProfil);

        //TextView
        mBrithProfil = findViewById(R.id.brithProfil);
        mRadiusProfil = findViewById(R.id.radiusProfil);
        mCityProfil = findViewById(R.id.cityProfil);
        mSexUserProfil = findViewById(R.id.sexUserProfil);

        //Button
        mConfirmProfil = findViewById(R.id.confirmProfil);
        mBackProfil = findViewById(R.id.backProfil);
        mdelete = findViewById(R.id.delete);
        //reauth = findViewById(R.id.aut);

        //ImageView
        mImageProfil = findViewById(R.id.imageProfil);


        //SeekBar
        mRadiusSeekBarProfil = findViewById(R.id.radiusseekBarProfil);

        mRadiusSeekBarProfil.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                userRadius = String.valueOf(progress);
                mRadiusProfil.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        //FirebaseAuth
        mAuth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        //Przypisanie id Uzytkownika
        userId = mAuth.getCurrentUser().getUid();

        //DatabaseReference
        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);

        LayoutInflater li = LayoutInflater.from(ProfilActivity.this);
        dialogView = li.inflate(R.layout.custom_dialog, null);
        //Pobranie informacji o Uzytkowniku
        getUserInfo();


        //Usatwienie daty kalendarza
        final Calendar calendar = Calendar.getInstance();
        yearx = (calendar.get(Calendar.YEAR)) - 18;
        monthx = calendar.get(Calendar.MONTH);
        dayx = calendar.get(Calendar.DAY_OF_MONTH);


        mdelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("tag", "User start dialog.");
                alert();
                Log.e("tag", "User start delete.");


            }
        });


        //Usatwienie Daty
        ShowDialogOnButtonClick();

        //Wywołanie galerii i załadowanie do ImageView
        mImageProfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 1);
            }
        });

        //Button Confirm
        mConfirmProfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Metoda zapisująca nowe informacje
                saveUserInformation();
            }
        });


        //Button Back
        mBackProfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                return;
            }
        });
    }

    public void alert() {
        Log.e("tag", "User start dialog in.");
        // inflate alert dialog xml
        LayoutInflater li = LayoutInflater.from(ProfilActivity.this);
        View dialogView = li.inflate(R.layout.custom_dialog, null);


        userInput = dialogView
                .findViewById(R.id.eEmail);
        passwordInput = dialogView
                .findViewById(R.id.ePassword);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                ProfilActivity.this, R.style.MyDialogTheme);
        // set title
        alertDialogBuilder.setTitle("Custom Dialog");
        // set custom dialog icon
        alertDialogBuilder.setIcon(R.drawable.user);
        // set custom_dialog.xml to alertdialog builder
        alertDialogBuilder.setView(dialogView);


        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int id) {
                                if (!validate()) {
                                    Toast.makeText(ProfilActivity.this, "Fill empty ", Toast.LENGTH_SHORT).show();
                                    Log.w("tag", "Validate false empty ");
                                } else {
                                    Log.w("tag", "get info");
                                    String email = userInput.getText().toString();
                                    String password = passwordInput.getText().toString();

// Get auth credentials from the user for re-authentication. The example below shows
// email and password credentials but there are multiple possible providers,
// such as GoogleAuthProvider or FacebookAuthProvider.

                                    AuthCredential credential = EmailAuthProvider.getCredential(email, password);
                                    Log.w("tag", "User re-authenticated.start");
// Prompt the user to re-provide their sign-in credentials
                                    user.reauthenticate(credential)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    Log.e("tag", "User re-authenticated.");
                                                    Log.w("tag", "User re-authenticated start metoda delete.");
                                                    deleteAccount();
                                                }

                                            });
                                }


                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int id) {
                                dialog.cancel();
                            }
                        });
        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        // show it
        alertDialog.show();
    }

    public boolean validate() {
        boolean valid = true;

        String email = userInput.getText().toString();
        String password = passwordInput.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            userInput.setError("enter a valid email address");
            valid = false;
        } else {
            userInput.setError(null);
        }

        if (password.isEmpty() || password.length() < 6 || password.length() > 10) {
            passwordInput.setError("between 6 and 10 alphanumeric characters");
            valid = false;
        } else {
            passwordInput.setError(null);
        }

        return valid;
    }
//        });
//    }


//    private  void reauth(){
//
//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//
//// Get auth credentials from the user for re-authentication. The example below shows
//// email and password credentials but there are multiple possible providers,
//// such as GoogleAuthProvider or FacebookAuthProvider.
//
//        AuthCredential credential = EmailAuthProvider.getCredential(e,password.);
//        Log.e("tag", "User re-authenticated.start");
//// Prompt the user to re-provide their sign-in credentials
//        user.reauthenticate(credential)
//                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        Log.e("tag", "User re-authenticated.");
//                    }
//
//                });
//        Log.e("tag", "User re-authenticated dupaa.");
//    }

    private void deleteAccount() {
        final ProgressDialog progressDialog = new ProgressDialog(ProfilActivity.this, R.style.MyDialogTheme);
        progressDialog.setTitle("Delete Account");
// Progress Dialog Style Spinner
        Log.w("tag", "User dialog .");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("Wait Deleting...");
        progressDialog.show();


        progressDialog.setCancelable(true);
        new Thread(new Runnable() {
            public void run() {
                try {
                    Thread.sleep(10000);
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                    mUserDatabase.removeValue();


                    user.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Intent mainintent = new Intent(ProfilActivity.this, LoginActivity.class);
                            startActivity(mainintent);
                            finish();
                            return;
                        }
                    });
                } catch (Exception e) {
                    progressDialog.setMessage("Error Account not delete ");
                    Toast.makeText(ProfilActivity.this, "Error Account not delete ", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
                progressDialog.dismiss();
            }
        }).start();


    }

    private void getUserInfo() {

        mUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                    //Pobranie Imienia itd.
                    if (map.get("name") != null) {
                        username = map.get("name").toString();
                        mNameProfil.setText(username);
                    }
                    if (map.get("brith") != null) {
                        userbrith = map.get("brith").toString();
                        mBrithProfil.setText(userbrith);
                    }
                    if (map.get("phone") != null) {
                        userphone = map.get("phone").toString();
                        mPhoneProfil.setText(userphone);
                    }
                    if (map.get("description") != null) {
                        userDescription = map.get("description").toString();
                        mDescriptionProfil.setText(userDescription);
                    }

                    if (map.get("sex") != null) {
                        userSex = map.get("sex").toString();
                        switch (userSex) {
                            case "Male":
                                mSexUserProfil.setText("Mężczyzna");
                                break;
                            case "Female":
                                mSexUserProfil.setText("Kobieta");
                                break;
                        }

                    }
                    if (map.get("city") != null) {
                        userCity = map.get("city").toString();
                        mCityProfil.setText(userCity);
                    }
                    if (map.get("radius") != null) {
                        userRadius = map.get("radius").toString();
                        mRadiusProfil.setText(userRadius);
                        mRadiusSeekBarProfil.setProgress(Integer.parseInt(userRadius));
                    }

                    //Załadowanie zdjecia
                    Glide.clear(mImageProfil);
                    if (map.get("profileImageUrl") != null) {
                        userprofileImageUrl = map.get("profileImageUrl").toString();
                        switch (userprofileImageUrl) {
                            case "default":
                                Glide.with(getApplication()).load(R.mipmap.ic_default).into(mImageProfil);
                                break;
                            default:
                                Glide.with(getApplication()).load(userprofileImageUrl).into(mImageProfil);
                                break;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void saveUserInformation() {

        //Przypisanie wartosci z "okienek" do zmiennych
        username = mNameProfil.getText().toString();
        userphone = mPhoneProfil.getText().toString();
        userbrith = mBrithProfil.getText().toString();
        userDescription = mDescriptionProfil.getText().toString();
        userRadius = mRadiusProfil.getText().toString();

        //Map dodaje do bazy Firebase
        Map userInfo = new HashMap();
        userInfo.put("name", username);
        userInfo.put("phone", userphone);
        userInfo.put("description", userDescription);
        userInfo.put("radius", userRadius);
        userInfo.put("brith", userbrith);

        //Metoda wywoluje zapis do bazy
        mUserDatabase.updateChildren(userInfo);

        //Do zapisanie zdjecia
        if (resultUri != null) {
            StorageReference filepath = FirebaseStorage.getInstance().getReference().child("profileImages").child(userId);
            Bitmap bitmap = null;

            try {
                bitmap = MediaStore.Images.Media.getBitmap(getApplication().getContentResolver(), resultUri);
            } catch (IOException e) {
                e.printStackTrace();
            }

            //Do zapisanie zdjecia
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
            byte[] data = baos.toByteArray();
            UploadTask uploadTask = filepath.putBytes(data);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    finish();
                }
            });
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Uri downloadUrl = taskSnapshot.getDownloadUrl();

                    //Zapisanie adresu URL zdjecia do bazy
                    Map userInfo = new HashMap();
                    userInfo.put("profileImageUrl", downloadUrl.toString());

                    //Metoda wywoluje zapis do bazy
                    mUserDatabase.updateChildren(userInfo);
                    finish();
                    return;
                }
            });
        } else {
            finish();
        }
    }

    //Do zapisanie zdjecia
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            final Uri imageUri = data.getData();
            resultUri = imageUri;
            mImageProfil.setImageURI(resultUri);
        }
    }

    //Kalnedarz....
    public void ShowDialogOnButtonClick() {
        mBrithProfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DIALOG_ID);
            }
        });
    }

    @Override
    protected Dialog onCreateDialog(int id) {

        if (id == DIALOG_ID) {
            return new DatePickerDialog(this, dpickerListner, yearx, monthx, dayx);
        } else {
            return null;
        }

    }

}
