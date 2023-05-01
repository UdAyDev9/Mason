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

import narasimhaa.com.mitraservice.BrandsActivity;
import narasimhaa.com.mitraservice.Model.MaterialDevelopers.ServicesDataItemSize;
import narasimhaa.com.mitraservice.R;
import narasimhaa.com.mitraservice.SizesActivity;


public class SizeBrandsAdapter extends RecyclerView.Adapter<SizeBrandsAdapter.SizeBrandsViewHolder> {

    List<ServicesDataItemSize> list;
    Context context;
    private boolean isFromSizes;

    public SizeBrandsAdapter(List<ServicesDataItemSize> list, Context context,boolean isFromSizes) {
        this.list = list;
        this.context = context;
        this.isFromSizes = isFromSizes;
    }

    @NonNull
    @Override
    public SizeBrandsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_brands, parent, false);
        return new SizeBrandsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SizeBrandsViewHolder holder, int position) {

        try {

            //getSERVICE_NAME gives sizes/brands
            holder.sizesBrand.setText(list.get(position).getSERVICE_NAME());
            if (isFromSizes == true){
                holder.tvSizeBrandLabel.setText("Size");

            }


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

                        if (isFromSizes == true) {

                            alertForDeleteSize();
                        }else {
                            alertForDeleteBrand();

                        }
                    }
                });




        }

        private void alertForDeleteBrand(){

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Delete Brand");
            builder.setMessage("Are you sure, you want to delete this Brand?");
            builder.setCancelable(false);
            builder.setPositiveButton("Yes", (dialog, which) -> {

                ((BrandsActivity) context).onClickDeleteBrand(list.get(getAdapterPosition()).getSERVICE_NAME());


                Log.d("delete", "alertAfterRolesChanged: ");

            });

            builder.setNegativeButton("Cancel", (dialog, which) -> {

                Log.d("delete", "alertAfterRolesChanged: ");
                dialog.cancel();

            });

            builder.create().show();

        }

        private void alertForDeleteSize(){

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Delete Size");
            builder.setMessage("Are you sure, you want to delete this Size?");
            builder.setCancelable(false);
            builder.setPositiveButton("Yes", (dialog, which) -> {

                ((SizesActivity) context).onClickDeleteSize(list.get(getAdapterPosition()).getSERVICE_NAME());


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
