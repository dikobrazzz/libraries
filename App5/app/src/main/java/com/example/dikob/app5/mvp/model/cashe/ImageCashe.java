package com.example.dikob.app5.mvp.model.cashe;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;

import com.example.dikob.app5.App;
import com.example.dikob.app5.mvp.common.Utils;
import com.example.dikob.app5.mvp.model.image.RealmImage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import io.realm.Realm;
import timber.log.Timber;

public class ImageCashe {

    private static final String FOLDER_NAME = "Pictures";

    public File saveImage(String url, Bitmap resource) {
        if (!getImageDir().exists() && !getImageDir().mkdirs()) {
            throw new RuntimeException(("Failed to create directory: " + getImageDir().toString()));
        }
        final String fileformat = url.contains(".jpg") ? ".jpg" : ".png";
        final File imageFile = new File(getImageDir(), Utils.SHA1(url) + fileformat);
        FileOutputStream fos;

        try {
            fos = new FileOutputStream(imageFile);
            resource.compress(fileformat.equals(".jpg") ? Bitmap.CompressFormat.JPEG : Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
        } catch (Exception e) {
            Timber.e(e);
            return null;
        }

        Realm.getDefaultInstance().executeTransactionAsync(realm -> {
            RealmImage realmImage = new RealmImage();
            realmImage.setUrl(url);
            realmImage.setUrlInDevice(imageFile.toString());
            realm.copyToRealm(realmImage);
        });
        return imageFile;
    }

    public File getFile(String url) {
        RealmImage realmImage = Realm.getDefaultInstance().where(RealmImage.class).equalTo( "url", url).findFirst();
        if (realmImage != null){
            return new File(realmImage.getUrlInDevice());
        }
        return null;
    }

    public boolean contains(String url) {
        return Realm.getDefaultInstance().where(RealmImage.class).equalTo("url", url).count() > 0;
    }

    public static void clear (){
        Realm.getDefaultInstance().executeTransaction(realm -> realm.delete(RealmImage.class));
        delFileOrDirRecursive(getImageDir());
    }

    private static void delFileOrDirRecursive(File imageDir) {
        if (imageDir.isDirectory()){
            for (File child : imageDir.listFiles()){
                delFileOrDirRecursive(child);
            }
        }
        imageDir.delete();
    }

    public static long getDirOrFileSize(File imageDir){
        long size = 0;
        if (imageDir.isDirectory()){
            for (File file : imageDir.listFiles()){
                size += getDirOrFileSize(file);
            }
        }else {
            size = imageDir.length();
        }
        return size;
    }

    private static File getImageDir() {
        return new File(App.getInstance().getExternalFilesDir(null) + "/" + FOLDER_NAME);
    }
}
