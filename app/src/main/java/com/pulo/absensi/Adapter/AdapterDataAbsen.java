package com.pulo.absensi.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pulo.absensi.Model.ModelDataAbsen;
import com.pulo.absensi.Model.ModelDataGuru;
import com.pulo.absensi.R;

import java.util.List;

public class AdapterDataAbsen extends RecyclerView.Adapter<AdapterDataAbsen.MyViewHolder> {

    private Context context;
    private List<ModelDataAbsen> list;
    private AdapterDataAbsen.Dialog dialog;


    public interface Dialog{
        void onClick(int pos);
    }
    public void setDialog(AdapterDataAbsen.Dialog dialog) {
        this.dialog = dialog;
    }

    public AdapterDataAbsen(Context context, List<ModelDataAbsen> list){
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public AdapterDataAbsen.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_data_absensi, parent, false);
        return new AdapterDataAbsen.MyViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull AdapterDataAbsen.MyViewHolder holder, int position) {
        holder.name.setText(list.get(position).getNama());
        holder.kelas.setText(list.get(position).getKelas());
        holder.matpel.setText(list.get(position).getMatpel());
        holder.ket.setText(list.get(position).getKet());
        holder.tgl.setText(list.get(position).getTgl());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView name, kelas,matpel,tgl,ket;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tbl_nama);
            ket = itemView.findViewById(R.id.tbl_ket);
            kelas = itemView.findViewById(R.id.tbl_kelas);
            matpel = itemView.findViewById(R.id.tbl_matpel);
            tgl = itemView.findViewById(R.id.tbl_tgl);
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
