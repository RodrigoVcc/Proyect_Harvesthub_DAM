package com.example.proyectodamii;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class Mapa extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_mapa);

        Toolbar toolbar = findViewById(R.id.toolbar_map);
        setSupportActionBar(toolbar);

        // Inicializar el mapa
        SupportMapFragment mapFragment = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.mapa_main);
        mapFragment.getMapAsync(this);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mapa_main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_mapas, menu);
        return true;
    }

    //Comportamiento del mapa
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        // Coordenadas
        LatLng ues = new LatLng(13.978918054647709, -89.56886658917158);
        // Agregar marcador en esa ubicación
        mMap.addMarker(new MarkerOptions()
                .position(ues)
                .title("HarvestHub Local"));
        // Mover la cámara al marcador con zoom
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ues, 15));

        // Mover la cámara al marcador con zoom
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ues, 15));
    // Controles de maapa
    mMap.getUiSettings().setZoomControlsEnabled(true); // Botones de zoom
        mMap.getUiSettings().setCompassEnabled(true); // Brújula
        mMap.getUiSettings().setMapToolbarEnabled(true); // Barra de herramientas
        mMap.getUiSettings().setMyLocationButtonEnabled(true); // mi ubicación
        mMap.getUiSettings().setAllGesturesEnabled(true); // Permitir gestos
        // tipo de mapa
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID); //
    }
}
