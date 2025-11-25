package com.example.proyectodamii;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.proyectodamii.databinding.ActivityLoginBinding;
import com.example.proyectodamii.db.daoUsuario;
import com.google.android.material.chip.Chip;

public class MainActivity extends AppCompatActivity {

    ActivityLoginBinding enlacevista;
    daoUsuario dao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.login), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        enlacevista = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(enlacevista.getRoot());
        dao = new daoUsuario(this);
        dao.sincronizariniciar();
        dao.insertar_usuarioFirebase_a_sqlite(this);

        //Login elementos
        Button btnlogin = findViewById(R.id.btnLogin);
        Button btnregistrologin = findViewById(R.id.btnRegistrar);
        Chip chprecordar = findViewById(R.id.chipRecordar);
        EditText correo_usuario = findViewById(R.id.editCorreousuario);
        EditText editpass = findViewById(R.id.editPass);
        Button btnsalir = findViewById(R.id.btnSalida);


        //configuracion del boton chip con sharedpreferences
        SharedPreferences configuracion = getSharedPreferences("Registro_usuario", Context.MODE_PRIVATE);

        String guardarusuario =  configuracion.getString("nombre","");
        String guardarpass = configuracion.getString("contrasenia","");
        boolean recordar = configuracion.getBoolean("recordar",false);

        if(recordar){
            correo_usuario.setText(guardarusuario);
            editpass.setText(guardarpass);
            chprecordar.setChecked(true);
        }



        //Login comportamiento
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user=  enlacevista.editCorreousuario.getText().toString();
                String contra = enlacevista.editPass.getText().toString();

                //Verifica si los datos estan en sharedpreferences para el boton chip
                SharedPreferences.Editor editor = configuracion.edit();
                if (chprecordar.isChecked()) {
                    editor.putString("nombre", user);
                    editor.putString("contrasenia", contra);
                    editor.putBoolean("recordar", true);
                    editor.apply();
                } else {
                    editor.clear();
                    editor.apply();
                }

                //validacion de usuario
                    Boolean validar = dao.verfCredenciales(user,contra);
                    if(validar== true){
                            Toast.makeText(MainActivity.this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show();
                            Intent intent= new Intent(MainActivity.this, Inicio.class);
                            startActivity(intent);
                            finish();


                    }else {
                        Toast.makeText(MainActivity.this, "Usuario o contraseña incorrectos sql", Toast.LENGTH_SHORT).show();
                        Log.d("LOGIN", "Contraseña ingresada: " + contra);
                       // Log.d("LOGIN", "Hash almacenado: " + contra_crypt);
                       Log.d("LOGIN", "Resultado verificación: " + dao.verfCredenciales(user, contra));
                    }

            }
        });

        //Restaurar contrasenia


        //Cambio de layout
        btnregistrologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(MainActivity.this, Registro.class);
                startActivity(intent);
            }
        });



        //Salida de la app
        btnsalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishAffinity();
            }
        });






    }
}