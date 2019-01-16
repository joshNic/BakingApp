package com.example.joshuamugisha.udacitybakingapp.activity;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.joshuamugisha.udacitybakingapp.R;
import com.example.joshuamugisha.udacitybakingapp.adapters.ResultAdapter;
import com.example.joshuamugisha.udacitybakingapp.model.Result;
import com.example.joshuamugisha.udacitybakingapp.viewmodel.RecipeViewModel;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static Context mContext;
    private RecyclerView recyclerView;
    private List<Result> baking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.recipeRecyclerView);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        RecipeViewModel model = ViewModelProviders.of(this).get(RecipeViewModel.class);

        model.getHeroes().observe(this, new Observer<List<Result>>() {
            @Override
            public void onChanged(@Nullable List<Result> resultList) {
                recyclerView.setAdapter(new ResultAdapter(MainActivity.this, resultList));
            }
        });

    }


}
