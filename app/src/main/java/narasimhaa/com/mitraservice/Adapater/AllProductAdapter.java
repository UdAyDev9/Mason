package narasimhaa.com.mitraservice.Adapater;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import narasimhaa.com.mitraservice.AddMaterialActivity;
import narasimhaa.com.mitraservice.Model.MaterialDevelopers.DataItem;
import narasimhaa.com.mitraservice.R;
import narasimhaa.com.mitraservice.Utility.MyUtilities;

public class AllProductAdapter extends RecyclerView.Adapter<AllProductAdapter.MyViewHolder> {

    List<DataItem> dataItemList;
    Context context;
    TextView tvTotalValue;
    Double tempTotalValue = 0.0;

    public AllProductAdapter(List<DataItem> dataItemList, Context context) {
        this.dataItemList = dataItemList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View myView = inflater.inflate(R.layout.item_products_list, parent, false);
        return new MyViewHolder(myView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        DataItem dataItem = dataItemList.get(position);


        holder.tvMaterialType.setText(dataItem.getSERVICETYPE());
        holder.tvBusinessName.setText(dataItem.getBUSINESSNAME());
        holder.tvBrandNames.setText(dataItem.getBRANDNAME());
        //holder.tv_offer_price.setText(dataItem.getPRICE());
        holder.tv_mrp.setText(dataItem.getMRP());
        holder.tv_sub_category.setText(dataItem.getHEIGHT());
        holder.tvDoorDelivery.setText(dataItem.getDOORDELIVERY());
        holder.tv_size.setText(dataItem.getWEIGHT());
        holder.tv_shape.setText(dataItem.getBUSINESSTYPE());
        holder.txt_totalProductPrice.setText(dataItem.getPRICE());


        holder.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int quantity = (int) Integer.parseInt(String.valueOf(holder.quantity.getText()));
                quantity++;
                Double cost = Double.parseDouble(String.valueOf(holder.txt_totalProductPrice.getText()));
                double itemFinalPrice = cost + Double.parseDouble(String.valueOf(holder.tv_offer_price.getText()));
                holder.quantity.setText("" + quantity);
                holder.txt_totalProductPrice.setText("" + itemFinalPrice);
                //databaseManager.itemsUpdate(holder.txt_skuId.getText().toString(),itemFinalPrice,quantity);
                tvTotalValue.setText(""+itemFinalPrice);
                tempTotalValue = itemFinalPrice - tempTotalValue;
                updateValue();
            }
        });
        holder.minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int quantity = (int) Integer.parseInt(String.valueOf(holder.quantity.getText()));
                if (quantity > 0) {
                    //holder.minus.setVisibility(View.VISIBLE);
                    quantity--;
                    Double cost = Double.parseDouble(String.valueOf(holder.txt_totalProductPrice.getText()));
                    double itemFinalPrice = cost - Double.parseDouble(String.valueOf(holder.tv_offer_price.getText()));
                    holder.quantity.setText(quantity + "");
                    holder.txt_totalProductPrice.setText("" + itemFinalPrice);
                    //databaseManager.itemsUpdate(holder.txt_skuId.getText().toString(),itemFinalPrice,quantity);
                    tempTotalValue = tempTotalValue+itemFinalPrice;
                    updateValue();

                } else if (quantity == 0) {

                }
                //databaseManager.deleteItems(holder.txt_skuId.getText().toString());
                //holder.minus.setVisibility(View.INVISIBLE);

            }
        });



    }

    @Override
    public int getItemCount() {
        return dataItemList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tvMaterialType, tvBusinessName, tvBusinessType, tvDoorDelivery, tvBrandNames, tv_brand_names, textView7, tv_sub_category, tv_shape, tv_size, tv_mrp, tv_offer_price;
        private Button update_btn;
        private AppCompatTextView txt_totalProductPrice, quantity, plus, minus;


        public MyViewHolder(@NonNull View itemView) {

            super(itemView);
            tvMaterialType = itemView.findViewById(R.id.tv_material_type);
            tvBusinessName = itemView.findViewById(R.id.tv_business_name);
            tvBrandNames = itemView.findViewById(R.id.tv_brand_names_);
            tvDoorDelivery = itemView.findViewById(R.id.tv_door_delivery);
            tv_sub_category = itemView.findViewById(R.id.tv_sub_category);
            tv_shape = itemView.findViewById(R.id.tv_shape);
            tv_size = itemView.findViewById(R.id.tv_size);
            tv_mrp = itemView.findViewById(R.id.tv_mrp);
            tv_offer_price = itemView.findViewById(R.id.tv_offer_price);

            plus = (AppCompatTextView) itemView.findViewById(R.id.plus);
            minus = (AppCompatTextView) itemView.findViewById(R.id.minus);
            quantity = (AppCompatTextView) itemView.findViewById(R.id.count);
            txt_totalProductPrice = (AppCompatTextView) itemView.findViewById(R.id.totalProductPrice);

        }
    }

    private void updateValue(){


        tvTotalValue.setText(""+tempTotalValue);

    }
}
