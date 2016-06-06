package com.appspace.evytinkadmin.api;

import com.appspace.evytinkadmin.model.EvyTinkUser;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by siwaweswongcharoen on 6/6/2016 AD.
 */
public interface EvyTinkAPIService {

    @GET("evycheckfbloginjson.aspx")
    Call<EvyTinkUser[]> register(@Query("evarfid") String id, @Query("fname") String name);
}
