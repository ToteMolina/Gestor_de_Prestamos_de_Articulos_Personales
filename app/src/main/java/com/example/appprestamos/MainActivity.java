package com.example.appprestamos;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.example.appprestamos.AppDatabasePackage.appDatabase;
import com.example.appprestamos.entitys.Categorias;
import com.example.appprestamos.entitys.Personas;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView navigationView;
    private appDatabase db_conn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        //prueba de base de datos
        db_conn = appDatabase.getInstance(getApplicationContext());
        appDatabase.databaseWriteExecutor.execute(()->{
           db_conn.categoria_dao().insertCategoria(new Categorias("Cargadores"));
           runOnUiThread(()->{
               Toast.makeText(this, "Datos Insertados", Toast.LENGTH_SHORT).show();
            });
       });

        navigationView = findViewById(R.id.menuBotton);
        loadFragment(new ArticulosFragment());

        navigationView.setOnItemSelectedListener(item -> {
            item.setCheckable(true);

            if (item.getItemId() == R.id.personas){
                Toast.makeText(this, "Personas", Toast.LENGTH_SHORT).show();
                loadFragment(new PersonaFragment());
                return true;
            } else if (item.getItemId() == R.id.articulos){
                loadFragment(new ArticulosFragment());
                return true;
            } else if (item.getItemId() == R.id.prestamos){
                loadFragment(new PrestamosFragment());
                return true;
            }
            return false;
        });
    }

    private void loadFragment(Fragment fragment){
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.containerMain, fragment)
                .commit();
    }
}