package com.example.mobileshopapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class ProductFragment extends Fragment {
    Button btnCraete;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_product, container, false);
        btnCraete = view.findViewById(R.id.buttonAddProduct);

        btnCraete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chuyenDoiManHinh(new CreateEditProductFragment());
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