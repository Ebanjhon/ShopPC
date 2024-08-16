package com.example.mobileshopapp;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;


public class AdminMainFragment extends Fragment {
    CardView cardProduct,cardAcount, cardCategory,cardCompany;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_main, container, false);
        // ánh xạ
        cardProduct = view.findViewById(R.id.manaProduct);
        cardAcount = view.findViewById(R.id.manaAcount);
        cardCategory = view.findViewById(R.id.manaCategory);
        cardCompany = view.findViewById(R.id.manaCompany);

        cardProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chuyenDoiManHinh(new ProductFragment());
            }
        });

        cardAcount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chuyenDoiManHinh(new AcountFragment());
            }
        });

        cardCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chuyenDoiManHinh(new CategoryFragment());
            }
        });

        cardCompany.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chuyenDoiManHinh(new CompanyFragment());
            }
        });

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