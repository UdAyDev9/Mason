package narasimhaa.com.mitraservice.Adapater;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.List;

import narasimhaa.com.mitraservice.ImagesViewActivity;
import narasimhaa.com.mitraservice.Model.Filter.FilterDataItem;
import narasimhaa.com.mitraservice.R;
import narasimhaa.com.mitraservice.Utility.MyUtilities;


public class ResultsAdapter extends RecyclerView.Adapter<ResultsAdapter.ResultsViewHolder> {

    List<FilterDataItem> list;
    Context context;

    public ResultsAdapter(List<FilterDataItem> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ResultsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.list_row, parent, false);
        return new ResultsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ResultsViewHolder holder, int position) {

        try {

            holder.name.setText(list.get(position).getNAME());
            holder.tv_service.setText(list.get(position).getSERVICENAME().trim());
            holder.tv_city.setText(list.get(position).getCITY());
            holder.tv_address.setText(list.get(position).getDISTRICT());
            holder.tv_mobile.setText(list.get(position).getCONTACTNO());
            holder.tv_email.setText(list.get(position).getEMAILID());
            holder.tv_within_range.setText(list.get(position).getWITHINRANGE());
            holder.tv_charge.setText(new StringBuilder(context.getResources().getString(R.string.Rs)).append(list.get(position).getQUALIFICATION()));
            holder.tv_pincode.setText(list.get(position).getPINCODE());
            holder.tv_exp.setText(list.get(position).getEXPERIENCE());

            if (list.get(position).getSERVICENAME().contains("Pop")|| list.get(position).getSERVICENAME().contains("POP")){

                holder.images.setVisibility(View.VISIBLE);

            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ResultsViewHolder extends RecyclerView.ViewHolder {

        TextView name,tv_service,tv_city,tv_address,tv_mobile,tv_email,tv_within_range,tv_pincode,tv_charge,tv_exp;
        TextView images;
        public ResultsViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tv_name);
            tv_service = itemView.findViewById(R.id.tv_service);
            tv_city = itemView.findViewById(R.id.tv_city);
            tv_address = itemView.findViewById(R.id.tv_address);
            tv_mobile = itemView.findViewById(R.id.tv_mobile);
            tv_email = itemView.findViewById(R.id.tv_email);
            tv_pincode = itemView.findViewById(R.id.tv_pincode);
            tv_charge = itemView.findViewById(R.id.tv_charges);
            tv_email = itemView.findViewById(R.id.tv_email);
            tv_exp = itemView.findViewById(R.id.tv_exp);
            tv_within_range = itemView.findViewById(R.id.tv_available_out_of_location);
            images = itemView.findViewById(R.id.tv_images);

            images.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(context, ImagesViewActivity.class);
                    intent.putExtra(MyUtilities.PREF_EMAIL,list.get(getAdapterPosition()).getEMAILID());
                    context.startActivity(intent);

                }
            });
        }
    }

}
