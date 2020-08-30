package com.example.e_commerceapp.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e_commerceapp.Interfaces.Item_Click_Listener;
import com.example.e_commerceapp.R;

public class Cart_View_Holder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView Product_Name_Item,Product_Quantity_Item,Product_Price_Item;
    private  Item_Click_Listener listener;


    public Cart_View_Holder(@NonNull View itemView) {
        super(itemView);

        Product_Name_Item = itemView.findViewById(R.id.cart_item_pro_name_txtID);
        Product_Quantity_Item = itemView.findViewById(R.id.cart_item_pro_quantity_txtID);
        Product_Price_Item = itemView.findViewById(R.id.cart_item_pro_price_txtID);

    }

    @Override
    public void onClick(View view) {
                listener.onClick(view,getAdapterPosition(),false);
    }

    public  void setItem_Click_Listener(Item_Click_Listener listener){
        this.listener = listener;

    }
}
