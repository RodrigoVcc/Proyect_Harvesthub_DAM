package com.example.proyectodamii.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class daoPedidos extends SQLiteOpenHelper {
    FirebaseFirestore firestore;
    public daoPedidos(@Nullable Context context) {
        super(context, "Pedidos.db", null, 2);
        //Inicia firestore
        firestore = FirebaseFirestore.getInstance();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create Table pedidos(id_pedido integer primary key autoincrement, id_usuario integer, total integer, fecha DATE, " +
                " sincronizado integer default 0," +
                "foreign key (id_usuario) references usuario(id) )");//para mainpular datos en firebase y sincronizarlos

        sqLiteDatabase.execSQL("create Table pedido_detalles(id_detalle integer primary key autoincrement, pedido_id integer, producto_id integer, cantidad integer, " +
                "foreign key (pedido_id) references pedidos(id_pedido),foreign key (producto_id) references productos(id_producto))");//para mainpular datos en firebase y sincronizarlos

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop Table if exists pedidos");
        sqLiteDatabase.execSQL("drop Table if exists pedido_detalles");
    }

    public  long insertar_pedido(int id_usuario, int total, String fecha){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contenedor = new ContentValues();
        contenedor.put("id_usuario",id_usuario);
        contenedor.put("total",total);
        contenedor.put("fecha",fecha);
        contenedor.put("sincronizado", 0);

       return sqLiteDatabase.insert("pedidos",null, contenedor);
    }


    public boolean insertar_detalles_pedido(int id_usuario, int total, String fecha, int pedidoId, List<Integer> productosId,
                                            List<Integer> cantidades) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        long idpedido;

        sqLiteDatabase.beginTransaction();

        idpedido = insertar_pedido(id_usuario,total,fecha);
        if (idpedido == -1){
            return false;
        }

        for(Integer idProducto: productosId){
            ContentValues contenedor_detalles = new ContentValues();
            contenedor_detalles.put("pedido_id", pedidoId);
            contenedor_detalles.put("producto_id", productosId.get(idProducto));
            contenedor_detalles.put("cantidad",cantidades.get(idProducto));
            long id_detalles = sqLiteDatabase.insert("pedido_detalles",null,contenedor_detalles);

            if (id_detalles == -1){
                return  false;
            }

        }

        sqLiteDatabase.setTransactionSuccessful();

        return true;
    }


    public void sincronizar_pedidos_pendientes() {
        SQLiteDatabase db = this.getReadableDatabase();

        // Obteniene los pedidos no sincronizados
        Cursor cursor = db.rawQuery(
                "SELECT * FROM pedidos WHERE sincronizado = 0",
                null
        );

        while (cursor.moveToNext()) {
            // Crea el mapa de datos para Firebase
            Map<String, Object> pedido = new HashMap<>();
            pedido.put("id_local", cursor.getInt(cursor.getColumnIndexOrThrow("id_pedido")));
            pedido.put("id_usuario", cursor.getInt(cursor.getColumnIndexOrThrow("id_usuario")));
            pedido.put("total", cursor.getInt(cursor.getColumnIndexOrThrow("total")));
            pedido.put("fecha", cursor.getString(cursor.getColumnIndexOrThrow("fecha")));
            pedido.put("sincronizado", true);
            pedido.put("timestamp", FieldValue.serverTimestamp());

            // Sube la data a Firestore
            firestore.collection("pedidos")
                    .add(pedido)
                    .addOnSuccessListener(documentReference -> {
                        // Actualiza el registro local con id de Firebase
                        String firebaseId = documentReference.getId();
                        marcar_sincronizado(cursor.getInt(cursor.getColumnIndexOrThrow("id_pedido")), firebaseId);
                    })
                    .addOnFailureListener(e -> {
                        Log.e("Firebase", "Error sincronizando pedido", e);
                    });
        }

        cursor.close();
    }

    private void marcar_sincronizado(int idLocal, String firebaseId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("sincronizado", 1);
        values.put("idfirebase", firebaseId);

        db.update("pedidos", values,
                "id_pedido = ?",
                new String[]{String.valueOf(idLocal)}
        );
    }

    public void sincronizar_detalles_pedido(int idPedidoLocal, String idPedidoFirebase) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM pedido_detalles WHERE pedido_id = ?",
                new String[]{String.valueOf(idPedidoLocal)}
        );

        while (cursor.moveToNext()) {
            Map<String, Object> detalle = new HashMap<>();
            detalle.put("pedido_id", idPedidoFirebase);
            detalle.put("producto_id", cursor.getInt(cursor.getColumnIndexOrThrow("producto_id")));
            detalle.put("cantidad", cursor.getInt(cursor.getColumnIndexOrThrow("cantidad")));

            firestore.collection("pedido_detalles")
                    .add(detalle)
                    .addOnSuccessListener(documentReference -> {
                        Log.d("Firebase", "Detalle sincronizado: " + documentReference.getId());
                    })
                    .addOnFailureListener(e -> {
                        Log.e("Firebase", "Error sincronizando detalle", e);
                    });
        }

        cursor.close();
    }


    public void insertar_y_sincronizar_pedido(int id_usuario, int total, String fecha,int pedido,
                                              List<Integer> productosId, List<Integer> cantidades) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.beginTransaction();
        try {
            // Insertar localmente
            boolean exito = insertar_detalles_pedido(id_usuario, total, fecha, pedido, productosId, cantidades);

            if (exito) {
                db.setTransactionSuccessful();
                // Iniciar sincronización en segundo plano
                new Thread(this::sincronizar_pedidos_pendientes).start();
            }
        } finally {
            db.endTransaction();
        }
    }
}



