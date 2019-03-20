package com.ondande.cureserv;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.rengwuxian.materialedittext.MaterialEditText;
import com.szagurskii.patternedtextwatcher.PatternedTextWatcher;

import dmax.dialog.SpotsDialog;

public class sign_up extends Fragment {
    Context context;
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

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        context = activity;
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
                final SpotsDialog waitDialog = new SpotsDialog(context);
                waitDialog.show();
                waitDialog.setMessage("Waiting...");
                if(TextUtils.isEmpty(edt_name.getText().toString())){
                    Toast.makeText(context, "Please enter your name", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(edt_address.getText().toString())){
                    Toast.makeText(context, "Please enter your address", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(edt_birth.getText().toString())){
                    Toast.makeText(context, "Please enter your birthdate", Toast.LENGTH_SHORT).show();
                    return;
                }

                Activity activity = getActivity();
                if (activity instanceof MainActivity){
                    MainActivity main = (MainActivity) activity;
                    main.finishRegistration(edt_name.getText().toString(),edt_birth.getText().toString(),edt_address.getText().toString());
                }
            }
        });
    }

}
