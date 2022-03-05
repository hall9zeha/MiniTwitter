package com.barryzea.minitwitter.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.barryzea.minitwitter.Common.Constants;
import com.barryzea.minitwitter.Common.SharedPreferencesManager;
import com.barryzea.minitwitter.Data.TweetViewModel;
import com.barryzea.minitwitter.Model.RequestCreateTweet;
import com.barryzea.minitwitter.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.jetbrains.annotations.NotNull;

public class NewTweetDialog  extends DialogFragment implements View.OnClickListener {

    private ImageView ivUser, ivClose;
    private Button btnNewTweet;
    private EditText editTextTweet;
    private Dialog dialogNewTweet;
    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogNewTweetStyle);
        dialogNewTweet = getDialog();
    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override

    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.new_tweet_dialog_layout, container, false);
        ivUser =view.findViewById(R.id.imageViewUser);
        ivClose=view.findViewById(R.id.imageViewClose);
        btnNewTweet = view.findViewById(R.id.buttonNewTweet);
        editTextTweet=view.findViewById(R.id.editTextNewTweet);
        ivClose.setOnClickListener(this);
        btnNewTweet.setOnClickListener(this);

        String photoUrl= SharedPreferencesManager.getStringValuePreference(Constants.PHOTO_URL_PREF);
        if(!photoUrl.isEmpty()) {
            Glide.with(getActivity()).load(Constants.BASE_URL_FILES + photoUrl)
                    .centerCrop()
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(ivUser);
        }
        return view;

    }

    @Override
    public void onClick(View view) {
        int id=view.getId();
        String mensaje = editTextTweet.getText().toString();

        if(id==R.id.imageViewClose){
            if(!mensaje.isEmpty()){
                showAlertDialog();
            }
            else{
                getDialog().dismiss();
            }



        }
        else if(id==R.id.buttonNewTweet){

            if(mensaje.isEmpty()){
                Toast.makeText(getActivity(), "Debe de escribir algo", Toast.LENGTH_SHORT).show();
            }
            else {
                TweetViewModel tweetViewModel = new ViewModelProvider(getActivity()).get(TweetViewModel.class);
                tweetViewModel.insertNewTweet(mensaje);
                getDialog().dismiss();
            }
        }
    }

    private void showAlertDialog() {
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        builder.setTitle("Cancelar tweet");
        builder.setMessage("Realmente quiere eliminar el tweet, el mensaje de borrará");
        builder.setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                getDialog().dismiss();
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }


}
