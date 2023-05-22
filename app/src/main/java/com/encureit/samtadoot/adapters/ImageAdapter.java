package com.encureit.samtadoot.adapters;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.encureit.samtadoot.Helpers.Helper;
import com.encureit.samtadoot.databinding.SingleImageBinding;
import com.encureit.samtadoot.models.CandidateDetails;

import java.util.List;

public class ImageAdapter extends BaseAdapter {
    private Activity mContext;
    private List<CandidateDetails> photoList;
    private OnImageDeleteListener onImageDeleteListener;

    public ImageAdapter(Activity mContext, List<CandidateDetails> photoList, OnImageDeleteListener onImageDeleteListener) {
        this.mContext = mContext;
        this.photoList = photoList;
        this.onImageDeleteListener = onImageDeleteListener;
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
            int image_parent_h_w = Helper.getScreenWidth(mContext) / 2;
            float dip_in_pxl = Helper.convertDpToPixel(10,mContext);
            int image_h_w = (int) (image_parent_h_w - dip_in_pxl);
            viewHolder.binding.llMain.setLayoutParams(new RelativeLayout.LayoutParams(image_parent_h_w,image_parent_h_w));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(image_h_w,image_h_w);
            params.setMargins(10,10,10,10);
            viewHolder.binding.imageView.setLayoutParams(params);
            viewHolder.binding.imageViewCapture.setLayoutParams(params);
            convertView = viewHolder.binding.getRoot();

            convertView.setTag(viewHolder);
        }
        else {
            viewHolder=(ViewHolder)convertView.getTag();
        }
        if (position == 0) {
            viewHolder.binding.imageViewCapture.setVisibility(View.VISIBLE);
            viewHolder.binding.imageView.setVisibility(View.GONE);
            viewHolder.binding.imgDeleteImage.setVisibility(View.GONE);
            viewHolder.binding.imgDeleteImage.setVisibility(View.GONE);
        } else {
            int image_parent_h_w = Helper.getScreenWidth(mContext) / 2;
            float dip_in_pxl = Helper.convertDpToPixel(10,mContext);
            int image_h_w = (int) (image_parent_h_w - dip_in_pxl);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(image_h_w,image_h_w);
            params.setMargins(10,10,10,10);
            viewHolder.binding.imageView.setLayoutParams(params);
            viewHolder.binding.imageViewCapture.setLayoutParams(params);

            viewHolder.binding.imgDeleteImage.setVisibility(View.VISIBLE);
            viewHolder.binding.imageViewCapture.setVisibility(View.GONE);
            viewHolder.binding.imageView.setVisibility(View.VISIBLE);
            String path = photoList.get(position).getSurvey_que_values();
            if (path != null && !path.isEmpty()) {
                Bitmap bitmap = BitmapFactory.decodeFile(path);
                if (bitmap != null) {
                    // binding.imgViewFoto.setImageBitmap(bitmap);
                    viewHolder.binding.imageView.setImageBitmap(bitmap);
                }
            }
            viewHolder.binding.imgDeleteImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onImageDeleteListener != null) {
                        onImageDeleteListener.onImageDelete(position);
                    }
                }
            });
        }
        return convertView;
    }

    public class ViewHolder {
        private SingleImageBinding binding;
    }

    public interface OnImageDeleteListener {
        void onImageDelete(int position);
    }
}
