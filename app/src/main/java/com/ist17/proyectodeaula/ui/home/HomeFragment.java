package com.ist17.proyectodeaula.ui.home;

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

import com.ist17.proyectodeaula.EliminarPiloto;
import com.ist17.proyectodeaula.ModificarPiloto;
import com.ist17.proyectodeaula.EliminarPiloto;
import com.ist17.proyectodeaula.VerPiloto;
import com.ist17.proyectodeaula.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textHome;
        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        // Agregar listener al botón "Modificar Piloto"
        Button buttonModificarPiloto = binding.buttonModificarPiloto;
        buttonModificarPiloto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ModificarPiloto.class);
                startActivity(intent);
            }
        });

        // Agregar listener al botón "Eliminar Ocupante"
        Button buttonEliminarOcupante = binding.buttonEliminarOcupante;
        buttonEliminarOcupante.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EliminarPiloto.class);
                startActivity(intent);
            }
        });

        // Agregar listener al botón "Eliminar Ocupante"
        Button button_ver_datos_ocupante = binding.buttonVerDatosOcupante;
        button_ver_datos_ocupante.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), VerPiloto.class);
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
