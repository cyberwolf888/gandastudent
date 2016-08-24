package com.gandaedukasi.gandaedukasi.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.gandaedukasi.gandaedukasi.DetailPaymentActivity;
import com.gandaedukasi.gandaedukasi.R;
import com.gandaedukasi.gandaedukasi.models.Payment;

import java.util.List;

/**
 * Created by Karen on 8/24/2016.
 */

public class PaymentAdapter extends RecyclerView.Adapter<PaymentAdapter.PaymentViewHolder> {

    public class PaymentViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView tvTitle;
        TextView tvKeterangan;
        TextView tvPaymentNo;
        TextView tvCost;

        public PaymentViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cvPayment);
            tvTitle = (TextView)itemView.findViewById(R.id.tvTitle);
            tvKeterangan = (TextView)itemView.findViewById(R.id.tvKeterangan);
            tvPaymentNo = (TextView)itemView.findViewById(R.id.tvPaymentNo);
            tvCost = (TextView)itemView.findViewById(R.id.tvCost);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Payment feedItem = listItems.get(getAdapterPosition());
                    Intent i = new Intent(mContext, DetailPaymentActivity.class);
                    i.putExtra("id",feedItem.id);
                    i.putExtra("title",feedItem.title);
                    i.putExtra("keterangan",feedItem.keterangan);
                    i.putExtra("cost",feedItem.cost);
                    mContext.startActivity(i);
                }
            });

        }

    }

    List<Payment> listItems;
    private Context mContext;

    public PaymentAdapter(Context context, List<Payment> listItems){
        this.listItems = listItems;
        this.mContext = context;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public PaymentViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_payment, viewGroup, false);
        PaymentViewHolder rjvh = new PaymentViewHolder(v);
        return rjvh;
    }

    @Override
    public void onBindViewHolder(PaymentViewHolder paymentViewHolder, int i) {
        paymentViewHolder.tvTitle.setText(listItems.get(i).title);
        paymentViewHolder.tvKeterangan.setText(listItems.get(i).keterangan);
        paymentViewHolder.tvPaymentNo.setText("No. Pembayaran: "+listItems.get(i).id);
        paymentViewHolder.tvCost.setText("Rp. "+listItems.get(i).cost);

    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }
}