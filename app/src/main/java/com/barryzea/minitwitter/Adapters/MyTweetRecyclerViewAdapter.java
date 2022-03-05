package com.barryzea.minitwitter.Adapters;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;


import com.barryzea.minitwitter.Common.Constants;
import com.barryzea.minitwitter.Common.SharedPreferencesManager;
import com.barryzea.minitwitter.Data.TweetViewModel;
import com.barryzea.minitwitter.Model.Likes;
import com.barryzea.minitwitter.Model.Tweet;
import com.barryzea.minitwitter.R;
import com.barryzea.minitwitter.databinding.FragmentTweetListBinding;
import com.barryzea.minitwitter.databinding.FragmentTweetListItemBinding;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;



public class MyTweetRecyclerViewAdapter extends RecyclerView.Adapter<MyTweetRecyclerViewAdapter.ViewHolder> {

    private Context ctx;
    private  List<Tweet> mValues;
    private String username;
    private TweetViewModel tweetViewModel;


    public MyTweetRecyclerViewAdapter(Context ctx, List<Tweet> items) {
        mValues = items;
        this.ctx=ctx;
        username= SharedPreferencesManager.getStringValuePreference(Constants.USER_NAME_PREF);
        tweetViewModel = new ViewModelProvider((FragmentActivity) ctx).get(TweetViewModel.class);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(FragmentTweetListItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        if(mValues !=null) {
            holder.mItem = mValues.get(position);
            holder.userName.setText("@"+mValues.get(position).getUser().getUsername());
            holder.message.setText(mValues.get(position).getMensaje());
            holder.countLikes.setText(String.valueOf(holder.mItem.getLikes().size()));

            holder.ivArrowMenu.setVisibility(View.GONE);
            if(holder.mItem.getUser().getUsername().equals(username)){
                holder.ivArrowMenu.setVisibility(View.VISIBLE);
            }
            holder.ivArrowMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    tweetViewModel.showDialogTweetMenu(ctx, Integer.parseInt(holder.mItem.getId().toString()));
                }
            });


            if (!holder.mItem.getUser().getPhotoUrl().equals("")) {
                Glide.with(ctx).load("https://www.minitwitter.com/apiv1/uploads/photos/" + holder.mItem.getUser().getPhotoUrl())
                        .dontAnimate()
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .centerCrop()
                        .into(holder.userAvatar);
            }
            Glide.with(ctx).load(R.drawable.ic_like)

                    .into(holder.ivLike);

            holder.countLikes.setTextColor(ctx.getResources().getColor(R.color.black));
            holder.countLikes.setTypeface(null, Typeface.NORMAL);
            for (Likes like : mValues.get(position).getLikes()) {
                if (like.getUsername().equals(username)) {
                    Glide.with(ctx).load(R.drawable.ic_like_pink)

                            .into(holder.ivLike);
                    holder.countLikes.setTextColor(ctx.getResources().getColor(R.color.pink));
                    holder.countLikes.setTypeface(null, Typeface.BOLD);
                    break;
                }
            }
            holder.ivLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    tweetViewModel.likeTweet(Integer.parseInt(holder.mItem.getId().toString()));
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if(mValues !=null){
        return mValues.size();}
        else
        {
            return 0;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public  TextView userName;
        public  TextView message;
        public TextView countLikes;
        public ImageView userAvatar;
        public ImageView ivLike;
        public  Tweet mItem;
        public ImageView ivArrowMenu;


        public ViewHolder(FragmentTweetListItemBinding binding) {
            super(binding.getRoot());

            userName = binding.textViewUsername;
            message = binding.textViewContent;
            countLikes = binding.textViewLikes;
            userAvatar = binding.imageViewAvatar;
            ivLike = binding.imageViewLike;
            ivArrowMenu= binding.imageViewArrow;
        }

        @Override
        public String toString() {
            return super.toString() + " '" + userName.getText() + "'";
        }
    }
    public void setNewData(List<Tweet> tweetList){
        this.mValues=tweetList;
        notifyDataSetChanged();
    }
}