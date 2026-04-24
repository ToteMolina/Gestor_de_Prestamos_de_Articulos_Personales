package com.example.appprestamos.Adaptadores;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appprestamos.AppDatabasePackage.appDatabase;
import com.example.appprestamos.InsertPersonaActivity;
import com.example.appprestamos.R;
import com.example.appprestamos.entitys.Personas;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

public class PersonaAdapter extends RecyclerView.Adapter<PersonaAdapter.PersonaVH> {
    public List<Personas> dataPersonas;
    public Context context;

    public PersonaAdapter(Context context, List<Personas> dataPersonas) {
        this.context = context;
        this.dataPersonas = dataPersonas;
    }

    @NonNull
    @Override
    public PersonaAdapter.PersonaVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_persona, parent, false);
        return new PersonaVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PersonaAdapter.PersonaVH holder, int position) {
        Personas personas = dataPersonas.get(position);

        holder.txtNombrePersona.setText(personas.nombrePersona);
        holder.txtNumeroContacto.setText(personas.numeroContacto);


        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(holder.itemView.getContext(), InsertPersonaActivity.class);
            intent.putExtra("ID_PERSONA", personas.idPersona);
            intent.putExtra("NOMBRE", personas.nombrePersona);
            intent.putExtra("NUMERO", personas.numeroContacto);
            holder.itemView.getContext().startActivity(intent);
        });

        holder.itemView.setOnLongClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Eliminar contacto")
                    .setMessage("¿Estás seguro de eliminar a " + personas.nombrePersona + "?")
                    .setNegativeButton("Cancelar", null)
                    .setPositiveButton("Eliminar", (dialog, which) -> {


                        appDatabase.databaseWriteExecutor.execute(() -> {
                            try {

                                appDatabase.getInstance(context).personas_dao().eliminarPersona(personas);


                                ((Activity) context).runOnUiThread(() -> {
                                    dataPersonas.remove(position);
                                    notifyItemRemoved(position);
                                    android.widget.Toast.makeText(context, "Persona eliminada", android.widget.Toast.LENGTH_SHORT).show();
                                });

                            } catch (Exception e) {

                                ((Activity) context).runOnUiThread(() -> {
                                    new AlertDialog.Builder(context)
                                            .setTitle("Error al eliminar")
                                            .setMessage("No puedes eliminar a esta persona porque tiene préstamos registrados.")
                                            .setPositiveButton("Entendido", null)
                                            .show();
                                });
                            }
                        });
                    })
                    .show();
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return dataPersonas.size();
    }

    public class PersonaVH extends RecyclerView.ViewHolder {
        public TextView txtNombrePersona;
        public TextView txtNumeroContacto;
        public PersonaVH(@NonNull View itemView) {
            super(itemView);
            txtNombrePersona = itemView.findViewById(R.id.txtNamePerson);
            txtNumeroContacto = itemView.findViewById(R.id.txtnumero);
        }
    }
}
