package com.ondande.cureserv;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.rengwuxian.materialedittext.MaterialEditText;
import com.szagurskii.patternedtextwatcher.PatternedTextWatcher;

import dmax.dialog.SpotsDialog;

public class sign_up extends Fragment {
    MaterialEditText edt_name;
    MaterialEditText edt_address;
    MaterialEditText edt_birth;
    View view;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        return view;
    }

    public void Registration(String phone){
        edt_name = (MaterialEditText)view.findViewById(R.id.edt_name);
        edt_address = (MaterialEditText)view.findViewById(R.id.edt_address);
        edt_birth = (MaterialEditText)view.findViewById(R.id.edt_birth);

        Button btn_conf_s_up = (Button)view.findViewById(R.id.btn_conf_s_up);

        edt_birth.addTextChangedListener(new PatternedTextWatcher("####-##-##"));

        btn_conf_s_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final SpotsDialog alertDialog = new SpotsDialog(sign_up.this);
                alertDialog.show();
                alertDialog.setMessage("Waiting...");
            }
        });
    }

}
