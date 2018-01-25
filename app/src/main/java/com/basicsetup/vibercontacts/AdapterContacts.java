package com.clevertrap.contactsfromviber;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class AdapterContacts extends RecyclerView.Adapter<AdapterContacts.ViewHolder> {

    public static class ViewHolder extends RecyclerView.ViewHolder{

        private TextView textViewName = null;
        private TextView textViewNumber = null;
        private Button btnMessage = null;
        private Button btnCall = null;


        public ViewHolder(View itemView, View.OnClickListener onClickListener) {
            super(itemView);

            textViewName = itemView.findViewById(R.id.textViewName);
            textViewNumber = itemView.findViewById(R.id.textViewNumber);
            btnMessage = itemView.findViewById(R.id.btnMessage);
            btnCall = itemView.findViewById(R.id.btnCall);

            btnMessage.setOnClickListener(onClickListener);
            btnCall.setOnClickListener(onClickListener);
        }
    }

    private List<ModelContact> listModels = null;
    private View.OnClickListener onClickListener = null;

    public AdapterContacts(List<ModelContact> listModels, View.OnClickListener onClickListener){
        this.listModels = listModels;
        this.onClickListener = onClickListener;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_contact,null);
        ViewHolder holder = new ViewHolder(view,onClickListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        ModelContact model = listModels.get(position);
        holder.textViewName.setText(model.getContactName());
        holder.textViewNumber.setText(model.getContactNumber());
        holder.btnCall.setTag(position);
        holder.btnMessage.setTag(position);
    }

    @Override
    public int getItemCount() {
        return listModels == null?0:listModels.size();
    }

    public ModelContact getItem(int pos){
        return listModels.get(pos);
    }
}
