package com.example.appprestamos.Adaptadores;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appprestamos.R;
import com.example.appprestamos.entitys.Articulos;

import java.util.List;

public class ArticuloAdapter extends RecyclerView.Adapter<ArticuloAdapter.ArticuloVH> {
    public List<Articulos> dataArticulos;
    public Context context;

    public ArticuloAdapter(Context context, List<Articulos> dataArticulos) {
        this.dataArticulos = dataArticulos;
        this.context = context;
    }

    @NonNull
    @Override
    public ArticuloAdapter.ArticuloVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_articulo, parent, false);
        return new ArticuloVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ArticuloAdapter.ArticuloVH holder, int position) {
        Articulos articulo = dataArticulos.get(position);

        holder.tvNombreArticulo.setText(articulo.nombre);
        holder.tvDescripcion.setText(articulo.descripcion);
        if(articulo.estado.equals("Disponible"))
        {
            holder.tvEstado.setTextColor(Color.parseColor("#00b300"));
            holder.tvEstado.setText(articulo.estado);
        }
        else if(articulo.estado.equals("Prestado")){
            holder.tvEstado.setTextColor(Color.parseColor("#cc0022"));
            holder.tvEstado.setText(articulo.estado);
        }

    }

    @Override
    public int getItemCount() {
        return dataArticulos.size();
    }

    public class ArticuloVH extends RecyclerView.ViewHolder {
        public TextView tvNombreArticulo;
        public TextView tvDescripcion;
        public TextView tvEstado;
        public ArticuloVH(@NonNull View itemView) {
            super(itemView);
            tvNombreArticulo = itemView.findViewById(R.id.tvNombreArticulo);
            tvDescripcion = itemView.findViewById(R.id.tvDescripcion);
            tvEstado = itemView.findViewById(R.id.txtEstadoArticulo);
        }
    }
}
