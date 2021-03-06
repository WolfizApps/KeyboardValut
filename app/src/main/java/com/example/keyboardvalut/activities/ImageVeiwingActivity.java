package com.example.keyboardvalut.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.example.keyboardvalut.R;
import com.example.keyboardvalut.databinding.ActivityImageViewingBinding;
import com.example.keyboardvalut.interfaces.ClickListener;
import com.example.keyboardvalut.utils.ImageRotationUtil;
import com.example.keyboardvalut.utils.ScreenUtils;

public class ImageVeiwingActivity extends AppCompatActivity implements ClickListener, LifecycleObserver {

    Context context;
    ActivityImageViewingBinding binding;

    String imagePath;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_image_viewing);
        ScreenUtils.hidingStatusBar(this);

        context = this;
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
        if (getIntent().getStringExtra("activityTag").equals("media")||getIntent().getStringExtra("activityTag").equals("breakInAlert")) {
            imagePath = getIntent().getStringExtra("path");
            BitmapFactory.Options Options = new BitmapFactory.Options();
            Options.inSampleSize = 3;
            Options.inJustDecodeBounds = false;
            Bitmap myBitmap = BitmapFactory.decodeFile(imagePath);
            Bitmap rotatedBitMap = ImageRotationUtil.rotatedBitmap(imagePath, myBitmap);
            binding.ivImage.setImage(ImageSource.bitmap(rotatedBitMap));
        } else {
            imagePath = getIntent().getStringExtra("path");
            binding.ivImage.setImage(ImageSource.uri(imagePath));
        }

        binding.setClickHandler(this);
    }
    int lifeCycleChecker = 0;
    @Override
    protected void onRestart() {
        super.onRestart();
        if (lifeCycleChecker==1)
        {
            startActivity(new Intent(this,VaultPasswordEnteringActivity.class));
            finish();

        }

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    void onMoveToBackground() {
        lifeCycleChecker = 1;
    }

    private void movingToBackActivity() {

        if (getIntent().getStringExtra("activityTag").equals("media")) {
            intent = new Intent(this, VaultMediaActivity.class);
        } else if (getIntent().getStringExtra("activityTag").equals("breakInAlert")){
            intent = new Intent(this, BreakInAlertImagesActivity.class);
        }else {
            intent = new Intent(this, VaultPhotosActivity.class);
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnBack:
                movingToBackActivity();
                break;
        }
    }
}