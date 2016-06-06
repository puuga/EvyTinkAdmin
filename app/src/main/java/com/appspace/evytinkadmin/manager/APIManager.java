package com.appspace.evytinkadmin.manager;

import com.appspace.evytinkadmin.api.EvyTinkAPIService;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by siwaweswongcharoen on 6/6/2016 AD.
 */
public class APIManager {

    private static APIManager instance;
    private EvyTinkAPIService service;

    public static APIManager getInstance() {
        if (instance == null)
            instance = new APIManager();
        return instance;
    }

    private APIManager() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://evbt.azurewebsites.net/docs/page/theme/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(EvyTinkAPIService.class);
    }

    public EvyTinkAPIService getEvyTinkAPIService() {
        return service;
    }
}
