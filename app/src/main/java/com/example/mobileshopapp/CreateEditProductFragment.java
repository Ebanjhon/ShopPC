package com.example.mobileshopapp;

import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreateEditProductFragment extends Fragment {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    List<Category> categories = new ArrayList<>();
    List<Company> companies = new ArrayList<>();
    ImageView imgView;
    Button btnAddimage, createProduct;
    private String cateID, compaID;
    private Uri uri = null; // mặc định hiển thị img
    private ActivityResultLauncher<Intent> activityResultLauncher;
    private ImageAdapter imageAdapter;
    private List<Uri> imageUris = new ArrayList<>();
    private RecyclerView recyclerView;
    EditText editTextName, editTextPrice, editTextMultiLine;
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        // xử lý khi chọn ảnh
        View view = inflater.inflate(R.layout.fragment_create_edit_product, container, false);
        imgView = view.findViewById(R.id.imageView);
        btnAddimage = view.findViewById(R.id.buttonAdd);
        editTextName = view.findViewById(R.id.editTextNameProduct);
        editTextPrice = view.findViewById(R.id.priceProduct);
        editTextMultiLine = view.findViewById(R.id.editTextMultiLine);
        createProduct = view.findViewById(R.id.btnCreateUpdateCom);
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
        // Khởi tạo RecyclerView
        recyclerView = view.findViewById(R.id.recyclerView);
        imageAdapter = new ImageAdapter(getActivity(), imageUris);
        recyclerView.setAdapter(imageAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        // Khởi tạo ActivityResultLauncher chọn nhiều ảnh
        ActivityResultLauncher<Intent> imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        ClipData clipData = result.getData().getClipData();
                        if (clipData != null) {
                            // Người dùng đã chọn nhiều ảnh
                            imageUris.clear(); // Xóa danh sách hiện tại nếu cần
                            for (int i = 0; i < clipData.getItemCount(); i++) {
                                Uri imageUri = clipData.getItemAt(i).getUri();
                                imageUris.add(imageUri);
                            }
                            imageAdapter.notifyDataSetChanged(); // Cập nhật RecyclerView
                        } else {
                            // Người dùng chỉ chọn một ảnh
                            Uri imageUri = result.getData().getData();
                            imageUris.clear(); // Xóa danh sách hiện tại nếu cần
                            imageUris.add(imageUri);
                            imageAdapter.notifyDataSetChanged(); // Cập nhật RecyclerView
                        }
                    } else {
                        // Người dùng hủy chọn ảnh hoặc xảy ra lỗi
                        Toast.makeText(getActivity(), "Task Cancelled or Error", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        btnAddimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true); // Cho phép chọn nhiều ảnh
                imagePickerLauncher.launch(Intent.createChooser(intent, "Select Pictures"));
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
                        Picasso.get().load(uri).into(imgView);
                    } else if (result.getResultCode() == ImagePicker.RESULT_ERROR) {
                        // Xử lý lỗi nếu có
                        Toast.makeText(getActivity(), "Error: " + ImagePicker.getError(result.getData()), Toast.LENGTH_SHORT).show();
                    } else {
                        // Người dùng hủy chọn ảnh
                        Toast.makeText(getActivity(), "Task Cancelled", Toast.LENGTH_SHORT).show();
                    }
                }
        );
        // chon ảnh
        imgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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

        // Dữ liệu cho Spinner cate
        CollectionReference cateCollection = db.collection("Categories");
        // Lấy tất cả tài liệu từ collection "Users"
        cateCollection.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<String> cate = new ArrayList<>();
                    categories.clear();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String id = document.getId();
                        String name = document.getString("name");
                        cate.add(name);
                        Category c = new Category(id, name);
                        categories.add(c);
                    }
                    Spinner spinnerCate = view.findViewById(R.id.spinnerCategory);
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, cate);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerCate.setAdapter(adapter);

                    // Thêm OnItemSelectedListener để lấy index của mục đang chọn
                    spinnerCate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            cateID = categories.get(position).getId();
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            // Xử lý khi không có mục nào được chọn (tùy chọn)
                        }
                    });
                }else{
                    Toast.makeText(getContext(), "error", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Dữ liệu cho Spinner company
        CollectionReference companiesCollection = db.collection("Companies");
        // Lấy tất cả tài liệu từ collection "Users"
        companiesCollection.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<String> compa = new ArrayList<>();
                    companies.clear();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String id = document.getId();
                        String name = document.getString("name");
                        String img = document.getString("image");
                        compa.add(name);
                        Company c = new Company(id, name, img);
                        companies.add(c);
                    }
                    Spinner spinnerCompa = view.findViewById(R.id.spinnerCompany);
                    ArrayAdapter<String> adapterCompany = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, compa);
                    adapterCompany.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerCompa.setAdapter(adapterCompany);
                    // Thêm OnItemSelectedListener để lấy index của mục đang chọn
                    spinnerCompa.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            // Lấy chỉ số của mục đang được chọn
                            compaID = companies.get(position).getId();
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            // Xử lý khi không có mục nào được chọn (tùy chọn)
                        }
                    });

                }
            }
        });

        // nhất button tạo product
        createProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), cateID, Toast.LENGTH_SHORT).show();

                String nameProduct = editTextName.getText().toString();
                String price = editTextPrice.getText().toString();
                if(nameProduct.isEmpty())
                {
                    Toast.makeText(getActivity(), "Tên không được trống!", Toast.LENGTH_SHORT).show();
                    return;
                }else if (price.isEmpty()){
                    Toast.makeText(getActivity(), "Giá không được để trống!", Toast.LENGTH_SHORT).show();
                    return;
                }else if(uri==null){
                    Toast.makeText(getActivity(), "Bạn chưa chọn ảnh!", Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    StorageReference imageRef = storageRef.child("images/" + System.currentTimeMillis() + ".jpg");
                    // Bắt đầu tải ảnh lên Firebase Storage
                    Map<String, Object> product = new HashMap<>();
                    product.put("name", nameProduct);
                    product.put("price", Integer.parseInt(editTextPrice.getText().toString()));
                    product.put("category", cateID);
                    product.put("company", compaID);
                    product.put("detail", editTextMultiLine.getText().toString());
                    imageRef.putFile(uri)
                            .addOnSuccessListener(taskSnapshot -> {
                                // Lấy URL tải về sau khi tải lên thành công
                                imageRef.getDownloadUrl().addOnSuccessListener(downloadUri -> {
                                    String imageurl = downloadUri.toString(); // đường dẫn ảnh sau khi up load xong
                                    product.put("image", imageurl);
                                    // bắt đâu lưu vào firebase thông tin khác
                                    db.collection("Products").add(product).addOnSuccessListener(documentReference  -> {
                                        String idPro = documentReference.getId();
                                                imageAdapter.upLoadImages(idPro);
                                                // gọi hàm ể lưu dữ liệu các ảnh
                                                Toast.makeText(getActivity(), "Đã thêm thành công", Toast.LENGTH_SHORT).show();
                                               //reset
                                                editTextMultiLine.setText("");
                                                editTextName.setText("");
                                                editTextPrice.setText(null);
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
                }
            }
        });
        return view;
    }
}