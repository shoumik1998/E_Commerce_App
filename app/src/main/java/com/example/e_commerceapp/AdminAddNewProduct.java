package com.example.e_commerceapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AdminAddNewProduct extends AppCompatActivity {
    private ImageView Select_product_imageView;
    private EditText Product_Name,Product_Descriptions,Product_Price;
    private Button Add_Product_Btn;
    private String category,name,descriptions,price,save_current_date,save_current_time,product_random_key,download_image_url;
    private  static  final  int GALLERY_PICKER=1;
    private Uri image_uri;
    private  StorageReference product_image_ref;
    private DatabaseReference products_ref;
    private ProgressDialog loadingBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_new_product);

        Select_product_imageView=findViewById(R.id.add_new_product_imageID);
        Product_Name = findViewById(R.id.product_name_editID);
        Product_Descriptions = findViewById(R.id.product_descriptions_editID);
        Product_Price = findViewById(R.id.product_price_editID);
        Add_Product_Btn=findViewById(R.id.add_new_product_btnID);
        loadingBar=new ProgressDialog(this);


        category=getIntent().getStringExtra("Category");
        product_image_ref= FirebaseStorage.getInstance().getReference().child("product Images");
        products_ref=FirebaseDatabase.getInstance().getReference().child("Products");

        Select_product_imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                open_gallery();
            }
        });

        Add_Product_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validate_product_data();
            }
        });




    }

    private void validate_product_data() {
        name=Product_Name.getText().toString();
        descriptions=Product_Descriptions.getText().toString();
        price=Product_Price.getText().toString();

        if (image_uri==null){
            Toast.makeText(this, "Image is required", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(name)){
            Toast.makeText(this, "Product Name is required", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(descriptions)){
            Toast.makeText(this, "Product Description is required", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(price)){
            Toast.makeText(this, "Product Price is required", Toast.LENGTH_SHORT).show();
        }else {
            store_product_information();
        }

    }

    private void store_product_information() {
        loadingBar.setTitle("Adding new product");
        loadingBar.setMessage("Wait till saving product");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        Calendar calendar=Calendar.getInstance();
        SimpleDateFormat current_date = new SimpleDateFormat("MMM dd yyyy");
        save_current_date = current_date.format(calendar.getTime());

        SimpleDateFormat current_time = new SimpleDateFormat("HH:mm:ss a");
        save_current_time = current_time.format(calendar.getTime());
        product_random_key=save_current_date+save_current_time;

        final StorageReference file_path = product_image_ref.child(image_uri.getLastPathSegment() + product_random_key + ".jpg");

        final UploadTask uploadTask=file_path.putFile(image_uri);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AdminAddNewProduct.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(AdminAddNewProduct.this, "Image uploaded successfully ", Toast.LENGTH_SHORT).show();

                final Task<Uri> urlTask=uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()){
                            throw task.getException();
                        }
                        download_image_url=file_path.getDownloadUrl().toString();
                        return  file_path.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()){
                            download_image_url=task.getResult().toString();
                            Toast.makeText(AdminAddNewProduct.this, "got the image url successfully", Toast.LENGTH_SHORT).show();
                            save_product_info_to_database();
                        }

                    }
                });

            }
        });

    }

    private void save_product_info_to_database() {
        HashMap<String ,Object> product_map=new HashMap<>();

        product_map.put("pid", product_random_key);
        product_map.put("date", save_current_date);
        product_map.put("time", save_current_time);
        product_map.put("description", descriptions);
        product_map.put("image", download_image_url);
        product_map.put("category", category);
        product_map.put("price", price);
        product_map.put("pname", name);





        products_ref.child(product_random_key).updateChildren(product_map)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            loadingBar.dismiss();
                            Toast.makeText(AdminAddNewProduct.this, "Product is uploaded successfully", Toast.LENGTH_SHORT).show();
                        }else {
                            loadingBar.dismiss();
                            Toast.makeText(AdminAddNewProduct.this, ""+task.getException().toString(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    private void open_gallery() {
        Intent gallery_intent=new Intent();
        gallery_intent.setAction(Intent.ACTION_GET_CONTENT);
        gallery_intent.setType("image/*");
        startActivityForResult(gallery_intent,GALLERY_PICKER);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==GALLERY_PICKER && resultCode==RESULT_OK && data!=null){
            image_uri=data.getData();
            Select_product_imageView.setImageURI(image_uri);


        }
    }
}