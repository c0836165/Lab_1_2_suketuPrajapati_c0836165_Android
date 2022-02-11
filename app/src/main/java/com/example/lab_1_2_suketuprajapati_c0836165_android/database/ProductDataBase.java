package com.example.lab_1_2_suketuprajapati_c0836165_android.database;


import android.content.Context;
import android.util.Log;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.lab_1_2_suketuprajapati_c0836165_android.model.Product;

@Database(entities = {Product.class}, version = 1, exportSchema = false)
public abstract class ProductDataBase extends RoomDatabase {
    private static final String LOG_TAG = ProductDataBase.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "productlist";
    private static ProductDataBase sInstance;

    public static ProductDataBase getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                Log.d(LOG_TAG, "Creating new database instance");
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        ProductDataBase.class, ProductDataBase.DATABASE_NAME)
                        .build();
            }
        }
        Log.d(LOG_TAG, "Getting the database instance");
        return sInstance;
    }

    public abstract ProductDAO productDAO();
}

