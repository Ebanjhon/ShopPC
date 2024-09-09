package com.example.mobileshopapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.List;

public class AdapterOrder extends RecyclerView.Adapter<AdapterOrder.ViewHolder> {
    private List<Order> orders;
    private Context context;

    public AdapterOrder(List<Order> orders, Context context) {
        this.orders = orders;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Order order = orders.get(position);

        // Hiển thị thông tin đơn hàng
        holder.tvOrderID.setText("Mã đơn: " + order.getOrderID());
        holder.tvOrderDate.setText(formatOrderDate(order.getOrderDate()));
        if(order.getState())
        {
            holder.tvOrderStateT.setVisibility(View.VISIBLE);
        }else {
            holder.tvOrderStateF.setVisibility(View.VISIBLE);
        }
        holder.tvOrderTotal.setText(order.getFormattedTotal());
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvOrderID;
        TextView tvOrderDate;
        TextView tvOrderStateT;
        TextView tvOrderStateF;
        TextView tvOrderTotal;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOrderID = itemView.findViewById(R.id.orderID);
            tvOrderDate = itemView.findViewById(R.id.orderDate);
            tvOrderStateT = itemView.findViewById(R.id.status_true);
            tvOrderStateF = itemView.findViewById(R.id.status_false);
            tvOrderTotal = itemView.findViewById(R.id.orderPrice);
        }
    }

    // Hàm để định dạng ngày tháng
    private String formatOrderDate(String date) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        try {
            return outputFormat.format(inputFormat.parse(date));
        } catch (Exception e) {
            e.printStackTrace();
            return date;
        }
    }
}
