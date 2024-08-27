package com.example.mobileshopapp;

import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {

    List<Comment> comments;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private OnItemClickListener onItemClickListener;
    public CommentAdapter(List<Comment> comments, OnItemClickListener onItemClickListener) {
        this.comments = comments;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public CommentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentAdapter.ViewHolder holder, int position) {
        Comment comment = comments.get(position);
        holder.tvContent.setText(comment.getText());
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        holder.tvDate.setText(formatter.format(Long.parseLong(comment.getDate())));
        fetchUserData(holder, comment);
    }

    private void fetchUserData(ViewHolder holder, Comment comment) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DocumentReference docRef = db.collection("Users").document(user.getUid());
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    User _user = new User(user.getUid(), document.getString("username")
                            , document.getString("role"), document.getString("firstname")
                            ,document.getString("lastname"), document.getString("email")
                            ,document.getString("phone"), document.getString("avatar")
                            ,document.getString("address"), document.getString("birthday"));
                    Picasso.get()
                            .load(_user.getAvatar())
                            .error(R.drawable.avatar)
                            .into(holder.imgUser);
                    if(user.getUid().equals(comment.getUserId())){
                        holder.tvName.setText("Bạn");
                        holder.itemView.setOnLongClickListener(v -> {
                            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                            builder.setTitle("Xác nhận xóa");
                            builder.setMessage("Bạn có chắc chắn muốn xóa bình luận này?");
                            builder.setPositiveButton("Có", (dialog, which) -> {
                                db.collection("Comments").document(comment.getId()).delete();
                                comments.remove(comment);
                                notifyDataSetChanged();
                            });
                            builder.setNegativeButton("Không", (dialog, which) -> {
                            });
                            builder.show();
                            return true;
                        });
                        holder.itemView.setOnClickListener(v -> {
                            onItemClickListener.onItemClickListener(comment);
                        });
                    }else {
                        holder.tvName.setText(_user.getFirstname() + " " + _user.getLastname());
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvContent, tvDate;
        ImageView imgUser;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.txtUser);
            tvContent = itemView.findViewById(R.id.txtComment);
            tvDate = itemView.findViewById(R.id.txtTime);
            imgUser = itemView.findViewById(R.id.imgUser);
        }
    }

    public interface OnItemClickListener {
        void onItemClickListener(Comment comment);
    }
}
