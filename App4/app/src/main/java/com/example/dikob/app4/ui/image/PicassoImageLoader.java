package com.example.dikob.app4.ui.image;

import android.widget.ImageView;

import com.example.dikob.app4.mvp.model.image.IImageLoader;
import com.squareup.picasso.Picasso;

public class PicassoImageLoader implements IImageLoader<ImageView> {
    @Override
    public void loadInto(String url, ImageView container) {
        Picasso.get().load(url).into(container);
    }
}
