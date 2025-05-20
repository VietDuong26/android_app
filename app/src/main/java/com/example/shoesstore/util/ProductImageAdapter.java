package com.example.shoesstore.util;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shoesstore.R;

import java.util.List;

public class ProductImageAdapter extends RecyclerView.Adapter<ProductImageAdapter.ImageViewHolder> {
    List<Uri> imageLinks;
    public void addImage(Uri uri, int position) {
        if (position >= 0 && position <= imageLinks.size()) {
            imageLinks.add(position, uri);
            notifyItemInserted(position);
        }
    }

    public void deleteImage(int position) {
        if (position >= 0 && position < imageLinks.size()) {
            imageLinks.remove(position);
            notifyItemRemoved(position);
        }
    }

    public ProductImageAdapter(List<Uri> imageLinks) {
        this.imageLinks = imageLinks;
    }
    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_product_image, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        holder.imageView.setImageURI(imageLinks.get(position));
        holder.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = holder.getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION) {
                    deleteImage(pos);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return imageLinks.size();
    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        ImageButton imageButton;
        public ImageViewHolder(View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.imageView);
            imageButton=itemView.findViewById(R.id.btnRemove);
        }

    }


}
