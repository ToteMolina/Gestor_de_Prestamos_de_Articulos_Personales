package com.example.appprestamos.DAOS;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.appprestamos.entitys.Prestamos;

import java.util.List;

@Dao
public interface prestamoDAO {
    @Insert
    long insertPrestamo(Prestamos prestamos);

    // no devuelto = activo
    @Query("SELECT * FROM prestamos WHERE devuelto = 0")
    List<Prestamos> getPrestamosActivos();

    // devuelto = historial
    @Query("SELECT * FROM prestamos WHERE devuelto = 1")
    List<Prestamos> getHistorialPrestamos();

    @Update
    void updatePrestamo(Prestamos prestamo);
}
