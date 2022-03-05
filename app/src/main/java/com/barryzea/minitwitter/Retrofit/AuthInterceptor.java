package com.barryzea.minitwitter.Retrofit;

import com.barryzea.minitwitter.Common.Constants;
import com.barryzea.minitwitter.Common.SharedPreferencesManager;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {

        String token = SharedPreferencesManager.getStringValuePreference(Constants.TOKEN_PREF);
        Request request= chain.request().newBuilder().addHeader("Authorization","Bearer " +token).build();

        return chain.proceed(request);
    }
}
