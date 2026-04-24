package com.example.appprestamos.Adaptadores;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appprestamos.R;
import com.example.appprestamos.entitys.Articulos;
import com.example.appprestamos.entitys.Personas;
import com.example.appprestamos.entitys.Prestamos;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PrestamoAdapter extends RecyclerView.Adapter<PrestamoAdapter.PrestamoVH> {
    private List<Prestamos> dataPrestamos;
    private List<Articulos> dataArticulos;
    private List<Personas> dataPersonas;
    private Context context;
    private SimpleDateFormat sdf;

    // creamos la interface para el listener (para escuchar el "click")
    public interface OnPrestamoClickListener{
        void OnDevolverClick(Prestamos prestamo);
    }
    private OnPrestamoClickListener listener;

    public PrestamoAdapter(Context context, List<Prestamos> dataPrestamos, List<Articulos> dataArticulos, List<Personas> dataPersonas, OnPrestamoClickListener listener) {
        this.context = context;
        this.dataPrestamos = dataPrestamos;
        this.dataArticulos = dataArticulos;
        this.dataPersonas = dataPersonas;
        this.listener = listener;
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

        holder.tvArticulo.setText(nombreArticulo);
        holder.tvPersona.setText("Prestado a: " + nombrePersona);

        // obtenemos la fecha exacta del día de hoy
        Date fechaHoy = new Date();

        if (prestamo.fechaDevoEstimada != null){
            // comparamos las fechas, si la fecha de hoy es después que la de la fecha de devolución estimada
            if (!prestamo.devuelto && fechaHoy.after(prestamo.fechaDevoEstimada)){
                // si es así, está vencida la fecha
                holder.tvFechaDevo.setText("¡ATRASADO! Debió devolver: " + sdf.format(prestamo.fechaDevoEstimada));
                holder.tvFechaDevo.setTextColor(context.getResources().getColor(R.color.loan_overdue));
                holder.tvFechaDevo.setTypeface(null, Typeface.BOLD);
            } else {
                holder.tvFechaDevo.setText("Devolución: " + sdf.format(prestamo.fechaDevoEstimada));
                holder.tvFechaDevo.setTextColor(context.getResources().getColor(R.color.state_info));
                holder.tvFechaDevo.setTypeface(null, Typeface.ITALIC);
            }
        } else {
            holder.tvFechaDevo.setText("Devolución: No definida");
        }

        if (prestamo.devuelto){
            // si ya está devuelto (en el historial), ocultamos el botón
            holder.btnDevolver.setVisibility(View.GONE);
        } else {
            holder.btnDevolver.setVisibility(View.VISIBLE);
            holder.btnDevolver.setOnClickListener(v->{
                if (listener != null) {
                    listener.OnDevolverClick(prestamo);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return dataPrestamos.size();
    }

    public class PrestamoVH extends RecyclerView.ViewHolder {
        public TextView tvArticulo, tvPersona, tvFechaDevo;
        public Button btnDevolver;

        public PrestamoVH(@NonNull View itemView) {
            super(itemView);
            tvArticulo = itemView.findViewById(R.id.tvArticulo);
            tvPersona = itemView.findViewById(R.id.tvPersona);
            tvFechaDevo = itemView.findViewById(R.id.tvFechaDevo);
            btnDevolver = itemView.findViewById(R.id.btnDevolver);
        }
    }
}
