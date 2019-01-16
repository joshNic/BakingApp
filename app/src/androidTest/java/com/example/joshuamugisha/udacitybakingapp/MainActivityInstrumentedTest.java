package com.example.joshuamugisha.udacitybakingapp;


import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.joshuamugisha.udacitybakingapp.activity.MainActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class MainActivityInstrumentedTest {
    @Rule
    public ActivityTestRule<MainActivity> activity = new ActivityTestRule<MainActivity>(MainActivity.class);

    @Test
    public void RecyclerViewIsShown(){
        onView(withId(R.id.recipeRecyclerView)).check(matches(isDisplayed()));
    }
    @Test
    public void recyclerViewItems(){
        onView(withId(R.id.recipeRecyclerView)).perform(RecyclerViewActions.scrollToPosition(1));
    }
    @Test
    public void recyclerViewItemsClick(){
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {

        }
        onView(withId(R.id.recipeRecyclerView))

                .perform(
                        RecyclerViewActions.scrollToPosition(1)
                ).perform(click());
    }



}
