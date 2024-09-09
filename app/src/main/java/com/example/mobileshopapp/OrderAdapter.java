package com.example.mobileshopapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private List<Order> orders;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DatabaseReference database = FirebaseDatabase.getInstance().getReference("Users");
    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        public TextView idOrder, nameClient, total, dayOrder, statusOrder;
        Button btnAccept;
        public OrderViewHolder(View itemView) {
            super(itemView);
            idOrder = itemView.findViewById(R.id.idOrder);
            nameClient = itemView.findViewById(R.id.nameClient);
            total = itemView.findViewById(R.id.total);
            dayOrder = itemView.findViewById(R.id.dayOrder);
            statusOrder = itemView.findViewById(R.id.status_order);
            btnAccept = itemView.findViewById(R.id.btnAccept);
        }
    }

    public OrderAdapter(List<Order> orders) {
        this.orders = orders;
    }

    @Override
    public OrderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_manager, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(OrderViewHolder holder, int position) {
        Order order = orders.get(position);

        // Cập nhật thông tin đơn hàng
        holder.idOrder.setText("Mã đơn: " + order.getOrderID());
        holder.total.setText(order.getFormattedTotal());
        holder.dayOrder.setText(order.getOrderDate());

        // Cập nhật thông tin người dùng từ Firebase
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users").child(order.getUserID());
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if (user != null) {
                    holder.nameClient.setText("Tên khách hàng: " + user.getFirstname() + " " + user.getLastname());
                } else {
                    holder.nameClient.setText("Tên khách hàng: Không tìm thấy");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Xử lý lỗi
                Toast.makeText(holder.itemView.getContext(), "Lỗi khi tải thông tin người dùng", Toast.LENGTH_SHORT).show();
            }
        });
        if(order.getState())
        {
            // Cập nhật trạng thái của đơn hàng
            holder.statusOrder.setVisibility(View.VISIBLE);
            holder.statusOrder.setText("Đã xác nhận");
        }else{
            holder.btnAccept.setVisibility(View.VISIBLE);
        }


        // Xử lý sự kiện nhấn nút xác nhận
        holder.btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tham chiếu đến collection 'Orders' và document có id tương ứng
                db.collection("Orders").document(order.getOrderID())
                        .update("isPaid", true)
                        .addOnSuccessListener(aVoid -> {
                            // Thành công
                            holder.statusOrder.setText("Đã xác nhận");
                            holder.statusOrder.setVisibility(View.VISIBLE);
                            holder.btnAccept.setVisibility(View.GONE);
                            Toast.makeText(holder.itemView.getContext(), "Đã xác nhận đơn hàng!", Toast.LENGTH_SHORT).show();
                        })
                        .addOnFailureListener(e -> {
                            // Thất bại
                            Toast.makeText(holder.itemView.getContext(), "Xác nhận thất bại!", Toast.LENGTH_SHORT).show();
                        });
            }
        });
    }


    @Override
    public int getItemCount() {
        return orders.size();
    }
}
