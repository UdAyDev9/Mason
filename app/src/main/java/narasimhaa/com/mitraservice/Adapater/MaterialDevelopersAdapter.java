package narasimhaa.com.mitraservice.Adapater;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.List;

import narasimhaa.com.mitraservice.AddMaterialActivity;
import narasimhaa.com.mitraservice.Model.MaterialDevelopers.DataItem;
import narasimhaa.com.mitraservice.R;
import narasimhaa.com.mitraservice.Utility.MyUtilities;

public class MaterialDevelopersAdapter extends RecyclerView.Adapter<MaterialDevelopersAdapter.MyViewHolder> {

   List<DataItem> dataItemList;
   Context context;

    public MaterialDevelopersAdapter(List<DataItem> dataItemList, Context context) {
        this.dataItemList = dataItemList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View myView = inflater.inflate(R.layout.row_material_developers_item,parent,false);
        return new MyViewHolder(myView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        DataItem dataItem = dataItemList.get(position);

        try{

            holder.tvMaterialType.setText(dataItem.getSERVICETYPE());
            holder.tvBusinessName.setText(dataItem.getBUSINESSNAME());
            holder.tvBrandNames.setText(dataItem.getBRANDNAME());
            holder.tvBusinessType.setText(dataItem.getBUSINESSTYPE());
            holder.textView7.setText(dataItem.getWEIGHT());
            holder.tvShape.setText(dataItem.getHEIGHT());
            holder.tvOfferPrice.setText(dataItem.getPRICE());
            holder.tvMrp.setText(dataItem.getMRP());
            holder.tvMrp.setText(dataItem.getMRP());
            holder.tvMrp.setText(dataItem.getMRP());
            holder.tvMrp.setText(dataItem.getMRP());
            holder.tvMrp.setText(dataItem.getMRP());
            holder.tv_perimeter.setText(dataItem.getPERIMETER());
            holder.tv_length.setText(dataItem.getLENGTH());
            holder.tv_thickness.setText(dataItem.getTHICKNESS());
            holder.tv_weight.setText(dataItem.getwEIGHT());

            if (!dataItem.getSERVICETYPE().equalsIgnoreCase("CEMENT")||!dataItem.getSERVICETYPE().equalsIgnoreCase("PAINTS")){

                holder.tvBrandNames.setVisibility(View.VISIBLE);
                holder.tv_brand_names.setVisibility(View.VISIBLE);

            }else {

                holder.tvBrandNames.setVisibility(View.GONE);
                holder.tv_brand_names.setVisibility(View.GONE);

            }


        }catch (Exception e){

        }

    }

    @Override
    public int getItemCount() {
        return dataItemList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tvMaterialType,tvBusinessName,tvBusinessType,tvDoorDelivery,tvBrandNames,tv_brand_names,textView7,tvShape,tvMrp,tvOfferPrice,tv_perimeter, tv_length, tv_weight, tv_thickness;
        private Button update_btn;


        public MyViewHolder(@NonNull View itemView) {

            super(itemView);
            tvMaterialType = itemView.findViewById(R.id.tv_material_type);
            tvBusinessName = itemView.findViewById(R.id.tv_business_name);
            tvBusinessType = itemView.findViewById(R.id.tv_business_type);
            tvBrandNames = itemView.findViewById(R.id.tv_brand_names);
            tv_brand_names = itemView.findViewById(R.id.textView66);
            textView7 = itemView.findViewById(R.id.tv_size);
            tvShape = itemView.findViewById(R.id.tv_shape);
            tvMrp = itemView.findViewById(R.id.tv_mrp);
            tvOfferPrice = itemView.findViewById(R.id.tv_offer_price);
            update_btn = itemView.findViewById(R.id.update_btn);
            tv_perimeter = itemView.findViewById(R.id.tv_perimeter);
            tv_length    = itemView.findViewById(R.id.tv_length);
            tv_weight    = itemView.findViewById(R.id.tv_weight);
            tv_thickness = itemView.findViewById(R.id.tv_thickness);



            update_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(context, AddMaterialActivity.class);
                    intent.putExtra("material_update_yes_or_no","yes");
                    intent.putExtra(MyUtilities.INTENT_KEY_MATERIAL_TYPE,dataItemList.get(getAdapterPosition()).getSERVICETYPE()+"");
                    intent.putExtra(MyUtilities.INTENT_KEY_BUSINESS_NAME,dataItemList.get(getAdapterPosition()).getBUSINESSNAME()+"");
                    intent.putExtra(MyUtilities.INTENT_KEY_BRAND_NAME,dataItemList.get(getAdapterPosition()).getBRANDNAME()+"");
                    intent.putExtra(MyUtilities.INTENT_KEY_BUSINESS_TYPE,dataItemList.get(getAdapterPosition()).getBUSINESSTYPE()+"");
                    intent.putExtra(MyUtilities.INTENT_KEY_DESC,dataItemList.get(getAdapterPosition()).getDESCRIPTION()+"");
                    intent.putExtra(MyUtilities.INTENT_KEY_DOOR_DELIVERY,dataItemList.get(getAdapterPosition()).getDOORDELIVERY()+"");
                    intent.putExtra(MyUtilities.INTENT_KEY_PRICE,dataItemList.get(getAdapterPosition()).getMRP()+"");
                    intent.putExtra(MyUtilities.INTENT_KEY_OFFER_PRICE,dataItemList.get(getAdapterPosition()).getPRICE()+"");
                    intent.putExtra(MyUtilities.INTENT_KEY_ID,dataItemList.get(getAdapterPosition()).getID()+"");
                    context.startActivity(intent);

                }
            });

        }
    }
}
