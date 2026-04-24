package com.example.appprestamos.AppDatabasePackage;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.appprestamos.DAOS.articulosDAO;
import com.example.appprestamos.DAOS.categoriaDAO;
import com.example.appprestamos.entitys.Articulos;
import com.example.appprestamos.entitys.Categorias;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(
        entities = {Categorias.class, Articulos.class},
        version = 1,
        exportSchema = true
)
public abstract class appDatabase extends RoomDatabase {
    public abstract categoriaDAO categoria_dao();
    public abstract articulosDAO articulos_dao();

    private static volatile appDatabase INSTANCE;
    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(4);
    public static appDatabase getInstance(Context context){
        if (INSTANCE == null){
            synchronized (appDatabase.class){
                if (INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            appDatabase.class,
                            "db_prestamos"
                    ).build();
                }
            }

        }
        return INSTANCE;
    }
}
