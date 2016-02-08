package com.example.sashok.instaview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by asmurthy on 12/2/15.
 */
public class InstagramPhotoAdapter extends ArrayAdapter<Photo> {
    public InstagramPhotoAdapter(Context context, List<Photo> objects) {
        super(context,0, objects);
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        ViewHolder holder;
        if (view != null) {
            holder = (ViewHolder) view.getTag();
        } else {
            view = LayoutInflater.from(getContext()).inflate(R.layout.item_photo, parent, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }

        Photo photo = getItem(position);

        holder.tvCaption.setText(photo.caption);
        holder.tvUser.setText(photo.userName);
        holder.tvLikes.setText(
                "Likes: " + Integer.toString(photo.likesCount) +
                 " ..." + photo.timestamp
        );
        holder.tvCommentsCount.setText("Comments: " + photo.commentsCount);
        holder.tvComment1.setText(photo.comment1);
        holder.tvComment2.setText(photo.comment2);

        //clear the image.. clean it
        //ivPhoto.setImageResource(0);
        //Insert the image using picasso
        Picasso.with(getContext())
                .load(photo.imageUrl)
                .fit()
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .centerCrop()
                .into(holder.ivPhoto);
        Picasso.with(getContext()).load(photo.userImageUrl).into(holder.ivUser);

        return view;
    }
    static class ViewHolder {
        @Bind(R.id.tvUser) TextView tvUser;
        @Bind(R.id.tvLikes) TextView tvLikes;
        @Bind(R.id.tvCaption) TextView tvCaption;
        @Bind(R.id.ivPhoto) ImageView ivPhoto;
        @Bind(R.id.ivUser) ImageView ivUser;
        @Bind(R.id.tvCommentsCount) TextView tvCommentsCount;
        @Bind(R.id.tvComment1) TextView tvComment1;
        @Bind(R.id.tvComment2) TextView tvComment2;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}

