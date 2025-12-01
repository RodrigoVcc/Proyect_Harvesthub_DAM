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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NotificacionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NotificacionFragment extends Fragment {

    RecyclerView rv;
    LinearLayoutManager linearLayoutManager;
    ArrayList<Notificacion_item> fuente_datos;
    RecycleAdapter_notify recycleAdapter;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public NotificacionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NotificacionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NotificacionFragment newInstance(String param1, String param2) {
        NotificacionFragment fragment = new NotificacionFragment();
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
        return inflater.inflate(R.layout.fragment_notificacion, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rv = view.findViewById(R.id.rv_notificacion);
        fuente_datos = new ArrayList<>();
        fuente_datos.add(new Notificacion_item("Pedido entregado","Tu pedido fue entregado con exito"));
        fuente_datos.add(new Notificacion_item("Pedido Recibido","Tu pedido esta en camino"));

        rv.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
        recycleAdapter = new RecycleAdapter_notify(fuente_datos);
        rv.setAdapter(recycleAdapter);

    }


    class RecycleAdapter_notify extends RecyclerView.Adapter<RecycleAdapter_notify.notifyHolder>{

        ArrayList<Notificacion_item> data;

        public RecycleAdapter_notify(ArrayList<Notificacion_item> data) {
            this.data = data;
        }

        @NonNull
        @Override
        public RecycleAdapter_notify.notifyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(requireContext()).inflate(R.layout.item_notificacion,parent,false);
            return new RecycleAdapter_notify.notifyHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecycleAdapter_notify.notifyHolder holder, int position) {

            holder.texttitulo.setText(data.get(position).getTitulo());
            holder.txtDescripcion.setText(data.get(position).getDescripcion());



        }

        @Override
        public int getItemCount() {
            return data.size();
        }


        class notifyHolder extends RecyclerView.ViewHolder{
            TextView texttitulo;
            TextView txtDescripcion;
            public notifyHolder(@NonNull View itemView) {
                super(itemView);
                texttitulo = itemView.findViewById(R.id.textTitulo_notificacion);
                txtDescripcion = itemView.findViewById(R.id.txtDescripcion_noti);
            }
        }
    }

    public static class Notificacion_item {//Busca obtener el titulo del rv y sus items
        private final String titulo;
        private final String descripcion;

        public Notificacion_item(String titulo, String descripcion) {
            this.titulo = titulo;
            this.descripcion = descripcion;
        }
        public String getTitulo() { return titulo; }

        public String getDescripcion() {
            return descripcion;
        }
    }
}