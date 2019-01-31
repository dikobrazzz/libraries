package com.example.dikob.app4.ui.image;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.dikob.app4.mvp.model.image.IImageLoader;

public class GlideImageLoader implements IImageLoader<ImageView> {
    @Override
    public void loadInto(String url, ImageView container) {
        GlideApp.with(container.getContext())
                .load(url)
                .into(container);
}
}
