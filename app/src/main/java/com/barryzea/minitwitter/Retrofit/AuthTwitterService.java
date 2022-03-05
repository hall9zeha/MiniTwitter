package com.barryzea.minitwitter.Retrofit;

import com.barryzea.minitwitter.Model.RequestCreateTweet;
import com.barryzea.minitwitter.Model.RequestLogin;
import com.barryzea.minitwitter.Model.ResponseAuth;
import com.barryzea.minitwitter.Model.ResponseUploadPhoto;
import com.barryzea.minitwitter.Model.Tweet;
import com.barryzea.minitwitter.Model.TweetDeleted;
import com.barryzea.minitwitter.Model.User;
import com.barryzea.minitwitter.Model.UserProfile;
import com.barryzea.minitwitter.Model.UserProfileUpdate;

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface AuthTwitterService {

    @GET("users/profile")
    Call<UserProfile> getUserProfile();
    @PUT("users/profile")
    Call<UserProfile> updateUserProfile(@Body UserProfileUpdate userProfileUpdate);
    @GET("tweets/all")
    Call<List<Tweet>> getAllTweets();
    @POST("tweets/create")
    Call<Tweet> createTweet(@Body  RequestCreateTweet requestCreateTweet);
    @POST("tweets/like/{idTweet}")
    Call<Tweet> likeTweet(@Path("idTweet") int idTweet);
    @DELETE("tweets/{idTweet}")
    Call<TweetDeleted> deleteTweet(@Path("idTweet") int idTweet);
    //método para cargar foto
    @Multipart
    @POST("users/uploadprofilephoto")
    Call<ResponseUploadPhoto> uploadPhotoProfile(@Part("file\"; filename=\"photo.jpeg\" ")RequestBody file);
}
