package com.example.appprestamos;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ArticulosFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ArticulosFragment extends Fragment {
    private FloatingActionButton btnInsertar;

    private ActivityResultLauncher<Intent> launcherInsert;


    public static ArticulosFragment newInstance(String param1, String param2) {
        ArticulosFragment fragment = new ArticulosFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        launcherInsert = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result->{
            if (result.getResultCode() == Activity.RESULT_OK){
                Log.i("info", "EVENTO EJECUTAR");
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_articulos, container, false);
        btnInsertar = view.findViewById(R.id.btnInsertar);
        btnInsertar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), InsertProductoActivity.class);
                launcherInsert.launch(intent);
            }
        });
        return view;
    }
}