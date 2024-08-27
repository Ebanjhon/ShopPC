package com.example.mobileshopapp;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {
    private List<Uri> imageUris;
    private Context context;
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();


    public ImageAdapter(Context context, List<Uri> imageUris) {
        this.context = context;
        this.imageUris = imageUris;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_image, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        Uri imageUri = imageUris.get(position);
        Picasso.get().load(imageUri).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return imageUris.size();
    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }

    public void upLoadImages(String productId){
        if (imageUris.isEmpty()) {
            return;
        }
        // Vòng lặp qua từng Uri trong danh sách
        for (Uri imageUri : imageUris) {
            // Tạo tham chiếu đến Firebase Storage với đường dẫn theo productId
            StorageReference storageRef = storage.getReference().child("products/" + productId + "/" + System.currentTimeMillis() + ".jpg");
            // Upload ảnh lên Firebase Storage
            storageRef.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        // Lấy URL của ảnh vừa được upload
                        storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                            // Lưu URL của ảnh vào Firestore dưới productId
                            Map<String, Object> imageData = new HashMap<>();
                            imageData.put("imageUrl", uri.toString());
                            imageData.put("productId", productId);

                            db.collection("ProductImages").add(imageData)
                                    .addOnSuccessListener(documentReference -> {
                                        // Ảnh đã được lưu thành công vào Firestore
                                        Toast.makeText(context, "Ảnh đã được upload và lưu thành công!", Toast.LENGTH_SHORT).show();
                                    })
                                    .addOnFailureListener(e -> {
                                        // Xử lý lỗi nếu việc lưu vào Firestore thất bại
                                        System.err.println("Lỗi khi lưu URL ảnh vào Firestore: " + e.getMessage());
                                    });
                        });
                    })
                    .addOnFailureListener(e -> {
                        // Xử lý lỗi nếu việc upload lên Firebase Storage thất bại
                        System.err.println("Lỗi khi upload ảnh: " + e.getMessage());
                    });
        }
    }
}
