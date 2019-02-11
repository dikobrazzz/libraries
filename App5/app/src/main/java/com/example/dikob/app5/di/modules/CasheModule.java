package com.example.dikob.app5.di.modules;

import com.example.dikob.app5.mvp.model.cashe.ICashe;
import com.example.dikob.app5.mvp.model.cashe.ImageCashe;
import com.example.dikob.app5.mvp.model.cashe.PaperCashe;
import com.example.dikob.app5.mvp.model.cashe.RealmCashe;
import com.example.dikob.app5.mvp.model.cashe.RoomCashe;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@Module
public class CasheModule {

    @Named("room")
    @Provides
    public ICashe roomCashe(){
        return new RoomCashe();
    }

    @Named("realm")
    @Provides
    public ICashe realmCashe(){
        return new RealmCashe();
    }

    @Named("paper")
    @Provides
    public ICashe paperCashe(){
        return new PaperCashe();
    }

    @Provides
    public ImageCashe imageCashe(){
        return new ImageCashe();
    }

}
