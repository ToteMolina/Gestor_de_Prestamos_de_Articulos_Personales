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

import com.example.appprestamos.Adaptadores.CategoriaAdapter;
import com.example.appprestamos.Adaptadores.PersonaAdapter;
import com.example.appprestamos.AppDatabasePackage.appDatabase;
import com.example.appprestamos.entitys.Categorias;
import com.example.appprestamos.entitys.Personas;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PersonaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PersonaFragment extends Fragment {

    public RecyclerView rvPersonas;
    public PersonaAdapter adapter;

    private appDatabase db_conn;
    private FloatingActionButton btnInsertarPersona;
    private ActivityResultLauncher<Intent> launcherInsert;


    public PersonaFragment() {
        // Required empty public constructor
    }


    public static PersonaFragment newInstance() {
        PersonaFragment fragment = new PersonaFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        launcherInsert = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result-> {
            if(result.getResultCode() == Activity.RESULT_OK){
                Log.i("info", "EVENTO EJECUTAR");
                cargarListaPersonas();
            }

        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_persona, container, false);
        btnInsertarPersona = view.findViewById(R.id.btnInsertarPersona);
        rvPersonas = view.findViewById(R.id.rvPersonas);

        rvPersonas.setLayoutManager(new LinearLayoutManager(getContext()));

        db_conn = appDatabase.getInstance(getContext());

        cargarListaPersonas();
        btnInsertarPersona.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), InsertPersonaActivity.class);
                launcherInsert.launch(intent);
            }
        });

        return view;
        // Inflate the layout for this fragment

    }
    @Override
    public void onResume() {
        super.onResume();
        cargarListaPersonas();
    }

    private void cargarListaPersonas(){
        appDatabase.databaseWriteExecutor.execute(()->{
            List<Personas> listaPersonas = db_conn.personas_dao().getAllPersonas();

            Log.d("DEBUG_APP", "Personas encontradas en BD: " + listaPersonas.size());

            if (getActivity() != null){
                getActivity().runOnUiThread(()->{
                    adapter = new PersonaAdapter(getContext(), listaPersonas);
                    rvPersonas.setAdapter(adapter);
                });
            }
        });
    }
}