package com.example.proyectodamii.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.proyectodamii.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CarritoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CarritoFragment extends Fragment {

    RecyclerView rv;
    LinearLayoutManager linearLayoutManager;
    ArrayList<Producto_item> fuente_datos;
    RecycleAdapter_carrito recycleAdapter;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CarritoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CarritoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CarritoFragment newInstance(String param1, String param2) {
        CarritoFragment fragment = new CarritoFragment();
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
        return inflater.inflate(R.layout.fragment_carrito, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rv = view.findViewById(R.id.rv_carrito);
        fuente_datos = new ArrayList<>();
        fuente_datos.add(new Producto_item("Manzanas"));
        fuente_datos.add(new Producto_item("Leche"));
        fuente_datos.add(new Producto_item("Jugo"));

        rv.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        recycleAdapter = new RecycleAdapter_carrito(fuente_datos);
        rv.setAdapter(recycleAdapter);

    }


    class RecycleAdapter_carrito extends RecyclerView.Adapter<RecycleAdapter_carrito.carritoHolder>{

        ArrayList<Producto_item> data;

        public RecycleAdapter_carrito(ArrayList<Producto_item> data) {
            this.data = data;
        }

        @NonNull
        @Override
        public CarritoFragment.RecycleAdapter_carrito.carritoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(requireContext()).inflate(R.layout.item_carrito,parent,false);
            return new carritoHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull carritoHolder holder, int position) {

            holder.texttitulo.setText(data.get(position).getTitulo());

        }

        @Override
        public int getItemCount() {
            return data.size();
        }


        class carritoHolder extends RecyclerView.ViewHolder{
            TextView texttitulo;
            public carritoHolder(@NonNull View itemView) {
                super(itemView);
                texttitulo = itemView.findViewById(R.id.textTitulo_producto);
            }
        }
    }

    public static class Producto_item {//Busca obtener el titulo del rv y sus items
        private final String titulo;

        public Producto_item(String titulo) {
            this.titulo = titulo;
        }

        public String getTitulo() { return titulo; }
    }
}