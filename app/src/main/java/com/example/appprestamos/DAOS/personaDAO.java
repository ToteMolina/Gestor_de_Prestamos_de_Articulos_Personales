package com.example.appprestamos.DAOS;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.appprestamos.entitys.Personas;

import java.util.List;

@Dao
public interface personaDAO {
    @Query("SELECT * FROM personas")
    List<Personas> getAllPersonas();

    @Insert
    long insertarPersona(Personas persona);

    @Update
    int actualizarPersona(Personas persona);

    @Delete
    int eliminarPersona(Personas persona);

    @Query("DELETE FROM personas WHERE idPersona = :idPersona")
    int deleteById(int idPersona);

    @Query("UPDATE personas SET nombrePersona = :nuevoNombre, numeroContacto = :nuevoNumero WHERE idPersona = :idPersona")
    int updateById(int idPersona, String nuevoNombre, String nuevoNumero);
}
