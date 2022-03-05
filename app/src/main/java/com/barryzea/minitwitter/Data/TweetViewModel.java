package com.barryzea.minitwitter.Data;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.barryzea.minitwitter.Dialogs.BottomModalTweetFragment;
import com.barryzea.minitwitter.Model.RequestCreateTweet;
import com.barryzea.minitwitter.Model.Tweet;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TweetViewModel extends AndroidViewModel {

    private TweetRepository tweetRepository;
    private LiveData<List<Tweet>> tweetList;
    private LiveData<List<Tweet>> favTweetList;
    public TweetViewModel(@NonNull @NotNull Application application) {
        super(application);
        tweetRepository= new TweetRepository();
        tweetList=tweetRepository.getAllTweets();

    }
    public LiveData<List<Tweet>> getAllTweets(){
        return tweetList;
    }
    public LiveData<List<Tweet>> getAllFavTweets(){
        favTweetList=tweetRepository.getAllFavTweets();
        return favTweetList;
    }
    public LiveData<List<Tweet>> getNewTweets(){
        return tweetList=tweetRepository.getAllTweets();
    }
    public LiveData<List<Tweet>> getNewFavTweets(){
        getNewTweets();
        return  getAllFavTweets();
    }
    public void showDialogTweetMenu(Context ctx, int idTweet){
        BottomModalTweetFragment dialog= BottomModalTweetFragment.newInstance(idTweet);
        dialog.show(((AppCompatActivity)ctx).getSupportFragmentManager(), "dialogMenuTweeter");

    }

    public void insertNewTweet(String mensaje) {
            tweetRepository.insertNewTweet(mensaje);
    }
    public void deleteTweet(int idTweet){
        tweetRepository.deleteTweet(idTweet);
    }
    public void likeTweet(int idTweet){
        tweetRepository.likeNewTweet(idTweet);
    }
}
