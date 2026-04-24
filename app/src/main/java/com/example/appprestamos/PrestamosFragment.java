package com.example.appprestamos;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.appprestamos.Adaptadores.PrestamoAdapter;
import com.example.appprestamos.AppDatabasePackage.appDatabase;
import com.example.appprestamos.entitys.Articulos;
import com.example.appprestamos.entitys.Personas;
import com.example.appprestamos.entitys.Prestamos;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;


public class PrestamosFragment extends Fragment {
    private RecyclerView rvPrestamos;
    private Button btnVerActivos, btnVerHistorial;
    private FloatingActionButton btnNuevoPrestamo;
    private appDatabase db_conn;
    private PrestamoAdapter adapter;
    private ActivityResultLauncher<Intent> launcherInsert;
    private boolean viendoActivos = true; // controla qué lista estamos viendo

    public PrestamosFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        launcherInsert = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), result -> {
                    if (result.getResultCode() == Activity.RESULT_OK){
                        cargarLista();
                    }
                });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_prestamos, container, false);

        rvPrestamos = view.findViewById(R.id.rvPrestamos);
        btnVerActivos = view.findViewById(R.id.btnVerActivos);
        btnVerHistorial = view.findViewById(R.id.btnVerHistorial);
        btnNuevoPrestamo = view.findViewById(R.id.btnNuevoPrestamo);

        rvPrestamos.setLayoutManager(new LinearLayoutManager(getContext()));
        db_conn = appDatabase.getInstance(getContext());

        btnVerActivos.setOnClickListener(v->{
            viendoActivos = true;
            actualizarColoresBotones();
            cargarLista();
        });

        btnVerHistorial.setOnClickListener(v->{
            viendoActivos = false;
            actualizarColoresBotones();
            cargarLista();
        });

        btnNuevoPrestamo.setOnClickListener(v->{
            Intent intent = new Intent(getContext(), InsertPrestamoActivity.class);
            launcherInsert.launch(intent);
        });

        cargarLista();
        return view;
    }

    private void actualizarColoresBotones(){
        if (viendoActivos){
            btnVerActivos.setBackgroundColor(Color.parseColor("#FF6A3D"));
            btnVerHistorial.setBackgroundColor(Color.parseColor("#7A7A7A"));
        } else {
            btnVerActivos.setBackgroundColor(Color.parseColor("#7A7A7A"));
            btnVerHistorial.setBackgroundColor(Color.parseColor("#FF6A3D"));
        }
    }

    private void cargarLista(){
        appDatabase.databaseWriteExecutor.execute(()->{
            List<Prestamos> listaPrestamos;
            if (viendoActivos){
                listaPrestamos = db_conn.prestamo_dao().getPrestamosActivos();
            } else {
                listaPrestamos = db_conn.prestamo_dao().getHistorialPrestamos();
            }

            List<Articulos> listaArticulos = db_conn.articulos_dao().getAllArticulos();
            List<Personas> listaPersonas = db_conn.personas_dao().getAllPersonas();

            if (getActivity() != null){
                getActivity().runOnUiThread(()->{
                    adapter = new PrestamoAdapter(
                            getContext(),
                            listaPrestamos,
                            listaArticulos,
                            listaPersonas,
                            prestamoClickeado ->{
                                new AlertDialog.Builder(getContext())
                                        .setTitle("Finalizar préstamo")
                                        .setMessage("¿Seguro que deseas marcar este artículo como devuelto? Esta acción lo moverá al historial")
                                        .setPositiveButton("Sí, devolver", (dialog, which) -> {
                                            appDatabase.databaseWriteExecutor.execute(()->{
                                                prestamoClickeado.devuelto = true;
                                                db_conn.prestamo_dao().updatePrestamo(prestamoClickeado);

                                                db_conn.articulos_dao().actualizarEstado(prestamoClickeado.idArticulos, "Disponible");

                                                getActivity().runOnUiThread(()->{
                                                    cargarLista();
                                                    Toast.makeText(getContext(), "Préstamo finalizado", Toast.LENGTH_SHORT).show();
                                                });
                                            });
                                        })
                                        .setNegativeButton("Cancelar", (dialog, which) -> {
                                            dialog.dismiss();
                                        })
                                        .show();
                            });
                    rvPrestamos.setAdapter(adapter);
                });
            }
        });
    }
}