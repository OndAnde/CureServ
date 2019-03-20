package com.ondande.cureserv;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rengwuxian.materialedittext.MaterialEditText;

public class sign_up extends Fragment {
    MaterialEditText edt_name;
    MaterialEditText edt_address;
    MaterialEditText edt_birth;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);

        edt_name = (MaterialEditText)view.findViewById(R.id.edt_name);
        edt_address = (MaterialEditText)view.findViewById(R.id.edt_address);
        edt_birth = (MaterialEditText)view.findViewById(R.id.edt_birth);
        return view;
    }

    public void Registration(String phone){
        
    }

}
