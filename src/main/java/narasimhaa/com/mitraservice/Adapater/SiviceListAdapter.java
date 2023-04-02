package narasimhaa.com.mitraservice.Adapater;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import narasimhaa.com.mitraservice.AddServicesType;
import narasimhaa.com.mitraservice.BrandsActivity;
import narasimhaa.com.mitraservice.Model.MaterialDevelopers.ServicesDataItemSize;
import narasimhaa.com.mitraservice.Model.service.ServicesDataItem;
import narasimhaa.com.mitraservice.R;
import narasimhaa.com.mitraservice.ServiceListActivity;
import narasimhaa.com.mitraservice.SizesActivity;


public class SiviceListAdapter extends RecyclerView.Adapter<SiviceListAdapter.SizeBrandsViewHolder> {

    List<ServicesDataItem> list;
    Context context;

    public SiviceListAdapter(List<ServicesDataItem> list, Context context) {
        this.list = list;
        this.context = context;

    }

    @NonNull
    @Override
    public SizeBrandsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_seriveslist, parent, false);
        return new SizeBrandsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SizeBrandsViewHolder holder, int position) {

        try {

            //getSERVICE_NAME gives sizes/brands
            holder.sizesBrand.setText(list.get(position).getSERVICE_NAME());



        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class SizeBrandsViewHolder extends RecyclerView.ViewHolder {

        TextView sizesBrand,tvSizeBrandLabel;
        private Button deleteBtn;

        public SizeBrandsViewHolder(@NonNull View itemView) {
            super(itemView);
            sizesBrand = itemView.findViewById(R.id.tv_size_brand);
            tvSizeBrandLabel = itemView.findViewById(R.id.tv_size_brand_label);
            deleteBtn = itemView.findViewById(R.id.delete_btn);



            deleteBtn.setOnClickListener(new View.OnClickListener()  {
                    @Override
                    public void onClick(View view) {


                            alertForDeleteMaterial();

                    }
                });




        }


        private void alertForDeleteMaterial(){

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Delete Size");
            builder.setMessage("Are you sure, you want to delete this Material?");
            builder.setCancelable(false);
            builder.setPositiveButton("Yes", (dialog, which) -> {

                ((ServiceListActivity) context).onClickDeleteMaterial(list.get(getAdapterPosition()).getSERVICE_NAME());


                Log.d("delete", "alertAfterRolesChanged: ");

            });

            builder.setNegativeButton("Cancel", (dialog, which) -> {

                Log.d("delete", "alertAfterRolesChanged: ");
                dialog.cancel();

            });

            builder.create().show();

        }

    }
}
