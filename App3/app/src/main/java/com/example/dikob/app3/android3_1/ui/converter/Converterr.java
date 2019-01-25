package com.example.dikob.app3.android3_1.ui.converter;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

import com.example.dikob.app3.android3_1.mvp.model.MyConverter;

import io.reactivex.Completable;
import io.reactivex.functions.Action;
import timber.log.Timber;

public class Converterr implements MyConverter {

    Context context;

    public Converterr(Context context) {
        this.context = context;
    }

    @Override
    public Completable convertJpgToPng(final String src, String dst) {
        return Completable.fromAction(() -> {
            Thread.sleep(3000);
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), Uri.parse(src));
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, context.getContentResolver().openOutputStream(Uri.parse(dst)));
            Timber.d("CONVERTED");
        });
    }
}
