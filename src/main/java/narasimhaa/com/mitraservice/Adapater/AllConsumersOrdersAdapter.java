package narasimhaa.com.mitraservice.Adapater;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import narasimhaa.com.mitraservice.Model.OrdersDataItem;
import narasimhaa.com.mitraservice.R;
import narasimhaa.com.mitraservice.Utility.MyUtilities;

public class AllConsumersOrdersAdapter extends RecyclerView.Adapter<AllConsumersOrdersAdapter.MyResultsViewHolder> {

    List<OrdersDataItem> list;
    Context context;

    public AllConsumersOrdersAdapter(List<OrdersDataItem> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public MyResultsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_consumer_order_details, parent, false);
        return new MyResultsViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull MyResultsViewHolder holder, int position) {

        try {

            OrdersDataItem dataItem = list.get(position);

            holder.tvMaterialType.setText(dataItem.getS_NAME());
            holder.tvBusinessName.setText(dataItem.getB_NAME());
            holder.tvBrandNames.setText(dataItem.getBRAND_NAME());
            holder.tv_offer_price.setText(dataItem.getI_PRICE());
            holder.tv_sub_category.setText(dataItem.getSIZE());
            holder.tv_size.setText(dataItem.getSIZE());
            holder.tv_shape.setText(dataItem.getSIZE());
            holder.tvOrderId.setText(dataItem.getORD_ID());
            holder.tv_ordered_on.setText(dataItem.getCREATED_DATETIME());
            holder.tv_door_delivery.setText(dataItem.getDELIVERY_TYPE());
            holder.tv_order_city.setText(dataItem.getCITY());
            switch (list.get(position).getSTATUS()){

                case MyUtilities.ORDER_STATUS_PENDING :

                    holder.tv_pending.setBackground(ContextCompat.getDrawable(context, R.drawable.curved_shape2));
                    holder.tv_pending.setTextColor(ContextCompat.getColor(context, R.color.white));
                    break;
                case MyUtilities.ORDER_STATUS_QUOTED:

                    holder.tv_quoted.setBackground(ContextCompat.getDrawable(context, R.drawable.curved_shape2));
                    holder.tv_quoted.setTextColor(ContextCompat.getColor(context, R.color.white));
                    break;
                case MyUtilities.ORDER_STATUS_PROCESSED:

                    holder.tv_processed.setBackground(ContextCompat.getDrawable(context, R.drawable.curved_shape2));
                    holder.tv_processed.setTextColor(ContextCompat.getColor(context, R.color.white));
                    break;
                case MyUtilities.ORDER_STATUS_DELIVERED:

                    holder.tv_delivered.setBackground(ContextCompat.getDrawable(context, R.drawable.curved_shape2));
                    holder.tv_delivered.setTextColor(ContextCompat.getColor(context, R.color.white));
                    break;
                default:
                    return;

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyResultsViewHolder extends RecyclerView.ViewHolder {


        private TextView tvMaterialType, tvBusinessName, tv_door_delivery, tvOrderId, tvBrandNames, tv_ordered_on, textView7, tv_sub_category, tv_shape, tv_size, tv_mrp, tv_offer_price,tv_pending, tv_quoted, tv_processed, tv_delivered,tv_order_city;
        private Button update_btn;
        private AppCompatTextView txt_totalProductPrice, tvQuantity, plus, minus;
        private LinearLayout moreDetailsLayout, quantityLayout;
        private int quantity = 0;

        private CheckBox checkbox;

        public MyResultsViewHolder(@NonNull View itemView) {
            super(itemView);

            tvMaterialType = itemView.findViewById(R.id.tv_material_type);
            tvBusinessName = itemView.findViewById(R.id.tv_business_name);
            tvBrandNames = itemView.findViewById(R.id.tv_brand_names_);
            tvOrderId = itemView.findViewById(R.id.tv_order_id);
            tv_sub_category = itemView.findViewById(R.id.tv_sub_category);
            tv_shape = itemView.findViewById(R.id.tv_shape);
            tv_size = itemView.findViewById(R.id.tv_size);
            tv_mrp = itemView.findViewById(R.id.tv_mrp);
            tv_offer_price = itemView.findViewById(R.id.tv_offer_price);
            tv_door_delivery = itemView.findViewById(R.id.tv_door_delivery);
            tv_ordered_on = itemView.findViewById(R.id.tv_ordered_on);
            checkbox = (CheckBox) itemView.findViewById(R.id.checkbox);
            tv_pending = itemView.findViewById(R.id.tv_pending);
            tv_quoted = itemView.findViewById(R.id.tv_quoted);
            tv_processed = itemView.findViewById(R.id.tv_processed);
            tv_delivered = itemView.findViewById(R.id.tv_delivered);
            checkbox = (CheckBox) itemView.findViewById(R.id.checkbox);
            tv_order_city = (TextView) itemView.findViewById(R.id.tv_order_city);



        }
    }
}