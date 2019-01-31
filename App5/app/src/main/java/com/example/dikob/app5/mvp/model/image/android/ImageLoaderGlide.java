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
                    String out = Environment.getExternalStorageDirectory() + "/" + Environment.DIRECTORY_DCIM + "/" + System.currentTimeMillis() + ".png";
//                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
//                    resource.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    String urlInDevice = Uri.fromFile(new File(out)).toString();
                    Realm realm = Realm.getDefaultInstance();
                    RealmImage realmImage = realm.where(RealmImage.class).equalTo( "url", Utils.SHA1(url)).findFirst();
                    if (realmImage == null){
                        realm.executeTransaction(innerRealm -> {
                            RealmImage newRealmImage = innerRealm.createObject(RealmImage.class, Utils.SHA1(url));
                            newRealmImage.setUrlInDevice(urlInDevice);
                        });
                    }
                    try {
                        resource.compress(Bitmap.CompressFormat.PNG, 100, container.getContext()
                                .getContentResolver().openOutputStream(Uri.parse(realmImage.getUrlInDevice())));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    realm.close();
//                    Paper.book("images").write(Utils.SHA1(url), stream.toByteArray());
                    return false;
                }
            }).into(container);
        } else {
            String sha1 = Utils.SHA1(url);
            Realm realm = Realm.getDefaultInstance();
            RealmImage realmImage = realm.where(RealmImage.class).equalTo("url", Utils.SHA1(url)).findFirst();
            if (realmImage != null){
                String urlInDevice = realmImage.getUrlInDevice();
                GlideApp.with(container.getContext())
                        .load(urlInDevice)
                        .into(container);
                realm.close();
            }
//            if(Paper.book("images").contains(sha1)){
//                byte[] bytes = Paper.book("images").read(sha1);
//                GlideApp.with(container.getContext())
//                        .load(bytes)
//                        .into(container);
//            }
        }
    }
}
