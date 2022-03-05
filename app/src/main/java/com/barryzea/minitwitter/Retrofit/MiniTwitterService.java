package com.barryzea.minitwitter.Retrofit;

import com.barryzea.minitwitter.Model.RequestLogin;
import com.barryzea.minitwitter.Model.RequestSignUp;
import com.barryzea.minitwitter.Model.ResponseAuth;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface MiniTwitterService {

    @POST("auth/login")
    Call<ResponseAuth> doLogin(@Body RequestLogin requestLogin);
    @POST("auth/signUp")
    Call<ResponseAuth> doSignUp(@Body RequestSignUp requestSignUp);
}
