package com.example.lab_1_2_suketuprajapati_c0836165_android;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.lab_1_2_suketuprajapati_c0836165_android.adapters.ProductAdaptor;
import com.example.lab_1_2_suketuprajapati_c0836165_android.database.ProductDataBase;
import com.example.lab_1_2_suketuprajapati_c0836165_android.database.ProductRepo;
import com.example.lab_1_2_suketuprajapati_c0836165_android.model.Product;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    FloatingActionButton floatingActionButton;
    private RecyclerView mRecyclerView;
    private ProductAdaptor mAdapter;
    private ProductDataBase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        floatingActionButton = findViewById(R.id.addfab);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, EditProduct.class));
            }
        });

        mRecyclerView = findViewById(R.id.rview);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize the adapter and attach it to the RecyclerView
        mAdapter = new ProductAdaptor(this);
        mRecyclerView.setAdapter(mAdapter);
        mDb = ProductDataBase.getInstance(getApplicationContext());
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            // Called when a user swipes left or right on a ViewHolder
            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int swipeDir) {
                // Here is where you'll implement swipe to delete
                ProductRepo.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        int position = viewHolder.getAdapterPosition();
                        List<Product> tasks = mAdapter.getTasks();
                        mDb.productDAO().delete(tasks.get(position));
                        retrieveTasks();
                    }
                });
            }
        }).attachToRecyclerView(mRecyclerView);

    }

    @Override
    protected void onResume() {
        super.onResume();
        retrieveTasks();
    }

    private void retrieveTasks() {
        ProductRepo.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                final List<Product> product = mDb.productDAO().loadAllProduct();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        mAdapter.setTasks(product);
                    }
                });
            }
        });


    }
}