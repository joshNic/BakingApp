package com.example.joshuamugisha.udacitybakingapp.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.joshuamugisha.udacitybakingapp.adapters.ResultAdapter;
import com.example.joshuamugisha.udacitybakingapp.model.Result;
import com.example.joshuamugisha.udacitybakingapp.network.ApiClient;
import com.example.joshuamugisha.udacitybakingapp.network.ApiInterface;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RecipeViewModel extends ViewModel {

    //this is the data that we will fetch asynchronously
    private MutableLiveData<List<Result>> ResultList;

    //we will call this method to get the data
    public LiveData<List<Result>> getHeroes() {
        //if the list is null
        if (ResultList == null) {
            ResultList = new MutableLiveData<List<Result>>();
            //we will load it asynchronously from server in this method
            loadHeroes();
        }

        //finally we will return the list
        return ResultList;
    }


    //This method is using Retrofit to get the JSON data from URL
    private void loadHeroes() {
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
//                        List<Result> recipies = response.body();
                        ResultList.setValue(response.body());
//                        recyclerView.setAdapter(new ResultAdapter(getApplicationContext(), recipies));
//                      Log.d("TAG", "<<<<<<<<<<<<<<<<<Number of movies received:>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> ");
//                        Toast.makeText(getApplicationContext(), "Hello toast!", Toast.LENGTH_SHORT).show();

                    }
                }
            }

            @Override
            public void onFailure(Call<List<Result>> call, Throwable t) {
//                pb.setVisibility(ProgressBar.INVISIBLE);
                // Log error here since request failed
//                Log.e(TAG, t.toString());

//                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();

//                Log.d("error-------------->", t.getLocalizedMessage());

            }
        });


    }
}
