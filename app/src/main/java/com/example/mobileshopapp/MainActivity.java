package com.example.mobileshopapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.mobileshopapp.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    private BottomNavigationView navBottomView;
    private FrameLayout frameLayout;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            // Tải menu mới từ XML

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        // màn hình ban đầu
        chuyenDoiManHinh(new HomeFragment());
        // ánh xạ
        mAuth = FirebaseAuth.getInstance();
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        navBottomView = findViewById(R.id.bottomNavView);
        frameLayout = findViewById(R.id.frameLayoutMain);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        // nghe sự kiện nhấn vào botton nav
        navBottomView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if(R.id.Home == itemId) {
                    chuyenDoiManHinh(new HomeFragment());
                }else if (R.id.Filter == itemId) {
                    chuyenDoiManHinh(new FilterFragment());
                }else if (R.id.Cart == itemId) {
                    chuyenDoiManHinh(new CartFragment());
                }else if (R.id.Profile == itemId) {
                    FirebaseUser currentUser = mAuth.getCurrentUser();
                    if(currentUser == null){
                        chuyenDoiManHinh(new NotSignInFragment());
                    }else{
                        chuyenDoiManHinh(new ProfileFragment());
                    }
                }
                return true;
            }
        });

    }

    // hàm chuyển fragment
    private void chuyenDoiManHinh(Fragment fragment)
    {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayoutMain, fragment);
        fragmentTransaction.commit();
    }
}