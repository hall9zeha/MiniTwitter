package com.barryzea.minitwitter.Ui;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.barryzea.minitwitter.Common.Constants;
import com.barryzea.minitwitter.Common.SharedPreferencesManager;
import com.barryzea.minitwitter.Dialogs.NewTweetDialog;
import com.barryzea.minitwitter.R;
import com.barryzea.minitwitter.Ui.Profile.ProfileFragment;
import com.barryzea.minitwitter.Ui.Profile.ProfileViewModel;
import com.barryzea.minitwitter.Ui.Tweet.TweetListFragment;
import com.barryzea.minitwitter.databinding.ActivityDashboardBinding;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import org.jetbrains.annotations.NotNull;


public class DashboardActivity extends AppCompatActivity implements PermissionListener {


    private FloatingActionButton fab;
    private ImageView ivAvatar;
    private ProfileViewModel profileViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().hide();


        setContentView(R.layout.activity_dashboard);
        profileViewModel= new ViewModelProvider(this).get(ProfileViewModel.class);

        fab=findViewById(R.id.fab);
        ivAvatar=findViewById(R.id.imageViewAvatarToolbar);
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.nav_host_fragment_activity_dashboard, TweetListFragment.newInstance(Constants.TWEETS_ALL), "fragment")
                .commit();

        BottomNavigationView navView = findViewById(R.id.nav_view);

        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
                Fragment f=null;
                switch(item.getItemId()){
                    case R.id.navigation_home:
                        fab.show();
                        f=TweetListFragment.newInstance(Constants.TWEETS_ALL);

                        break;
                    case R.id.navigation_dashboard:
                        fab.hide();
                        f=TweetListFragment.newInstance(Constants.TWEETS_FAV);

                        break;
                    case R.id.navigation_notifications:
                        f= new ProfileFragment();
                        fab.hide();
                        break;

                }

                if(f!=null){
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.nav_host_fragment_activity_dashboard,f)
                            .commit();
                return true;
                }
                return false;
            }
        });
       fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NewTweetDialog dialog= new NewTweetDialog();
                dialog.show(getSupportFragmentManager(), "dialogNewTweet");
            }
        });
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
       /*
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_dashboard);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);


        */
        String photoUrl= SharedPreferencesManager.getStringValuePreference(Constants.PHOTO_URL_PREF);
        if(!photoUrl.isEmpty()) {
            Glide.with(this).load(Constants.BASE_URL_FILES + photoUrl)
                    .dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(ivAvatar);


        }
        profileViewModel.photoProfile.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (!s.isEmpty()) {
                    Glide.with(DashboardActivity.this).load(Constants.BASE_URL_FILES + s)
                            .dontAnimate()
                            .centerCrop()
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true)
                            .into(ivAvatar);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == Constants.SELECT_PHOTO_RESPONSE) {
                if (data != null) {
                    Uri imageUri = data.getData();
                    String[] filePathColumn ={MediaStore.Images.Media.DATA};
                    Cursor c=getContentResolver().query(imageUri, filePathColumn, null, null,null);
                    if(c !=null){
                        c.moveToFirst();
                        int imageIndex=c.getColumnIndex(filePathColumn[0]);
                        String photoPath= c.getString(imageIndex);
                        profileViewModel.uploadPhoto(photoPath);
                        c.close();
                    }
                }
            }
        }
    }

    @Override
    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
        Intent intent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, Constants.SELECT_PHOTO_RESPONSE);

    }

    @Override
    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
        Toast.makeText(this, "Se han denegado los permisos necesarios", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {

    }
}