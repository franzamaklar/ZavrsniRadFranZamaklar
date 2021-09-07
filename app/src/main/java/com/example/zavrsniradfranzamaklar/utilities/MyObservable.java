package com.example.zavrsniradfranzamaklar.utilities;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.zavrsniradfranzamaklar.inputs.DeathList;

public class MyObservable extends ViewModel {

    private MutableLiveData<DeathList> selected = new MutableLiveData<>();

    public void select(DeathList deathList){
        selected.setValue(deathList);

    }

    public LiveData<DeathList> getSelected(){
        return selected;
    }


}
