package com.ondande.cureserv;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.annotation.Nullable;
import android.support.design.button.MaterialButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.accountkit.Account;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitCallback;
import com.facebook.accountkit.AccountKitError;
import com.facebook.accountkit.AccountKitLoginResult;
import com.facebook.accountkit.ui.AccountKitActivity;
import com.facebook.accountkit.ui.AccountKitConfiguration;
import com.facebook.accountkit.ui.LoginType;
import com.ondande.cureserv.Model.CheckUserResponse;
import com.ondande.cureserv.Model.User;
import com.ondande.cureserv.Retrofit.CureServAPI;
import com.ondande.cureserv.Utils.Common;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.szagurskii.patternedtextwatcher.PatternedTextWatcher;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    Button btn_sign_up;
    private static final int REQUEST_CODE = 1000;
    CureServAPI mService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mService = Common.getAPI();

        btn_sign_up = (Button)findViewById(R.id.btn_sign_up);
        btn_sign_up.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                 startLoginPage(LoginType.PHONE);
            }
        });

//        printKeyHash();
    }

    private void startLoginPage(LoginType loginType) {

        Intent intent = new Intent(this, AccountKitActivity.class);
        AccountKitConfiguration.AccountKitConfigurationBuilder builder =
                new AccountKitConfiguration.AccountKitConfigurationBuilder(loginType,
                        AccountKitActivity.ResponseType.TOKEN);
        intent.putExtra(AccountKitActivity.ACCOUNT_KIT_ACTIVITY_CONFIGURATION, builder.build());
        startActivityForResult(intent, REQUEST_CODE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE){
            AccountKitLoginResult result = data.getParcelableExtra(AccountKitLoginResult.RESULT_KEY);

            if(result.getError() != null){
                Toast.makeText(this, ""+result.getError().getErrorType().getMessage(), Toast.LENGTH_SHORT).show();

            }
            else if(result.wasCancelled()){
                Toast.makeText(this, "Cancel", Toast.LENGTH_SHORT).show();
            }
            else{
                if(result.getAccessToken() != null){
                    final SpotsDialog alertDialog = new SpotsDialog(MainActivity.this);
                    alertDialog.show();
                    alertDialog.setMessage("Waiting...");

                    AccountKit.getCurrentAccount(new AccountKitCallback<Account>() {
                        @Override
                        public void onSuccess(final Account account) {
                            mService.checkUserExists(account.getPhoneNumber().toString())
                                    .enqueue(new Callback<CheckUserResponse>() {
                                        @Override
                                        public void onResponse(Call<CheckUserResponse> call, Response<CheckUserResponse> response) {
                                            CheckUserResponse userResponse = response.body();
                                            if(userResponse.isExists()){
                                                alertDialog.dismiss();
                                            }
                                            else{
                                                alertDialog.dismiss();

                                                showRegisterDialog(account.getPhoneNumber().toString());
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<CheckUserResponse> call, Throwable t) {

                                        }
                                    });
                        }

                        @Override
                        public void onError(AccountKitError accountKitError) {
                            Log.d("ERROR", accountKitError.getErrorType().getMessage());
                        }
                    });
                }

            }
        }

    }

    private void showRegisterDialog(final String phone) {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
        alertDialog.setTitle("SIGN UP");

        LayoutInflater inflater = this.getLayoutInflater();
        View sign_up = inflater.inflate(R.layout.sign_up, null);
        final MaterialEditText edt_name = (MaterialEditText)sign_up.findViewById(R.id.edt_name);
        final MaterialEditText edt_address = (MaterialEditText)sign_up.findViewById(R.id.edt_address);
        final MaterialEditText edt_birth = (MaterialEditText)sign_up.findViewById(R.id.edt_birth);

        MaterialButton btn_conf = (MaterialButton)sign_up.findViewById(R.id.btn_conf);

        edt_birth.addTextChangedListener(new PatternedTextWatcher("####-##-##"));

        btn_conf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog.create().dismiss();

                final SpotsDialog waitDialog = new SpotsDialog(MainActivity.this);
                waitDialog.show();
                waitDialog.setMessage("Waiting...");
                if (TextUtils.isEmpty(edt_name.getText().toString())) {
                    Toast.makeText(MainActivity.this, "Please enter your name", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(edt_address.getText().toString())) {
                    Toast.makeText(MainActivity.this, "Please enter your address", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(edt_birth.getText().toString())) {
                    Toast.makeText(MainActivity.this, "Please enter your birthdate", Toast.LENGTH_SHORT).show();
                    return;
                }

                mService.registerNewUser(phone,
                        edt_name.getText().toString(),
                        edt_birth.getText().toString(),
                        edt_address.getText().toString(),
                        1,
                        "good",
                        "1",
                        "psychologist")
                        .enqueue(new Callback<User>() {
                            @Override
                            public void onResponse(Call<User> call, Response<User> response) {
                                waitDialog.dismiss();

                                User user = response.body();
                                if(TextUtils.isEmpty(user.getError_msg())){
                                    Toast.makeText(MainActivity.this, "User register successfully", Toast.LENGTH_SHORT);


                                }
                            }

                            @Override
                            public void onFailure(Call<User> call, Throwable t) {
                                waitDialog.dismiss();
                            }
                        });
            }

        });
        alertDialog.setView(sign_up);
        alertDialog.show();
    }

    private void printKeyHash() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo("com.ondande.cureserv", PackageManager.GET_SIGNATURES);
            for(Signature signature:info.signatures){
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KEYHASH", Base64.encodeToString(md.digest(),Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

    }


}
