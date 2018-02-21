package com.example.pokeout.pokeout.CategoryAdd;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.pokeout.pokeout.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CategoryAddActivity extends AppCompatActivity {

    private Button mConfirm, mBack;

    private EditText mDescription,mName;

    private ImageView mImage;

    private FirebaseAuth mAuth;
    private DatabaseReference mCategoryDatabase, mUsersDatabase;

    private String userId, categoryId, categoryName, categoryDescription;

    private Uri resultUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_add);

        //EditText
        mName = (EditText)findViewById(R.id.nameCategoryAdd);
        mDescription = (EditText)findViewById(R.id.descriptionCategoryAdd);

        //Button
        mConfirm = (Button)findViewById(R.id.confirmCategoryAdd);
        mBack = (Button)findViewById(R.id.backCategoryAdd);

        //ImageView
        mImage = (ImageView)findViewById(R.id.imageCategoryAdd);

        //FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        mUsersDatabase = FirebaseDatabase.getInstance().getReference();

        //Przypisanie id Uzytkownika
        userId = mAuth.getCurrentUser().getUid();

        categoryId = mUsersDatabase.child("Category").push().getKey();

        //DatabaseReference
        mCategoryDatabase = FirebaseDatabase.getInstance().getReference().child("Category").child(categoryId);


        //Wywołanie galerii i załadowanie do ImageView
        mImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,1);
            }
        });


        //Button Confirm
        mConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Metoda zapisująca nowe informacje
                saveCategory();
            }
        });

        //Button Back
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                return;
            }
        });
    }



    private void saveCategory() {

        categoryName = mName.getText().toString();
        categoryDescription = mDescription.getText().toString();

        //Map dodaje do bazy
        Map userInfo = new HashMap();
        userInfo.put("name", categoryName);
        userInfo.put("description", categoryDescription);
        mCategoryDatabase.updateChildren(userInfo);
        mCategoryDatabase.child("users").child(userId).setValue(true);
        mUsersDatabase.child("Users").child(userId).child("category").child(categoryId).setValue(true);

        if(resultUri != null){
            StorageReference filepath = FirebaseStorage.getInstance().getReference().child("categoryImages").child(categoryId);
            Bitmap bitmap = null;

            try {
                bitmap = MediaStore.Images.Media.getBitmap(getApplication().getContentResolver(), resultUri);
            } catch (IOException e) {
                e.printStackTrace();
            }

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

                    Map userInfo = new HashMap();
                    userInfo.put("categoryImageUrl", downloadUrl.toString());
                    mCategoryDatabase.updateChildren(userInfo);

                    finish();
                    return;
                }
            });
        }else{
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == Activity.RESULT_OK){
            final Uri imageUri = data.getData();
            resultUri = imageUri;
            mImage.setImageURI(resultUri);
        }
    }
}
