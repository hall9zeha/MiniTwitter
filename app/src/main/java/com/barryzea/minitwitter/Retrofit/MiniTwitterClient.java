package com.barryzea.minitwitter.Retrofit;

import com.barryzea.minitwitter.Common.Constants;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MiniTwitterClient {
    private static MiniTwitterClient instance=null;
    private MiniTwitterService miniTwitterService;
    private Retrofit retrofit;

    public MiniTwitterClient() {
        retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL_MINI_TWITTER)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        miniTwitterService = retrofit.create(MiniTwitterService.class);
    }
    public static MiniTwitterClient getInstance(){
        if(instance==null){
            instance= new MiniTwitterClient();
        }
        return instance;
    }
    public MiniTwitterService getMiniTwitterService(){
            return miniTwitterService;
    }
}
