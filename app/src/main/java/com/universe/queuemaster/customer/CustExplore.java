package com.universe.queuemaster.customer;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.universe.queuemaster.R;

public class CustExplore extends Fragment {

    ImageButton imageButtonPhotography;
    ImageButton imageButtonFnB;
    ImageButton imageButtonMusic;
    ImageButton imageButtonIT;
    ImageButton imageButtonLimitedT;
    ImageButton imageButtonExtra;
    ImageButton imageButtonRetail;
    Button SearchQueryButton;

    private static final String TAG = "CustExplore";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.cust_main_explore, container,false);

        SearchQueryButton = view.findViewById(R.id.BtnSearchResult);

        SearchQueryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), Cust_Search_Merchant.class));
            }


        });

        return view;
    }



}

