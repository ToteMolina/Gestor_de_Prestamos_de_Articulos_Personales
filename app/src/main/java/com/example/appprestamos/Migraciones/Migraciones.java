package com.example.appprestamos.Migraciones;

import androidx.annotation.NonNull;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

public class Migraciones {
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
    // para migración 5, agregando columna de estado a tabla de articulos
    public static final Migration MIGRATION_4_5 = new Migration(4, 5) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase db) {
            db.execSQL("ALTER TABLE articulos ADD COLUMN estado TEXT DEFAULT 'Disponible'");
        }
    };
}
