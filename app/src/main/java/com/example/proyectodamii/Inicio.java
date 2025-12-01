package com.example.proyectodamii;

import android.os.Bundle;


import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.proyectodamii.databinding.ActivityMainBinding;
import com.example.proyectodamii.fragments.AjustesFragment;
import com.example.proyectodamii.fragments.CarritoFragment;
import com.example.proyectodamii.fragments.HomeFragment;
import com.example.proyectodamii.fragments.NotificacionFragment;


public class Inicio extends AppCompatActivity {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        cambiarFragment(new HomeFragment());

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.editUsr) {
                cambiarFragment(new AjustesFragment());
            }else if(item.getItemId()== R.id.home_id){
                cambiarFragment(new HomeFragment());
            }else if(item.getItemId()==R.id.carrito){
                cambiarFragment(new CarritoFragment());
            }else if(item.getItemId()==R.id.notify){
                cambiarFragment(new NotificacionFragment());
            }

            return true;
        });


    }


    private void cambiarFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }


}


/*

         //

        // Vincular el Toolbar
Toolbar toolbar = findViewById(R.id.toolbar);
setSupportActionBar(toolbar);

        // Establecer el título
        if (getSupportActionBar() != null) {
getSupportActionBar().setTitle("Inicio");
        }



    // Inflar el menú en el Toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_navbar, menu);
        return true;
    }


    // Manejar clics en los ítems del menú
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.home){
            Intent intent= new Intent(Home.this, Ajustes.class);
            startActivity(intent);
        } else if (id == R.id.carrito) {
            Intent intent= new Intent(Home.this, Ajustes.class);
            startActivity(intent);

        } else if (id == R.id.notify) {
            Intent intent= new Intent(Home.this, Ajustes.class);
            startActivity(intent);
        }else if (id == R.id.editUsr){
            Intent intent= new Intent(Home.this, Ajustes.class);
            startActivity(intent);
        }

        if (id == R.id.edit) {
            Intent intent= new Intent(Home.this, Ajustes.class);
            startActivity(intent);
            return true;
        }else if (id == R.id.mapas) {
            Intent intent= new Intent(Home.this, Mapa.class);
            startActivity(intent);
            return true;


        return super.onOptionsItemSelected(item);
    }*/
