package com.example.proyectodamii;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class daoUsuario extends SQLiteOpenHelper {
    public static final String nombreBD="Usuarios.db";
    FirebaseFirestore firestore;



    public daoUsuario(@Nullable Context context) {
        super(context, "Usuarios.db", null, 1);
        //Iniciar firestore
        firestore = FirebaseFirestore.getInstance();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create Table usuario(id integer primary key autoincrement, nombre TEXT, correo TEXT, contrasenia TEXT," +
                "idfirebase TEXT, sincronizado integer default 0 )");//para mainpular datos en firebase y sincronizarlos
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop Table if exists usuario");
    }

    //Constructor de registros
    public  Boolean insertar(String nombre, String correo, String pass){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contenedor = new ContentValues();
        contenedor.put("nombre",nombre);
        contenedor.put("correo",correo);
        contenedor.put("contrasenia",pass);
        long resultado = sqLiteDatabase.insert("usuario",null,contenedor);
        if(resultado == -1){
            return false;
        }else {
            //Sincronizacion con firebase
            sincronizar((int) resultado,nombre,correo,pass);
            return true;
        }
    }

    //Editar nombre de usuario
    public  Boolean cambiarnombre (String nombre, String correo, Context context){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contenedor = new ContentValues();
        contenedor.put("nombre",nombre);
        String[] dondearg = {correo};
        int resultado = sqLiteDatabase.update("usuario",contenedor, "correo = ?",dondearg);
        if (resultado > 0) {
            // Buscar el idFirebase correspondiente a este usuario
            String idFirebase = obtenerIdFirebasePorCorreo(correo);

            if (idFirebase != null && !idFirebase.isEmpty()) {
                // Actualizar en Firestore
                Map<String, Object> actualizacion = new HashMap<>();
                actualizacion.put("nombre", nombre);

                firestore.collection("usuarios").document(idFirebase)
                        .update(actualizacion)
                        .addOnSuccessListener(aVoid -> Log.d("FIREBASE", "Nombre actualizado en Firestore"))
                        .addOnFailureListener(e -> Log.e("FIREBASE", "Error al actualizar Firestore: " + e.getMessage()));
            } else {
                Log.w("FIREBASE", "No se encontró idFirebase para el usuario con correo: " + correo);
            }

            return true;
        } else {
            return false;
        }
    }



    //Validaciones para ver si el usuario a ingresar ya existe
    public Boolean verfNombreCorreo(String nombre, String correo){
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();//lee la base de datos para validar
        Cursor cursor = sqLiteDatabase.rawQuery("Select * from usuario where nombre = ? and correo = ?", new String[]{nombre,correo});
        if(cursor.getCount()>0){
            return true;
        }else {
            return  false;
        }
    }
    public Boolean verfNombre(String nombre){
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("Select * from usuario where nombre = ?", new String[]{nombre});
        if(cursor.getCount()>0){
            return true;
        }else {
            return  false;
        }
    }

    public Boolean verfCorreo(String correo){
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("Select * from usuario where correo = ?", new String[]{correo});
        if(cursor.getCount()>0){
            return true;
        }else {
            return  false;
        }
    }

    public Boolean verfCredenciales(String nombre, String contrasenia){
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("Select * from usuario where nombre = ? and contrasenia = ?", new String[]{nombre, contrasenia});
        if(cursor.getCount()>0){
            return true;
        }else {
            return  false;
        }
    }
    public Boolean verfNombreCorreoContra(String nombre, String correo, String contrasenia){
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("Select * from usuario where nombre = ? and correo = ? and contrasenia = ?", new String[]{nombre, correo, contrasenia});
        if(cursor.getCount()>0){
            return true;
        }else {
            return  false;
        }
    }
    //--------------FIREBASE--------------------------------------------

    //Sincronizar con firebase
    public void sincronizar(int idlocal, String nombre, String correo, String pass){
        Map<String, Object> user = new HashMap<>();//
        user.put("nombre", nombre);
        user.put("correo", correo);
        user.put("contrasenia", pass);
        user.put("fecha_creacion", System.currentTimeMillis());

        firestore.collection("usuarios").add(user).addOnSuccessListener(documentReference -> {
            actualizaridfirebase(idlocal,documentReference.getId());
        }).addOnFailureListener(e -> {
            //por si falla la sincronizacion
            Log.e("Firebase","error al sincronizar"+e.getMessage());
        });

    }

    //Actualizar id firebase del usuario en sqlite
    private void actualizaridfirebase (int idlocal, String idfirebase){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contenedor = new ContentValues();
        contenedor.put("idfirebase",idfirebase);
        contenedor.put("sincronizado",1);

        db.update("usuario",contenedor,"id = ?", new String[]{String.valueOf(idlocal)});
        db.close();
    }

    //Sincronizar al entrar a la app
    public void sincronizariniciar(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM usuario WHERE sincronizado = 0",null);

        if(cursor != null && cursor.moveToFirst()){
            do{
                //sincroniza los datos de la tabla de firebase con los datos de SQLite
                int id = cursor.getInt(cursor.getColumnIndex("id"));
                String nombre = cursor.getString(cursor.getColumnIndex("nombre"));
                String correo = cursor.getString(cursor.getColumnIndex("correo"));
                String contrasenia = cursor.getString(cursor.getColumnIndex("contrasenia"));
                sincronizar(id,nombre,correo,contrasenia);

            }while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
    }

    void insertarFirebase_Sqlite() {
        firestore.collection("usuarios").get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (com.google.firebase.firestore.DocumentSnapshot document : queryDocumentSnapshots) {
                String firebaseId = document.getId();
                String nombre = document.getString("nombre");
                String correo = document.getString("correo");
                String contrasenia = document.getString("contrasenia");

                // Verificar si el usuario ya existe en SQLite
                if (!verfNombreCorreo(nombre, correo)) {
                    // Insertar nuevo usuario en SQLite
                    SQLiteDatabase db = this.getWritableDatabase();
                    ContentValues contenedor = new ContentValues();
                    contenedor.put("nombre", nombre);
                    contenedor.put("correo", correo);
                    contenedor.put("contrasenia", contrasenia);
                    contenedor.put("firebase_id", firebaseId);
                    contenedor.put("sincronizado", 1);

                    long resultado = db.insert("usuario", null, contenedor);

                    db.close();
                }
            }
        }).addOnFailureListener(e -> {
            Log.e("FIREBASE", "Error copiando desde Firebase: " + e.getMessage());
        });
    }
    private String obtenerIdFirebasePorCorreo(String correo) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT idfirebase FROM usuario WHERE correo = ?", new String[]{correo});

        String idFirebase = null;
        if (cursor.moveToFirst()) {
            idFirebase = cursor.getString(cursor.getColumnIndex("idfirebase"));
        }

        cursor.close();
        db.close();
        return idFirebase;
    }


}
