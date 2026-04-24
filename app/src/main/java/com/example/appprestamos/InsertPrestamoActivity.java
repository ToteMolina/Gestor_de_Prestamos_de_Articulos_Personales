package com.example.appprestamos;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.appprestamos.AppDatabasePackage.appDatabase;
import com.example.appprestamos.entitys.Articulos;
import com.example.appprestamos.entitys.Personas;
import com.example.appprestamos.entitys.Prestamos;
import com.google.android.material.textfield.TextInputEditText;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class InsertPrestamoActivity extends AppCompatActivity {
    private Spinner spArticulos, spPersonas;
    private TextInputEditText txtFechaPrestamo, txtFechaDevo;
    private appDatabase db_conn;
    private List<Articulos> listaArticulos;
    private List<Personas> listaPersonas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_insert_prestamo);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        spArticulos = findViewById(R.id.spArticulos);
        spPersonas = findViewById(R.id.spPeronas);
        txtFechaPrestamo = findViewById(R.id.txtFechaPrestamo);
        txtFechaDevo = findViewById(R.id.txtFechaDevo);

        txtFechaPrestamo.setOnClickListener(v->{
            mostrarCalendario(txtFechaPrestamo);
        });
        txtFechaDevo.setOnClickListener(v->{
            mostrarCalendario(txtFechaDevo);
        });

        db_conn = appDatabase.getInstance(getApplicationContext());

        cargarDatosSpinners();
    }

    private void mostrarCalendario(TextInputEditText editText){
        // obtenemos la fecha actual para que el calendario empiece con la fecha de hoy
        Calendar calendario = Calendar.getInstance();
        int anio = calendario.get(Calendar.YEAR);
        int mes = calendario.get(Calendar.MONTH);
        int dia = calendario.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dpd = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            String fechaSeleccionada = String.format(Locale.getDefault(), "%02d/%02d/%04d", dayOfMonth, (month+1), year);
            editText.setText(fechaSeleccionada);
        }, anio, mes, dia);
        dpd.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        dpd.show();
    }

    private void cargarDatosSpinners() {
        appDatabase.databaseWriteExecutor.execute(()->{
            listaArticulos = db_conn.articulos_dao().getArticulosDisponibles();
            listaPersonas = db_conn.personas_dao().getAllPersonas();

            Articulos articuloDefault = new Articulos();
            articuloDefault.idArticulos = -1;
            articuloDefault.nombre = "--- Seleccione un artículo ---";
            listaArticulos.add(0, articuloDefault);

            Personas personaDefault = new Personas();
            personaDefault.idPersona = -1;
            personaDefault.nombrePersona = "--- Seleccione una persona ---";
            personaDefault.numeroContacto = "00000000";
            listaPersonas.add(0, personaDefault);

            runOnUiThread(()->{
                // si la lista sólo tiene tamaño 1, significa que está en seleccione un articulo
                if (listaArticulos.size() == 1){
                    Toast.makeText(this, "No hay artículos disponibles para prestar", Toast.LENGTH_SHORT).show();
                    findViewById(R.id.btnGuardarPrestamo).setEnabled(false);
                }

                ArrayAdapter<Articulos> adapterArticulos = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, listaArticulos);
                spArticulos.setAdapter(adapterArticulos);
                ArrayAdapter<Personas> adapterPersonas = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, listaPersonas);
                spPersonas.setAdapter(adapterPersonas);
            });
        });
    }

    public void insertarPrestamo(View view){
//        if (spArticulos.getSelectedItem() == null || spPersonas.getSelectedItem() == null) {
//            Toast.makeText(this, "Debes registrar artículos y personas primero", Toast.LENGTH_SHORT).show();
//            return;
//        }

        if (spArticulos.getSelectedItemPosition() == 0){
            Toast.makeText(this, "Por favor seleccione un artículo de la lista", Toast.LENGTH_SHORT).show();
            return;
        }
        if (spPersonas.getSelectedItemPosition() == 0){
            Toast.makeText(this, "Por favor seleccione la persona que recibe el préstamo", Toast.LENGTH_SHORT).show();
        }

        String fechaInicioStr = txtFechaPrestamo.getText().toString();
        String fechaDevoStr = txtFechaDevo.getText().toString();

        if (fechaInicioStr.isEmpty() || fechaDevoStr.isEmpty()) {
            Toast.makeText(this, "Por favor ingrese las fechas", Toast.LENGTH_SHORT).show();
            return;
        }

        // convertir los textos a objeto date de java
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        Date dateInicio = null;
        Date dateDevo = null;

        try {
            dateInicio = sdf.parse(fechaInicioStr);
            dateDevo = sdf.parse(fechaDevoStr);
        } catch (ParseException e){
            Toast.makeText(this, "Formato de fecha incorrecto. Use DD/MM/AAAA", Toast.LENGTH_SHORT).show();
            return;
        }

        if (dateInicio.after(dateDevo) || dateInicio.equals(dateDevo)){
            Toast.makeText(this, "Error: El préstamo debe durar al menos hasta el día siguiente", Toast.LENGTH_SHORT).show();
            return;
        }

        Articulos articuloSeleccionado = (Articulos) spArticulos.getSelectedItem();
        Personas personaSeleccionada = (Personas) spPersonas.getSelectedItem();

        // variables finales para poder usarlas dentro del hilo en segundo plano
        final Date finalDateInicio = dateInicio;
        final Date finalDateDevo = dateDevo;

        appDatabase.databaseWriteExecutor.execute(()->{
            Prestamos nuevoPrestamo = new Prestamos();
            nuevoPrestamo.idArticulos = articuloSeleccionado.idArticulos;
            nuevoPrestamo.idPersona = personaSeleccionada.idPersona;
            nuevoPrestamo.fechaPrestamo = finalDateInicio;
            nuevoPrestamo.fechaDevoEstimada = finalDateDevo;
            nuevoPrestamo.devuelto = false;

            db_conn.prestamo_dao().insertPrestamo(nuevoPrestamo);
            db_conn.articulos_dao().actualizarEstado(articuloSeleccionado.idArticulos, "Prestado");

            runOnUiThread(()->{
                Toast.makeText(this, "Préstamo Registrado con Éxito", Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK);
                finish();
            });
        });
    }
}