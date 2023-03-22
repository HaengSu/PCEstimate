package com.app.pcestimate.view.board;

import static java.sql.DriverManager.println;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.pcestimate.R;
import com.app.pcestimate.datamodel.PostDataModel;

import java.util.ArrayList;

public class AdapterBoard extends RecyclerView.Adapter<AdapterBoard.ViewHolderMainBoard> {
    private ArrayList<PostDataModel> pList;

    public AdapterBoard(ArrayList<PostDataModel> list) {
        this.pList = list;
    }

    @NonNull
    @Override
    public ViewHolderMainBoard onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_main_board_post, parent, false);
        return new ViewHolderMainBoard(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolderMainBoard holder, int position) {
        holder.title.setText(pList.get(position).getTitle());
        holder.replayCount.setText("["+pList.get(position).getReplies().size()+""+"]");
        holder.onItemClick();
    }

    @Override
    public int getItemCount() {
        return pList.size();
    }

    public void updatePostList(ArrayList<PostDataModel> list) {
        pList = list;
        notifyItemChanged(0,pList.size());
    }

    public void resetPostList (ArrayList<PostDataModel> list) {
        pList = list;
        notifyDataSetChanged();
    }

    public class ViewHolderMainBoard extends RecyclerView.ViewHolder {
        private TextView title;
        private TextView replayCount;

        public ViewHolderMainBoard(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tv_title_item_post);
            replayCount = itemView.findViewById(R.id.tv_re_count_item_post);

            onItemClick();
        }

        private void onItemClick() {
            itemView.setOnClickListener(v -> {
                Intent i = new Intent(itemView.getContext(), ActivityDetailPost.class);
                i.putExtra("PostInfo", pList.get(getAdapterPosition()));
                itemView.getContext().startActivity(i);
            });
        }


    }
}























