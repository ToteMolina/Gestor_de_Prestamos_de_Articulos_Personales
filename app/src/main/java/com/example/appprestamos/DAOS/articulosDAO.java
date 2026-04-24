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
    @Query("SELECT * FROM articulos WHERE estado = 'Disponible'")
    List<Articulos> getDisponibles();
    @Query("SELECT * FROM articulos WHERE estado = 'Prestado'")
    List<Articulos> getPrestados();
    @Query("UPDATE articulos SET estado = :nuevoEstado WHERE idArticulos = :id")
    void actualizarEstado(int id, String nuevoEstado);
}
