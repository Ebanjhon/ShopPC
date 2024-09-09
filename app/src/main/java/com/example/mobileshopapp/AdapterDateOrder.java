package com.example.mobileshopapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdapterDateOrder extends RecyclerView.Adapter<AdapterDateOrder.ViewHolder> {

    private Map<String, List<Order>> dateOrderMap; // Dữ liệu được nhóm theo tháng
    private Context context;

    public AdapterDateOrder(Map<String, List<Order>> dateOrderMap, Context context) {
        this.dateOrderMap = dateOrderMap;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_date_order, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Lấy danh sách các tháng
        String[] months = dateOrderMap.keySet().toArray(new String[0]);
        String month = months[position];
        List<Order> ordersInMonth = dateOrderMap.get(month);

        holder.textViewDate.setText(month); // Hiển thị tháng

        // Thiết lập adapter cho RecyclerView bên trong
        AdapterOrder adapterOrder = new AdapterOrder(ordersInMonth, context);
        holder.recyclerViewOrder.setLayoutManager(new LinearLayoutManager(context));
        holder.recyclerViewOrder.setAdapter(adapterOrder);
    }

    @Override
    public int getItemCount() {
        return dateOrderMap.size(); // Số lượng các tháng
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewDate;
        RecyclerView recyclerViewOrder;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewDate = itemView.findViewById(R.id.textViewDate);
            recyclerViewOrder = itemView.findViewById(R.id.recyclerViewOrder);
        }
    }
}
