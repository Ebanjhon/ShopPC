package com.example.mobileshopapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.util.ArrayList;

public class ProductDetailFragment extends Fragment {
    TextView nameProduct, compaPro, price;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference docRef;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_product_detail, container, false);
        nameProduct = view.findViewById(R.id.nameProduct);
        compaPro = view.findViewById(R.id.nameCompany);
        price = view.findViewById(R.id.priceTxt);
        // lấy dữ liệu truyền
        Bundle bundle = getArguments();
        if (bundle != null) {
            Product product = bundle.getParcelable("product");
            if (product != null) {
//                String id = product.getId();
                String setPrice = String.valueOf(product.getPrice());
                price.setText(setPrice + " VNĐ") ;
//                String image = product.getImage();//                int price = product.getPrice();
                nameProduct.setText(product.getName());
                docRef = db.collection("Companies").document(product.getCompany());
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
        // Tạo danh sách hình ảnh
        ArrayList<SlideModel> imageList = new ArrayList<>();

        // Thêm hình ảnh vào danh sách
        imageList.add(new SlideModel("https://i.pinimg.com/564x/fe/82/3e/fe823e70e845e711cd0ce0fe0613c822.jpg", "The animal population decreased by 58 percent in 42 years.", ScaleTypes.CENTER_CROP));
        imageList.add(new SlideModel("https://i.pinimg.com/564x/fe/82/3e/fe823e70e845e711cd0ce0fe0613c822.jpg", "The animal population decreased by 58 percent in 42 years.", ScaleTypes.CENTER_CROP));
        imageList.add(new SlideModel("https://i.pinimg.com/564x/fe/82/3e/fe823e70e845e711cd0ce0fe0613c822.jpg", "The animal population decreased by 58 percent in 42 years.", ScaleTypes.CENTER_CROP));
        imageList.add(new SlideModel("https://i.pinimg.com/564x/fe/82/3e/fe823e70e845e711cd0ce0fe0613c822.jpg", "The animal population decreased by 58 percent in 42 years.", ScaleTypes.CENTER_CROP));

        // Lấy đối tượng ImageSlider từ layout
        ImageSlider imageSlider = view.findViewById(R.id.image_slider);

        // Đặt danh sách hình ảnh cho ImageSlider
        imageSlider.setImageList(imageList);

        return view;
    }
}