package com.example.shoesstore.util;

import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.shoesstore.R;
import com.example.shoesstore.dto.ProductDto;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    private List<ProductDto> productList;

    public interface OnItemClickListener {
        void onItemClick(ProductDto product);
    }

    private OnItemClickListener listener;

    public ProductAdapter(List<ProductDto> productList, OnItemClickListener listener) {
        this.productList = productList;
        this.listener = listener;
    }


    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item, parent, false);
        RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(
                Resources.getSystem().getDisplayMetrics().widthPixels * 48 / 100,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        view.setLayoutParams(params);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        ProductDto product = productList.get(position);
        holder.tvProductName.setText(product.getName());
        holder.tvProductPrice.setText(CommonUtil.formatCurrency(product.getSkus().get(0).getPrice()));
        Glide.with(holder.imgProduct.getContext()).load(product.getProductImages().get(0).getLink()).into(holder.imgProduct);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemClick(product);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProduct;
        TextView tvProductName, tvProductPrice;

        public ProductViewHolder(View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.imgProduct);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvProductPrice = itemView.findViewById(R.id.tvProductPrice);
        }
    }

}
