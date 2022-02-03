package com.pulo.absensi.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pulo.absensi.Model.ModelDataGuru;
import com.pulo.absensi.R;

import java.util.List;

public class AdapterDataGuru  extends RecyclerView.Adapter<AdapterDataGuru.MyViewHolder>{
    private Context context;
    private List<ModelDataGuru> list;
    private Dialog dialog;

    public interface Dialog{
        void onClick(int pos);
    }

    public void setDialog(Dialog dialog) {
        this.dialog = dialog;
    }

    public AdapterDataGuru(Context context, List<ModelDataGuru> list){
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_guru, parent, false);
        return new MyViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.name.setText(list.get(position).getNama());
        holder.nip.setText("NIP : "+list.get(position).getNip());
        holder.email.setText("Email : "+list.get(position).getEmail());
        holder.tlp.setText("Telepon : "+list.get(position).getTelepon());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView name, email,tlp,nip;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.list_nama);
            email = itemView.findViewById(R.id.list_email);
            tlp = itemView.findViewById(R.id.list_tlp);
            nip = itemView.findViewById(R.id.list_nip);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (dialog!=null){
                        dialog.onClick(getLayoutPosition());
                    }
                }
            });
        }
    }
}
