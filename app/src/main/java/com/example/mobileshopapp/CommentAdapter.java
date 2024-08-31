package com.example.mobileshopapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {
    private List<Comment> comments;
    private Context context;
    private FirebaseFirestore db;

    public CommentAdapter(Context context, List<Comment> comments) {
        this.context = context;
        this.comments = comments;
        this.db = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_comment, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        Comment comment = comments.get(position);
        holder.tvContent.setText(comment.getText());
        holder.tvDate.setText(chuyenThoiGian(comment.getDate()));

        // Lấy thông tin người dùng từ Firestore
        db.collection("Users").document(comment.getUserId())
                .get()
                .addOnCompleteListener(userTask -> {
                    if (userTask.isSuccessful()) {
                        DocumentSnapshot userDoc = userTask.getResult();
                        if (userDoc.exists()) {
                            String username = userDoc.getString("username");
                            String avatar = userDoc.getString("avatar");
                            holder.tvName.setText(username);
                            if (avatar != null && !avatar.isEmpty()) {
                                Picasso.get().load(avatar).into(holder.imgUser);
                            }
                        }
                    }
                });
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public static class CommentViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        TextView tvContent;
        TextView tvDate;
        ImageView imgUser;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.txtUser);
            tvContent = itemView.findViewById(R.id.txtComment);
            tvDate = itemView.findViewById(R.id.txtTime);
            imgUser = itemView.findViewById(R.id.imgUser);
        }
    }

    public String chuyenThoiGian(long time){
        // Chuyển đổi timestamp thành Date
        Date date = new Date(time);

        // Định dạng ngày giờ theo mẫu mong muốn
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());

        // Chuyển đổi Date thành chuỗi
        return sdf.format(date);
    }
}
