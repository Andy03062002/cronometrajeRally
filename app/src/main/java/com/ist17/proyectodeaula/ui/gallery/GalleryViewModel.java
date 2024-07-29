package com.ist17.proyectodeaula.ui.gallery;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class GalleryViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public GalleryViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Bienvenido puedes ver detalles de su vehiculo");
    }

    public LiveData<String> getText() {
        return mText;
    }
}