package com.example.dikob.app5.navigation;

import android.support.v4.app.Fragment;

import com.example.dikob.app5.ui.fragment.MainFragment;

import ru.terrakok.cicerone.android.support.SupportAppScreen;

public class Screens {

    public static class MainScreen extends SupportAppScreen{
        String arg;

        public MainScreen(String arg){
            this.arg = arg;
        }

        @Override
        public Fragment getFragment() {
            return MainFragment.getInstance(arg);
        }
    }
}
