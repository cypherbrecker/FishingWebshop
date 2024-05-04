package com.example.fishingwebshop.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fishingwebshop.R;
import com.example.fishingwebshop.activities.RegisterActivity;
import com.example.fishingwebshop.models.MyCartModel;
import com.example.fishingwebshop.models.MyOrderModel;

import java.util.List;

public class MyOrderAdapter extends RecyclerView.Adapter<MyOrderAdapter.ViewHolder>{

    private static final String TAG = RegisterActivity.class.getSimpleName();

    Context context;
    List<MyOrderModel> list;

    int totalAmount = 0;

    MyCartModel myCartModel = null;

    public MyOrderAdapter(Context context, List<MyOrderModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyOrderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyOrderAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.my_order_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyOrderAdapter.ViewHolder holder, int position) {
        holder.name.setText(list.get(position).getProductName());
        holder.price.setText(String.valueOf(list.get(position).getProductPrice())+" Ft");
        holder.totalQuantity.setText(String.valueOf(list.get(position).getTotalQuantity())+ " db");
        holder.totalPrice.setText(String.valueOf(list.get(position).getTotalPrice())+ " Ft");


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, price, totalQuantity, totalPrice;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.product_name);
            price = itemView.findViewById(R.id.product_price);
            totalQuantity = itemView.findViewById(R.id.total_quantity);
            totalPrice = itemView.findViewById(R.id.total_price);
        }
    }
}
