package com.example.e_commerceapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.e_commerceapp.Prevalent.Prevalent;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsActivity extends AppCompatActivity {
    private TextView Close,Update;
    private CircleImageView Pro_Pic;
    private EditText Phone,Name, Address;
    private Uri image_uri;
    private  String image_url=null;
    private UploadTask uploadTask;
    private StorageReference profile_pic_ref;
    private String checker = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Close = findViewById(R.id.settings_closeID);
        Update = findViewById(R.id.settings_updateID);

        Pro_Pic = findViewById(R.id.settings_profile_picID);
        Phone = findViewById(R.id.settings_phone_numberID);
        Name = findViewById(R.id.settings_full_nameID);
        Address = findViewById(R.id.settings_addressID);

        profile_pic_ref = FirebaseStorage.getInstance().getReference().child("profile pictures");

        user_info_display(Phone,Name,Address);

        Close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checker == "clicked") {
                    user_info_saved();

                } else {
                    update_only_user_info();

                }
            }
        });

        Pro_Pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checker = "clicked";
                CropImage.activity(image_uri).setAspectRatio(1, 1)
                        .start(SettingsActivity.this);

            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            image_uri = result.getUri();
            Pro_Pic.setImageURI(image_uri);
        } else {
        }
    }

    private void update_only_user_info() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users");
        HashMap<String, Object> user_map = new HashMap<>();
        user_map.put("name", Name.getText().toString());
        user_map.put("address", Address.getText().toString());
        user_map.put("phoneOrder", Phone.getText().toString());

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Updating info");
        progressDialog.setMessage("Wait till updating info");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        ref.child(Prevalent.current_online_users.getPhone()).updateChildren(user_map);
        progressDialog.dismiss();
        Toast.makeText(this, "User info updated successfully", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(SettingsActivity.this, HomeActivity.class));
        finish();
    }

    private void user_info_saved() {
        if (TextUtils.isEmpty(Phone.getText().toString())) {
            Toast.makeText(this, "Phone number is empty", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(Name.getText().toString())) {
            Toast.makeText(this, "Name  is empty", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(Address.getText().toString())) {
            Toast.makeText(this, "Address  is empty", Toast.LENGTH_SHORT).show();
        }
        else if (checker.equals("clicked")) {
            upload_image();
        }

    }

    private void upload_image() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading Image");
        progressDialog.setMessage("Wait till uploading image ");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        if (image_uri != null) {
            final StorageReference file_ref = profile_pic_ref.child(Prevalent.current_online_users.getPhone() + ".jpg");
            uploadTask = file_ref.putFile(image_uri);

            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Task<? extends Object> then(@NonNull Task task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return file_ref.getDownloadUrl();
                }
            })
                    .addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            if (task.isSuccessful()) {
                                Uri my_url = (Uri) task.getResult();
                                image_url = my_url.toString();

                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users");
                                HashMap<String, Object> user_map = new HashMap<>();
                                user_map.put("name", Name.getText().toString());
                                user_map.put("address", Address.getText().toString());
                                user_map.put("phoneOrder", Phone.getText().toString());
                                user_map.put("image", image_url);
                                ref.child(Prevalent.current_online_users.getPhone()).updateChildren(user_map);


                                progressDialog.dismiss();
                                startActivity(new Intent(SettingsActivity.this, HomeActivity.class));
                                Toast.makeText(SettingsActivity.this, "User info updated successfully", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(SettingsActivity.this, "Error Occurred", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            Toast.makeText(this, "Image is not selected ", Toast.LENGTH_SHORT).show();
        }




    }

    private void user_info_display(EditText phone, EditText name, EditText address) {

        DatabaseReference user_ref = FirebaseDatabase.getInstance().getReference().child("Users").child(Prevalent.current_online_users.getPhone());
        user_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    if (snapshot.child("image").exists()){
                        String image = snapshot.child("image").getValue().toString();
                        String phone = snapshot.child("phone").getValue().toString();
                        String phone_order = snapshot.child("phoneOrder").getValue().toString();
                        String name = snapshot.child("name").getValue().toString();
                        String address = snapshot.child("address").getValue().toString();

                        Picasso.get().load(image).into(Pro_Pic);
                        Phone.setText(phone_order);
                        Name.setText(name);
                        Address.setText(address);


                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}