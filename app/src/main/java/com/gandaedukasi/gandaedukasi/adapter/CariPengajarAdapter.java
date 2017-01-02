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

public class CariPengajarAdapter extends RecyclerView.Adapter<CariPengajarAdapter.CariPengajarViewHolder> {

    public class CariPengajarViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView nama_pengajar;
        TextView label_mapel;
        TextView tempat;
        ImageView imageTeacher;
        Button btnCari;

        public CariPengajarViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(items.size() == getAdapterPosition()){

                    }else{
                        CariPengajar feedItem = items.get(getAdapterPosition());
                        Intent i = new Intent(mContext, DetailPengajarActivity.class);
                        i.putExtra("pengajar_id",feedItem.pengajar_id);
                        i.putExtra("mapel_id",feedItem.mapel_id);
                        mContext.startActivity(i);
                    }
                }
            });

            cv = (CardView)itemView.findViewById(R.id.cvCariPengajar);
            nama_pengajar = (TextView) itemView.findViewById(R.id.teacherName);
            label_mapel = (TextView) itemView.findViewById(R.id.tvLabelMapel);
            tempat = (TextView) itemView.findViewById(R.id.tvTempat);
            imageTeacher = (ImageView) itemView.findViewById(R.id.imageTeacher);
            btnCari = (Button) itemView.findViewById(R.id.btnCari);

        }
    }

    List<CariPengajar> items;
    String mapel_id;
    private Context mContext;
    private static int VIEW_TYPE_FOOTER = 2;
    private static int VIEW_TYPE_CELL = 1;
    public CariPengajarAdapter(Context context, List<CariPengajar> items, String mapel_id){
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
        if (viewType == VIEW_TYPE_CELL) {
            itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_cari_pengajar, viewGroup, false);
        }
        else {
            itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_button_cari_pengajar, viewGroup, false);
        }

        return new CariPengajarViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CariPengajarViewHolder cariPengajarViewHolder, int i) {
        if(i == items.size()) {
                //click for button
            cariPengajarViewHolder.btnCari.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toast.makeText(mContext, "Button Clicked, Mapel ID "+mapel_id, Toast.LENGTH_LONG).show();
                    Intent i = new Intent(mContext, CariPengajarCabangLainActivity.class);
                    i.putExtra("mapel_id",mapel_id);
                    mContext.startActivity(i);
                }
            });
        }else{
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

    }

    @Override
    public int getItemViewType(int position) {
        return (position == items.size()) ? VIEW_TYPE_FOOTER : VIEW_TYPE_CELL;
    }

    @Override
    public int getItemCount() {
        return items.size()+1;
    }

}
