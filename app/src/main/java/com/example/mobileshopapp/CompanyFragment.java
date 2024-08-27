package com.example.mobileshopapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CompanyFragment extends Fragment {
    Button btnCreateUpdate, pickLogo;
    ImageButton imgCancel;
    EditText txtEditCate;
    ImageView viewImg;
    private Uri uri = null;
    List<Company> companies = new ArrayList<>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    CompanyAdaptor companyAdaptor;
    private ActivityResultLauncher<Intent> activityResultLauncher;
    StorageReference storageRef = storage.getReference();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_company, container, false);
        btnCreateUpdate = view.findViewById(R.id.btnCreateUpdateCom);
        imgCancel = view.findViewById(R.id.btnCancelEdit);
        pickLogo = view.findViewById(R.id.buttonChoseImg);
        viewImg = view.findViewById(R.id.imageViewLogo);
        txtEditCate = view.findViewById(R.id.editTextCom);
        ListView listv = (ListView) view.findViewById(R.id.listViewCom);

        // back
        TextView btnBack = view.findViewById(R.id.back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getActivity().getSupportFragmentManager().getBackStackEntryCount() > 0) {
                    getActivity().getSupportFragmentManager().popBackStack();
                } else {
                    requireActivity().finish();
                }
            }
        });

        // lấy dữ liệu
        db.collection("Companies")
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        Toast.makeText(getActivity(), "Lỗi khi lắng nghe dữ liệu.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (value != null) {
                        // Xóa danh sách cũ trước khi thêm dữ liệu mới
                        companies.clear();
                        // Nạp dữ liệu vào list
                        for (QueryDocumentSnapshot document : value) {
                            String id = document.getId();
                            String name = document.getString("name");
                            String img = document.getString("logoCompany");
                            Company c = new Company(id, name, img);
                            companies.add(c);
                        }
                        // Hiển thị dữ liệu
                        companyAdaptor = new CompanyAdaptor(getActivity(), companies, view);
                        listv.setAdapter(companyAdaptor);
                    }
                });

        // Khởi tạo ActivityResultLauncher
        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        // Lấy URL của ảnh
                        uri = result.getData().getData();
                        // Thực hiện hành động với URL của ảnh
                        Picasso.get().load(uri).into(viewImg);
                    } else if (result.getResultCode() == ImagePicker.RESULT_ERROR) {
                        // Xử lý lỗi nếu có
                        Toast.makeText(getActivity(), "Error: " + ImagePicker.getError(result.getData()), Toast.LENGTH_SHORT).show();
                    } else {
                        // Người dùng hủy chọn ảnh
                        Toast.makeText(getActivity(), "Task Cancelled", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        // lắng nghe chọn ảnh
        pickLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Sử dụng ActivityResultLauncher để khởi chạy ImagePicker từ Fragment
                ImagePicker.with(getActivity())
                        .crop()                    // Crop image (Optional)
                        .compress(1024)            // Final image size will be less than 1 MB (Optional)
                        .maxResultSize(1080, 1080)    // Final image resolution will be less than 1080 x 1080 (Optional)
                        .createIntent(intent -> {
                            activityResultLauncher.launch(intent);
                            return null;
                        });
            }
        });

        // xử lý khi có nhấn vào nút cập nhật hoặc tạo
        btnCreateUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, Object> compa = new HashMap<>();
                compa.put("name", txtEditCate.getText().toString());
                // kiểm tra đầu vào
                if (compa.get("name").toString().isEmpty()) {
                    Toast.makeText(getActivity(), "Tên trống!", Toast.LENGTH_SHORT).show();
                    return;
                }

                // kểm tra item là create
                if (companyAdaptor.company == null) {
                    if (uri == null) {
                        Toast.makeText(getActivity(), "Ảnh không được trống!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    // lưu ảnh trước
                    // Tạo một tham chiếu lưu ảnh trong Firebase Storage
                    StorageReference imageRef = storageRef.child("images/" + System.currentTimeMillis() + ".jpg");

                    // Bắt đầu tải ảnh lên Firebase Storage
                    imageRef.putFile(uri)
                            .addOnSuccessListener(taskSnapshot -> {
                                // Lấy URL tải về sau khi tải lên thành công
                                imageRef.getDownloadUrl().addOnSuccessListener(downloadUri -> {
                                    String imageurl = downloadUri.toString(); // đường dẫn ảnh sau khi up load xong
                                    compa.put("logoCompany", imageurl);
                                    // bắt đâu lưu vào firebase
                                    db.collection("Companies").add(compa).addOnSuccessListener(aVoid -> {
                                                // Xử lý khi lưu dữ liệu thành công
                                                Toast.makeText(getActivity(), "Đã thêm thành công", Toast.LENGTH_SHORT).show();
                                                txtEditCate.setText("");
                                                uri = null;
                                                Picasso.get().load(R.drawable.imagewait).into(viewImg);
                                                companyAdaptor.setCompany(null);
                                            })
                                            .addOnFailureListener(e -> {
                                                // Xử lý khi lưu dữ liệu thất bại
                                                System.err.println("Lỗi khi thêm dữ liệu: " + e.getMessage());
                                            });
                                });
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(getActivity(), "Upload thất bại!", Toast.LENGTH_SHORT).show();
                            });
                } else {
                    if(uri == null)
                    {
                        compa.put("logoCompany",companyAdaptor.company.getImage());
                        db.collection("Companies")
                                .document(companyAdaptor.company.getId())
                                .update(compa)
                                .addOnSuccessListener(aVoid -> {
                                    // Xử lý khi cập nhật thành công
                                    Toast.makeText(getActivity(), "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                                    txtEditCate.setText("");
                                    uri = null;
                                    Picasso.get().load(R.drawable.imagewait).into(viewImg);
                                    companyAdaptor.setCompany(null);
                                })
                                .addOnFailureListener(e -> {
                                    // Xử lý khi cập nhật thất bại
                                    Toast.makeText(getActivity(), companyAdaptor.getCompany().getName(), Toast.LENGTH_SHORT).show();
                                });
                    }else{
                        // Cập nhật dữ liệu
                        StorageReference imageRef = storageRef.child("images/" + System.currentTimeMillis() + ".jpg");

                        // Bắt đầu tải ảnh lên Firebase Storage
                        imageRef.putFile(uri)
                                .addOnSuccessListener(taskSnapshot -> {
                                    // Lấy URL tải về sau khi tải lên thành công
                                    imageRef.getDownloadUrl().addOnSuccessListener(downloadUri -> {
                                        String imageurl = downloadUri.toString(); // đường dẫn ảnh sau khi up load xong
                                        compa.put("logoCompany",imageurl);
                                        db.collection("Companies")
                                                .document(companyAdaptor.company.getId())
                                                .update(compa)
                                                .addOnSuccessListener(aVoid -> {
                                                    // Xử lý khi cập nhật thành công
                                                    Toast.makeText(getActivity(), "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                                                    txtEditCate.setText("");
                                                    uri = null;
                                                    Picasso.get().load(R.drawable.imagewait).into(viewImg);
                                                    companyAdaptor.setCompany(null);
                                                })
                                                .addOnFailureListener(e -> {
                                                    // Xử lý khi cập nhật thất bại
                                                    Toast.makeText(getActivity(), companyAdaptor.getCompany().getName(), Toast.LENGTH_SHORT).show();
                                                });
                                    });
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(getActivity(), "Upload thất bại!", Toast.LENGTH_SHORT).show();
                                });
                    }
                }
            }
        });

        // tắt edit
        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imgCancel.setVisibility(View.GONE);
                btnCreateUpdate.setText("Create");
                Drawable drawable = btnCreateUpdate.getBackground();
                DrawableCompat.setTint(drawable, Color.parseColor("#2D77E5"));
                btnCreateUpdate.setBackground(drawable);
                Picasso.get().load(R.drawable.imagewait).into(viewImg);
                txtEditCate.setText("");
                uri = null;
                companyAdaptor.setCompany(null);
            }
        });


        return view;
    }

    // hàm up ảnh lên firebase
    public String UploadImage(){

        return null;
    }
}