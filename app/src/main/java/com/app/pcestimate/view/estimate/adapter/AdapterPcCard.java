package com.app.pcestimate.view.estimate.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.pcestimate.R;
import com.app.pcestimate.datamodel.PcDataModel;
import com.app.pcestimate.util.ImageUtils;

import java.util.ArrayList;

public class AdapterPcCard extends RecyclerView.Adapter<AdapterPcCard.PcViewHolder> {

    private ArrayList<PcDataModel> list = new ArrayList<>();

    @NonNull
    @Override
    public PcViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pc_card, parent, false);
        return new PcViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PcViewHolder holder, int position) {
        holder.onBind(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setList(ArrayList<PcDataModel> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    class PcViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageThumbnail;
        private TextView tvTitle;
        private TextView tvPriceHint;
        private TextView tvPrice;
        private TextView tvCate;

        public PcViewHolder(@NonNull View itemView) {
            super(itemView);

            imageThumbnail = (ImageView) itemView.findViewById(R.id.imageThumbnail);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            tvPriceHint = (TextView) itemView.findViewById(R.id.tvPriceHint);
            tvPrice = (TextView) itemView.findViewById(R.id.tvPrice);
            tvCate = (TextView) itemView.findViewById(R.id.tvCate);
        }

        void onBind(PcDataModel item) {
            // binding
            tvCate.setText(item.category);
            tvTitle.setText(item.title);
            tvPrice.setText(item.price);

            // image setting
            ImageUtils.loadImageWithUrl(itemView.getContext(), item.thumb, imageThumbnail);

        }
    }
}
