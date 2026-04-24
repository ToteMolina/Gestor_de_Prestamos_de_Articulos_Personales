package com.example.appprestamos.Adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appprestamos.R;
import com.example.appprestamos.entitys.Categorias;

import java.util.List;

public class CategoriaAdapter extends RecyclerView.Adapter<CategoriaAdapter.CategoriaVH> {
    public List<Categorias> dataCategorias;
    public Context context;
    public interface click_listener{
        void onItemClick(Categorias categorias);
    }

    public click_listener listener;

    public CategoriaAdapter(Context context, List<Categorias> dataCategorias, click_listener click) {
        this.dataCategorias = dataCategorias;
        this.context = context;
        this.listener = click;
    }

    @NonNull
    @Override
    public CategoriaAdapter.CategoriaVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_categorias, parent, false);
        return new CategoriaVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoriaAdapter.CategoriaVH holder, int position) {

        Categorias categorias = dataCategorias.get(position);

        holder.txtNombreCategoria.setText(categorias.nombreCategoria);

    }

    @Override
    public int getItemCount() {
        return dataCategorias.size();
    }

    public class CategoriaVH extends RecyclerView.ViewHolder {
        public TextView txtNombreCategoria;
        public CategoriaVH(@NonNull View itemView) {
            super(itemView);
            txtNombreCategoria = itemView.findViewById(R.id.txtNombreCategoria);
            itemView.setOnClickListener(v->{
                if (listener != null && getAdapterPosition() != RecyclerView.NO_POSITION){
                    listener.onItemClick(dataCategorias.get(getAdapterPosition()));
                }
            });
        }
    }
}
