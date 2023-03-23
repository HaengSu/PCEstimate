package com.app.pcestimate.view.board;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.pcestimate.R;

import java.util.ArrayList;

public class AdapterReplay extends RecyclerView.Adapter<AdapterReplay.ViewHolderReplay> {
    private ArrayList<String> rList;
    private OnItemClick callback;

    public AdapterReplay(ArrayList<String> list) {
        this.rList = list;
    }

    @NonNull
    @Override
    public ViewHolderReplay onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_reply, parent, false);
        return new ViewHolderReplay(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolderReplay holder, int position) {
        holder.replyContent.setText(rList.get(position));
    }

    @Override
    public int getItemCount() {
        return rList.size();
    }

    public void updateReplyList(ArrayList<String> list) {
        rList = list;
        notifyItemChanged(0, rList.size());
    }

    public void resetReplyList(ArrayList<String> list) {
        rList = list;
        notifyDataSetChanged();
    }

    public class ViewHolderReplay extends RecyclerView.ViewHolder {
        private TextView replyContent;
        private ImageView cancel;

        public ViewHolderReplay(@NonNull View itemView) {
            super(itemView);

            replyContent = itemView.findViewById(R.id.tv_reply);
            cancel = itemView.findViewById(R.id.im_delete_reply);

            onItemClick();
        }

        private void onItemClick() {
            cancel.setOnClickListener(v -> {
                callback.clickDelete(rList.get(getAdapterPosition()));
            });
        }
    }

    public void onItemClickListener(OnItemClick cb) {
        this.callback = cb;
    }

    interface OnItemClick {
        void clickDelete(String reply);
    }
}























