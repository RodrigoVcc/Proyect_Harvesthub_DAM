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
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    RecyclerView rv;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        rv = view.findViewById(R.id.recycle_view_main);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        List<Categoria> fuente_datos = new ArrayList<>();

        // Simulacion de los datos
        fuente_datos.add(new Categoria("Frutas",
                Arrays.asList(new ItemHijo("Manzana"), new ItemHijo("Banana"), new ItemHijo("Pera"))
        ));

        fuente_datos.add(new Categoria("Verduras",
                Arrays.asList(new ItemHijo("Zanahoria"), new ItemHijo("Pepino"), new ItemHijo("Papa"))
        ));
        fuente_datos.add(new Categoria("Granos Basicos",
                Arrays.asList(new ItemHijo("Zanahoria"), new ItemHijo("Pepino"), new ItemHijo("Papa"))
        ));

        rv.setAdapter(new RecycleAdapter_padre(fuente_datos));








    }

    //adapter para items padre
    class RecycleAdapter_padre extends RecyclerView.Adapter<RecycleAdapter_padre.padreHolder>{
        List<Categoria> data;//Inicia la lista
        //Constructor del adapter
        public RecycleAdapter_padre(List<Categoria> datos) {
            this.data= datos;
        }

        @NonNull
        @Override
        public padreHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            //Llamada del rv al framgemt
            View view = LayoutInflater.from(requireContext()).inflate(R.layout.rvpadre_layout,parent,false);
            return new RecycleAdapter_padre.padreHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull padreHolder holder, int position) {
            //llamada de las funcion getter de las categorias
            Categoria categoria = data.get(position);
            //Permite cambiar el textview
            holder.textView.setText(categoria.getTitulo());
            //Crea el adapter hijo para manejar los items hijo
            RecycleAdapter_hijo recycleAdapterHijo = new RecycleAdapter_hijo(categoria.getItems());
            holder.rvhijo.setLayoutManager(
                    //Define la orientacion del rv hijo
                    new LinearLayoutManager(holder.itemView.getContext(),LinearLayoutManager.HORIZONTAL,false));
            //establece el adapter hijo al rv hijo
            holder.rvhijo.setAdapter(recycleAdapterHijo);

        }

        @Override
        public int getItemCount() {//obtiene la cantidad de intems a mostrar
            return data.size();
        }

        //busca los elementos de los layouts del rv padre
        class padreHolder extends RecyclerView.ViewHolder{

            TextView textView;
            RecyclerView rvhijo;
            public padreHolder(@NonNull View itemView) {
                super(itemView);
                textView = itemView.findViewById(R.id.txtTituloSeccion);
                rvhijo = itemView.findViewById(R.id.rvHijos);
            }
        }
    }




    //Adapter Para items hijos
    class RecycleAdapter_hijo extends RecyclerView.Adapter<RecycleAdapter_hijo.MiHolder>{
        List<ItemHijo> data;
        public RecycleAdapter_hijo(List<ItemHijo> data) {
            this.data= data;
        }

        @NonNull
        @Override
        public MiHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(requireContext()).inflate(R.layout.card_layout,parent,false);
            return new MiHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MiHolder holder, int position) {
            holder.textView.setText(data.get(position).getNombre());
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        class MiHolder extends RecyclerView.ViewHolder{
            TextView textView;
            public MiHolder(@NonNull View itemView) {
                super(itemView);
                textView = itemView.findViewById(R.id.textTitulo);
            }
        }
    }

    //clases getters
    public static class Categoria {//Busca obtener el titulo del rv y sus items
        private final String titulo;
        private final List<ItemHijo> items;

        public Categoria(String titulo, List<ItemHijo> items) {
            this.titulo = titulo;
            this.items = items;
        }

        public String getTitulo() { return titulo; }
        public List<ItemHijo> getItems() { return items; }
    }
    public static class ItemHijo {//busca obtener los datos de las cards
        private final String nombre;

        public ItemHijo(String nombre) {
            this.nombre = nombre;
        }

        public String getNombre() {
            return nombre; }
    }
}