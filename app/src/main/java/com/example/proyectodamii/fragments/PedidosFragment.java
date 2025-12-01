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
import android.widget.ImageView;
import android.widget.TextView;

import com.example.proyectodamii.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PedidosFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PedidosFragment extends Fragment {

    RecyclerView rv;
    LinearLayoutManager linearLayoutManager;
    ArrayList<Pedido_item> fuente_datos;
    RecycleAdapter_pedidos recycleAdapter;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PedidosFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PedidosFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PedidosFragment newInstance(String param1, String param2) {
        PedidosFragment fragment = new PedidosFragment();
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
        return inflater.inflate(R.layout.fragment_pedidos, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rv = view.findViewById(R.id.rv_pedidos);
        fuente_datos = new ArrayList<>();
        fuente_datos.add(new Pedido_item("Pedido",0.55,"2025/11/26", Arrays.asList(
                new Item_imagen("Manzana",R.drawable.manzana), new Item_imagen("Arroz",R.drawable.arroz_libra)
        )));
        rv.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
        recycleAdapter = new RecycleAdapter_pedidos(fuente_datos);
        rv.setAdapter(recycleAdapter);

    }


    class RecycleAdapter_pedidos extends RecyclerView.Adapter<RecycleAdapter_pedidos.pedidosHolder>{

        ArrayList<Pedido_item> data;

        public RecycleAdapter_pedidos(ArrayList<Pedido_item> data) {
            this.data = data;
        }

        @NonNull
        @Override
        public RecycleAdapter_pedidos.pedidosHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(requireContext()).inflate(R.layout.item_pedido,parent,false);
            return new RecycleAdapter_pedidos.pedidosHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecycleAdapter_pedidos.pedidosHolder holder, int position) {
            Pedido_item pedidoItem = data.get(position);
            holder.texttitulo_pedido.setText(data.get(position).getTitulo());
            holder.txtprecio.setText(String.valueOf(data.get(position).getPrecio()));
            holder.txtFecha.setText(String.valueOf(data.get(position).getFecha()));

            RecycleAdapter_imagenes recycleAdapterImagenes = new RecycleAdapter_imagenes(pedidoItem.getItems());
            holder.rvimages.setLayoutManager(
                    new LinearLayoutManager(holder.itemView.getContext(),LinearLayoutManager.HORIZONTAL,false)
            );
            holder.rvimages.setAdapter(recycleAdapterImagenes);



        }

        @Override
        public int getItemCount() {
            return data.size();
        }


        class pedidosHolder extends RecyclerView.ViewHolder{
            TextView texttitulo_pedido;
            TextView textTitulo_pedido_imagen;
            TextView txtprecio;
            TextView txtFecha;
            RecyclerView rvimages;
            public pedidosHolder(@NonNull View itemView) {
                super(itemView);
                texttitulo_pedido = itemView.findViewById(R.id.textTitulo_producto);
                textTitulo_pedido_imagen = itemView.findViewById(R.id.textTitulo_producto_imagen);
                txtprecio = itemView.findViewById(R.id.textPrecio_total);
                txtFecha = itemView.findViewById(R.id.textFecha);
                rvimages = itemView.findViewById(R.id.rv_pedidos_imagenes);
            }
        }
    }

    class RecycleAdapter_imagenes extends RecyclerView.Adapter<RecycleAdapter_imagenes.MiHolder>{
        List<Item_imagen> data;
        public RecycleAdapter_imagenes(List<Item_imagen> data) {
            this.data= data;
        }

        @NonNull
        @Override
        public RecycleAdapter_imagenes.MiHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(requireContext()).inflate(R.layout.item_pedido_imagen,parent,false);
            return new RecycleAdapter_imagenes.MiHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecycleAdapter_imagenes.MiHolder holder, int position) {
            holder.textView.setText(data.get(position).getNombre());
            holder.url_imagen.setImageResource(data.get(position).getUrl_image());
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        class MiHolder extends RecyclerView.ViewHolder{
            TextView textView;
            ImageView url_imagen;
            public MiHolder(@NonNull View itemView) {
                super(itemView);
                textView = itemView.findViewById(R.id.textTitulo_producto_imagen);
                url_imagen = itemView.findViewById(R.id.item_image_pedido);
            }
        }
    }

    public static class Pedido_item {//Busca obtener el titulo del rv y sus items
        private final String titulo;
        private double precio;
        String fecha;
        private List<Item_imagen> items;

        public Pedido_item(String titulo, double precio, String fecha, List<Item_imagen> items) {
            this.titulo = titulo;
            this.items = items;
            this.precio = precio;
            this.fecha = fecha;
        }
        public String getTitulo() { return titulo; }

        public double getPrecio() {
            return precio;
        }

        public List<Item_imagen> getItems() {
            return items;
        }

        public String getFecha() {
            return fecha;
        }
    }
    public static class Item_imagen {//busca obtener los datos de las cards de las imagenes
        private final String nombre;
        int url_image;


        public Item_imagen(String nombre,int url_image) {
            this.nombre = nombre;
            this.url_image = url_image;
        }

        public String getNombre() {
            return nombre; }

        public int getUrl_image() {
            return url_image;
        }
    }
}