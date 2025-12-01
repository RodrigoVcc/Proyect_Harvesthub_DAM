package com.example.proyectodamii.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proyectodamii.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AjustesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AjustesFragment extends Fragment implements AdapterView.OnItemClickListener {




    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AjustesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AjustesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AjustesFragment newInstance(String param1, String param2) {
        AjustesFragment fragment = new AjustesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ajustes, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //TextView titulo = requireActivity().findViewById(R.id.txtNombre);

        //titulo.setText("Ajustes de Usuario");


        String opciones[]={"Perfil","Pedido","Metodo de pago","Eliminar Cuenta","Cerrar Sesion"};
        ListView listView;

        listView = view.findViewById(R.id.lista_ajustes);

        ArrayAdapter<String> arreglo = new ArrayAdapter<>(
          requireContext(),
                android.R.layout.simple_list_item_1,
                opciones
        );

        listView.setAdapter(arreglo);

        listView.setOnItemClickListener(this);


    }

//Comportamiento de los items (TEMPORAL)
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        switch (position){
            case 0:
                // Abrir Fragment Perfil
                Toast.makeText(requireContext(), "Ajustes cuenta", Toast.LENGTH_SHORT).show();
                break;

            case 1:
                // Abrir Fragment Pedido
                navigateTo(new PedidosFragment());
                break;

            case 2:
                // Abrir Fragment Método de pago
                Toast.makeText(requireContext(), "Metodos de pago", Toast.LENGTH_SHORT).show();
                break;

            case 3:
                Toast.makeText(requireContext(), "Eliminar cuenta", Toast.LENGTH_SHORT).show();
                break;

            case 4:
                Toast.makeText(requireContext(), "Cerrar sesión", Toast.LENGTH_SHORT).show();
                break;
        }
    }
    private void navigateTo(Fragment fragment){
        requireActivity()
                .getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frameLayout, fragment)
                .addToBackStack(null)
                .commit();
    }
}