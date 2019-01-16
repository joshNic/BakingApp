package com.example.joshuamugisha.udacitybakingapp;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.RemoteViews;

import com.example.joshuamugisha.udacitybakingapp.model.Ingredient;
import com.example.joshuamugisha.udacitybakingapp.model.Result;
import com.google.gson.Gson;

import java.util.List;

/**
 * Implementation of App Widget functionality.
 */
public class BakingAppWidget extends AppWidgetProvider {
    SharedPreferences sharedPreferences;

    public static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId, String recipeName, List<Ingredient> ingredientList) {


        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.baking_app_widget);

        views.setTextViewText(R.id.appwidget_text, recipeName);

        for (Ingredient ingredient : ingredientList) {
            views.setTextViewText(R.id.appwidgetIngredients, ingredient.getIngredient() + "" + ingredient.getMeasure() + "" + ingredient.getQuantity());
        }


        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        sharedPreferences = context.getSharedPreferences("SHARED_PREFERENCES",
                Context.MODE_PRIVATE);
        String result = sharedPreferences.getString("WIDGET_RESULT", null);
        Gson gson = new Gson();
        Result recipe = gson.fromJson(result, Result.class);
        String recipeName = recipe.getName();
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, recipeName, recipe.getIngredients());
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

