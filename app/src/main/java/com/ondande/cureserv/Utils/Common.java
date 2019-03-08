package com.ondande.cureserv.Utils;

import com.ondande.cureserv.Retrofit.CureServAPI;
import com.ondande.cureserv.Retrofit.RetrofitClient;

public class Common {
    public static final String BASE_URL = "http://10.0.2.2/CureServ/";

    public static CureServAPI getAPI(){
        return RetrofitClient.getClient(BASE_URL).create(CureServAPI.class);
    }
}
