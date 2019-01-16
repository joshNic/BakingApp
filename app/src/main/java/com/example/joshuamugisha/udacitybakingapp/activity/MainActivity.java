package com.example.joshuamugisha.udacitybakingapp.activity;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.joshuamugisha.udacitybakingapp.R;
import com.example.joshuamugisha.udacitybakingapp.adapters.ResultAdapter;
import com.example.joshuamugisha.udacitybakingapp.model.Result;
import com.example.joshuamugisha.udacitybakingapp.network.ApiClient;
import com.example.joshuamugisha.udacitybakingapp.network.ApiInterface;
import com.example.joshuamugisha.udacitybakingapp.viewmodel.RecipeViewModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private static Context mContext;
    private RecyclerView recyclerView;
//    private ProgressBar pb;
    private List<Result> baking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.recipeRecyclerView);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
//        pb = (ProgressBar) findViewById(R.id.determinateBar);

        RecipeViewModel model = ViewModelProviders.of(this).get(RecipeViewModel.class);

        model.getHeroes().observe(this, new Observer<List<Result>>() {
            @Override
            public void onChanged(@Nullable List<Result> resultList) {
//                adapter = new ResultAdapter(MainActivity.this, resultList);
                recyclerView.setAdapter(new ResultAdapter(MainActivity.this, resultList));
            }
        });


//        getRecipeData();
    }




    public void getRecipeData() {
        // Showing progress bar before making http request
//        pb.setVisibility(ProgressBar.VISIBLE);
//        int array[] = {25, 30, 10};
//        for(int x:array){
//            pb.setProgress(x);
//
//        }


        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        Call<List<Result>> call = apiService.recipieData();
        call.enqueue(new Callback<List<Result>>() {
            @Override
            public void onResponse(Call<List<Result>> call, Response<List<Result>> response) {
                if (response.isSuccessful()) {
//                    pb.setProgress(100);
                    if (response.body() != null) {
//                        pb.setVisibility(ProgressBar.INVISIBLE);
                        List<Result> recipies = response.body();
                        recyclerView.setAdapter(new ResultAdapter(getApplicationContext(), recipies));
//                      Log.d(TAG, "Number of movies received: " + movies.size());
                        Toast.makeText(getApplicationContext(), "Hello toast!", Toast.LENGTH_SHORT).show();

                    }
                }
            }

            @Override
            public void onFailure(Call<List<Result>> call, Throwable t) {
//                pb.setVisibility(ProgressBar.INVISIBLE);
                // Log error here since request failed
//                Log.e(TAG, t.toString());

                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();

                Log.d("error-------------->", t.getLocalizedMessage());

            }
        });


    }
}
