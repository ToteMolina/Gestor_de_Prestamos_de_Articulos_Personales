package com.example.appprestamos.entitys;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "articulos", foreignKeys = {@ForeignKey(entity = Categorias.class, parentColumns = "idCategoria",
        childColumns = "idCategoria",
        onDelete = ForeignKey.RESTRICT,
        onUpdate = ForeignKey.NO_ACTION
)})
public class Articulos {
    @PrimaryKey(autoGenerate = true)
    public int idArticulos;
    public String descripcion;
    public int idCategoria;
}
