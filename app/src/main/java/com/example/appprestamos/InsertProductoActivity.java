package com.example.appprestamos;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appprestamos.Adaptadores.CategoriaAdapter;
import com.example.appprestamos.AppDatabasePackage.appDatabase;
import com.example.appprestamos.entitys.Articulos;
import com.example.appprestamos.entitys.Categorias;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

public class InsertProductoActivity extends AppCompatActivity {
    public appDatabase db_conn;
    public List<Categorias> categoriaData;
    public RecyclerView listCat;
    public CategoriaAdapter adapter;
    public Categorias categorias;
    public TextInputEditText txtDescripcionArticulo;
    public TextInputEditText txtNombreArticulo; // para migración version 2

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_insert_producto);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        listCat = findViewById(R.id.list_categorias);
        txtDescripcionArticulo = findViewById(R.id.txtDescripcion);
        txtNombreArticulo = findViewById(R.id.txtNombreArticulo); // para migración version 2

        db_conn = appDatabase.getInstance(getApplicationContext());
        appDatabase.databaseWriteExecutor.execute(()->{
            //db_conn.categoria_dao().insertCategoria(new Categorias("Cargadores"));
            categoriaData = db_conn.categoria_dao().getAllCategorias();

            runOnUiThread(()->{
                //Toast.makeText(this, "Datos Insertados", Toast.LENGTH_SHORT).show();
                adapter = new CategoriaAdapter(getApplicationContext(), categoriaData, categoria -> {
                    Toast.makeText(this, "Categoria Seleccionada: "+String.valueOf(categoria.nombreCategoria), Toast.LENGTH_SHORT).show();
                    categorias = categoria;
                });
                listCat.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
                listCat.setAdapter(adapter);
                for (Categorias item:categoriaData){
                    Log.d("consulta", item.nombreCategoria);
                }
            });
        });
    }

    public void insertArticulo(View view) {

        if(categorias == null){
            Toast.makeText(this, "Por favor, selecciona una categoría de la lista", Toast.LENGTH_SHORT).show();
            return;
        }

        appDatabase.databaseWriteExecutor.execute(()->{
            Articulos articulos = new Articulos();
            articulos.nombre = txtNombreArticulo.getText().toString(); // para migración version 2
            articulos.descripcion = txtDescripcionArticulo.getText().toString();
            articulos.idCategoria = categorias.idCategoria;

            db_conn.articulos_dao().insertArticulos(articulos);

            runOnUiThread(()->{
                Toast.makeText(this, "Dato Insertado", Toast.LENGTH_SHORT).show();
                setResult(Activity.RESULT_OK);
                finish();
            });
        });
    }
}