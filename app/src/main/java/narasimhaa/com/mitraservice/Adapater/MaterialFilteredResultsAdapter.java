package narasimhaa.com.mitraservice.Adapater;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.List;

import narasimhaa.com.mitraservice.AllProductsListActivity;
import narasimhaa.com.mitraservice.AllProductsListActivityCopy;
import narasimhaa.com.mitraservice.Model.MaterialFilter.DataItem;
import narasimhaa.com.mitraservice.R;
import narasimhaa.com.mitraservice.Utility.MyUtilities;

public class MaterialFilteredResultsAdapter extends RecyclerView.Adapter<MaterialFilteredResultsAdapter.MyResultsViewHolder> {

    List<DataItem> list;
    Context context;

    public MaterialFilteredResultsAdapter(List<DataItem> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public MyResultsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_material_results, parent, false);
        return new MyResultsViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull MyResultsViewHolder holder, int position) {

        try {

            holder.name.setText(list.get(position).getNAME());
            holder.tv_email.setText(list.get(position).getEMAILID().trim());
            holder.tv_business_name.setText(list.get(position).getBUSINESSNAME().trim());
            holder.tv_material.setText(list.get(position).getSERVICETYPE().trim());
            holder.tv_door_delivery.setText(list.get(position).getDOORDELIVERY().trim());
            holder.tv_city.setText(list.get(position).getCITY());
            holder.tv_address.setText(list.get(position).getADDRESS());
            holder.tv_mobile.setText(list.get(position).getMOBILENO());
            holder.pincode.setText(list.get(position).getPINCODENO().trim());
            holder.tv_brands.setText(list.get(position).getBRANDNAME().trim());
            holder.tv_shape.setText(list.get(position).getHEIGHT().trim());
            holder.tv_sub_category.setText(list.get(position).getBUSINESSTYPE().trim());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyResultsViewHolder extends RecyclerView.ViewHolder {

        TextView name, tv_business_name, tv_material,  tv_business_type, tv_door_delivery, tv_desc, tv_city, tv_address, tv_mobile,  tv_within_range, tv_charge, tv_exp,tv_shape,tv_sub_category;
        TextView pincode,tv_email,tv_brands, tv_brands_label,tv_mrp,tv_our_price;
        Button btnProductsList;

        public MyResultsViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tv_name);
            tv_business_name = itemView.findViewById(R.id.tv_business_name);
            tv_material = itemView.findViewById(R.id.tv_material_type);
            tv_shape = itemView.findViewById(R.id.tv_shape);
            tv_sub_category = itemView.findViewById(R.id.tv_sub_category);
            tv_material = itemView.findViewById(R.id.tv_material_type);
            tv_brands = itemView.findViewById(R.id.tv_brand_names_);
            tv_city = itemView.findViewById(R.id.tv_city);
            tv_address = itemView.findViewById(R.id.tv_address_);
            tv_mobile = itemView.findViewById(R.id.tv_mobile);
            pincode = itemView.findViewById(R.id.tv_pincode);
            tv_door_delivery = itemView.findViewById(R.id.tv_door_delivery);
            tv_email = itemView.findViewById(R.id.tv_email);
            btnProductsList = itemView.findViewById(R.id.btn_all_products);

            btnProductsList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(context, AllProductsListActivityCopy.class);
                    intent.putExtra(MyUtilities.PREF_EMAIL,list.get(getAdapterPosition()).getEMAILID());
                    context.startActivity(intent);

                }
            });


        }
    }
}