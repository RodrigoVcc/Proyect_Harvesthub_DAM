package com.example.proyectodamii;

import android.content.Intent;
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

import com.example.proyectodamii.databinding.ActivityRegistroBinding;

public class Registro extends AppCompatActivity {

    ActivityRegistroBinding enlacevistas;
    daoUsuario dao;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registro);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.registro), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //sqlite
        dao = new daoUsuario(this);

        //Registro elemntos
        EditText editnombre = findViewById(R.id.editNombre_usuario);
        EditText editcontrenia = findViewById(R.id.editPassword);
        EditText editmail = findViewById(R.id.editCorreo);
        Button btnregistrar = findViewById(R.id.btnRegistro);
        Button btnsalir = findViewById(R.id.btnSalir);



        btnregistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String contrasenia = editcontrenia.getText().toString().trim();
                String user = editnombre.getText().toString().trim();
                String mail = editmail.getText().toString().trim();

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
                    //crea un nuevo usuario en la base de datos
                    Boolean verfNombreCorreo = dao.verfNombreCorreo(user,mail);
                    if(verfNombreCorreo == false){
                        Boolean insertar = dao.insertar(user,mail,contrasenia);
                        if(insertar==true){
                            Toast.makeText(Registro.this,"Usuario creado con exito", Toast.LENGTH_LONG).show();

                            //guardado en firestore



                            Intent intent= new Intent(Registro.this, MainActivity.class);
                            startActivity(intent);
                        }else{
                            Toast.makeText(getApplication(),"Error al registrarse",Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(getApplication(),"Correo ya registrado",Toast.LENGTH_SHORT).show();
                    }


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
