package com.encureit.samtadoot.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.encureit.samtadoot.databinding.SingleImageBinding;
import com.encureit.samtadoot.models.CandidateDetails;

import java.util.List;

public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private List<CandidateDetails> photoList;

    public ImageAdapter(Context mContext, List<CandidateDetails> photoList) {
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

    public void addItem(CandidateDetails bitmap) {
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
        String path = photoList.get(position).getSurvey_que_values();
        if (path != null && !path.isEmpty()) {
            Bitmap bitmap = BitmapFactory.decodeFile(path);
            if (bitmap != null) {
                // binding.imgViewFoto.setImageBitmap(bitmap);
                viewHolder.binding.imageView.setImageBitmap(bitmap);
            }
        }
        return convertView;
    }

    public class ViewHolder {
        private SingleImageBinding binding;
    }
}
