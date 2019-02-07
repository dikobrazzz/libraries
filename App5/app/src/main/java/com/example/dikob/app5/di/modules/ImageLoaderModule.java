package com.example.dikob.app5.di.modules;

import android.view.View;
import android.widget.ImageView;

import com.example.dikob.app5.mvp.model.cashe.ImageCashe;
import com.example.dikob.app5.mvp.model.image.ImageLoader;
import com.example.dikob.app5.mvp.model.image.android.ImageLoaderGlide;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@Module
public class ImageLoaderModule {

    @Named("glide")
    @Provides
    public ImageLoader<ImageView> glideImageLoader(ImageCashe imageCashe){
        return new ImageLoaderGlide(imageCashe);
    }
}
