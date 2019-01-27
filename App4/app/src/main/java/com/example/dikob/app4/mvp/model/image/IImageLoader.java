package com.example.dikob.app4.mvp.model.image;

public interface IImageLoader<T> {
    void loadInto(String url, T container);
}
