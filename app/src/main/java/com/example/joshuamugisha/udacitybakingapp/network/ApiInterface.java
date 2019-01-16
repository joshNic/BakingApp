package com.example.joshuamugisha.udacitybakingapp.network;

import com.example.joshuamugisha.udacitybakingapp.model.Result;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {
    @GET("topher/2017/May/59121517_baking/baking.json")
    Call<List<Result>> recipieData();
}
