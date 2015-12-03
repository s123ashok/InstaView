package com.example.sashok.instaview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sashok.instaview.Photo;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by asmurthy on 12/2/15.
 */
public class InstagramPhotoAdapter extends ArrayAdapter<Photo> {
    public InstagramPhotoAdapter(Context context, List<Photo> objects) {
        super(context,0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Photo photo = getItem(position );
        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_photo,parent,false);

        }
        TextView tvCaption = (TextView)convertView.findViewById(R.id.tvCaption);
        ImageView ivPhoto = (ImageView)convertView.findViewById(R.id.ivPhoto);

        tvCaption.setText(photo.caption);
        //clear the image.. clean it
        //ivPhoto.setImageResource(0);

        //Insert the image using picasso
        Picasso.with(getContext()).load(photo.imageUrl).into(ivPhoto);

        return convertView;
    }
}

