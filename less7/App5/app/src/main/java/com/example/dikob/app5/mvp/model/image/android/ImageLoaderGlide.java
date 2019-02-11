package com.example.dikob.app5.mvp.model.image.android;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.widget.ImageView;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.dikob.app5.mvp.model.cashe.ImageCashe;
import com.example.dikob.app5.mvp.model.image.ImageLoader;

import io.paperdb.Paper;
import io.realm.Realm;

import com.example.dikob.app5.mvp.common.Utils;
import com.example.dikob.app5.mvp.model.image.ImageLoader;
import com.example.dikob.app5.mvp.model.image.RealmImage;
import com.example.dikob.app5.ui.NetworkStatus;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;

public class ImageLoaderGlide implements ImageLoader<ImageView> {
    private ImageCashe imageCashe;

    public ImageLoaderGlide(ImageCashe imageCashe) {
        this.imageCashe = imageCashe;
    }

    @Override
    public void loadInto(@Nullable String url, ImageView container) {
        if (NetworkStatus.isOnline()) {
            GlideApp.with(container.getContext()).asBitmap().load(url).listener(new RequestListener<Bitmap>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                    return false;
                }

                @Override
                public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                    if (!imageCashe.contains(url)){
                        imageCashe.saveImage(url, resource);
                    }
                    return false;
                }
            }).into(container);
        } else {
            if (imageCashe.contains(url)){
                GlideApp.with(container.getContext())
                        .load(imageCashe.getFile(url))
                        .into(container);
            }
        }
    }
}
