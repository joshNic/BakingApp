package com.example.joshuamugisha.udacitybakingapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.joshuamugisha.udacitybakingapp.R;
import com.example.joshuamugisha.udacitybakingapp.activity.DetailActivity;
import com.example.joshuamugisha.udacitybakingapp.model.Result;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.MyViewHolder> {

    private Context mContext;
    private List<Result> recipeList;
    private Gson gson;
    private SharedPreferences sharedPreferences;


    public ResultAdapter(Context mContext, List<Result> recipeList) {
        this.recipeList = recipeList;
        this.mContext = mContext;
        sharedPreferences = mContext.getSharedPreferences("SHARED_PREFERENCES",
                Context.MODE_PRIVATE);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recipe_card, parent, false);

        return new MyViewHolder(view);
    }



    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Result recipe = recipeList.get(position);
        holder.name.setText(recipe.getName());

    }


    @Override
    public int getItemCount(){
        return recipeList.size();

    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView name;
        CardView recipeCard;

        public MyViewHolder(View view){

            super(view);
            name = (TextView) view.findViewById(R.id.recipeName);
            recipeCard = (CardView) view.findViewById(R.id.recipeCard);

            view.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION){
                        Result clickedDataItem = recipeList.get(pos);
                        gson = new Gson();
                        sharedPreferences.edit().putString("WIDGET_RESULT", gson.toJson(clickedDataItem)).apply();
                        Intent intent = new Intent(mContext, DetailActivity.class);
                        intent.putExtra("Recipe", clickedDataItem);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mContext.startActivity(intent);
                        Toast.makeText(v.getContext(), "You clicked " + clickedDataItem.getName(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

}
