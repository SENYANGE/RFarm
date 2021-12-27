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
import com.squareup.picasso.Picasso;


import java.util.List;

public class ExpenseRecordAdapter extends RecyclerView.Adapter<ExpenseRecordAdapter.ViewHolder>{
    public interface OnItemClickListener {
        void onItemClick(ExpenseInfo item);
    }
    Context context;
    List<ExpenseInfo> MainImageUploadInfoList;
    private final ExpenseRecordAdapter.OnItemClickListener listener;
    public ExpenseRecordAdapter(Context context, List<ExpenseInfo> TempList, ExpenseRecordAdapter.OnItemClickListener listener) {

        this.MainImageUploadInfoList = TempList;

        this.context = context;
        this.listener = listener;
    }
    @Override
    public ExpenseRecordAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.expense_cardview, parent, false);

        ExpenseRecordAdapter.ViewHolder viewHolder = new ExpenseRecordAdapter.ViewHolder(view);

        return viewHolder;
    }
    @Override
    public void onBindViewHolder(ExpenseRecordAdapter.ViewHolder holder, int position) {

        final ExpenseInfo UploadInfo = MainImageUploadInfoList.get(position);
        holder.bind(UploadInfo, listener);
        if(UploadInfo!=null){
            if(UploadInfo.getExp_date()!=null){
                holder.date_name.setText(UploadInfo.getExp_date());
            }
            if(UploadInfo.getExp_amount()!=null){
                //Loading image from Glide library.
                holder.amount_name.setText(UploadInfo.getExp_amount());
            }
            if(UploadInfo.getExp_category()!=null){
                holder.exp_cat_name.setText(UploadInfo.getExp_category());
            }
            if(UploadInfo.getExp_type()!=null){
                holder.type_name.setText(UploadInfo.getExp_type());
            }
            holder.exp_cat_img.setImageResource(R.drawable.bunny1);

        }


    }
    @Override
    public int getItemCount() {

        return MainImageUploadInfoList.size();
    }
    class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView  exp_cat_img;
        public TextView exp_cat_name,date_name,amount_name,type_name;

        public ViewHolder(View itemView) {
            super(itemView);
            exp_cat_img=itemView.findViewById(R.id.imageView17);
            exp_cat_name=itemView.findViewById(R.id.textView10);
            date_name=itemView.findViewById(R.id.textView16);
            amount_name=itemView.findViewById(R.id.textView18);
            type_name=itemView.findViewById(R.id.textView14);
                           }

        public void bind(ExpenseInfo exp, ExpenseRecordAdapter.OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    listener.onItemClick(exp);
                }
            });
        }
    }



}
