package com.example.proyectodamii.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
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
    ListView lv;
    ListaAdapter listaAdapter;
    Producto_item productoItem;
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
        fuente_datos.add(new Producto_item("Manzanas",R.drawable.manzana,0.10,1));
        fuente_datos.add(new Producto_item("Arroz libra",R.drawable.arroz_libra,0.55,1));
        fuente_datos.add(new Producto_item("Manzanas2",R.drawable.manzana,0.10,1));
        fuente_datos.add(new Producto_item("Manzanas3",R.drawable.manzana,0.10,1));

        rv.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
        recycleAdapter = new RecycleAdapter_carrito(fuente_datos);
        rv.setAdapter(recycleAdapter);

        lv = view.findViewById(R.id.listaResumen);
        listaAdapter = new ListaAdapter(requireContext(), fuente_datos);
        lv.setAdapter(listaAdapter);

        String suma = String.valueOf(SumaTotal(fuente_datos));

        TextView total = view.findViewById(R.id.txvTotal_sum);
        total.setText("$ "+ suma);

    }

    //funcion suma de total
    public double SumaTotal(ArrayList<Producto_item> dato){
        int contador;
        double suma = 0;

        for (contador=0; contador < dato.size(); contador++){
            suma += dato.get(contador).getPrecio();
        }


        return suma;
    }


    class RecycleAdapter_carrito extends RecyclerView.Adapter<RecycleAdapter_carrito.carritoHolder>{

        ArrayList<Producto_item> data;

        public RecycleAdapter_carrito(ArrayList<Producto_item> data) {
            this.data = data;
        }

        @NonNull
        @Override
        public carritoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(requireContext()).inflate(R.layout.item_carrito,parent,false);
            return new carritoHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull carritoHolder holder, int position) {

            holder.texttitulo.setText(data.get(position).getTitulo());
            holder.imagen_url.setImageResource(data.get(position).getUrl_image());
            holder.txtprecio.setText(String.valueOf(data.get(position).getPrecio()));
            holder.txtCantidad.setText(String.valueOf(data.get(position).getCantidad()));



        }

        @Override
        public int getItemCount() {
            return data.size();
        }


        class carritoHolder extends RecyclerView.ViewHolder{
            TextView texttitulo;
            TextView txtprecio;
            TextView txtCantidad;
            ImageView imagen_url;
            public carritoHolder(@NonNull View itemView) {
                super(itemView);
                texttitulo = itemView.findViewById(R.id.textTitulo_producto);
                txtprecio = itemView.findViewById(R.id.textPrecio);
                txtCantidad = itemView.findViewById(R.id.txtViewcant);
                imagen_url = itemView.findViewById(R.id.item_image);
            }
        }
    }

    public static class Producto_item {//Busca obtener el titulo del rv y sus items
        private final String titulo;
        int url_image;
        private double precio;
        Integer cantidad;

        public Producto_item(String titulo, int url_image, double precio, Integer cantidad) {
            this.titulo = titulo;
            this.url_image = url_image;
            this.precio = precio;
            this.cantidad = cantidad;
        }
        public String getTitulo() { return titulo; }

        public int getUrl_image() {
            return url_image;
        }

        public double getPrecio() {
            return precio;
        }

        public Integer getCantidad() {
            return cantidad;
        }
    }


    //Adapter de lisat detalles de producto
    public static class ListaAdapter extends ArrayAdapter<Producto_item>{

        public ListaAdapter(@NonNull Context context, ArrayList<Producto_item> datosArray) {
            super(context, R.layout.item_carrito_datos, datosArray);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View view, @NonNull ViewGroup parent) {
            Producto_item data = getItem(position);

            if(view==null){
                view = LayoutInflater.from(getContext()).inflate(R.layout.item_carrito_datos,parent,false);
            }
            //Obtener los items de item_carrito_datos
            TextView nombre_producto = view.findViewById(R.id.txvNombre_producto);
            TextView cantidad = view.findViewById(R.id.txvCantidad_producto);
            TextView precio_producto = view.findViewById(R.id.txvPrecio_producto);

            assert data != null;
            nombre_producto.setText(String.valueOf(data.getTitulo()));
            cantidad.setText(String.valueOf(data.getCantidad()));
            precio_producto.setText(String.valueOf(data.getPrecio()));


            return view;
        }
    }
}