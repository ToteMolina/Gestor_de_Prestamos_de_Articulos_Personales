package com.example.appprestamos;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.appprestamos.Adaptadores.PersonaAdapter;
import com.example.appprestamos.AppDatabasePackage.appDatabase;
import com.example.appprestamos.entitys.Personas;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

public class InsertPersonaActivity extends AppCompatActivity {

    public appDatabase db_conn;
    public List<Personas> personasData;
    public PersonaAdapter adapter;
    public Personas personas;
    public TextInputEditText txtNombrePersona;
    public TextInputEditText txtNumeroContacto;
    public Button btnAgregarPersona;
    private int idPersonaRecibido = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_insert_persona);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        db_conn = appDatabase.getInstance(getApplicationContext());
        txtNombrePersona = findViewById(R.id.txtNombrePersona);
        txtNumeroContacto = findViewById(R.id.txtNumeroContacto);
        btnAgregarPersona = findViewById(R.id.btnAgregarPersona);

        Intent intentRecibido = getIntent();

        idPersonaRecibido = intentRecibido.getIntExtra("ID_PERSONA", -1);
        if(idPersonaRecibido!= -1){
            String nombreRecibido = intentRecibido.getStringExtra("NOMBRE");
            String numeroRecibido = intentRecibido.getStringExtra("NUMERO");
            txtNombrePersona.setText(nombreRecibido);
            txtNumeroContacto.setText(numeroRecibido);

            btnAgregarPersona.setText("Actualizar Persona");
        }
    }

    public void insertarPersona(View view) {
        String nombrePersonaIngresado = txtNombrePersona.getText().toString();
        String numeroContactoIngresado = txtNumeroContacto.getText().toString();



        appDatabase.databaseWriteExecutor.execute(() -> {

            if (idPersonaRecibido == -1) {
                // INSERTAR NUEVA
                if (nombrePersonaIngresado.isEmpty() || numeroContactoIngresado.isEmpty()){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(InsertPersonaActivity.this, "Ingresa el nombre y el numero de la persona", Toast.LENGTH_SHORT).show();
                        }
                    });
                    return;
                }
                else {
                    Personas nuevaPersona = new Personas(nombrePersonaIngresado, numeroContactoIngresado);
                    db_conn.personas_dao().insertarPersona(nuevaPersona);
                }

            } else {
                // ACTUALIZAR EXISTENTE
                if (nombrePersonaIngresado.isEmpty() || numeroContactoIngresado.isEmpty()){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(InsertPersonaActivity.this, "Ingresa el nombre y el numero de la persona", Toast.LENGTH_SHORT).show();

                        }
                    });
                    return;
                }
                Personas personaEditada = new Personas(nombrePersonaIngresado, numeroContactoIngresado);
                personaEditada.idPersona = idPersonaRecibido;
                db_conn.personas_dao().actualizarPersona(personaEditada);
            }

            runOnUiThread(() -> {
                setResult(Activity.RESULT_OK);
                finish();
            });
        });



    }
}