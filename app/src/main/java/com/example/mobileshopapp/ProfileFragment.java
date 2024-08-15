package com.example.mobileshopapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate layout cho Fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        // ánh xạ
        mAuth = FirebaseAuth.getInstance();
        tbLayout = view.findViewById(R.id.tabLayout);
        vPager = view.findViewById(R.id.viewPager);

        // lấy dữ liệu tự firebase
        DocumentReference docRef = db.collection("Users").document(user.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        TextView usrname = view.findViewById(R.id.username);
                        usrname.setText(document.getString("firstname") + " " + document.getString("lastname"));
//                        Toast.makeText(getActivity(),username , Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), "Không tìm thấy dữ liệu", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "lỗi khi lấy dữ liệu", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // hiển thị avatar
        ShapeableImageView avatar = view.findViewById(R.id.avatar);
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
        if (shouldHideSecondTab && tbLayout.getTabCount() > 1) {
            tbLayout.getTabAt(1).view.setVisibility(View.GONE); // Ẩn tab thứ hai
        }

        return view;
    }

    private void chuyenDoiManHinh(Fragment fragment)
    {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayoutMain, fragment);
        fragmentTransaction.commit();
    }
}