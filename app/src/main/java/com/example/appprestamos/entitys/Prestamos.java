package com.example.appprestamos.entitys;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "prestamos",
        foreignKeys = {
        @ForeignKey(entity = Articulos.class, parentColumns = "idArticulos", childColumns = "idArticulos", onDelete = ForeignKey.RESTRICT),
        @ForeignKey(entity = Personas.class, parentColumns = "idPersona", childColumns = "idPersona", onDelete = ForeignKey.RESTRICT)
    }
)
public class Prestamos {
    @PrimaryKey(autoGenerate = true)
    public int idPrestamos;
    public int idArticulos;
    public int idPersona;
    public Date fechaPrestamo;
    public Date fechaDevoEstimada;
    public boolean devuelto;
}
