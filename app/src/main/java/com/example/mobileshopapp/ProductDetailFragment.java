package com.example.mobileshopapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ProductDetailFragment extends Fragment {
    private TextView nameProduct, compaPro, price, detail;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference docRef;
    private Button btnComment;
    private EditText edtComment;
    private RecyclerView rvComment;
    private User user;
    private FirebaseAuth mAuth;
    private DatabaseHelper dbHelper;
    private String productId;
    private ArrayList<SlideModel> imageList = new ArrayList<>();
    NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_product_detail, container, false);
        nameProduct = view.findViewById(R.id.nameProduct);
        compaPro = view.findViewById(R.id.nameCompany);
        price = view.findViewById(R.id.priceTxt);
        btnComment = view.findViewById(R.id.buttonSendComment);
        edtComment = view.findViewById(R.id.editTextComment);
        rvComment = view.findViewById(R.id.recyclerViewComment);
        detail = view.findViewById(R.id.textDetail);
        dbHelper = new DatabaseHelper(requireContext());
        mAuth = FirebaseAuth.getInstance();
        // back
        TextView btnBack = view.findViewById(R.id.back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               chuyenDoiManHinh(new HomeFragment());
            }
        });

        // lấy dữ liệu truyền
        Bundle bundle = getArguments();
        if (bundle != null) {
            Product product = bundle.getParcelable("product");
            if (product != null) {
                productId = product.getId();
                String setPrice = formatter.format(product.getPrice());
                setPrice = setPrice.replace(',', '.');
                price.setText(setPrice + " VNĐ") ;
                imageList.add(new SlideModel(product.getImage(), "", ScaleTypes.CENTER_CROP));
                nameProduct.setText(product.getName());
                docRef = db.collection("Companies").document(product.getCompany());
                detail.setText(product.getDetail());
            }
        }

        // lấy dữ liệu hãng
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    // Lấy dữ liệu từ document
                    String name = documentSnapshot.getString("name");
                    compaPro.setText(name);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), "Thất bại!", Toast.LENGTH_SHORT).show();
            }
        });
        // hiển thị hình ảnh
        db.collection("ProductImages")
                .whereEqualTo("productId", productId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    // Danh sách để lưu các URL của ảnh
                    List<String> imageUrls = new ArrayList<>();

                    // Lặp qua tất cả các tài liệu kết quả
                    for (DocumentSnapshot document : queryDocumentSnapshots) {
                        // Lấy URL của từng ảnh
                        String imageUrl = document.getString("imageUrl");
                        if (imageUrl != null) {
                            imageList.add(new SlideModel(imageUrl, "Hình ảnh sản phẩm", ScaleTypes.CENTER_CROP));
                        }
                    }
                    // hiển thị ảnh
                    ImageSlider imageSlider = view.findViewById(R.id.image_slider);
                    // Đặt danh sách hình ảnh cho ImageSlider
                    imageSlider.setImageList(imageList);
                    imageSlider.stopSliding();
                })
                .addOnFailureListener(e -> {
                    // Xử lý khi truy vấn thất bại
                    System.err.println("Lỗi khi lấy ảnh: " + e.getMessage());
                });

        // lấy bình luận và hiển thị
        db.collection("Comments")
                .whereEqualTo("productId", productId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if (querySnapshot != null && !querySnapshot.isEmpty()) {
                            // Xử lý dữ liệu từ kết quả truy vấn
                            ArrayList<Comment> comments = new ArrayList<>();
                            for (QueryDocumentSnapshot document : querySnapshot) {
                                String userId = document.getString("userId");
                                String content = document.getString("content");
                                Long timeCreate = document.getLong("timeCreate");

                                Comment comment = new Comment(document.getId(), productId, userId, content, timeCreate);
                                comments.add(comment);
                            }
                            // hàm xắp sếp thời gian
                            Collections.sort(comments, new Comparator<Comment>() {
                                @Override
                                public int compare(Comment c1, Comment c2) {
                                    return c1.getDate().compareTo(c2.getDate());
                                }
                            });
                            rvComment.setLayoutManager(new LinearLayoutManager(getContext()));
                            CommentAdapter commentAdapter = new CommentAdapter(getContext(), comments);
                            rvComment.setAdapter(commentAdapter);
                        } else {
                            // Không có comment nào với productId này
                        }
                    } else {
                        // Truy vấn thất bại
                        Toast.makeText(getActivity(), "Lỗi khi lấy dữ liệu bình luận", Toast.LENGTH_SHORT).show();
                    }
                });

        // hàm tạo cmt
        btnComment.setOnClickListener(v -> {
            String content = edtComment.getText().toString();
            FirebaseUser currentUser = mAuth.getCurrentUser();
            if(currentUser == null){
                Toast.makeText(getActivity(), "Vui Lòng đăng nhập để Bình luận!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (content.isEmpty()) {
                Toast.makeText(getActivity(), "Vui lòng nhập bình luận!", Toast.LENGTH_SHORT).show();
                return;
            }
            // Tạo tham chiếu đến collection "Comments"
            CollectionReference commentsRef = db.collection("Comments");

            // Dữ liệu cho comment mới
            Map<String, Object> comment = new HashMap<>();
            comment.put("productId", productId); // Thay thế PRODUCT_ID_HERE bằng ID sản phẩm
            comment.put("userId", currentUser.getUid()); // Thay thế USER_ID_HERE bằng ID người dùng
            comment.put("content", content);
            comment.put("timeCreate", System.currentTimeMillis()); // Thời gian tạo dưới dạng timestamp

            // Thêm comment mới vào Firestore
            commentsRef.add(comment)
                    .addOnSuccessListener(documentReference -> {
                        // Thành công
                        Toast.makeText(getActivity(), "Tạo bình luận thành công!", Toast.LENGTH_SHORT).show();
                        edtComment.setText("");
                    })
                    .addOnFailureListener(e -> {
                        // Thất bại
                        Toast.makeText(getActivity(), "Tạo bình luận thất bại!", Toast.LENGTH_SHORT).show();
                    });

        });

        return view;
    }

    public void chuyenDoiManHinh(Fragment fragment)
    {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayoutMain, fragment);
        fragmentTransaction.commit();
    }
}