package com.barryzea.minitwitter.Data;

import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.barryzea.minitwitter.Common.Constants;
import com.barryzea.minitwitter.Common.MyApp;
import com.barryzea.minitwitter.Common.SharedPreferencesManager;
import com.barryzea.minitwitter.Model.Likes;
import com.barryzea.minitwitter.Model.RequestCreateTweet;
import com.barryzea.minitwitter.Model.ResponseUploadPhoto;
import com.barryzea.minitwitter.Model.Tweet;
import com.barryzea.minitwitter.Model.TweetDeleted;
import com.barryzea.minitwitter.Model.User;
import com.barryzea.minitwitter.Model.UserProfile;
import com.barryzea.minitwitter.Model.UserProfileUpdate;
import com.barryzea.minitwitter.Retrofit.AuthTwitterClient;
import com.barryzea.minitwitter.Retrofit.AuthTwitterService;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileRepository {

    private AuthTwitterClient authTwitterClient;
    private AuthTwitterService authTwitterService;
    private MutableLiveData<UserProfile> userProfile;
    private MutableLiveData<String> photoProfile;

    public ProfileRepository(){

        authTwitterClient= new AuthTwitterClient();
        authTwitterService= authTwitterClient.getAuthTwitterService();
        userProfile=getUserProfile();
        if(photoProfile==null){
            photoProfile= new MutableLiveData<>();
        }

    }
    public MutableLiveData<UserProfile> getUserProfile() {
        if (userProfile == null) {
            userProfile = new MutableLiveData<>();
        }
        Call<UserProfile> call = authTwitterService.getUserProfile();
        call.enqueue(new Callback<UserProfile>() {
            @Override
            public void onResponse(Call<UserProfile> call, Response<UserProfile> response) {
                if(response.isSuccessful()){
                    userProfile.setValue(response.body());
                }
                else{
                    Toast.makeText(MyApp.getContext(), "Ha ocurrido un error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserProfile> call, Throwable t) {
                Toast.makeText(MyApp.getContext(), "Error en la conexión", Toast.LENGTH_SHORT).show();
            }
        });
        return userProfile;
    }

    public void updateUserProfile(UserProfileUpdate userProfileUpdate){
        Call<UserProfile> call=authTwitterService.updateUserProfile(userProfileUpdate);
        call.enqueue(new Callback<UserProfile>() {
            @Override
            public void onResponse(Call<UserProfile> call, Response<UserProfile> response) {
                if(response.isSuccessful()){
                    userProfile.setValue(response.body());
                }
                else{
                    Toast.makeText(MyApp.getContext(), "Ha ocurrido un error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserProfile> call, Throwable t) {
                Toast.makeText(MyApp.getContext(), "Error en la conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }

   public void  uploadPhoto(String photoPath){
       File file= new File(photoPath);

       RequestBody requestBody = RequestBody.create(MediaType.parse("image/jpeg"), file);
       Call<ResponseUploadPhoto> call = authTwitterService.uploadPhotoProfile(requestBody);
       call.enqueue(new Callback<ResponseUploadPhoto>() {
           @Override
           public void onResponse(Call<ResponseUploadPhoto> call, Response<ResponseUploadPhoto> response) {
               if(response.isSuccessful()){
                   SharedPreferencesManager.setSomeStringValue(Constants.PHOTO_URL_PREF, response.body().getFilename());
                   photoProfile.setValue(response.body().getFilename());
               }
               else{
                   Toast.makeText(MyApp.getContext(), "Ha ocurrido un error", Toast.LENGTH_SHORT).show();
               }
           }

           @Override
           public void onFailure(Call<ResponseUploadPhoto> call, Throwable t) {
               Toast.makeText(MyApp.getContext(), "Error en la conexión", Toast.LENGTH_SHORT).show();
           }
       });

   }
 public MutableLiveData<String> photoProfile(){
        return photoProfile;
 }
}
