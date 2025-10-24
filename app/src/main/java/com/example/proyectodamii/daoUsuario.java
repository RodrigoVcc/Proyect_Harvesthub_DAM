package com.example.proyectodamii;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;


public class daoUsuario extends SQLiteOpenHelper {
    public static final String nombreBD="Usuarios.db";



    public daoUsuario(@Nullable Context context) {
        super(context, "Usuarios.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create Table usuario(id integer primary key autoincrement, nombre TEXT, correo TEXT, contrasenia TEXT )");
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
            return true;
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



}
