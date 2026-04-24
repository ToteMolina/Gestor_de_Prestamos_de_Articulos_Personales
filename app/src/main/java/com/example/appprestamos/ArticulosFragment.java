package com.example.appprestamos;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.appprestamos.Adaptadores.ArticuloAdapter;
import com.example.appprestamos.AppDatabasePackage.appDatabase;
import com.example.appprestamos.entitys.Articulos;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ArticulosFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ArticulosFragment extends Fragment {
    private FloatingActionButton btnInsertar;
    private ActivityResultLauncher<Intent> launcherInsert;

    private RecyclerView rvArticulos;
    private appDatabase db_conn;
    private ArticuloAdapter adapter;
    public static ArticulosFragment newInstance(String param1, String param2) {
        ArticulosFragment fragment = new ArticulosFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        launcherInsert = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result->{
            Log.i("info", "EVENTO EJECUTAR - ACTUALIZANDO LISTA");
            cargarListaArticulos();
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_articulos, container, false);

        btnInsertar = view.findViewById(R.id.btnInsertar);
        rvArticulos = view.findViewById(R.id.rvArticulos);

        // configuramos cómo se mostrará la lista (de arriba hacia abajo)
        rvArticulos.setLayoutManager(new LinearLayoutManager(getContext()));

        db_conn = appDatabase.getInstance(getContext());

        cargarListaArticulos();

        btnInsertar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), InsertProductoActivity.class);
                launcherInsert.launch(intent);
            }
        });
        return view;
    }

    // metodo para consultar la DB y llenar el RecyclerView
    private void cargarListaArticulos(){
        appDatabase.databaseWriteExecutor.execute(()->{
            List<Articulos> listaArticulos = db_conn.articulos_dao().getAllArticulos();

            Log.d("DEBUG_APP", "Artículos encontrados en BD: " + listaArticulos.size());

            if (getActivity() != null){
                getActivity().runOnUiThread(()->{
                    adapter = new ArticuloAdapter(getContext(), listaArticulos);
                    rvArticulos.setAdapter(adapter);
                });
            }
        });
    }
}