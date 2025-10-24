package com.example.proyectodamii;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Registro extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registro);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.registro), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //Registro elemntos
        EditText editnombre_usuario = findViewById(R.id.editNombre_usuario);
        EditText editpass = findViewById(R.id.editPassword);
        EditText editemail = findViewById(R.id.editCorreo);
        Button btnregistrar = findViewById(R.id.btnRegistro);
        Button btnsalir = findViewById(R.id.btnSalir);



        btnregistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String contrasenia = editpass.getText().toString();
                String user = editnombre_usuario.getText().toString().trim();
                String mail = editemail.getText().toString();

                //validaciones
                if(user.isEmpty()||mail.isEmpty()||contrasenia.isEmpty()){
                    Toast.makeText(Registro.this, "Por favor complete todos los campos", Toast.LENGTH_SHORT).show();
                    return;
                } else if (user.length()<8) {
                    Toast.makeText(Registro.this, "El usuario debe tener al menos 8 caracteres", Toast.LENGTH_SHORT).show();
                    return;
                } else if (!Patterns.EMAIL_ADDRESS.matcher(mail).matches()) {
                    Toast.makeText(Registro.this, "El Correo no es valido", Toast.LENGTH_SHORT).show();
                    return;
                } else if (contrasenia.length()<8) {
                    Toast.makeText(Registro.this, "La contraseña debe tener al menos 8 caracteres", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    SharedPreferences configuracion = getSharedPreferences("Registro_usuario", Context.MODE_PRIVATE);

                    SharedPreferences.Editor editorpref = configuracion.edit();
                    editorpref.putString(user,contrasenia);
                    editorpref.apply();
                    Toast.makeText(Registro.this,"Usuario creado con exito", Toast.LENGTH_LONG).show();
                    
                    finish();
                }


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
