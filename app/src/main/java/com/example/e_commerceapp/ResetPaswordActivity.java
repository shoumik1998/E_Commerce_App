package com.example.e_commerceapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;

public class ResetPaswordActivity extends AppCompatActivity {
    private EditText Phone_Number,Security_Qst_1,Security_Qst_2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_pasword);

        Phone_Number = findViewById(R.id.r_p_phone);
        Security_Qst_1 = findViewById(R.id.r_p_parents_meet_editID);
        Security_Qst_2 = findViewById(R.id.r_p_super_hero_editID);

        
    }
}