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

import com.gandaedukasi.gandaedukasi.DetailProgramEdukasiActivity;
import com.gandaedukasi.gandaedukasi.R;
import com.gandaedukasi.gandaedukasi.models.ProgramEdukasi;
import com.gandaedukasi.gandaedukasi.utility.RequestServer;
import com.koushikdutta.ion.Ion;

import java.util.List;

/**
 * Created by Karen on 8/22/2016.
 */

public class ProgramEdukasiAdapter extends RecyclerView.Adapter<ProgramEdukasiAdapter.ProgramEdukasiViewHolder> {

    public class ProgramEdukasiViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        Button name;

        public ProgramEdukasiViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cvJadwal);
            name = (Button) itemView.findViewById(R.id.btnName);

            name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final ProgramEdukasi feedItem = listItems.get(getAdapterPosition());
                    final int position = getAdapterPosition();
                    Intent i = new Intent(mContext, DetailProgramEdukasiActivity.class);
                    i.putExtra("id",feedItem.id);
                    i.putExtra("name",feedItem.name);
                    i.putExtra("biaya",feedItem.biaya);
                    i.putExtra("desk",feedItem.desk);
                    mContext.startActivity(i);
                }
            });
        }

    }

    List<ProgramEdukasi> listItems;
    private Context mContext;

    public ProgramEdukasiAdapter(Context context, List<ProgramEdukasi> listItems){
        this.listItems = listItems;
        this.mContext = context;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public ProgramEdukasiViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_program_edukasi, viewGroup, false);
        ProgramEdukasiViewHolder rjvh = new ProgramEdukasiViewHolder(v);
        return rjvh;
    }

    @Override
    public void onBindViewHolder(ProgramEdukasiViewHolder rogramEdukasiViewHolder, int i) {
        rogramEdukasiViewHolder.name.setText(listItems.get(i).name);

    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }
}
