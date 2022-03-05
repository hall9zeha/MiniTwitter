package com.barryzea.minitwitter.Ui.Tweet;

import android.content.Context;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.barryzea.minitwitter.Adapters.MyTweetRecyclerViewAdapter;
import com.barryzea.minitwitter.Common.Constants;
import com.barryzea.minitwitter.Data.TweetViewModel;
import com.barryzea.minitwitter.Model.Tweet;
import com.barryzea.minitwitter.R;
import com.barryzea.minitwitter.Retrofit.AuthTwitterClient;
import com.barryzea.minitwitter.Retrofit.AuthTwitterService;

import java.util.ArrayList;
import java.util.List;


public class TweetListFragment extends Fragment {


    private static final String ARG_COLUMN_COUNT = "column-count";
    private MyTweetRecyclerViewAdapter adapterTweet;
    private  RecyclerView recyclerView;
    private List<Tweet>listTweet = new  ArrayList();
    private AuthTwitterService authTwitterService;
    private AuthTwitterClient authTwitterClient;
    private TweetViewModel tweetViewModel;
    private SwipeRefreshLayout swipeRefreshLayout;
    // TODO: Customize parameters
    private int tweetListType = 1;


    public TweetListFragment() {
    }


    public static TweetListFragment newInstance(int tweetListType) {
        TweetListFragment fragment = new TweetListFragment();
        Bundle args = new Bundle();
        args.putInt(Constants.TWEET_LIST_TYPE, tweetListType);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tweetViewModel =  new ViewModelProvider(getActivity()).get(TweetViewModel.class);
        if (getArguments() != null) {
            tweetListType = getArguments().getInt(Constants.TWEET_LIST_TYPE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tweet_list, container, false);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayoutTweet);
        recyclerView = view.findViewById(R.id.list);
        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(getActivity(),R.color.colorBlue));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                recyclerView.setAdapter(adapterTweet);
                if(tweetListType == Constants.TWEETS_ALL) {
                   loadNewTweets();
                }
                else if(tweetListType ==Constants.TWEETS_FAV){
                    
                    loadNewFavTweets();
                }

            }
        });
        // Set the adapter

            Context context = view.getContext();


                recyclerView.setLayoutManager(new LinearLayoutManager(context));


            initRetrofitData();

            adapterTweet=new MyTweetRecyclerViewAdapter(getActivity(), listTweet);
            recyclerView.setAdapter(adapterTweet);
        if(tweetListType == Constants.TWEETS_ALL) {
            setAdapterTweets();
        }
        else if(tweetListType ==Constants.TWEETS_FAV){
            setFavTweets();
        }

        return view;
    }

    private void loadNewFavTweets() {
        tweetViewModel.getNewFavTweets().observe(getActivity(), new Observer<List<Tweet>>() {
            @Override
            public void onChanged(List<Tweet> tweets) {
              listTweet=tweets;
              swipeRefreshLayout.setRefreshing(false);
              adapterTweet.setNewData(listTweet);
              tweetViewModel.getNewFavTweets().removeObserver(this);
            }
        });
    }

    private void setFavTweets() {
        tweetViewModel.getAllFavTweets().observe(getActivity(), new Observer<List<Tweet>>() {
            @Override
            public void onChanged(List<Tweet> tweets) {
                listTweet=tweets;
                adapterTweet.setNewData(listTweet);
            }
        });
    }

    private void initRetrofitData() {
        authTwitterClient = new AuthTwitterClient();
        authTwitterService=authTwitterClient.getAuthTwitterService();
    }

    private void setAdapterTweets(){

                    tweetViewModel.getAllTweets().observe(getActivity(), new Observer<List<Tweet>>() {
                        @Override
                        public void onChanged(List<Tweet> tweets) {
                            listTweet=tweets;
                            adapterTweet.setNewData(listTweet);

                        }
                    });



    }
    private void loadNewTweets(){

        tweetViewModel.getNewTweets().observe(getActivity(), new Observer<List<Tweet>>() {
            @Override
            public void onChanged(List<Tweet> tweets) {
                listTweet=tweets;
                adapterTweet.setNewData(listTweet);
                swipeRefreshLayout.setRefreshing(false);
                tweetViewModel.getNewTweets().removeObserver(this);
            }
        });



    }
}