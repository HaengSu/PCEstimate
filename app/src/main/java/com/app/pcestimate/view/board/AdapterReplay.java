package com.app.pcestimate.view.board;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.pcestimate.R;
import com.app.pcestimate.datamodel.Replies;
import com.google.firebase.firestore.model.ObjectValue;

import java.util.ArrayList;
import java.util.HashMap;

public class AdapterReplay extends RecyclerView.Adapter<AdapterReplay.ViewHolderReplay> {
    private ArrayList<Replies> rList;
    private OnItemClick callback;

    public AdapterReplay(ArrayList<Replies> list) {
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
        Object a = rList.get(position);
        HashMap<String,String> h = (HashMap<String, String>) a;
        h.get("reply");
        holder.replyContent.setText(h.get("reply"));
    }

    @Override
    public int getItemCount() {
        return rList.size();
    }

    public void updateReplyList(ArrayList<Replies> list) {
        rList = list;
        notifyDataSetChanged();
    }

    public void resetReplyList(ArrayList<Replies> list) {
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
                Object a = rList.get(getAdapterPosition());
                HashMap<String,String> h = (HashMap<String, String>) a;
                callback.clickDelete(h.get("reply"),getAdapterPosition());
            });
        }
    }

    public void onItemClickListener(OnItemClick cb) {
        this.callback = cb;
    }

    interface OnItemClick {
        void clickDelete(String reply,int position);
    }
}























