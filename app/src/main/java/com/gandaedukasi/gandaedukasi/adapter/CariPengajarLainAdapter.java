package com.gandaedukasi.gandaedukasi.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gandaedukasi.gandaedukasi.CariPengajarCabangLainActivity;
import com.gandaedukasi.gandaedukasi.DetailPengajarActivity;
import com.gandaedukasi.gandaedukasi.R;
import com.gandaedukasi.gandaedukasi.models.CariPengajar;
import com.koushikdutta.ion.Ion;

import java.util.List;

/**
 * Created by Karen on 8/12/2016.
 */

public class CariPengajarLainAdapter extends RecyclerView.Adapter<CariPengajarLainAdapter.CariPengajarViewHolder> {

    public class CariPengajarViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView nama_pengajar;
        TextView label_mapel;
        TextView tempat;
        ImageView imageTeacher;

        public CariPengajarViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CariPengajar feedItem = items.get(getAdapterPosition());
                    Intent i = new Intent(mContext, DetailPengajarActivity.class);
                    i.putExtra("pengajar_id",feedItem.pengajar_id);
                    i.putExtra("mapel_id",feedItem.mapel_id);
                    mContext.startActivity(i);
                }
            });

            cv = (CardView)itemView.findViewById(R.id.cvCariPengajar);
            nama_pengajar = (TextView) itemView.findViewById(R.id.teacherName);
            label_mapel = (TextView) itemView.findViewById(R.id.tvLabelMapel);
            tempat = (TextView) itemView.findViewById(R.id.tvTempat);
            imageTeacher = (ImageView) itemView.findViewById(R.id.imageTeacher);

        }
    }

    List<CariPengajar> items;
    String mapel_id;
    private Context mContext;
    public CariPengajarLainAdapter(Context context, List<CariPengajar> items, String mapel_id){
        this.items = items;
        this.mContext = context;
        this.mapel_id = mapel_id;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public CariPengajarViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView;
        itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_cari_pengajar, viewGroup, false);

        return new CariPengajarViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CariPengajarViewHolder cariPengajarViewHolder, int i) {

        if(items.get(i).photo.equals("")){
            cariPengajarViewHolder.imageTeacher.setImageResource(R.drawable.guest);
        }else{
            Ion.with(mContext)
                    .load(items.get(i).photo)
                    .withBitmap()
                    .placeholder(R.drawable.guest)
                    .error(R.drawable.guest)
                    .intoImageView(cariPengajarViewHolder.imageTeacher);
        }
        cariPengajarViewHolder.nama_pengajar.setText(items.get(i).nama_pengajar);
        cariPengajarViewHolder.label_mapel.setText(items.get(i).label_mapel);
        cariPengajarViewHolder.tempat.setText(items.get(i).label_tempat);

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

}
