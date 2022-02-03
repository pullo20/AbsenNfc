package com.pulo.absensi.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.pulo.absensi.Model.ModelDataSiswa;
import com.pulo.absensi.R;

import java.util.List;

public class AdapterDataSiswa extends RecyclerView.Adapter<AdapterDataSiswa.MyViewHolder>{

    private final Context context;
    private final List<ModelDataSiswa> list;
    private AdapterDataGuru.Dialog dialog;

    public interface Dialog{
        void onClick(int pos);
    }

    public void setDialog(AdapterDataGuru.Dialog dialog) {
        this.dialog = dialog;
    }

    public AdapterDataSiswa(Context context, List<ModelDataSiswa> list){
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public AdapterDataSiswa.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_siswa, parent, false);
        return new AdapterDataSiswa.MyViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull AdapterDataSiswa.MyViewHolder holder, int position) {
        holder.name.setText(list.get(position).getNama());
        holder.stb.setText("NIS : "+list.get(position).getStb());
        holder.kelas.setText("Kelas : "+list.get(position).getKelas());
        holder.jkl.setText("Jenis Kelamin : "+list.get(position).getJkl());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView name,kelas,jkl,stb;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.list_nama);
            kelas = itemView.findViewById(R.id.list_kelas);
            jkl = itemView.findViewById(R.id.list_jkl);
            stb = itemView.findViewById(R.id.list_stb);
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
