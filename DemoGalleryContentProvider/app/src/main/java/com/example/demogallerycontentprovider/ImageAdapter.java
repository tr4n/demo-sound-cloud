package com.example.demogallerycontentprovider;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {

    private List<ImageModel> imageModelList;

    public ImageAdapter(List<ImageModel> imageModelList) {
        this.imageModelList = imageModelList;
    }


    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.image_layout_item, viewGroup, false);
        return new ImageViewHolder(viewGroup.getContext(),itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder imageViewHolder, int i) {
        imageViewHolder.onBindData(imageModelList.get(i));
    }

    @Override
    public int getItemCount() {
        return imageModelList.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder{

        TextView tvTitle ;
        ImageView ivItem;
        private Context mContext;
        public ImageViewHolder(Context context, @NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title);
            ivItem = itemView.findViewById(R.id.iv_item);
            this.mContext = context;
        }

        public void onBindData(ImageModel imageModel){
            tvTitle.setText(imageModel.name);
          //  Picasso.get().load(imageModel.path).into(ivItem);
            Glide.with(mContext)
                    .load(new File(imageModel.path))
                    .into(ivItem);
        }
    }
}
