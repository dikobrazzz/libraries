package com.example.dikob.app3.android3_1.mvp.model;

import io.reactivex.Completable;

public interface MyConverter {
    Completable convertJpgToPng(String src, String dst);
}
