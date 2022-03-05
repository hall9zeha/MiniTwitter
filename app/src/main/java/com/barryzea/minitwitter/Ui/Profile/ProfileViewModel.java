package com.barryzea.minitwitter.Ui.Profile;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.barryzea.minitwitter.Data.ProfileRepository;
import com.barryzea.minitwitter.Model.User;
import com.barryzea.minitwitter.Model.UserProfile;
import com.barryzea.minitwitter.Model.UserProfileUpdate;

import org.jetbrains.annotations.NotNull;

public class ProfileViewModel extends AndroidViewModel {
    public ProfileRepository profileRepository;
    public LiveData<UserProfile> userProfile;
    public  LiveData<String> photoProfile;
    public ProfileViewModel(@NonNull @NotNull Application application) {
        super(application);
        profileRepository= new ProfileRepository();
        userProfile=  profileRepository.getUserProfile();
        photoProfile=profileRepository.photoProfile();
    }
    public void updateUserProfile(UserProfileUpdate userProfileUpdate){
        profileRepository.updateUserProfile(userProfileUpdate);
    }
    public void uploadPhoto(String photoPath){
        profileRepository.uploadPhoto(photoPath);
    }
}