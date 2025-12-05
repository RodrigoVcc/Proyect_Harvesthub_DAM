package com.example.proyectodamii;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

public class Checkout extends AppCompatActivity {
    RecyclerView rv;
   RecycleAdapter_Checkout adapter;
    ArrayList<Metodo_Pago.Tarjeta_item> fuente_datos;
    EditText direccion;

    private int posicion_seleccionado = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        rv = findViewById(R.id.rv_metodos_pago_checkout);
        fuente_datos = new ArrayList<>();
        fuente_datos.add(new Metodo_Pago.Tarjeta_item("Tarjeta de Credito","0000 **** **** 0000","Rodrigo Cruz"));
        fuente_datos.add(new Metodo_Pago.Tarjeta_item("Tarjeta de Credito","0000 **** **** 0000","Rodrigo Cruz"));
        fuente_datos.add(new Metodo_Pago.Tarjeta_item("Tarjeta de Credito","0000 **** **** 0000","Rodrigo Cruz"));
        fuente_datos.add(new Metodo_Pago.Tarjeta_item("Tarjeta de Credito","0000 **** **** 0000","Rodrigo Cruz"));


        ImageView btnretroceder = findViewById(R.id.imvAtras);
        btnretroceder.setOnClickListener(v ->
                finish()
        );
        rv.setLayoutManager(new LinearLayoutManager(Checkout.this, LinearLayoutManager.VERTICAL, false));
        adapter = new RecycleAdapter_Checkout(fuente_datos);
        rv.setAdapter(adapter);

        TextView total_orden = findViewById(R.id.txvOrden_total);
        TextView total_pago = findViewById(R.id.txvTotal_sum_chk);
        TextView impuesto = findViewById(R.id.txvImpuesto_total);
        String orden = "$ "+ getIntent().getStringExtra("total");
        //llama al dato que se compartio
        total_orden.setText(orden);
        double total = Double.parseDouble(getIntent().getStringExtra("total"))+ Double.parseDouble((String) impuesto.getText());


        total_pago.setText("$ "+String.valueOf(total));

        TextInputLayout icono = findViewById(R.id.icnDireccion);
        String dato_compartido = "";
        icono.setEndIconOnClickListener(view -> {
            Intent intent = new Intent(Checkout.this, Direccion.class);
            intent.putExtra("respuesta",dato_compartido);
            startActivityForResult(intent,100);
        });

        direccion = findViewById(R.id.edtDireccion);

        Button pagar = findViewById(R.id.btnPagar_ahora);
        pagar.setOnClickListener(view -> {
            Toast.makeText(this,"PAGO AHORA ", Toast.LENGTH_SHORT).show();
        });




    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && resultCode == RESULT_OK) {
            String respuesta = data.getStringExtra("respuesta");
            direccion.setText(respuesta);
        }
    }



    class RecycleAdapter_Checkout extends RecyclerView.Adapter<RecycleAdapter_Checkout.tarjetaHolder>{

        ArrayList<Metodo_Pago.Tarjeta_item> data;

        public RecycleAdapter_Checkout(ArrayList<Metodo_Pago.Tarjeta_item> data) {
            this.data = data;
        }

        @NonNull
        @Override
        public tarjetaHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(Checkout.this).inflate(R.layout.item_chekout,parent,false);
            return new tarjetaHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull tarjetaHolder holder, int position) {

            holder.texttitulo.setText(data.get(position).getTitulo());
            holder.txtnumero.setText(String.valueOf(data.get(position).getNumero_tarjeta()));
            holder.txtnombre.setText(String.valueOf(data.get(position).getNombre_duenio()));
            //Cambia el estado del radio button
            holder.rbtnActivo.setChecked(position== posicion_seleccionado);

            //Cambia el valor de posicion_seleccionado cuando se toca el card
            holder.itemView.setOnClickListener(view -> {
                posicion_seleccionado = holder.getAdapterPosition();
                notifyDataSetChanged();
            });
            //Cambia el valor de posicion_seleccionado cuando se toca el radiobutton
            holder.rbtnActivo.setOnClickListener(view -> {
                posicion_seleccionado = holder.getAdapterPosition();
                notifyDataSetChanged();
            });
        }

        @Override
        public int getItemCount() {
            return data.size();
        }


        class tarjetaHolder extends RecyclerView.ViewHolder{
            TextView texttitulo;
            TextView txtnumero;
            TextView txtnombre;
            RadioButton rbtnActivo;
            public tarjetaHolder(@NonNull View itemView) {
                super(itemView);
                texttitulo = itemView.findViewById(R.id.textTitulo_tarjeta_chk);
                txtnumero = itemView.findViewById(R.id.txvNumero_tarjeta_chk);
                txtnombre = itemView.findViewById(R.id.txvNombre_tarjeta_chk);
                rbtnActivo = itemView.findViewById(R.id.rbSeleccionar_chk);
            }
        }
    }
}