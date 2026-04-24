package com.example.appprestamos.DAOS;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.appprestamos.entitys.Categorias;

import java.util.List;

@Dao
public interface categoriaDAO {
    @Query("SELECT * FROM categorias")
    List<Categorias> getAllCategorias();

    @Insert
    long insertCategoria(Categorias categorias);
}
