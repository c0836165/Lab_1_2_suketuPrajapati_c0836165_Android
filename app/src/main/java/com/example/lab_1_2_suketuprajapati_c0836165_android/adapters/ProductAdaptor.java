package com.example.lab_1_2_suketuprajapati_c0836165_android.adapters;

import android.content.Context;
import android.content.Intent;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lab_1_2_suketuprajapati_c0836165_android.EditProduct;
import com.example.lab_1_2_suketuprajapati_c0836165_android.R;
import com.example.lab_1_2_suketuprajapati_c0836165_android.constants.Constants;
import com.example.lab_1_2_suketuprajapati_c0836165_android.database.ProductDataBase;
import com.example.lab_1_2_suketuprajapati_c0836165_android.model.Product;

import java.util.List;

public class ProductAdaptor extends RecyclerView.Adapter<ProductAdaptor.MyViewHolder> {
    private Context context;
    private List<Product> mProductList;

    public ProductAdaptor(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.productview, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductAdaptor.MyViewHolder holder, int i) {
        holder.pName.setText(mProductList.get(i).getName());
        holder.pDec.setText(mProductList.get(i).getDescription());
        holder.pPrice.setText(mProductList.get(i).getPrice());
        holder.pLat.setText(mProductList.get(i).getLat());
        holder.pLong.setText(mProductList.get(i).getLon());
    }



    @Override
    public int getItemCount() {
        if (mProductList == null) {
            return 0;
        }
        return mProductList.size();

    }

    public void setTasks(List<Product> productList) {
        mProductList = productList;
        notifyDataSetChanged();
    }

    public List<Product> getTasks() {

        return mProductList;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView pName, pDec, pPrice, pLat, pLong;
        ImageView editImage;
        ProductDataBase mDb;

        MyViewHolder(@NonNull final View itemView) {
            super(itemView);
            mDb = ProductDataBase.getInstance(context);
            pName = itemView.findViewById(R.id.ProductName);
            pDec = itemView.findViewById(R.id.ProductDec);
            pPrice = itemView.findViewById(R.id.ProductPrice);
            pLat = itemView.findViewById(R.id.Productlatitude);
            pLong = itemView.findViewById(R.id.Productlongitude);
            editImage = itemView.findViewById(R.id.edit_Image);
            editImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int elementId = mProductList.get(getAdapterPosition()).getId();
                    Intent i = new Intent(context, EditProduct.class);
                    i.putExtra(Constants.UPDATE_Person_Id, elementId);
                    context.startActivity(i);
                }
            });
        }
    }
}
