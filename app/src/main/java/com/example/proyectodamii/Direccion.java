package com.example.proyectodamii;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class Direccion extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener, GoogleMap.OnMapLongClickListener {
    private GoogleMap mMap;
    EditText direcciones;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_direccion);


        // Inicializar el mapa
        SupportMapFragment mapFragment = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.mapa_main_direccion);
        mapFragment.getMapAsync( this);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mapa_main_direccion), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

         direcciones = findViewById(R.id.edtDireccion);

        Button guardar = findViewById(R.id.btnConfirmar_direccion);
        Button cancelar =findViewById(R.id.btnCancelar_direccion);

        guardar.setOnClickListener(view -> {
            Intent data = new Intent();
            data.putExtra("respuesta",direcciones.getText().toString() );
            setResult(Checkout.RESULT_OK, data);
            finish();

        });

        ImageView retroceder = findViewById(R.id.imvAtras);
        retroceder.setOnClickListener(view -> {
            finish();
        });

        cancelar.setOnClickListener(view -> {
            finish();
        });

        ImageView modos = findViewById(R.id.modos_claro_oscuro);
        final boolean[] isDia = {false};

        modos.setOnClickListener(v -> {
            if (isDia[0]) {
                modos.setImageResource(R.drawable.noche);
            } else {
                modos.setImageResource(R.drawable.dia);
            }
            isDia[0] = !isDia[0];
        });

    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;


        this.mMap.setOnMapClickListener(this);
        this.mMap.setOnMapLongClickListener(this);
        // Coordenadas
        LatLng ues = new LatLng(13.978918054647709, -89.56886658917158);
        // Agregar marcador en esa ubicación
        mMap.addMarker(new MarkerOptions()
                .position(ues)
                .title("HarvestHub Local"));
        // Mover la cámara al marcador con zoom
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ues, 15));
        // Controles de maapa
        mMap.getUiSettings().setZoomControlsEnabled(true); // Botones de zoom
        mMap.getUiSettings().setCompassEnabled(true); // Brújula
        mMap.getUiSettings().setMapToolbarEnabled(true); // Barra de herramientas
        mMap.getUiSettings().setMyLocationButtonEnabled(true); // mi ubicación
        mMap.getUiSettings().setAllGesturesEnabled(true); // Permitir gestos
        // tipo de mapa
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL); //
    }


    //Esteablecer marcador
    @Override
    public void onMapClick(@NonNull LatLng latLng) {

        mMap.clear();

        LatLng marcador = new LatLng(latLng.latitude, latLng.longitude);
        // Agregar marcador en esa ubicación
        mMap.addMarker(new MarkerOptions()
                .position(marcador)
                .title(""));
        // Mover la cámara al marcador con zoom
        mMap.moveCamera(CameraUpdateFactory.newLatLng(marcador));

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> direccion = geocoder.getFromLocation(latLng.latitude,latLng.longitude,1);
            String marcar_direc = direccion.get(0).getAddressLine(0);
            direcciones.setText(marcar_direc);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


    @Override
    public void onMapLongClick(@NonNull LatLng latLng) {
        mMap.clear();
        LatLng marcador = new LatLng(latLng.latitude, latLng.longitude);
        // Agregar marcador en esa ubicación
        mMap.addMarker(new MarkerOptions()
                .position(marcador)
                .title(""));
        // Mover la cámara al marcador con zoom
        mMap.moveCamera(CameraUpdateFactory.newLatLng(marcador));

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> direccion = geocoder.getFromLocation(latLng.latitude,latLng.longitude,1);
            String marcar_direc = direccion.get(0).getAddressLine(0);
            direcciones.setText(marcar_direc);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}