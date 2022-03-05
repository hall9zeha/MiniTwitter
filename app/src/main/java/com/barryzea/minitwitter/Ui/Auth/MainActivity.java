package com.barryzea.minitwitter.Ui.Auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import com.barryzea.minitwitter.Common.Constants;
import com.barryzea.minitwitter.Common.SharedPreferencesManager;
import com.barryzea.minitwitter.Model.RequestLogin;
import com.barryzea.minitwitter.Model.ResponseAuth;
import com.barryzea.minitwitter.R;
import com.barryzea.minitwitter.Retrofit.MiniTwitterClient;
import com.barryzea.minitwitter.Retrofit.MiniTwitterService;
import com.barryzea.minitwitter.Ui.DashboardActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btnLogin;
    private TextView tvSignUp;
    private EditText editTextEmail, editTextPassword;
    private MiniTwitterClient miniTwitterClient;
    private MiniTwitterService miniTwitterService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
       setUpViews();
       setUpEvents();
       initServices();




    }
    private void setUpViews(){
        btnLogin=findViewById(R.id.buttonLogin);
        tvSignUp=findViewById(R.id.textViewRegister);
        editTextEmail=findViewById(R.id.editTextEmail);
        editTextPassword=findViewById(R.id.editTextPassword);
    }
    private void setUpEvents(){
        btnLogin.setOnClickListener(this);
        tvSignUp.setOnClickListener(this);
    }
    private void initServices(){
        miniTwitterClient=MiniTwitterClient.getInstance();
        miniTwitterService=miniTwitterClient.getMiniTwitterService();
    }

    @Override
    public void onClick(View view) {
        int idView=view.getId();
        switch (idView){
            case R.id.buttonLogin:
                goToLogin();
                break;
            case R.id.textViewRegister:
                goToRegister();
                break;
        }
    }

    private void goToLogin() {
        String email=editTextEmail.getText().toString();
        String passwd=editTextPassword.getText().toString();

        if (email.isEmpty()){
            editTextEmail.setError("El email es requerido");
        }
        else if(passwd.isEmpty()){
            editTextPassword.setError("Se requiere la contraseña");
        }
        else{
            RequestLogin requestLogin= new RequestLogin(email,passwd);

            Call<ResponseAuth> call=miniTwitterService.doLogin(requestLogin);
                    call.enqueue(new Callback<ResponseAuth>() {
                        @Override
                        public void onResponse(Call<ResponseAuth> call, Response<ResponseAuth> response) {
                            if(response.isSuccessful()){
                                SharedPreferencesManager.setSomeStringValue(Constants.TOKEN_PREF, response.body().getToken());
                                SharedPreferencesManager.setSomeStringValue(Constants.USER_NAME_PREF, response.body().getUserName());
                                SharedPreferencesManager.setSomeStringValue(Constants.PHOTO_URL_PREF, response.body().getPhotoUrl());
                                SharedPreferencesManager.setSomeStringValue(Constants.CREATED_PREF, response.body().getCreated());
                                SharedPreferencesManager.setSomeBooleanValue(Constants.ACTIVE, response.body().getActive());


                                Toast.makeText(MainActivity.this, "Conexión exitosa", Toast.LENGTH_SHORT).show();
                                String user=response.body().getUserName();
                                Toast.makeText(MainActivity.this, user, Toast.LENGTH_SHORT).show();
                                Intent intent= new Intent(MainActivity.this, DashboardActivity.class);
                                startActivity(intent);
                                finish();
                            }
                            else{
                                Toast.makeText(MainActivity.this, "Error en la conexión", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseAuth> call, Throwable t) {
                            Toast.makeText(MainActivity.this, "Ha habido un error", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    void goToRegister(){
        Intent intentRegister=new Intent(MainActivity.this, SignUpActivity.class);
        startActivity(intentRegister);
        finish();
    }
}