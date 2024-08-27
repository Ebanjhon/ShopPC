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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ProductDetailFragment extends Fragment {
    TextView nameProduct, compaPro, price, detail;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference docRef;
    private Button btnComment;
    private EditText edtComment;
    private RecyclerView rvComment;
    private User user;
    String productId;
    ArrayList<SlideModel> imageList = new ArrayList<>();
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
                            imageList.add(new SlideModel(imageUrl, "The animal population decreased by 58 percent in 42 years.", ScaleTypes.CENTER_CROP));
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

        // bình luận
//        db.collection("Comments").whereEqualTo("productId", productId).addSnapshotListener((value, error) -> {
//            if (value != null) {
//                ArrayList<Comment> comments = new ArrayList<>();
//                for (DocumentSnapshot documentSnapshot : value.getDocuments()) {
//                    Comment comment = documentSnapshot.toObject(Comment.class);
//                    comments.add(comment);
//                }
//
//                //sắp xếp giảm dần
//                comments.sort((o1, o2) -> {
//                    long time1 = Long.parseLong(o1.getDate());
//                    long time2 = Long.parseLong(o2.getDate());
//                    return (int) (time2 - time1);
//                });
//
//                CommentAdapter commentAdapter = new CommentAdapter(comments, new CommentAdapter.OnItemClickListener() {
//                    @Override
//                    public void onItemClickListener(Comment comment) {
//                        comment("edit", comment);
//                    }
//                });
//                rvComment.setAdapter(commentAdapter);
//                rvComment.setLayoutManager(new LinearLayoutManager(getActivity()));
//                commentAdapter.notifyDataSetChanged();
//            }
//        });

        return view;
    }
//    private void comment(String type, Comment comment) {
//        if (type.equals("edit")) {
//            edtComment.setText(comment.getText());
//        }
//
//        btnComment.setOnClickListener(v -> {
//            String _comment = edtComment.getText().toString();
//            if (_comment.isEmpty()) {
//                Toast.makeText(getActivity(), "Vui nhap binh luan", Toast.LENGTH_SHORT).show();
//                return;
//            }
//            // gọi hàm đăng ký ng dùng
//            if (type.equals("edit")) {
//                comment.setText(_comment);
//                comment.setDate(System.currentTimeMillis() + "");
//                db.collection("Comments").document(comment.getId()).set(comment).addOnSuccessListener(aVoid -> {
//                    Toast.makeText(getActivity(), "Sua binh luan thanh cong", Toast.LENGTH_SHORT).show();
//                    edtComment.setText("");
//                });
//            } else {
//                String id = db.collection("Comments").document().getId();
//                Comment comment1 = new Comment(id, productId, user.getIdUser(), _comment, System.currentTimeMillis() + "");
//                db.collection("Comments").document(id).set(comment1).addOnSuccessListener(aVoid -> {
//                    Toast.makeText(getActivity(), "Gui binh luan thanh cong", Toast.LENGTH_SHORT).show();
//                    edtComment.setText("");
//                });
//            }
//
//        });
//    }

    private void chuyenDoiManHinh(Fragment fragment)
    {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayoutMain, fragment);
        fragmentTransaction.commit();
    }
}