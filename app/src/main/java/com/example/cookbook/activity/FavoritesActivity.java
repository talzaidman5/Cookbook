package com.example.cookbook.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cookbook.R;
import com.example.cookbook.data.MainAdapterFavorites;
import com.example.cookbook.data.MySheredP;
import com.example.cookbook.data.Recipe;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class FavoritesActivity extends AppCompatActivity {
    private Gson gson = new Gson();
    public static final String KEY_RecipeList = "recipe_list";
    private MySheredP msp;
    private MainAdapterFavorites adapter;
    private ListView favorites_recipe;
    private List listNew = new ArrayList();
    private String uuid;
    private Button back, addToFavorites;
    private ArrayList<String> favoritesRecipe = new ArrayList<String>();

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);
        findViews();
        msp = new MySheredP(this);
      uuid = android.provider.Settings.Secure.getString(
                this.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
        adapter = new MainAdapterFavorites(this, listNew, uuid);
        favorites_recipe.setAdapter(adapter);

        getFromMSP();
        favorites_recipe.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String tempName = listNew.get(position).toString();
                if (favoritesRecipe.contains(tempName)) {
                    addToFavorites.setBackgroundResource(R.drawable.favorites);
                    favoritesRecipe.remove(tempName);
                }
                else {
                    favoritesRecipe.add(tempName);
                    addToFavorites.setBackgroundResource(R.drawable.img_selected_favorites);
                }
                putOnMSP();
            }
        });


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNewActivityMain();
            }
        });

    }
    private void openNewActivityMain() {
        Intent intent = new Intent(this, allRecipe.class);
        startActivity(intent);
        finish();
    }

    public void findViews() {
        favorites_recipe = findViewById(R.id.favorites_recipe);
        back = findViewById(R.id.back);
        addToFavorites = findViewById(R.id.addToFavorites);

    }
    private void putOnMSP() {
        String accountTemp = gson.toJson(favoritesRecipe);
        msp.putString(Recipes_Favorites.KEY_FavoritesSelected, accountTemp);
    }


    private void getFromMSP() {
        String data = msp.getString(KEY_RecipeList, "NA");
        if(data!= "NA") {
            Recipe[] temp = gson.fromJson(data, Recipe[].class);

            for (int i = 0; i < temp.length; i++) {
                listNew.add(temp[i].getName());
            }
            listNew = checkDoubles();

            adapter = new MainAdapterFavorites(this, listNew, uuid);
            favorites_recipe.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }

    private List checkDoubles() {
        HashSet<String> hashSet = new HashSet<String>();
        hashSet.addAll(listNew);
        listNew.clear();
        listNew.addAll(hashSet);
        return   listNew;
    }

}