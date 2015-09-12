package com.komootchallenge;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.komootchallenge.datamodel.Photo;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Lucas on 12/09/2015.
 */
public class PhotoAdapter extends BaseAdapter {
    private ArrayList<Photo> photos;
    private Context context;

    public PhotoAdapter(Context context) {
        this.context = context;
        photos = new ArrayList<>();
    }

    public void addAll(Photo[] photos) {
        if (this.photos.isEmpty()) {
            Collections.addAll(this.photos, photos);
            notifyDataSetChanged();
        } else {
            ArrayList<Long> idList = new ArrayList<>();
            boolean hasChanged = false;
            for (Photo photo : this.photos) {
                idList.add(photo.getPhoto_id());
            }
            for (Photo photo : photos) {
                if (!idList.contains(photo.getPhoto_id())) {
                    this.photos.add(0, photo);
                    hasChanged =true;
                }
            }
            if (hasChanged){
                notifyDataSetChanged();
            }
        }

    }

    public void clear(){
        photos.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return photos.size();
    }

    @Override
    public Photo getItem(int position) {
        return photos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.photo_row, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.imageview);
            viewHolder.textView = (TextView) convertView.findViewById(R.id.row_textview);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Glide.with(context).load(getItem(position).getPhoto_file_url()).into(viewHolder.imageView);
        viewHolder.textView.setText(getItem(position).getPhoto_title());
        return convertView;
    }

    private class ViewHolder {
        ImageView imageView;
        TextView textView;

    }
}
