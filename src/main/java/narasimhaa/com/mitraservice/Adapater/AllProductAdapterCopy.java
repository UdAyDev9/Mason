package narasimhaa.com.mitraservice.Adapater;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import narasimhaa.com.mitraservice.AllProductsListActivityCopy;
import narasimhaa.com.mitraservice.Listener.ProductListItemClickListener;
import narasimhaa.com.mitraservice.Model.MaterialDevelopers.DataItem;
import narasimhaa.com.mitraservice.R;

public class AllProductAdapterCopy extends RecyclerView.Adapter<AllProductAdapterCopy.MyViewHolder> {

    List<DataItem> dataItemList;
    Context context;
    TextView tvTotalValue;
    Double tempTotalValue = 0.0;
    protected ProductListItemClickListener productListItemClickListener;
    private Gson gson;
    private AdapterView.OnItemClickListener listener;
    private double totalPrice;
    private List<DataItem> items;
    private List<Double> selectedPrices = new ArrayList<>();
    private List<Integer> selectedQuantities = new ArrayList<>();
    private AllProductsListActivityCopy activity;

    public AllProductAdapterCopy(AllProductsListActivityCopy activity, List<DataItem> items) {
        this.activity = activity;
        this.items = items;
    }

   /* public AllProductAdapterCopy(List<DataItem> dataItemList, Context context, TextView tvTotalValue,OnItemClickListener listener) {
        this.dataItemList = dataItemList;
        this.context = context;
        this.tvTotalValue = tvTotalValue;
        this.listener = listener;
        this.totalPrice = 0;
    }*/

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View myView = inflater.inflate(R.layout.item_products_list, parent, false);
        return new MyViewHolder(myView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        DataItem dataItem = items.get(position);


        holder.tvMaterialType.setText(dataItem.getSERVICETYPE());
        holder.tvBusinessName.setText(dataItem.getBUSINESSNAME());
        holder.tvBrandNames.setText(dataItem.getBRANDNAME());
        holder.tv_offer_price.setText(dataItem.getPRICE());
        holder.tv_mrp.setText(dataItem.getMRP());
        holder.tv_sub_category.setText(dataItem.getHEIGHT());
        holder.tvDoorDelivery.setText(dataItem.getDOORDELIVERY());
        holder.tv_size.setText(dataItem.getWEIGHT());
        holder.tv_shape.setText(dataItem.getBUSINESSTYPE());
        holder.txt_totalProductPrice.setText(dataItem.getPRICE());

        int quantity = (int) Integer.parseInt(String.valueOf(holder.tvQuantity.getText()));

        holder.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
           /*     if (isChecked) {
                    selectedPrices.add(Double.valueOf(dataItem.getPRICE()));
                    selectedQuantities.add(Integer.parseInt(String.valueOf(holder.tvQuantity.getText())));
                } else {
                    selectedPrices.remove(dataItem.getPRICE());
                    selectedQuantities.remove(holder.tvQuantity);
                }
                activity.updateTotalPrice();*/
                int position = holder.getAdapterPosition();
                items.get(position).setSelected(isChecked);
                activity.updateTotalPrice();

            }
        });
        holder.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                DataItem product = items.get(position);
                product.setQuantity(product.getQuantity() + 1);
                holder.tvQuantity.setText(String.valueOf(product.getQuantity()));
                Double cost = Double.parseDouble(String.valueOf(holder.txt_totalProductPrice.getText()));
                double itemFinalPrice = cost + Double.parseDouble(String.valueOf(holder.tv_offer_price.getText()));
                holder.txt_totalProductPrice.setText("" + itemFinalPrice);
                activity.updateTotalPrice();
            }
        });
        holder.minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                DataItem product = items.get(position);
                if (product.getQuantity() > 0) {
                    product.setQuantity(product.getQuantity() - 1);
                    holder.tvQuantity.setText(String.valueOf(product.getQuantity()));
                    Double cost = Double.parseDouble(String.valueOf(holder.txt_totalProductPrice.getText()));
                    double itemFinalPrice = cost - Double.parseDouble(String.valueOf(holder.tv_offer_price.getText()));
                    holder.txt_totalProductPrice.setText("" + itemFinalPrice);

                }
                activity.updateTotalPrice();
            }
        });

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tvMaterialType, tvBusinessName, tvBusinessType, tvDoorDelivery, tvBrandNames, tv_brand_names, textView7, tv_sub_category, tv_shape, tv_size, tv_mrp, tv_offer_price;
        private Button update_btn;
        private AppCompatTextView txt_totalProductPrice, tvQuantity, plus, minus;
        private LinearLayout moreDetailsLayout, quantityLayout;
        private int quantity = 0;

        private CheckBox checkbox;

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
            checkbox = (CheckBox) itemView.findViewById(R.id.checkbox);


            plus = (AppCompatTextView) itemView.findViewById(R.id.plus);
            minus = (AppCompatTextView) itemView.findViewById(R.id.minus);
            tvQuantity = (AppCompatTextView) itemView.findViewById(R.id.count);
            txt_totalProductPrice = (AppCompatTextView) itemView.findViewById(R.id.totalProductPrice);


        }
    }


}
