package com.ondande.cureserv.Utils;

import com.ondande.cureserv.Retrofit.CureServAPI;
import com.ondande.cureserv.Retrofit.RetrofitClient;

public class Common {
    private static final String BASE_URL = "http://cureserv.000webhostapp.com";

    public static CureServAPI getAPI(){
        return RetrofitClient.getClient(BASE_URL).create(CureServAPI.class);
    }
}
