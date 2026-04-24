package com.example.appprestamos.AppDatabasePackage;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.appprestamos.Conversiones.Converters;
import com.example.appprestamos.DAOS.articulosDAO;
import com.example.appprestamos.DAOS.categoriaDAO;
import com.example.appprestamos.DAOS.personaDAO;
import com.example.appprestamos.DAOS.prestamoDAO;
import com.example.appprestamos.entitys.Articulos;
import com.example.appprestamos.entitys.Categorias;
import com.example.appprestamos.entitys.Personas;
import com.example.appprestamos.entitys.Prestamos;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//@Database(
//        entities = {Categorias.class, Articulos.class},
//        version = 2, // para migración version 2, se cambió de 1 a 2
//        exportSchema = true
//)
//@Database(
//        entities = {Categorias.class, Articulos.class, Personas.class},
//        version = 3, // para migración version 3, se cambió de 2 a 3
//        exportSchema = true
//)
@Database(
        entities = {Categorias.class, Articulos.class, Personas.class, Prestamos.class},
        version = 4,
        exportSchema = true
)
@TypeConverters({Converters.class}) // <- le decimos a Room que use nuestro converter
public abstract class appDatabase extends RoomDatabase {
    public abstract categoriaDAO categoria_dao();
    public abstract articulosDAO articulos_dao();
    public abstract personaDAO personas_dao();
    public abstract prestamoDAO prestamo_dao();

    private static volatile appDatabase INSTANCE;
    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(4);


    // para migración version 2, para agregar la columna nombre
    public static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase db) {
            db.execSQL("ALTER TABLE articulos ADD COLUMN nombre TEXT");
        }
    };
        //para migracion 3, para agregar tabla de personas
    public static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase db) {
            db.execSQL("CREATE TABLE IF NOT EXISTS `personas` (`idPersona` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    "`nombrePersona` TEXT, `numeroContacto` TEXT)");
        }
    };

    // para migración 4, agregando tabla de prestamos
    public static final Migration MIGRATION_3_4 = new Migration(3, 4) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase db) {
            db.execSQL("CREATE TABLE IF NOT EXISTS 'prestamos' (" +
                    "'idPrestamos' INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    "'idArticulos' INTEGER NOT NULL, " +
                    "'idPersona' INTEGER NOT NULL, " +
                    "'fechaPrestamo' INTEGER, " +
                    "'fechaDevoEstimada' INTEGER, " +
                    "'devuelto' INTEGER NOT NULL, " +
                    "FOREIGN KEY('idArticulos') REFERENCES 'articulos'('idArticulos') ON UPDATE NO ACTION ON DELETE RESTRICT , " +
                    "FOREIGN KEY(`idPersona`) REFERENCES `personas`(`idPersona`) ON UPDATE NO ACTION ON DELETE RESTRICT )");
        }
    };

    public static appDatabase getInstance(Context context){
        if (INSTANCE == null){
            synchronized (appDatabase.class){
                if (INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            appDatabase.class,
                            "db_prestamos"
                    )
                            .addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4)
                            .build();
                }
            }

        }
        return INSTANCE;
    }
}
