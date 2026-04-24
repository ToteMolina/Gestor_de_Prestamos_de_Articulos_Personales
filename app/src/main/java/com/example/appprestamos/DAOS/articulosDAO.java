package com.example.appprestamos.DAOS;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.appprestamos.entitys.Articulos;

import java.util.List;

@Dao
public interface articulosDAO {
    @Query("SELECT * FROM articulos")
    List<Articulos> getAllArticulos();

    @Insert
    long insertArticulos(Articulos articulos);
}
