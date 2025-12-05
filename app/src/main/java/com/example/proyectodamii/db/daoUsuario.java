package com.example.proyectodamii.db;

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

import at.favre.lib.crypto.bcrypt.BCrypt;


public class daoUsuario extends SQLiteOpenHelper {
    FirebaseFirestore firestore;



    public daoUsuario(@Nullable Context context) {
        super(context, "Usuarios.db", null, 1);
        //Inicia firestore
        firestore = FirebaseFirestore.getInstance();
    }


    //-----------------SQL------------------------------------

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create Table usuario(id integer primary key autoincrement, nombre TEXT, correo TEXT, contrasenia TEXT," +
                "idfirebase TEXT, sincronizado integer default 0 )");//para mainpular datos en firebase y sincronizarlos
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop Table if exists usuario");//Se elimina la tabla cuando cambia de version
    }

    //Constructor de registros
    public  Boolean insertar(String nombre, String correo, String pass){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contenedor = new ContentValues();
        contenedor.put("nombre",nombre);
        contenedor.put("correo",correo);

        //Encriptacion desde la bd
        String encryptado = daoUsuario.encrypt_pass(pass);
        contenedor.put("contrasenia",encryptado);
        long resultado = sqLiteDatabase.insert("usuario",null,contenedor);
        if(resultado == -1){
            return false;
        }else {
            //Sincronizacion con firebase
            if(!verfCorreo(correo)){
                sincronizar((int) resultado,nombre,correo,encryptado);
            }
            return true;
        }
    }

    //Editar nombre de usuario
    public  Boolean cambiarnombre (String nombre, String correo){
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

    public Boolean verfCredenciales(String correo, String contrasenia){
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("Select * from usuario where correo = ? ", new String[]{correo});
        boolean resultado = false;

        if (cursor != null && cursor.moveToFirst()) {
            // Obteniene la contraseña cifrada
            String crypt_pass = cursor.getString(cursor.getColumnIndexOrThrow("contrasenia"));

            // Compara las contrasenias (la ingresada con la cifrada)
            boolean verificar = daoUsuario.verficar_pass(contrasenia, crypt_pass);

            // Si coincide devuelve un true
            resultado = verificar;
        }

        cursor.close();
        sqLiteDatabase.close();
        return resultado;

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
        Map<String, Object> user = new HashMap<>();//diccionario con la data a enviar
        user.put("nombre", nombre);
        user.put("correo", correo);
        user.put("contrasenia", pass);
        user.put("fecha_creacion", System.currentTimeMillis());

        firestore.collection("usuarios").document(correo)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        // El correo ya existe en Firestore
                        Log.w("SYNC", "Este correo ya existe. No se reemplazará.");
                    } else {
                        // El correo no existe, se puede crear
                        firestore.collection("usuarios").document(correo)
                                .set(user)
                                .addOnSuccessListener(aVoid -> actualizaridfirebase(idlocal, correo))
                                .addOnFailureListener(e -> Log.e("Firebase", "Error: " + e.getMessage()));
                    }
                }).addOnFailureListener(e -> Log.e("Firebase", "Error al verificar correo: " + e.getMessage()));


    }
    //Verificar correo en firebase
    public interface CorreoListener { //interfaz que consulta a firebase
        void onResult(boolean existe);
    }
    public void  verfCorreo_en_firebase(String correo, CorreoListener correo_check){

        firestore.collection("usuarios").document(correo)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                        // Valida si el correo existe en Firestore
                        boolean existe = documentSnapshot.exists();
                    correo_check.onResult(existe);
                }).addOnFailureListener(e -> Log.e("Firebase", "Error al verificar correo: " + e.getMessage()));

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
        Cursor cursor = db.rawQuery("SELECT * FROM usuario WHERE sincronizado = 0",null);//puntero que a apunta a la BD en sqlite

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


    //El correo de usuario es el identificador de cada usuario
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


    //Actualiza la base de datos local en base a firebase
    public void insertar_usuarioFirebase_a_sqlite(Context context) {
        firestore.collection("usuarios")
                .addSnapshotListener((snapshots, e) -> {
                    if (e != null) {
                        Log.e("FIREBASE", "Error escuchando cambios: " + e.getMessage());
                        return;
                    }

                    if (snapshots == null) return;

                    SQLiteDatabase db = this.getWritableDatabase();

                    for (com.google.firebase.firestore.DocumentChange cambio : snapshots.getDocumentChanges()) {
                        String firebaseId = cambio.getDocument().getId();
                        String nombre = cambio.getDocument().getString("nombre");
                        String correo = cambio.getDocument().getString("correo");
                        String contrasenia = cambio.getDocument().getString("contrasenia");

                        switch (cambio.getType()) {
                            case ADDED:
                            case MODIFIED:
                                // Si el usuario no existe localmente se insertar a la base de datos
                                if (!verfNombreCorreo(nombre, correo)) {
                                    ContentValues contenedor = new ContentValues();
                                    contenedor.put("nombre", nombre);
                                    contenedor.put("correo", correo);
                                    contenedor.put("contrasenia", contrasenia);
                                    contenedor.put("idfirebase", firebaseId);
                                    contenedor.put("sincronizado", 1);
                                    db.insert("usuario", null, contenedor);
                                } else {
                                    // Si existe se actualizan sus datos
                                    ContentValues update = new ContentValues();
                                    update.put("nombre", nombre);
                                    update.put("contrasenia", contrasenia);
                                    db.update("usuario", update, "correo = ?", new String[]{correo});
                                }
                                break;

                            case REMOVED:
                                // Si el documento se elimina en Firebase se borrar de SQLite (mantiene un formato de base de datos)
                                db.delete("usuario", "idfirebase = ?", new String[]{firebaseId});
                                break;
                        }
                    }

                    db.close();
                });
    }

    //Encryptar contrasenia
    public static String encrypt_pass(String pass){

        return  BCrypt.withDefaults().hashToString(12, pass.toCharArray());
    }

    //Verifica la contrasenia encriptda
    public static Boolean verficar_pass(String pass, String cryp_pass){
        if (cryp_pass== null || pass==null){
            return  false;
        }

        BCrypt.Result verificado = BCrypt.verifyer().verify(pass.toCharArray(),cryp_pass);
        return verificado.verified;
    }
}
