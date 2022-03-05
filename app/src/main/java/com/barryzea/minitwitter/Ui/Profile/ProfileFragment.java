package com.barryzea.minitwitter.Ui.Profile;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import com.barryzea.minitwitter.Common.Constants;
import com.barryzea.minitwitter.Model.User;
import com.barryzea.minitwitter.Model.UserProfile;
import com.barryzea.minitwitter.Model.UserProfileUpdate;
import com.barryzea.minitwitter.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.CompositePermissionListener;
import com.karumi.dexter.listener.single.DialogOnDeniedPermissionListener;
import com.karumi.dexter.listener.single.PermissionListener;

public class ProfileFragment extends Fragment {

    private ProfileViewModel profileViewModel;
    private ImageView ivAvatar;
    private Button btnSave, btnChangePasswd;
    private EditText edtUserName, edtEmail, edtPasswd, edtDescription, edtWebsite;
    private boolean loadingData=true;
    private PermissionListener allPermissions;

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        profileViewModel = new ViewModelProvider(getActivity()).get(ProfileViewModel.class);
    }

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View v= inflater.inflate(R.layout.profile_fragment, container, false);
        btnSave=v.findViewById(R.id.buttonSaveEdit);
        btnChangePasswd=v.findViewById(R.id.buttonChangePassword);
        edtUserName=v.findViewById(R.id.editTextUserNameEdit);
        edtEmail=v.findViewById(R.id.editTextEmailEdit);
        edtPasswd=v.findViewById(R.id.editTextPasswordCurrent);
        edtWebsite=v.findViewById(R.id.editTextWebsite);
        edtDescription=v.findViewById(R.id.editTextDescription);
        ivAvatar=v.findViewById(R.id.imageViewAvatarEdit);

        btnSave.setOnClickListener(view ->{
            String username, email, descripcion, website,password;
            username=edtUserName.getText().toString();
            email=edtEmail.getText().toString();
            descripcion=edtDescription.getText().toString();
            website=edtWebsite.getText().toString();
            password=edtPasswd.getText().toString();

            if(username.isEmpty()){
                edtUserName.setError("El nombre de usuario es requerido");
            }
            else if(email.isEmpty()){
                edtEmail.setError("El email es requerido");
            }
            else if(password.isEmpty()){
                edtPasswd.setError("La contraseña es requerida");
            }
            else {

                UserProfileUpdate userUpdate = new UserProfileUpdate(username, email, descripcion, website, password);
                profileViewModel.updateUserProfile(userUpdate);
                Toast.makeText(getActivity(), "Enviando datos al servidor", Toast.LENGTH_SHORT).show();
                btnSave.setEnabled(false);
                edtPasswd.getText().clear();
                loadingData=false;
            }
        });
        btnChangePasswd.setOnClickListener(view->{

        });

        ivAvatar.setOnClickListener(view->{
            chekPermissions();
        });
        profileViewModel.userProfile.observe(getActivity(), new Observer<UserProfile>() {
            @Override
            public void onChanged(UserProfile user) {
                edtUserName.setText(user.getUsername());
                edtEmail.setText(user.getEmail());
                edtWebsite.setText(user.getWebsite());
                edtDescription.setText(user.getDescripcion());

                if(!user.getPhotoUrl().isEmpty()){
                    Glide.with(getActivity()).load(Constants.BASE_URL_FILES + user.getPhotoUrl())
                            .dontAnimate()
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .centerCrop()
                            .skipMemoryCache(true)
                            .into(ivAvatar);
                }
                if(!loadingData){
                    Toast.makeText(getActivity(), "Datos guardados correctamente", Toast.LENGTH_SHORT).show();
                    btnSave.setEnabled(true);
                }
            }
        });
        profileViewModel.photoProfile.observe(getActivity(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if(!s.isEmpty()){
                    Glide.with(getActivity()).load(Constants.BASE_URL_FILES + s)
                            .dontAnimate()
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true)
                            .into(ivAvatar);
                }
            }
        });
        return v;
    }
    private void chekPermissions(){
            PermissionListener dialogPermission=
                    DialogOnDeniedPermissionListener.Builder.withContext(getActivity())
                    .withTitle("Permisos")
                    .withMessage("Estos permisos son necesarios para subir una imagen")
                    .withButtonText("Aceptar")
                    .withIcon(R.mipmap.ic_launcher)
                    .build();
            allPermissions=new CompositePermissionListener(
                    (PermissionListener)getActivity(),
                    dialogPermission
            );
        Dexter.withContext(getActivity())
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(allPermissions)
                .check();


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }

}