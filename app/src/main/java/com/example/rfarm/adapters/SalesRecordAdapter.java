package com.example.rfarm.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.rfarm.R;
import com.example.rfarm.constructors.ExpenseInfo;
import com.example.rfarm.constructors.Rabbit_Products;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SalesRecordAdapter extends RecyclerView.Adapter<SalesRecordAdapter.ViewHolder>{
    public interface OnItemClickListener {
        void onItemClick(Rabbit_Products item);
    }
    Context context;
    List<Rabbit_Products> MainImageUploadInfoList;
    private final SalesRecordAdapter.OnItemClickListener listener;
    public SalesRecordAdapter(Context context, List<Rabbit_Products> TempList, SalesRecordAdapter.OnItemClickListener listener) {

        this.MainImageUploadInfoList = TempList;

        this.context = context;
        this.listener = listener;
    }
    @Override
    public SalesRecordAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sales_cardview, parent, false);

        SalesRecordAdapter.ViewHolder viewHolder = new SalesRecordAdapter.ViewHolder(view);

        return viewHolder;
    }
    @Override
    public void onBindViewHolder(SalesRecordAdapter.ViewHolder holder, int position) {

        final Rabbit_Products UploadInfo = MainImageUploadInfoList.get(position);
        holder.bind(UploadInfo, listener);
        if(UploadInfo!=null){
            if(UploadInfo.getAmount()!=0){
                holder.pd_amount_name.setText(String.valueOf(UploadInfo.getAmount()));
            }
            if(UploadInfo.getDate_sold()!=null){
                //Loading image from Glide library.
                holder.pd_date_name.setText(UploadInfo.getDate_sold());
            }
            if(UploadInfo.getPrdct_type()!=null){
                holder.pd_type_name.setText(UploadInfo.getPrdct_type());
            }
            if(UploadInfo.getQty()!=0){
                holder.pd_qty.setText(String.valueOf(UploadInfo.getQty()));
            }
            if(UploadInfo.getImg_url()!=null)
            {
                //Loading image from Glide library.
                Picasso.get().load(UploadInfo.getImg_url()).placeholder(R.drawable.bunnies2).into(holder.pt_cat_img);

            }
            if(UploadInfo.getRabbit_tag()!=null){
                holder.pd_tag_name.setText(UploadInfo.getRabbit_tag());
            }

        }


    }
    @Override
    public int getItemCount() {

        return MainImageUploadInfoList.size();
    }
    class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView pt_cat_img;
        public TextView pd_tag_name,pd_date_name,pd_amount_name,pd_type_name,pd_qty;

        public ViewHolder(View itemView) {
            super(itemView);
            pt_cat_img=itemView.findViewById(R.id.imageView15);
            pd_type_name=itemView.findViewById(R.id.textView20);
            pd_date_name=itemView.findViewById(R.id.textView22);
            pd_tag_name=itemView.findViewById(R.id.textView24);
            pd_amount_name=itemView.findViewById(R.id.textView26);
            pd_qty=itemView.findViewById(R.id.textView28);
        }

        public void bind(Rabbit_Products prdt, SalesRecordAdapter.OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    listener.onItemClick(prdt);
                }
            });
        }
    }



}
