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
}
