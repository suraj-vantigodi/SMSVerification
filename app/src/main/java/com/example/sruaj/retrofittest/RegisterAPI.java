package com.example.sruaj.retrofittest;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

/**
 * Created by Belal on 11/5/2015.
 */
public interface RegisterAPI {
    @FormUrlEncoded
    @POST("/android_sms/msg91/request_sms.php")
    public void insertUser(
            @Field("name") String name,
            @Field("email") String email,
            @Field("mobile") String mobile,
            Callback<Response> callback);
}
