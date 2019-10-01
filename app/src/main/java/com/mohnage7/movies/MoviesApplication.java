package com.mohnage7.movies;

import android.app.Application;

import com.mohnage7.movies.di.component.DaggerDataComponent;
import com.mohnage7.movies.di.component.DataComponent;
import com.mohnage7.movies.di.module.DataModule;

public class MoviesApplication extends Application {

    private static MoviesApplication instance;
    DataComponent dataComponent;

    public static MoviesApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        initDataComponent();
    }

    private void initDataComponent() {
        dataComponent = DaggerDataComponent.builder().
                dataModule(new DataModule(this))
                .build();
        dataComponent.inject(this);
    }

    public DataComponent getDataComponent() {
        return dataComponent;
    }
}
