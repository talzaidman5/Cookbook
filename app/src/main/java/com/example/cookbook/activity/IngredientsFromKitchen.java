package com.example.cookbook.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cookbook.R;
import com.example.cookbook.data.Account;
import com.example.cookbook.data.MySheredP;
import com.example.cookbook.data.Recipe;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;

public class IngredientsFromKitchen extends AppCompatActivity {
    public static final String KEY_FromHome = "fromHome";

    private Button new_BTN_add, new_add_one_ingr,back;
    private ListView listView_ingredient;
    private ArrayList<String> stringArrayList;
    private ArrayAdapter<String> stringArrayAdapter;
    private EditText new_EDIT_ingredient;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference("message");
    private MySheredP msp;
    private Gson gson = new Gson();
    private ArrayList<Recipe> allRecipes = new ArrayList<>();
    private ArrayList<Recipe> allRecipesHome = new ArrayList<>();
    public String uuid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredients_from_kitchen);
        stringArrayList = new ArrayList<>();
        stringArrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, stringArrayList);

        msp = new MySheredP(this);
        uuid = android.provider.Settings.Secure.getString(
                this.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
        checkRecipes();
        findViews();
        listView_ingredient.setAdapter(stringArrayAdapter);

        new_BTN_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (Recipe recipe: allRecipes) {
                    if (stringArrayList.size() > 0 && recipe.getIngredient().size() >0) {
                        if (stringArrayList.containsAll(recipe.getIngredient()))
                            allRecipesHome.add(recipe);
                    }
                }
                String allRecipesFromHome = gson.toJson(allRecipesHome);
                msp.putString(KEY_FromHome, allRecipesFromHome);

                Intent intent = new Intent(getApplicationContext(), Recipes_FromHome.class);
                startActivity(intent);
                finish();

            }
        });
        new_add_one_ingr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (new_EDIT_ingredient.getText().length()>0) {
                    listView_ingredient.setCacheColorHint(Color.WHITE);
                    stringArrayList.add(new_EDIT_ingredient.getText().toString());
                    stringArrayAdapter.notifyDataSetChanged();
                    new_EDIT_ingredient.getText().clear();
                }
                else
                {
                    new_EDIT_ingredient.setError("לא יכול להיות ריק");
                }
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void checkRecipes() {
        myRef.child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        Account tempRecipe = ds.getValue(Account.class);
                        allRecipes.addAll(tempRecipe.getRecipesAdds());
                        allRecipes.addAll(tempRecipe.getRecipesDessert());
                        allRecipes.addAll(tempRecipe.getRecipesFirsts());
                        allRecipes.addAll(tempRecipe.getRecipesMain());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    private void findViews(){
        new_BTN_add = findViewById(R.id.new_BTN_add);
        listView_ingredient = findViewById(R.id.listView_ingredient);
        new_add_one_ingr = findViewById(R.id.new_add_one_ingr);
        back = findViewById(R.id.back);
        new_EDIT_ingredient = findViewById(R.id.new_EDIT_ingredient);

    }
}