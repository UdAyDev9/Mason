package narasimhaa.com.mitraservice.Adapater;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.List;

import narasimhaa.com.mitraservice.AllUserListAdminActivity;
import narasimhaa.com.mitraservice.Model.MaterialFilter.DataItem;
import narasimhaa.com.mitraservice.R;

public class AllUserListAdminAdapter extends RecyclerView.Adapter<AllUserListAdminAdapter.MyResultsViewHolder> {

    List<DataItem> list;
    Context context;

    public AllUserListAdminAdapter(List<DataItem> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public MyResultsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_all_user_admin, parent, false);
        return new MyResultsViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull MyResultsViewHolder holder, int position) {

        try {


            holder.itemView.getRootView().setVisibility(View.VISIBLE);
            holder.name.setText(list.get(position).getNAME());
            holder.tv_email.setText(list.get(position).getEMAILID().trim());
            holder.tv_business_name.setText(list.get(position).getBUSINESSNAME().trim());
            holder.tv_business_type.setText(list.get(position).getUserType().trim());
            holder.tv_city.setText(list.get(position).getCITY());
            holder.tv_address.setText(list.get(position).getADDRESS());
            holder.tv_mobile.setText(list.get(position).getMOBILENO());
            holder.pincode.setText(list.get(position).getPINCODENO().trim());
            if (list.get(position).getSTATUS().equalsIgnoreCase("ACTIVE")) {

                holder.switch_change_status.setChecked(true);

            }


        } catch (ArithmeticException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyResultsViewHolder extends RecyclerView.ViewHolder {

        TextView name, tv_business_name, tv_business_type, tv_city, tv_address, tv_mobile;
        TextView pincode, tv_email;
        RelativeLayout relativeLayout;
        Switch switch_change_status;

        public MyResultsViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tv_name);
            tv_business_name = itemView.findViewById(R.id.tv_business_name);
            tv_business_type = itemView.findViewById(R.id.tv_type);
            tv_city = itemView.findViewById(R.id.tv_city);
            tv_address = itemView.findViewById(R.id.tv_address_);
            tv_mobile = itemView.findViewById(R.id.tv_mobile);
            pincode = itemView.findViewById(R.id.tv_pincode);
            tv_email = itemView.findViewById(R.id.tv_email);
            relativeLayout = itemView.findViewById(R.id.relative_main);
            switch_change_status = itemView.findViewById(R.id.switch_change_status);

            switch_change_status.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    if (list.get(getAdapterPosition()).getSTATUS().equalsIgnoreCase("INACTIVE"))
                        ((AllUserListAdminActivity) context).onClickUpdateDealerActiveState(list.get(getAdapterPosition()).getEMAILID(), "ACTIVE");
                    else
                        ((AllUserListAdminActivity) context).onClickUpdateDealerActiveState(list.get(getAdapterPosition()).getEMAILID(), "INACTIVE");


                }
            });


        }

    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}