package com.example.e_commerceapp.ViewHolder;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e_commerceapp.R;

public class Order_ViewHolder extends RecyclerView.ViewHolder {
    public TextView User_Name,User_Phone,Price,Address,City,Date_Time;
    public Button See_Ordered_Products;

    public Order_ViewHolder(@NonNull View itemView) {
        super(itemView);

        User_Name = itemView.findViewById(R.id.admin_orders_user_name_txtID);
        User_Phone = itemView.findViewById(R.id.admin_orders_user_phone_txtID);
        Price = itemView.findViewById(R.id.admin_orders_price_txtID);
        Address = itemView.findViewById(R.id.admin_orders_user_address_txtID);
        City = itemView.findViewById(R.id.admin_orders_user_city_name_txtID);
        Date_Time = itemView.findViewById(R.id.admin_orders_date_txtID);
        See_Ordered_Products = itemView.findViewById(R.id.admin_orders_BtnID);


    }
}
