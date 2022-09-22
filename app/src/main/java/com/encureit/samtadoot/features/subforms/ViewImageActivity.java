package com.encureit.samtadoot.features.subforms;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import com.encureit.samtadoot.R;
import com.encureit.samtadoot.database.DatabaseUtil;
import com.encureit.samtadoot.databinding.ActivityViewImageBinding;
import com.encureit.samtadoot.lib.AppKeys;
import com.encureit.samtadoot.models.CandidateDetails;

public class ViewImageActivity extends AppCompatActivity {
    private ActivityViewImageBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_view_image);
        Intent intent = getIntent();
        if (intent.hasExtra(AppKeys.IMAGE)) {
            CandidateDetails details = intent.getParcelableExtra(AppKeys.IMAGE);
            String path = details.getSurvey_que_values();
            if (path != null && !path.isEmpty()) {
                Bitmap bitmap = BitmapFactory.decodeFile(path);
                if (bitmap != null) {
                    // binding.imgViewFoto.setImageBitmap(bitmap);
                    binding.imgView.setImageBitmap(bitmap);
                }
            }
        } else {
            onBackPressed();
        }
    }
}