package com.example.mobileshopapp;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;


public class ProfileFragment extends Fragment {
    private FirebaseAuth mAuth;
    private TabLayout tbLayout;
    private ViewPager2 vPager;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DatabaseHelper dbHelper;
    private ImageButton btnPiker;
    private ShapeableImageView avatar;
    private ActivityResultLauncher<Intent> activityResultLauncher;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate layout cho Fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        dbHelper = new DatabaseHelper(getContext());
        // ánh xạ
        mAuth = FirebaseAuth.getInstance();
        User ur = dbHelper.getUser();
        tbLayout = view.findViewById(R.id.tabLayout);
        vPager = view.findViewById(R.id.viewPager);
        btnPiker = view.findViewById(R.id.imageButtonAvatar);

        // lấy dữ liệu
        TextView usrname = view.findViewById(R.id.username);
        usrname.setText(ur.getFirstname() + " " + ur.getLastname());

        // hiển thị avatar
        avatar = view.findViewById(R.id.avatar);
        String imageUrl = "https://i.pinimg.com/564x/6b/d8/28/6bd828068a62aab41e75ebf829e2fc5d.jpg";
        Picasso.get().load(imageUrl).into(avatar);

        // set pageview hiển thị nội dung của fragment
        ViewPagerAdapter adapter = new ViewPagerAdapter(getActivity());
        vPager.setAdapter(adapter);

        new TabLayoutMediator(tbLayout, vPager,
                (tab, position) -> tab.setText(adapter.getPageTitle(position))
        ).attach();

        // Điều kiện để ẩn tab thứ hai
        boolean shouldHideSecondTab = false; // Bạn có thể thay đổi điều kiện này
        // ẩn tab nếu là admin hoặc nhân viên
        if (!(ur.getRole().equals("client")) && tbLayout.getTabCount() > 1) {
            tbLayout.getTabAt(1).view.setVisibility(View.GONE); // Ẩn tab thứ hai
        }

        // Khởi tạo ActivityResultLauncher
        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        // Lấy URL của ảnh
                        Uri uri = result.getData().getData();
                        // Thực hiện hành động với URL của ảnh
                        Toast.makeText(getActivity(), "ok", Toast.LENGTH_SHORT).show();
                        Picasso.get().load(uri).into(avatar);
                    } else if (result.getResultCode() == ImagePicker.RESULT_ERROR) {
                        // Xử lý lỗi nếu có
                        Toast.makeText(getActivity(), "Error: " + ImagePicker.getError(result.getData()), Toast.LENGTH_SHORT).show();
                    } else {
                        // Người dùng hủy chọn ảnh
                        Toast.makeText(getActivity(), "Task Cancelled", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        // thực hiện upload ảnh
        btnPiker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Sử dụng ActivityResultLauncher để khởi chạy ImagePicker từ Fragment
                ImagePicker.with(getActivity())
                        .crop()	    			// Crop image (Optional)
                        .compress(1024)			// Final image size will be less than 1 MB (Optional)
                        .maxResultSize(1080, 1080)	// Final image resolution will be less than 1080 x 1080 (Optional)
                        .createIntent(intent -> {
                            activityResultLauncher.launch(intent);
                            return null;
                        });
            }
        });

        return view;
    }

}