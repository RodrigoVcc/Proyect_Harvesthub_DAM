package com.example.proyectodamii;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.chip.Chip;

public class MainActivity extends AppCompatActivity {

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


        //Login elementos
        Button btnlogin = findViewById(R.id.btnLogin);
        Button btnregistrologin = findViewById(R.id.btnRegistrar);
        Chip chprecordar = findViewById(R.id.chipRecordar);
        EditText editnombre_usuario = findViewById(R.id.editNombre_usuario);
        EditText editpass = findViewById(R.id.editPassword);
        Button btnsalir = findViewById(R.id.btnSalir);


        //configuracion del boton chip con sharedpreferences
        SharedPreferences configuracion = getSharedPreferences("Registro_usuario", Context.MODE_PRIVATE);

        String guardarusuario =  configuracion.getString("usuario","");
        String guardarpass = configuracion.getString("contraseña","");
        boolean recordar = configuracion.getBoolean("recordar",false);

        if(recordar){
            editnombre_usuario.setText(guardarusuario);
            editpass.setText(guardarpass);
            chprecordar.setChecked(true);
        }



        //Login comportamiento
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user=  editnombre_usuario.getText().toString();
                String contra = editpass.getText().toString();

                //Verifica si los datos estan en sharedpreferences chip
                SharedPreferences.Editor editor = configuracion.edit();
                if (chprecordar.isChecked()) {
                    editor.putString("usuario", user);
                    editor.putString("contraseña", contra);
                    editor.putBoolean("recordar", true);
                    editor.apply();
                } else {
                    editor.clear();
                    editor.apply();
                }
                //validacion de usuario
                String datos = configuracion.getString(user,null);
                if (datos != null && datos.equals(contra)) {
                    Toast.makeText(MainActivity.this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show();
            /*
            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();*/
                    setContentView(R.layout.activity_main);

                } else {
                    Toast.makeText(MainActivity.this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show();
                }
            }
        });
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