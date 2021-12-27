package com.example.rfarm.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.rfarm.R;
import com.example.rfarm.constructors.Rabbit_Products;
import com.example.rfarm.constructors.mating_records;
import com.squareup.picasso.Picasso;

import java.util.List;

public class BreedingHistoryAdapter extends RecyclerView.Adapter<BreedingHistoryAdapter.ViewHolder>{
public interface OnItemClickListener {
    void onItemClick(mating_records item);
}
    Context context;
    List<mating_records> MainImageUploadInfoList;
    private final BreedingHistoryAdapter.OnItemClickListener listener;
    public BreedingHistoryAdapter(Context context, List<mating_records> TempList, BreedingHistoryAdapter.OnItemClickListener listener) {

        this.MainImageUploadInfoList = TempList;

        this.context = context;
        this.listener = listener;
    }
    @Override
    public BreedingHistoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.breeding_history_card, parent, false);

        BreedingHistoryAdapter.ViewHolder viewHolder = new BreedingHistoryAdapter.ViewHolder(view);

        return viewHolder;
    }
    @Override
    public void onBindViewHolder(BreedingHistoryAdapter.ViewHolder holder, int position) {

        final mating_records UploadInfo = MainImageUploadInfoList.get(position);
        holder.bind(UploadInfo, listener);
        if(UploadInfo!=null){
            if(UploadInfo.getN_bunnies_alive()!=0){
                holder.no_alive.setText(holder.no_alive.getText().toString()+String.valueOf(UploadInfo.getN_bunnies_alive()));
            }
            if(UploadInfo.getBirth_date()!=null){
                //Loading image from Glide library.
                holder.date_b.setText(holder.date_b.getText().toString()+UploadInfo.getBirth_date());
            }
            if(UploadInfo.getBuck_nm()!=null){
                holder.buck.setText(holder.buck.getText().toString()+UploadInfo.getBuck_nm());
            }
            if(UploadInfo.getDoe_nm()!=null){
                holder.doe.setText(holder.doe.getText().toString()+UploadInfo.getDoe_nm());
            }
            if(UploadInfo.getImage()!=null)
            {
                //Loading image from Glide library.
                Picasso.get().load(UploadInfo.getImage()).placeholder(R.drawable.bunny1).into(holder.doe_img);

            }
            if(UploadInfo.getN_bunnies_sold()!=0){
                holder.sold_bunnies.setText(holder.sold_bunnies.getText().toString()+UploadInfo.getN_bunnies_sold());
            }
            if(UploadInfo.getN_bunnies_died()!=0){
                holder.no_dead.setText(holder.no_dead.getText().toString()+String.valueOf(UploadInfo.getN_bunnies_died()));
            }
            if(UploadInfo.getN_bunnies_sold()!=0&&UploadInfo.getN_bunnies_alive()!=0){
                holder.no_bunnies.setText(holder.no_bunnies.getText().toString()+String.valueOf(UploadInfo.getN_bunnies_sold()+UploadInfo.getN_bunnies_alive()));
            }

        }


    }
    @Override
    public int getItemCount() {

        return MainImageUploadInfoList.size();
    }
class ViewHolder extends RecyclerView.ViewHolder {

    public ImageView doe_img;
    public TextView no_bunnies,no_alive,no_dead,buck,doe,date_b,sold_bunnies;

    public ViewHolder(View itemView) {
        super(itemView);
        doe_img=itemView.findViewById(R.id.imageView19);
        doe=itemView.findViewById(R.id.textView31);
        buck=itemView.findViewById(R.id.textView33);
        no_alive=itemView.findViewById(R.id.textView36);
        no_bunnies=itemView.findViewById(R.id.textView35);
        no_dead=itemView.findViewById(R.id.textView38);
        date_b=itemView.findViewById(R.id.textView34);
        sold_bunnies=itemView.findViewById(R.id.textView37);
    }

    public void bind(mating_records prdt, BreedingHistoryAdapter.OnItemClickListener listener) {
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                listener.onItemClick(prdt);
            }
        });
    }
}



}
