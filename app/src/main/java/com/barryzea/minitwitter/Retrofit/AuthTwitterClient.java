package com.barryzea.minitwitter.Retrofit;

import com.barryzea.minitwitter.Common.Constants;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AuthTwitterClient {
    private static AuthTwitterClient instance=null;
    private AuthTwitterService authTwitterService;
    private Retrofit retrofit;

    public AuthTwitterClient() {
        //Incluir en la cabecera de la peticion el token que autoriza el usuario

        OkHttpClient.Builder okHttp=new OkHttpClient.Builder();
        okHttp.addInterceptor(new AuthInterceptor());
        OkHttpClient client=okHttp.build();
        retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL_MINI_TWITTER)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        authTwitterService = retrofit.create(AuthTwitterService.class);
    }
    public static AuthTwitterClient getInstance(){
        if(instance==null){
            instance= new AuthTwitterClient();
        }
        return instance;
    }
    public AuthTwitterService getAuthTwitterService(){
            return authTwitterService;
    }
}
