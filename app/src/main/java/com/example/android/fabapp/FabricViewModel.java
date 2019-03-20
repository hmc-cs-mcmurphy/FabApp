package com.example.android.fabapp;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import java.util.List;

public class FabricViewModel extends AndroidViewModel {
    private FabricRepository mRepository;

    private LiveData<List<Fabric>> mAllFabrics;

    public FabricViewModel (Application application) {
        super(application);
        mRepository = new FabricRepository(application);
        mAllFabrics = mRepository.getAllFabrics();
    }

    LiveData<List<Fabric>> getAllFabrics() { return mAllFabrics; }

    public void insert(Fabric fabric) { mRepository.insert(fabric); }
}
