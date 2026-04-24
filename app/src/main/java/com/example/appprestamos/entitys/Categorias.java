package com.example.appprestamos.entitys;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "categorias")
public class Categorias {
    @PrimaryKey(autoGenerate = true)
    public int idCategoria;
    public String nombreCategoria;

    public Categorias() {
    }

    public Categorias(int idCategoria, String nombreCategoria) {
        this.idCategoria = idCategoria;
        this.nombreCategoria = nombreCategoria;
    }

    public Categorias(String nombreCategoria) {
        this.nombreCategoria = nombreCategoria;
    }
}
