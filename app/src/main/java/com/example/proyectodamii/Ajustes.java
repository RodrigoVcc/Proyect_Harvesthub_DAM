package com.example.proyectodamii;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.proyectodamii.db.daoUsuario;
import com.google.firebase.firestore.FirebaseFirestore;

public class Ajustes extends AppCompatActivity {

    FirebaseFirestore db;

    daoUsuario dao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.toolbar_edituser);

        // Vincular el Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar_user);
        setSupportActionBar(toolbar);


        dao = new daoUsuario(this);

        EditText editCorreo = findViewById(R.id.editCorreo);
        EditText editnombre = findViewById(R.id.editNombreusuario);
        Button actualizar = findViewById(R.id.btnRegistrarse);




        actualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nuevoNombre = editnombre.getText().toString().trim();
                String correo = editCorreo.getText().toString().trim();

                // Validaciones
                if (nuevoNombre.isEmpty()) {
                    Toast.makeText(Ajustes.this, "El nombre no puede estar vacío", Toast.LENGTH_SHORT).show();
                    return;
                }
                boolean verfcorreo = dao.verfCorreo(correo);
                if(verfcorreo){
                    boolean exito = dao.cambiarnombre(nuevoNombre, correo);

                    if (exito) {
                        Toast.makeText(Ajustes.this, "Perfil actualizado correctamente", Toast.LENGTH_SHORT).show();
                        setResult(RESULT_OK);
                        finish();
                    } else {
                        Toast.makeText(Ajustes.this, "Error al actualizar el perfil", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(Ajustes.this, "Error al Correo no encontrado", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_navbar, menu);
        return true;
    }

    /*
    // Manejar clics en los ítems del menú
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.edit) {
            Intent intent= new Intent(Ajustes.this, Ajustes.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.mapas) {
        Intent intent= new Intent(Ajustes.this, Mapa.class);
        startActivity(intent);
        return true;

        return super.onOptionsItemSelected(item);
    }*/




}
