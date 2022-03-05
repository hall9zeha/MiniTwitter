package com.barryzea.minitwitter.Ui.Auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import com.barryzea.minitwitter.Common.Constants;
import com.barryzea.minitwitter.Common.SharedPreferencesManager;
import com.barryzea.minitwitter.Model.RequestSignUp;
import com.barryzea.minitwitter.Model.ResponseAuth;
import com.barryzea.minitwitter.R;
import com.barryzea.minitwitter.Retrofit.MiniTwitterClient;
import com.barryzea.minitwitter.Retrofit.MiniTwitterService;
import com.barryzea.minitwitter.Ui.DashboardActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {
    private Button  btnRegister;
    private TextView tvLogin;
    private EditText edUserName;
    private EditText edEmail;
    private EditText edPassword;
    private MiniTwitterClient miniTwitterClient;
    private MiniTwitterService miniTwitterService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_sign_up);

        setUpViews();
        setUpEvents();


    }

    private void setUpViews() {
        tvLogin=findViewById(R.id.textViewBackToLogin);
        btnRegister=findViewById(R.id.buttonRegister);
        edUserName=findViewById(R.id.editTextTextUserNameRegister);
        edEmail=findViewById(R.id.editTextEmailRegister);
        edPassword=findViewById(R.id.editTextPasswordRegister);
    }

    private void setUpEvents() {
        tvLogin.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int idView=view.getId();
        switch(idView){
            case R.id.buttonRegister:
                goToDashboard();
                break;
            case R.id.textViewBackToLogin:
                backToLogin();
                break;
        }
    }

    private void goToDashboard() {
        String userName=edUserName.getText().toString();
        String email=edEmail.getText().toString();
        String passwd=edPassword.getText().toString();
        if(userName.isEmpty()){
            edUserName.setError("El nombre de usuario es requerido");
        }
        else if(email.isEmpty()){
            edEmail.setError("El email es requerido");
        }
        else if(passwd.isEmpty() || passwd.length() <4){
            edPassword.setText("La contraseña es requerida y debe tener al menos cuatro caracteres");
        }
        else{
            miniTwitterClient=MiniTwitterClient.getInstance();
            miniTwitterService=miniTwitterClient.getMiniTwitterService();

            RequestSignUp requestSignUp=new RequestSignUp(userName, email, passwd, Constants.codeUdemy);
            Call<ResponseAuth> call = miniTwitterService.doSignUp(requestSignUp);
            call.enqueue(new Callback<ResponseAuth>() {
                @Override
                public void onResponse(Call<ResponseAuth> call, Response<ResponseAuth> response) {
                    if(response.isSuccessful()){
                        SharedPreferencesManager.setSomeStringValue(Constants.TOKEN_PREF, response.body().getToken());
                        SharedPreferencesManager.setSomeStringValue(Constants.USER_NAME_PREF, response.body().getUserName());
                        SharedPreferencesManager.setSomeStringValue(Constants.PHOTO_URL_PREF, response.body().getPhotoUrl());
                        SharedPreferencesManager.setSomeStringValue(Constants.CREATED_PREF, response.body().getCreated());
                        SharedPreferencesManager.setSomeBooleanValue(Constants.ACTIVE, response.body().getActive());

                        Intent intent=new Intent(SignUpActivity.this, DashboardActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else{
                        Toast.makeText(SignUpActivity.this, "Ha ocurrido un error", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseAuth> call, Throwable t) {
                    Toast.makeText(SignUpActivity.this, "Error en la conexión", Toast.LENGTH_SHORT).show();
                }
            });

        }
    }

    void backToLogin(){
        Intent intentBackToLogin=new Intent(SignUpActivity.this, MainActivity.class);
        startActivity(intentBackToLogin);
        finish();
    }
}