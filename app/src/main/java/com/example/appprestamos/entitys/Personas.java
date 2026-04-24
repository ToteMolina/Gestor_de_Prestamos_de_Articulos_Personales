package com.example.appprestamos.entitys;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "personas")
public class Personas {
    @PrimaryKey(autoGenerate = true)
    public int idPersona;
    public String nombrePersona;
    public String numeroContacto;

    public Personas(String nombrePersona, String numeroContacto) {
        this.nombrePersona = nombrePersona;
        this.numeroContacto = numeroContacto;
    }

    @Override
    public String toString() {
        return nombrePersona;
    }
}
