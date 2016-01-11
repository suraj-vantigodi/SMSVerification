package com.example.sruaj.retrofittest;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

/**
 * Created by sruaj on 1/10/2016.
 */
public interface RegisterOTP {
    @FormUrlEncoded
    @POST("/android_sms/msg91/verify_otp.php")
    public void checkOTP(
            @Field("otp") String otp,
            Callback<Response> callback);
}
