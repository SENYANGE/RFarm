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
import com.squareup.picasso.Picasso;

import java.util.List;

public class HerdRecyclerViewAdapter extends RecyclerView.Adapter<HerdRecyclerViewAdapter.ViewHolder> {
    public interface OnItemClickListener {
        void onItemClick(Rabbit item);
    }

    final HerdRecyclerViewAdapter.OnItemClickListener listener;
    Context context;
    List<Rabbit> MainImageUploadInfoList;

    public HerdRecyclerViewAdapter(Context context, List<Rabbit> TempList, HerdRecyclerViewAdapter.OnItemClickListener listener) {

        this.MainImageUploadInfoList = TempList;
        this.context = context;
        this.listener=listener;
    }

    @Override
    public HerdRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.herd_row, parent, false);

        HerdRecyclerViewAdapter.ViewHolder viewHolder = new HerdRecyclerViewAdapter.ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(HerdRecyclerViewAdapter.ViewHolder holder, int position) {
      final Rabbit UploadInfo = MainImageUploadInfoList.get(position);
        holder.bind(UploadInfo, listener);
        //notifyItemRemoved(holder.getAdapterPosition());
        if(UploadInfo!=null){
          if(UploadInfo.getRabbit_tag()!=null){
              holder.imageNameTextView.setText(UploadInfo.getRabbit_tag());
          }
          if(UploadInfo.getImage()!=null){
              //Loading image from Glide library.
              Picasso.get().load(UploadInfo.getImage()).placeholder(R.drawable.bunnies2).into(holder.imageView);

          }
          if(UploadInfo.getBreed()!=null){
            holder.breedTextView.setText(UploadInfo.getBreed());
          }
          if(UploadInfo.getAlive()!=null){
              if(UploadInfo.getAlive().equals("true")){
                  holder.alive.setText("Alive");
                  holder.alive.setTextColor(Color.GREEN);
                  holder.alive.setButtonTintList(ColorStateList.valueOf(Color.GREEN));
                  holder.alive.setChecked(true);

              }else{
                  holder.alive.setText("Dead");
                  holder.alive.setTextColor(Color.RED);
                  holder.alive.setChecked(true);
                  holder.alive.setButtonTintList(ColorStateList.valueOf(Color.RED));
              }
          }
          if(UploadInfo.getSex()!=null){
              if(UploadInfo.getSex().equals("doer")){
                  holder.sex.setButtonTintList(ColorStateList.valueOf(Color.BLUE));
                  holder.sex.setTextColor(Color.BLUE);
                  holder.sex.setChecked(true);
                  holder.sex.setText("Doer");

              }else {
                  holder.sex.setButtonTintList(ColorStateList.valueOf(Color.BLACK));
                  holder.sex.setTextColor(Color.RED);
                  holder.sex.setText("Buck");
                  holder.sex.setChecked(true);
              }
          }
          if(UploadInfo.getSold()!=null){
              if(UploadInfo.getSold().equals("true")){
                  holder.sold.setText("Sold");
                  holder.sold.setTextColor(Color.GREEN);
                  holder.sold.setChecked(true);
                  holder.sold.setButtonTintList(ColorStateList.valueOf(Color.GREEN));
              }else{
                  holder.sold.setText("Available");
                  holder.sold.setTextColor(Color.YELLOW);
                  holder.sold.setChecked(true);
                  holder.sold.setButtonTintList(ColorStateList.valueOf(Color.YELLOW));

              }

          }
      }

    }

    @Override
    public int getItemCount() {

        return MainImageUploadInfoList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageView;
        public TextView imageNameTextView,breedTextView;
        public RadioButton sold,alive,sex;

        public ViewHolder(View itemView) {
            super(itemView);
            breedTextView=itemView.findViewById(R.id.card_breed);
            imageView = (ImageView) itemView.findViewById(R.id.rab_image_row);
            imageNameTextView = (TextView) itemView.findViewById(R.id.her_row_rabtag);
            sold=itemView.findViewById(R.id.radio_sold);
            sex=itemView.findViewById(R.id.radio_sex);
            alive=itemView.findViewById(R.id.alive_dead);

        }
        public void bind(Rabbit rabbit, HerdRecyclerViewAdapter.OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(rabbit);

                }
            });
        }
    }
}