package com.example.rfarm.adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.rfarm.R;
import com.example.rfarm.constructors.Rabbit;
import com.example.rfarm.constructors.mating_records;
import com.squareup.picasso.Picasso;

import java.util.List;

public class bunniesAdapter extends RecyclerView.Adapter<bunniesAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(mating_records item);
    }

    Context context;
    List<mating_records> MainImageUploadInfoList;
    private final bunniesAdapter.OnItemClickListener listener;

    public bunniesAdapter(Context context, List<mating_records> TempList, bunniesAdapter.OnItemClickListener listener) {

        this.MainImageUploadInfoList = TempList;

        this.context = context;
        this.listener = listener;
    }

    @Override
    public bunniesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bunnies_card, parent, false);

        bunniesAdapter.ViewHolder viewHolder = new bunniesAdapter.ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(bunniesAdapter.ViewHolder holder, int position) {

        final mating_records UploadInfo = MainImageUploadInfoList.get(position);
        holder.bind(UploadInfo, listener);
        if(UploadInfo!=null){
            if(UploadInfo.getBirth_date()!=null){
                holder.birth_date.setText("Date Of Birth: "+UploadInfo.getBirth_date());
            }
            if(UploadInfo.getBuck_nm()!=null){
                //Loading image from Glide library.
                   holder.parent_buck_name.setText("Buck: "+UploadInfo.getBuck_nm());
            }
            if(UploadInfo.getDoe_nm()!=null){
                holder.parent_doe_name.setText("Doe: "+UploadInfo.getDoe_nm());
            }
            if(UploadInfo.getNumber_of_bunnies()!=0){
             holder.no_bunnies.setText("Number Of Bunnies: "+String.valueOf(UploadInfo.getNumber_of_bunnies()));
            }
            if(UploadInfo.getN_bunnies_alive()!=0){
            holder.no_alive_bunnies.setText("Alive: "+String.valueOf(UploadInfo.getN_bunnies_alive()));
            }
            if(UploadInfo.getN_bunnies_died()!=-1){
               holder.no_dead_bunnies.setText("Dead: "+String.valueOf(UploadInfo.getN_bunnies_died()));
            }
            if(UploadInfo.getImage()!=null){
                Picasso.get().load(UploadInfo.getImage()).placeholder(R.drawable.bunny1).into(holder.parent_doe);

            }
        }


    }

    @Override
    public int getItemCount() {

        return MainImageUploadInfoList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView parent_doe;
        public TextView parent_doe_name,parent_buck_name,no_bunnies,birth_date,no_alive_bunnies,no_dead_bunnies;

        public ViewHolder(View itemView) {
            super(itemView);
            parent_doe=itemView.findViewById(R.id.parent_doer);
           parent_doe_name=itemView.findViewById(R.id.parent_doer_name);
           parent_buck_name=itemView.findViewById(R.id.buck_name_birth);
           birth_date=itemView.findViewById(R.id.doer_brth_date);
           no_bunnies=itemView.findViewById(R.id.no_bunnies_birth);
           no_alive_bunnies=itemView.findViewById(R.id.alive_bunnies);
           no_dead_bunnies=itemView.findViewById(R.id.dead_bunnies);
        }

        public void bind(mating_records bunny, bunniesAdapter.OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    listener.onItemClick(bunny);
                }
            });
        }
    }
}