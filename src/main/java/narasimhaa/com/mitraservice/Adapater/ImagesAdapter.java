package narasimhaa.com.mitraservice.Adapater;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import narasimhaa.com.mitraservice.ImagesActivity;
import narasimhaa.com.mitraservice.Model.PathsItem;
import narasimhaa.com.mitraservice.R;

public class ImagesAdapter extends RecyclerView.Adapter<ImagesAdapter.ImagesViewHolder> {

    List<PathsItem> pathsItems = new ArrayList<>();
    private Context context;
    private boolean isFromFilteredLsit = false;


    public ImagesAdapter(List<PathsItem> pathsItems, Context context,boolean isFromFilteredLsit) {
        this.pathsItems = pathsItems;
        this.context = context;
        this.isFromFilteredLsit = isFromFilteredLsit;
    }

    @NonNull
    @Override
    public ImagesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.list_imge_row, parent, false);
        return new ImagesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImagesViewHolder holder, int position) {

        try {

            String baseUrl = "http://65.1.178.54/app/";

            Glide.with(context).load(baseUrl+pathsItems.get(position).getIMAGEPATH()).placeholder(R.drawable.image_place_holder).into(holder.imgView);


        } catch (Exception e) {

            e.printStackTrace();

        }

    }

    @Override
    public int getItemCount() {
        return pathsItems.size();
    }

    public class ImagesViewHolder extends RecyclerView.ViewHolder {

        private ImageView imgView;

        public ImagesViewHolder(@NonNull View itemView) {
            super(itemView);
            imgView = itemView.findViewById(R.id.img_image);

            if (isFromFilteredLsit == false){

                itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {

                        alertForDeleteImage();

                        return false;
                    }
                });

            }


        }

        private void alertForDeleteImage(){

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Delete Image");
            builder.setMessage("Are you sure, you want to delete this image?");
            builder.setCancelable(false);
            builder.setPositiveButton("Yes", (dialog, which) -> {

                ((ImagesActivity) context).onClickDeleteImage(pathsItems.get(getAdapterPosition()).getIMAGEPATH());


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
