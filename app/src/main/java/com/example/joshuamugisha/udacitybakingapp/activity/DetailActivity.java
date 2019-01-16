package com.example.joshuamugisha.udacitybakingapp.activity;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.joshuamugisha.udacitybakingapp.BakingAppWidget;
import com.example.joshuamugisha.udacitybakingapp.R;
import com.example.joshuamugisha.udacitybakingapp.fragments.DetailActivityFragment;
import com.example.joshuamugisha.udacitybakingapp.model.Ingredient;
import com.example.joshuamugisha.udacitybakingapp.model.Result;
import com.example.joshuamugisha.udacitybakingapp.model.Step;

import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity {

    private boolean mTwoPane;
    private RecyclerView recyclerView;
    public Result bakingProcess;
    public List<Ingredient> recipeIngredient;
    public List<Step> recipeStep;
    public String recipeName;
    public ArrayList<Object> bakingObjects;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_activity);

        bakingObjects = new ArrayList<Object>();


        Intent intentThatStartedThisActivity = getIntent();
        if (intentThatStartedThisActivity.hasExtra("Recipe")) {

            bakingProcess = getIntent().getParcelableExtra("Recipe");
            recipeIngredient = bakingProcess.getIngredients();
            recipeStep = bakingProcess.getSteps();
            recipeName = bakingProcess.getName();
            bakingObjects.addAll(recipeIngredient);
            bakingObjects.addAll(recipeStep);

            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
            Bundle bundle = new Bundle();
            int appWidgetId = bundle.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
            BakingAppWidget.updateAppWidget(this, appWidgetManager, appWidgetId, recipeName,
                    recipeIngredient);


            //Toast.makeText(this, recipeStep + " ", Toast.LENGTH_SHORT).show();
            setTitle(recipeName);

        }else{
            Toast.makeText(this, "Data not available", Toast.LENGTH_SHORT).show();
        }


        if (findViewById(R.id.item_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
            Toast.makeText(this, "twopane", Toast.LENGTH_SHORT).show();
        }

        View recyclerView = findViewById(R.id.item_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);

    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {

        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(new RecipeAndStepAdapter(bakingObjects, mTwoPane));
    }


    public class RecipeAndStepAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        // The items to display in your RecyclerView
        private List<Object> dataSet;
        private static final int INGREDIENT = 0;
        private static final int STEP = 1;
        public  boolean isTwoPane;

        public RecipeAndStepAdapter(List<Object> dataSet, boolean isTwoPane) {
            this.dataSet = dataSet;
            this.isTwoPane = isTwoPane;
        }

        @Override
        public int getItemViewType(int position) {
            if (dataSet.get(position) instanceof Ingredient ) {
                return INGREDIENT;
            } else if (dataSet.get(position) instanceof Step) {
                return STEP;
            }
            return -1;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            RecyclerView.ViewHolder viewHolder;
            LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());

            switch (viewType) {
                case INGREDIENT:
                    View userView = inflater.inflate(R.layout.layout_view_holder_ingredient, viewGroup, false);
                    viewHolder = new IngredientViewHolder(userView);
                    break;
                case STEP:
                    View imageView = inflater.inflate(R.layout.layout_view_holder_step, viewGroup, false);
                    viewHolder = new StepViewHolder(imageView);
                    break;
                default:
                    View view = inflater.inflate(R.layout.layout_view_holder_step, viewGroup, false);
                    viewHolder = new StepViewHolder(view);
                    break;
            }
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
            switch (viewHolder.getItemViewType()) {
                case INGREDIENT:
                    IngredientViewHolder ingredientVh = (IngredientViewHolder) viewHolder;
                    configureIngredientViewHolder(ingredientVh, position);
                    break;
                case STEP:
                    StepViewHolder stepVh = (StepViewHolder) viewHolder;
                    configureStepViewHolder(stepVh, position);
                    break;
                default:
                    StepViewHolder defaultVh = (StepViewHolder) viewHolder;
                    configureStepViewHolder(defaultVh, position);
                    break;
            }
        }

        private void configureIngredientViewHolder(IngredientViewHolder ingredientVh, int position) {
            Ingredient ingredient = (Ingredient) dataSet.get(position);

            if (ingredient != null) {
                Double quant = ingredient.getQuantity();

                ingredientVh.getIngredient().setText( ingredient.getIngredient());
                ingredientVh.getMeasure().setText( ingredient.getMeasure());
                ingredientVh.getQuantity().setText( Double.toString(quant));
            }
        }

        private void configureStepViewHolder(StepViewHolder stepVh, int position) {
            Step step = (Step) dataSet.get(position);
            if (step != null) {
                stepVh.getShortDesc().setText(step.getShortDescription());
            }
        }


        @Override
        public int getItemCount() {
            return this.dataSet.size();
        }

        public class StepViewHolder extends RecyclerView.ViewHolder {

            public TextView shortDesc, desc, videoUrl;

            public StepViewHolder(View view) {
                super(view);
                shortDesc = (TextView) view.findViewById(R.id.short_description);
                view.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        int pos = getAdapterPosition();
                        if (pos != RecyclerView.NO_POSITION) {
                            if (isTwoPane) {
                                Step clickedDataItem = (Step)dataSet.get(pos);
                                Bundle arguments = new Bundle();
                                arguments.putParcelable("Steps", clickedDataItem);
                                DetailActivityFragment fragment = new DetailActivityFragment();
                                fragment.setArguments(arguments);
                                getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.item_detail_container, fragment)
                                        .commit();
                            } else {
                                Context context = v.getContext();
                                Step clickedDataItem = (Step)dataSet.get(pos);
                                Intent intent = new Intent(context, VideoActivity.class);
                                intent.putExtra("Steps", clickedDataItem);
                                context.startActivity(intent);
                            }
                        }

                    }
                });
            }

            public TextView getShortDesc() {
                return this.shortDesc;
            }

            public void setShortDesc(TextView shortDesc) {
                this.shortDesc = shortDesc;
            }


        }

        public class IngredientViewHolder extends RecyclerView.ViewHolder {

            public TextView ingredient, measure, quantity;

            public IngredientViewHolder(View view) {
                super(view);
                ingredient = (TextView) view.findViewById(R.id.name);
                measure = (TextView)view.findViewById(R.id.measure);
                quantity = (TextView) view.findViewById(R.id.quantity);
            }

            public TextView getIngredient() {
                return this.ingredient;
            }

            public void setIngredient(TextView ingredient) {
                this.ingredient = ingredient;
            }

            public TextView getMeasure() {
                return this.measure;
            }

            public void setMeasure(TextView measure) {
                this.measure = measure;
            }

            public TextView getQuantity() {
                return this.quantity;
            }

            public void setQuantity(TextView quantity) {
                this.quantity = quantity;
            }

        }

    }
}
