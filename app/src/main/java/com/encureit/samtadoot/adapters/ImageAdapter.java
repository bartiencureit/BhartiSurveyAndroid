package com.encureit.samtadoot.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.encureit.samtadoot.databinding.SingleImageBinding;

import java.util.List;

public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private List<Bitmap> photoList;

    public ImageAdapter(Context mContext, List<Bitmap> photoList) {
        this.mContext = mContext;
        this.photoList = photoList;
    }

    @Override
    public int getCount() {
        return photoList.size();
    }

    @Override
    public Object getItem(int i) {
        return photoList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public void addItem(Bitmap bitmap) {
        photoList.add(bitmap);
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if(convertView==null){
            viewHolder = new ViewHolder();
            viewHolder.binding = SingleImageBinding.inflate(LayoutInflater.from(mContext));
            convertView = viewHolder.binding.getRoot();

            convertView.setTag(viewHolder);
        }
        else {
            viewHolder=(ViewHolder)convertView.getTag();
        }
        viewHolder.binding.imageView.setImageBitmap(photoList.get(position));
        return convertView;
    }

    public class ViewHolder {
        private SingleImageBinding binding;
    }
}
