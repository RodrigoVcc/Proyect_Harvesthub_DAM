package com.example.proyectodamii;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jspecify.annotations.NonNull;

import java.util.ArrayList;

public class Metodo_Pago extends AppCompatActivity {
    RecyclerView rv;
    RecycleAdapter_tarjetas adapter;
    ArrayList<Tarjeta_item> fuente_datos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_metodo_pago);
        rv = findViewById(R.id.rv_metodos_pago);
        fuente_datos = new ArrayList<>();
        fuente_datos.add(new Tarjeta_item("Tarjeta de Credito","0000 **** **** 0000","Rodrigo Cruz"));
        fuente_datos.add(new Tarjeta_item("Tarjeta de Credito","0000 **** **** 0000","Rodrigo Cruz"));
        fuente_datos.add(new Tarjeta_item("Tarjeta de Credito","0000 **** **** 0000","Rodrigo Cruz"));
        fuente_datos.add(new Tarjeta_item("Tarjeta de Credito","0000 **** **** 0000","Rodrigo Cruz"));
        rv.setLayoutManager(new LinearLayoutManager(Metodo_Pago.this, LinearLayoutManager.VERTICAL, false));
        adapter = new RecycleAdapter_tarjetas(fuente_datos);
        rv.setAdapter(adapter);

        ImageView btnretroceder = findViewById(R.id.imvAtras);

        btnretroceder.setOnClickListener(view -> {
            finish();
        });

    }

    static class RecycleAdapter_tarjetas extends RecyclerView.Adapter<RecycleAdapter_tarjetas.tarjetaHolder>{

        ArrayList<Tarjeta_item> data;

        public RecycleAdapter_tarjetas(ArrayList<Tarjeta_item> data) {
            this.data = data;
        }

        @NonNull
        @Override
        public tarjetaHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_metodo_pago,parent,false);
            return new tarjetaHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull tarjetaHolder holder, int position) {

            holder.texttitulo.setText(data.get(position).getTitulo());
            holder.txtnumero.setText(String.valueOf(data.get(position).getNumero_tarjeta()));
            holder.txtnombre.setText(String.valueOf(data.get(position).getNombre_duenio()));
        }

        @Override
        public int getItemCount() {
            return data.size();
        }


        class tarjetaHolder extends RecyclerView.ViewHolder{
            TextView texttitulo;
            TextView txtnumero;
            TextView txtnombre;
            public tarjetaHolder(@NonNull View itemView) {
                super(itemView);
                texttitulo = itemView.findViewById(R.id.textTitulo_tarjeta);
                txtnumero = itemView.findViewById(R.id.txvNumero_tarjeta);
                txtnombre = itemView.findViewById(R.id.txvNombre_tarjeta);
            }
        }
    }

    public static class Tarjeta_item {//Busca obtener el titulo del rv y sus items
        private final String titulo;

        private String numero_tarjeta;
        private String nombre_duenio;

        public Tarjeta_item(String titulo, String numero_tarjeta, String nombre_duenio) {
            this.titulo = titulo;
            this.numero_tarjeta = numero_tarjeta;
            this.nombre_duenio = nombre_duenio;
        }

        public String getTitulo() {
            return titulo;
        }

        public String getNumero_tarjeta() {
            return numero_tarjeta;
        }

        public String getNombre_duenio() {
            return nombre_duenio;
        }
    }

}