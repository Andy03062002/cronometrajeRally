package com.ist17.proyectodeaula.ui.gallery;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.ist17.proyectodeaula.Registrovehiculos;
import com.ist17.proyectodeaula.VerVehiculos;
import com.ist17.proyectodeaula.databinding.FragmentGalleryBinding;
import com.ist17.proyectodeaula.punto_control;
import com.ist17.proyectodeaula.validar_punto;


public class GalleryFragment extends Fragment {

    private FragmentGalleryBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        GalleryViewModel galleryViewModel =
                new ViewModelProvider(this).get(GalleryViewModel.class);

        binding = FragmentGalleryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textGallery;
        galleryViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        Button button_ver_datos_vehiculos = binding.buttonVerDatosVehiculo;
        button_ver_datos_vehiculos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), VerVehiculos.class);
                startActivity(intent);
            }
        });
        Button bttnVerPDC = binding.bttnVerPDC;
        bttnVerPDC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), punto_control.class);
                startActivity(intent);
            }
        });
        Button bttn_registrarpunto = binding.bttnRegistrarpunto;
        bttn_registrarpunto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), validar_punto.class);
                startActivity(intent);
            }
        });

        Button bttn_registrar = binding.bttnRegistrar;
        bttn_registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Registrovehiculos.class);
                startActivity(intent);
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}