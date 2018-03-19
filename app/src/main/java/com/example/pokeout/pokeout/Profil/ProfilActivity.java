package com.example.pokeout.pokeout.Profil;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.pokeout.pokeout.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
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

    private EditText mNameProfil, mPhoneProfil, mDescriptionProfil;

    private TextView mBrithProfil;

    private Button mConfirmProfil, mBackProfil;

    private RadioGroup mRadioGroupProfil;

    private RadioButton mFemaleProfil, mMaleProfil;

    private ImageView mImageProfil;

    private FirebaseAuth mAuth;
    private DatabaseReference mUserDatabase;

    private String userId, username, userphone, userprofileImageUrl, userSex, userbrith, userDescription;

    private Uri resultUri;

    int yearx,monthx,dayx;
    static final int DIALOG_ID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);


        //EditText
        mNameProfil = (EditText)findViewById(R.id.nameProfil);
        mPhoneProfil = (EditText)findViewById(R.id.phoneProfil);
        mDescriptionProfil = (EditText)findViewById(R.id.descriptionProfil);

        //TextView
        mBrithProfil = (TextView)findViewById(R.id.brithProfil);

        //Button
        mConfirmProfil = (Button)findViewById(R.id.confirmProfil);
        mBackProfil = (Button)findViewById(R.id.backProfil);

        //ImageView
        mImageProfil = (ImageView)findViewById(R.id.imageProfil);

        //RadioGroup
//        mRadioGroupProfil = (RadioGroup)findViewById(R.id.radioGroupProfil);

        //RadioButton
//        mFemaleProfil = (RadioButton)findViewById(R.id.rbFemaleProfil);
//        mMaleProfil = (RadioButton)findViewById(R.id.rbMaleProfil);

        //FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        //Przypisanie id Uzytkownika
        userId = mAuth.getCurrentUser().getUid();

        //DatabaseReference
        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);

        //Pobranie informacji o Uzytkowniku
        getUserInfo();

        //Usatwienie daty kalendarza
        final Calendar calendar = Calendar.getInstance();
        yearx = (calendar.get(Calendar.YEAR))-18;
        monthx = calendar.get(Calendar.MONTH);
        dayx = calendar.get(Calendar.DAY_OF_MONTH);

        //Usatwienie Daty
        ShowDialogOnButtonClick();

        //Wywołanie galerii i załadowanie do ImageView
        mImageProfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,1);
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



    private void getUserInfo() {

        mUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists() && dataSnapshot.getChildrenCount()>0){
                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                    //Pobranie Imienia itd.
                    if(map.get("name")!=null){
                        username = map.get("name").toString();
                        mNameProfil.setText(username);
                    }
                    if(map.get("brith")!=null){
                        userbrith = map.get("brith").toString();
                        mBrithProfil.setText(userbrith);
                    }
                    if(map.get("phone")!=null){
                        userphone = map.get("phone").toString();
                        mPhoneProfil.setText(userphone);
                    }
                    if(map.get("description")!=null){
                        userDescription = map.get("description").toString();
                        mDescriptionProfil.setText(userDescription);
                    }
//                    if(map.get("sex")!=null){
//                        userSex = map.get("sex").toString();
//                        switch (userSex){
//                            case "Male":
//                                mMaleProfil.setChecked(true);
//                                mFemaleProfil.setChecked(false);
//                                break;
//                            case "Female":
//                                mMaleProfil.setChecked(false);
//                                mFemaleProfil.setChecked(true);
//                                break;
//                        }
//
//                    }
                    //Załadowanie zdjecia
                    Glide.clear(mImageProfil);
                    if(map.get("profileImageUrl")!=null){
                        userprofileImageUrl = map.get("profileImageUrl").toString();
                        switch(userprofileImageUrl){
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
//        userSex = radioButton.getText().toString();
        userbrith = mBrithProfil.getText().toString();
        userDescription = mDescriptionProfil.getText().toString();

        //Map dodaje do bazy Firebase
        Map userInfo = new HashMap();
        userInfo.put("name", username);
        userInfo.put("phone", userphone);
        userInfo.put("description", userDescription);

        //Sprawdzenie ktory radiobutton jest wybrany
//        if (mMaleProfil.isChecked()){
//            userInfo.put("sex", "Male");
//        }else{
//            userInfo.put("sex", "Female");
//        }
        userInfo.put("brith", userbrith);

        //Metoda wywoluje zapis do bazy
        mUserDatabase.updateChildren(userInfo);

        //Do zapisanie zdjecia
        if(resultUri != null){
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
        }else{
            finish();
        }
    }

    //Do zapisanie zdjecia
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == Activity.RESULT_OK){
            final Uri imageUri = data.getData();
            resultUri = imageUri;
            mImageProfil.setImageURI(resultUri);
        }
    }

//Kalnedarz....
    public void ShowDialogOnButtonClick(){
        mBrithProfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DIALOG_ID);
            }
        });
    }
    @Override
    protected Dialog onCreateDialog(int id) {

        if(id == DIALOG_ID){
            return new DatePickerDialog(this,dpickerListner, yearx,monthx,dayx);
        }else {
            return null;
        }

    }

    private DatePickerDialog.OnDateSetListener dpickerListner = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            yearx = year;
            monthx = month+1;
            dayx = dayOfMonth;
            mBrithProfil.setText(Integer.toString(yearx)+"/"+Integer.toString(monthx)+"/"+Integer.toString(dayx));
        }
    };

}
