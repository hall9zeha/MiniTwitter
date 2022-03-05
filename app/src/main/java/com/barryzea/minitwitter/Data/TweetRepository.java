package com.barryzea.minitwitter.Data;

import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.barryzea.minitwitter.Common.Constants;
import com.barryzea.minitwitter.Common.MyApp;
import com.barryzea.minitwitter.Common.SharedPreferencesManager;
import com.barryzea.minitwitter.Model.Likes;
import com.barryzea.minitwitter.Model.RequestCreateTweet;
import com.barryzea.minitwitter.Model.Tweet;
import com.barryzea.minitwitter.Model.TweetDeleted;
import com.barryzea.minitwitter.Retrofit.AuthTwitterClient;
import com.barryzea.minitwitter.Retrofit.AuthTwitterService;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TweetRepository {

    private AuthTwitterClient authTwitterClient;
    private AuthTwitterService authTwitterService;
    private MutableLiveData<List<Tweet>> allTweets;
    private MutableLiveData<List<Tweet>> allFavTweets;
    private String userName;
    TweetRepository(){

        authTwitterClient= new AuthTwitterClient();
        authTwitterService= authTwitterClient.getAuthTwitterService();
        allTweets=getAllTweets();
        userName= SharedPreferencesManager.getStringValuePreference(Constants.USER_NAME_PREF);
    }
    public MutableLiveData<List<Tweet>> getAllTweets(){
        if(allTweets==null){
            allTweets= new MutableLiveData<>();
        }
        Call<List<Tweet>> call= authTwitterService.getAllTweets();
        call.enqueue(new Callback<List<Tweet>>() {
            @Override
            public void onResponse(Call<List<Tweet>> call, Response<List<Tweet>> response) {
                if(response.isSuccessful()){
                    allTweets.setValue(response.body());
                }
                else{
                    Toast.makeText(MyApp.getContext(), "Ha ocurrido algo", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Tweet>> call, Throwable t) {
                Toast.makeText(MyApp.getContext(), "Error en la conexión", Toast.LENGTH_SHORT).show();
            }
        });
        return allTweets;
    }

    public MutableLiveData<List<Tweet>> getAllFavTweets(){
        if(allFavTweets ==null){
            allFavTweets = new MutableLiveData<>();
        }
        List<Tweet> newFavList= new ArrayList<>();
        Iterator itTweets=allTweets.getValue().iterator();
        while(itTweets.hasNext()){
            Tweet current = (Tweet) itTweets.next();
            Iterator itLikes= current.getLikes().iterator();
            boolean enc=false;
            while(itLikes.hasNext() && !enc){
                Likes like=(Likes)itLikes.next();
                if(like.getUsername().equals(userName)){
                    enc=true;
                    newFavList.add(current);
                }
            }
        }
        allFavTweets.setValue(newFavList);
        return allFavTweets;
    }
    public void insertNewTweet(String mensaje){

        RequestCreateTweet tweet = new RequestCreateTweet(mensaje);

        Call<Tweet> call = authTwitterService.createTweet(tweet);
        call.enqueue(new Callback<Tweet>() {
            @Override
            public void onResponse(Call<Tweet> call, Response<Tweet> response) {
                if(response.isSuccessful()){
                    List<Tweet> listClone= new ArrayList<>();
                    listClone.add(response.body());
                   /* for(int i=0; i<allTweets.getValue().size(); i++){
                        listClone.add(new Tweet(allTweets.getValue().get(i)));
                    }*/
                    for(Tweet t : allTweets.getValue()){
                        listClone.add(t);
                    }

                    allTweets.setValue(listClone);
                }
                else{
                    Toast.makeText(MyApp.getContext(), "Ha ocurrido algo", Toast.LENGTH_SHORT).show();
                    Log.e("Nuevo Tweet", response.message().toString());
                }
            }

            @Override
            public void onFailure(Call<Tweet> call, Throwable t) {
                Toast.makeText(MyApp.getContext(), "Error en la conexión", Toast.LENGTH_SHORT).show();
            }
        });

    }
    public void deleteTweet(int idTweet){
        Call<TweetDeleted> call= authTwitterService.deleteTweet(idTweet);
        call.enqueue(new Callback<TweetDeleted>() {
            @Override
            public void onResponse(Call<TweetDeleted> call, Response<TweetDeleted> response) {
                if(response.isSuccessful()){
                    List<Tweet> listClone=new ArrayList<>();
                    for(int i=0; i<allTweets.getValue().size(); i++){
                        if(allTweets.getValue().get(i).getId() != idTweet ){
                            listClone.add(new Tweet(allTweets.getValue().get(i)));
                        }
                    }
                    allTweets.setValue(listClone);
                    getAllFavTweets();
                }
                else{
                    Toast.makeText(MyApp.getContext(), "Ha ocurrido algo", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<TweetDeleted> call, Throwable t) {
                Toast.makeText(MyApp.getContext(), "Error en la conexión", Toast.LENGTH_SHORT).show();
            }
        });

    }
    public void likeNewTweet(int idTweet){



        Call<Tweet> call = authTwitterService.likeTweet(idTweet);
        call.enqueue(new Callback<Tweet>() {
            @Override
            public void onResponse(Call<Tweet> call, Response<Tweet> response) {
                if(response.isSuccessful()){
                    List<Tweet> listClone= new ArrayList<>();

                   /* for(int i=0; i<allTweets.getValue().size(); i++){
                        listClone.add(new Tweet(allTweets.getValue().get(i)));
                    }*/
                    for(Tweet t : allTweets.getValue()){
                        if(t.getId() == idTweet) {
                            listClone.add(response.body());
                        }
                        else{
                            listClone.add(t);
                        }
                    }

                    allTweets.setValue(listClone);
                    getAllFavTweets();
                }
                else{
                    Toast.makeText(MyApp.getContext(), "Ha ocurrido algo", Toast.LENGTH_SHORT).show();
                    Log.e("Nuevo Tweet", response.message().toString());
                }
            }

            @Override
            public void onFailure(Call<Tweet> call, Throwable t) {
                Toast.makeText(MyApp.getContext(), "Error en la conexión", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
