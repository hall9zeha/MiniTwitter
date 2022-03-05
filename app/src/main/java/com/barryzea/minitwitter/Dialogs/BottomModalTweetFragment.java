package com.barryzea.minitwitter.Dialogs;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.barryzea.minitwitter.Common.Constants;
import com.barryzea.minitwitter.Data.TweetViewModel;
import com.barryzea.minitwitter.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.navigation.NavigationView;

import org.jetbrains.annotations.NotNull;


public class BottomModalTweetFragment extends BottomSheetDialogFragment {
    private TweetViewModel tweetViewModel;
    private int  idTweet;
    public BottomModalTweetFragment() {
        // Required empty public constructor
    }


    public static BottomModalTweetFragment newInstance(int tweetId) {
        BottomModalTweetFragment fragment = new BottomModalTweetFragment();
        Bundle args = new Bundle();
        args.putInt(Constants.DIALOG_TWEET_ID, tweetId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tweetViewModel = new ViewModelProvider(getActivity()).get(TweetViewModel.class);
        if (getArguments() != null) {
            idTweet = getArguments().getInt(Constants.DIALOG_TWEET_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =inflater.inflate(R.layout.fragment_bottom_modal_tweet, container, false);
        NavigationView nav=v.findViewById(R.id.bottomNavigationModalMenu);
        nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
                int id=item.getItemId();
                if(id == R.id.action_delete_tweet){
                    tweetViewModel.deleteTweet(idTweet);
                    getDialog().dismiss();
                    return true;
                }
                return false;
            }
        });
        return v;
    }
}