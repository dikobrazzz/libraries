package com.example.dikob.app5.mvp.model.image;

import android.support.annotation.Nullable;

import com.example.dikob.app5.mvp.model.cashe.ImageCashe;

import javax.inject.Inject;


public interface ImageLoader<T> {
    void loadInto(@Nullable String url, T container);
}
