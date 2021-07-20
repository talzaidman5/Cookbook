package com.example.cookbook.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cookbook.R;
import com.example.cookbook.data.MainAdapterRecipes;
import com.example.cookbook.data.MySheredP;
import com.example.cookbook.data.Recipe;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Recipes_Favorites extends AppCompatActivity {
    private static final String KEY_RECIPE = "Recipe";
    private ListView dessert_recipe;
    private MainAdapterRecipes adapter;
    private String uuid;
    private MySheredP msp;
    private Gson gson = new Gson();
    private Recipe chosenRecipe;
    private List listNew = new ArrayList();
    private Button back,add;
    private ArrayList<String> favoritesRecipe = new ArrayList<String>();
    public static final String KEY_FavoritesSelected = "favoritesSelected";
    private Recipe[] allRecipes;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_recipes_favorites);

        uuid = android.provider.Settings.Secure.getString(
                this.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
        msp = new MySheredP(this);

        findViews();
        getFromMSP();

        dessert_recipe.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String tempName = listNew.get(position).toString();
                chosenRecipe = getRecipeByNameDessert(tempName);
                openNewActivityReadRecipe();
            }
        });


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNewActivityMain();
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNewActivityNewRecipe();
            }
        });
    }

    private void openNewActivityNewRecipe() {
        Intent intent = new Intent(this, newRecipe.class);
        startActivity(intent);
        finish();
    }
    public void findViews() {
        dessert_recipe = findViewById(R.id.dessert_recipe);
        back = findViewById(R.id.back);
        add = findViewById(R.id.add);

    }

    @Override
    public void onBackPressed() {
        openNewActivityMain();
    }

    private void openNewActivityMain() {
        Intent intent = new Intent(this, allRecipe.class);
        startActivity(intent);
        finish();
    }


    private void openNewActivityReadRecipe() {
        Intent intent = new Intent(this, ReadRecipeFavorites.class);
        String s = new Gson().toJson(chosenRecipe);
        intent.putExtra(KEY_RECIPE, s);
        startActivity(intent);
        finish();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void getFromMSP() {

       String temp = msp.getString(FavoritesActivity.KEY_RecipeList, "NA");
        if(temp!= "NA") {
             allRecipes = gson.fromJson(temp, Recipe[].class);

            String favoritesSelected = msp.getString(KEY_FavoritesSelected, "NA");
            if(!favoritesSelected.equals("NA"))
                favoritesRecipe = gson.fromJson(favoritesSelected, ArrayList.class);

            for (int i = 0; i < favoritesRecipe.size(); i++) {
                listNew.add(favoritesRecipe.get(i));
            }
            listNew = checkDoubles();

            adapter = new MainAdapterRecipes(this, listNew, uuid);
            dessert_recipe.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }
    public Recipe getRecipeByNameDessert(String name) {
        for (int i = 0; i < allRecipes.length; i++)
            if (allRecipes[i].getName().equals(name))
                return allRecipes[i];
        return null;
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    private List checkDoubles() {

        HashSet<String> hashSet = new HashSet<String>();
        hashSet.addAll(listNew);
        listNew.clear();
        listNew.addAll(hashSet);
        return listNew;
    }

}