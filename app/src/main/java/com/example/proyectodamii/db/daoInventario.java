package com.example.proyectodamii.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;


public class daoInventario extends SQLiteOpenHelper {

    String TABLE_NAME = "productos";
    FirebaseFirestore firestore;
    public daoInventario(@Nullable Context context) {
        super(context, "Productos.db",null,2);
    }

    //-----------------SQL------------------------------------

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create Table productos(id_producto integer primary key autoincrement, nombre_producto TEXT, descripcion TEXT, " +
                "precio double, stock double, tipo TEXT, img_url TEXT," +
                "idfirebase TEXT, sincronizado integer default 0 )");//para mainpular datos en firebase y sincronizarlos
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop Table if exists productos");
    }


    public long agregarProducto(Producto producto) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("nombre_producto", producto.getNombre());
        values.put("descripcion", producto.getDescripcion());
        values.put("precio", producto.getPrecio());
        values.put("stock", producto.getStock());
        values.put("tipo", producto.getTipo());
        values.put("img_url", producto.getImgUrl());

        long id = db.insert(TABLE_NAME, null, values);
        db.close();

        return id;
    }

    /**
     * Método para obtener todos los productos de la base de datos
     * @return Lista de productos
     */
    public List<Producto> obtenerTodosProductos() {
        List<Producto> listaProductos = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " ORDER BY nombre_producto", null);

        if (cursor.moveToFirst()) {
            do {
                Producto producto = new Producto();
                producto.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id_producto")));
                producto.setNombre(cursor.getString(cursor.getColumnIndexOrThrow("nombre_producto")));
                producto.setDescripcion(cursor.getString(cursor.getColumnIndexOrThrow("descripcion")));
                producto.setPrecio(cursor.getDouble(cursor.getColumnIndexOrThrow("precio")));
                producto.setStock(cursor.getDouble(cursor.getColumnIndexOrThrow("stock")));
                producto.setTipo(cursor.getString(cursor.getColumnIndexOrThrow("tipo")));
                producto.setImgUrl(cursor.getString(cursor.getColumnIndexOrThrow("img_url")));

                listaProductos.add(producto);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return listaProductos;
    }

    /**
     * Método para obtener un producto por su ID local
     * @param id ID del producto en la base de datos local
     * @return Objeto Producto o null si no se encuentra
     */
    public Producto obtenerProductoPorId(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Producto producto = null;

        Cursor cursor = db.query(TABLE_NAME,
                null,
                "id_producto = ?",
                new String[]{String.valueOf(id)},
                null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            producto = new Producto();
            producto.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id_producto")));
            producto.setNombre(cursor.getString(cursor.getColumnIndexOrThrow("nombre_producto")));
            producto.setDescripcion(cursor.getString(cursor.getColumnIndexOrThrow("descripcion")));
            producto.setPrecio(cursor.getDouble(cursor.getColumnIndexOrThrow("precio")));
            producto.setStock(cursor.getDouble(cursor.getColumnIndexOrThrow("stock")));
            producto.setTipo(cursor.getString(cursor.getColumnIndexOrThrow("tipo")));
            producto.setImgUrl(cursor.getString(cursor.getColumnIndexOrThrow("img_url")));

            cursor.close();
        }

        db.close();
        return producto;
    }

    /**
     * Método para buscar productos por nombre
     * @param nombre Nombre o parte del nombre a buscar
     * @return Lista de productos que coinciden con la búsqueda
     */
    public List<Producto> buscarProductosPorNombre(String nombre) {
        List<Producto> listaProductos = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME +
                        " WHERE nombre_producto LIKE ? ORDER BY nombre_producto",
                new String[]{"%" + nombre + "%"});

        if (cursor.moveToFirst()) {
            do {
                Producto producto = new Producto();
                producto.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id_producto")));
                producto.setNombre(cursor.getString(cursor.getColumnIndexOrThrow("nombre_producto")));
                producto.setDescripcion(cursor.getString(cursor.getColumnIndexOrThrow("descripcion")));
                producto.setPrecio(cursor.getDouble(cursor.getColumnIndexOrThrow("precio")));
                producto.setStock(cursor.getDouble(cursor.getColumnIndexOrThrow("stock")));
                producto.setTipo(cursor.getString(cursor.getColumnIndexOrThrow("tipo")));
                producto.setImgUrl(cursor.getString(cursor.getColumnIndexOrThrow("img_url")));

                listaProductos.add(producto);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return listaProductos;
    }

    /**
     * Método para actualizar un producto existente
     * @param producto Producto con los datos actualizados
     * @return true si se actualizó correctamente
     */
    public boolean actualizarProducto(Producto producto) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("nombre_producto", producto.getNombre());
        values.put("descripcion", producto.getDescripcion());
        values.put("precio", producto.getPrecio());
        values.put("stock", producto.getStock());
        values.put("tipo", producto.getTipo());
        values.put("img_url", producto.getImgUrl());

        int rowsAffected = db.update(TABLE_NAME, values,
                "id_producto = ?", new String[]{String.valueOf(producto.getId())});

        db.close();
        return rowsAffected > 0;
    }

    /**
     * Método para eliminar un producto por su ID
     * @param id ID del producto a eliminar
     * @return true si se eliminó correctamente
     */
    public boolean eliminarProducto(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsAffected = db.delete(TABLE_NAME,
                "id_producto = ?", new String[]{String.valueOf(id)});
        db.close();
        return rowsAffected > 0;
    }

    /**
     * Método para obtener productos por tipo
     * @param tipo Tipo de producto a filtrar
     * @return Lista de productos del tipo especificado
     */
    public List<Producto> obtenerProductosPorTipo(String tipo) {
        List<Producto> listaProductos = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME,
                null,
                "tipo = ?",
                new String[]{tipo},
                null, null, "nombre_producto");

        if (cursor != null && cursor.moveToFirst()) {
            do {
                Producto producto = new Producto();
                producto.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id_producto")));
                producto.setNombre(cursor.getString(cursor.getColumnIndexOrThrow("nombre_producto")));
                producto.setDescripcion(cursor.getString(cursor.getColumnIndexOrThrow("descripcion")));
                producto.setPrecio(cursor.getDouble(cursor.getColumnIndexOrThrow("precio")));
                producto.setStock(cursor.getDouble(cursor.getColumnIndexOrThrow("stock")));
                producto.setTipo(cursor.getString(cursor.getColumnIndexOrThrow("tipo")));
                producto.setImgUrl(cursor.getString(cursor.getColumnIndexOrThrow("img_url")));

                listaProductos.add(producto);
            } while (cursor.moveToNext());
            cursor.close();
        }

        db.close();
        return listaProductos;
    }

    /**
     * Método para contar el total de productos
     * @return Número total de productos en la base de datos
     */
    public int contarProductos() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_NAME, null);

        int count = 0;
        if (cursor != null && cursor.moveToFirst()) {
            count = cursor.getInt(0);
            cursor.close();
        }

        db.close();
        return count;
    }

    /**
     * Método para obtener productos con stock bajo (menor a 10 unidades)
     * @return Lista de productos con stock bajo
     */
    public List<Producto> obtenerProductosStockBajo() {
        List<Producto> listaProductos = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME +
                " WHERE stock < 10 ORDER BY stock ASC", null);

        if (cursor.moveToFirst()) {
            do {
                Producto producto = new Producto();
                producto.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id_producto")));
                producto.setNombre(cursor.getString(cursor.getColumnIndexOrThrow("nombre_producto")));
                producto.setDescripcion(cursor.getString(cursor.getColumnIndexOrThrow("descripcion")));
                producto.setPrecio(cursor.getDouble(cursor.getColumnIndexOrThrow("precio")));
                producto.setStock(cursor.getDouble(cursor.getColumnIndexOrThrow("stock")));
                producto.setTipo(cursor.getString(cursor.getColumnIndexOrThrow("tipo")));
                producto.setImgUrl(cursor.getString(cursor.getColumnIndexOrThrow("img_url")));

                listaProductos.add(producto);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return listaProductos;
    }

    /**
     * Método para obtener productos ordenados por precio
     * @param ascendente true para orden ascendente, false para descendente
     * @return Lista de productos ordenados por precio
     */
    public List<Producto> obtenerProductosOrdenadosPorPrecio(boolean ascendente) {
        List<Producto> listaProductos = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String orden = ascendente ? "ASC" : "DESC";
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME +
                " ORDER BY precio " + orden, null);

        if (cursor.moveToFirst()) {
            do {
                Producto producto = new Producto();
                producto.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id_producto")));
                producto.setNombre(cursor.getString(cursor.getColumnIndexOrThrow("nombre_producto")));
                producto.setDescripcion(cursor.getString(cursor.getColumnIndexOrThrow("descripcion")));
                producto.setPrecio(cursor.getDouble(cursor.getColumnIndexOrThrow("precio")));
                producto.setStock(cursor.getDouble(cursor.getColumnIndexOrThrow("stock")));
                producto.setTipo(cursor.getString(cursor.getColumnIndexOrThrow("tipo")));
                producto.setImgUrl(cursor.getString(cursor.getColumnIndexOrThrow("img_url")));

                listaProductos.add(producto);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return listaProductos;
    }



    public static class Producto {
        private int id;
        private String nombre;
        private String descripcion;
        private double precio;
        private double stock;
        private String tipo;
        private String imgUrl;

        // Constructores
        public Producto() {}

        public Producto(String nombre, String descripcion, double precio, double stock, String tipo, String imgUrl) {
            this.nombre = nombre;
            this.descripcion = descripcion;
            this.precio = precio;
            this.stock = stock;
            this.tipo = tipo;
            this.imgUrl = imgUrl;
        }

        // Getters y Setters
        public int getId() { return id; }
        public void setId(int id) { this.id = id; }

        public String getNombre() { return nombre; }
        public void setNombre(String nombre) { this.nombre = nombre; }

        public String getDescripcion() { return descripcion; }
        public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

        public double getPrecio() { return precio; }
        public void setPrecio(double precio) { this.precio = precio; }

        public double getStock() { return stock; }
        public void setStock(double stock) { this.stock = stock; }

        public String getTipo() { return tipo; }
        public void setTipo(String tipo) { this.tipo = tipo; }

        public String getImgUrl() { return imgUrl; }
        public void setImgUrl(String imgUrl) { this.imgUrl = imgUrl; }

        @Override
        public String toString() {
            return nombre + " - $" + precio + " (Stock: " + stock + ")";
        }
    }
}


