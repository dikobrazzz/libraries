package com.example.dikob.app5.di.modules;

import org.mockito.Mockito;

import dagger.Module;
import dagger.Provides;
import ru.terrakok.cicerone.Router;

@Module
public class TestCiceroneModule {

    @Provides
    public Router router(){
        return Mockito.mock(Router.class);
    }

}
