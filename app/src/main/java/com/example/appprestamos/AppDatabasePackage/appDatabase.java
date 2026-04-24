package com.example.appprestamos.AppDatabasePackage;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.appprestamos.DAOS.articulosDAO;
import com.example.appprestamos.DAOS.categoriaDAO;
import com.example.appprestamos.DAOS.personaDAO;
import com.example.appprestamos.Migraciones.Migraciones;
import com.example.appprestamos.entitys.Articulos;
import com.example.appprestamos.entitys.Categorias;
import com.example.appprestamos.entitys.Personas;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//@Database(
//        entities = {Categorias.class, Articulos.class},
//        version = 2, // para migración version 2, se cambió de 1 a 2
//        exportSchema = true
//)
@Database(
        entities = {Categorias.class, Articulos.class, Personas.class},
        version = 3, // para migración version 3, se cambió de 2 a 3
        exportSchema = true
)
public abstract class appDatabase extends RoomDatabase {
    public abstract categoriaDAO categoria_dao();
    public abstract articulosDAO articulos_dao();
    public abstract personaDAO personas_dao();
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
                    )
                            .addMigrations(Migraciones.MIGRATION_1_2, Migraciones.MIGRATION_2_3)
                            .build();
                }
            }

        }
        return INSTANCE;
    }
}
