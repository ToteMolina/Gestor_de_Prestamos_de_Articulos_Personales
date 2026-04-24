package com.example.appprestamos.Adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appprestamos.R;
import com.example.appprestamos.entitys.Articulos;
import com.example.appprestamos.entitys.Personas;
import com.example.appprestamos.entitys.Prestamos;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class PrestamoAdapter extends RecyclerView.Adapter<PrestamoAdapter.PrestamoVH> {
    private List<Prestamos> dataPrestamos;
    private List<Articulos> dataArticulos;
    private List<Personas> dataPersonas;
    private Context context;
    private SimpleDateFormat sdf;
    public PrestamoAdapter(Context context, List<Prestamos> dataPrestamos, List<Articulos> dataArticulos, List<Personas> dataPersonas) {
        this.context = context;
        this.dataPrestamos = dataPrestamos;
        this.dataArticulos = dataArticulos;
        this.dataPersonas = dataPersonas;
        this.sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
    }
    @NonNull
    @Override
    public PrestamoAdapter.PrestamoVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_prestamo, parent, false);
        return new PrestamoVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PrestamoAdapter.PrestamoVH holder, int position) {
        Prestamos prestamo = dataPrestamos.get(position);

        // buscar el nombre del artículo por su id
        String nombreArticulo = "Desconocido";
        for (Articulos art : dataArticulos) {
            if (art.idArticulos == prestamo.idArticulos) {
                nombreArticulo = art.nombre;
                break;
            }
        }

        // buscar el nombre de la persona por su id
        String nombrePersona = "Desconocido";
        for (Personas per : dataPersonas) {
            if (per.idPersona == prestamo.idPersona) {
                nombrePersona = per.nombrePersona;
                break;
            }
        }

        // mostrar los datos en la tarjeta
        holder.tvArticulo.setText(nombreArticulo);
        holder.tvPersona.setText("Prestado a: " + nombrePersona);

        if (prestamo.fechaDevoEstimada != null) {
            holder.tvFechaDevo.setText("Devolución: " + sdf.format(prestamo.fechaDevoEstimada));
        } else {
            holder.tvFechaDevo.setText("Devolución: No definida");
        }
    }

    @Override
    public int getItemCount() {
        return dataPrestamos.size();
    }

    public class PrestamoVH extends RecyclerView.ViewHolder {
        public TextView tvArticulo, tvPersona, tvFechaDevo;

        public PrestamoVH(@NonNull View itemView) {
            super(itemView);
            tvArticulo = itemView.findViewById(R.id.tvArticulo);
            tvPersona = itemView.findViewById(R.id.tvPersona);
            tvFechaDevo = itemView.findViewById(R.id.tvFechaDevo);
        }
    }
}
