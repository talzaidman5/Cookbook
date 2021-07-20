package com.example.cookbook.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cookbook.R;
import com.example.cookbook.data.Account;
import com.example.cookbook.data.MySheredP;
import com.example.cookbook.data.allAccounts;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;


public class allRecipe extends AppCompatActivity {

    public static final String KEY_Account = "account";
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("message");
    public String uuid;
    private ArrayList<Account> allAccounts = new ArrayList<>();
    private com.example.cookbook.data.allAccounts tempAllAccounts = new allAccounts();
    private Account account = new Account();
    private MySheredP msp;
    private Gson gson = new Gson();
    private Button all_BTN_first,all_BTN_adds,all_BTN_main,all_BTN_dessert,back,all_BTN_favorites,all_BTN_favoritesSelected,all_BTN_fromHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_all_recipe);

        findViews();

        msp = new MySheredP(this);

        uuid = android.provider.Settings.Secure.getString(
                this.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);

//        initListData();
        all_BTN_dessert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNewActivityDessert();
            }
        });


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNewActivityMain();
            }
        });
        all_BTN_fromHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), IngredientsFromKitchen.class);
                startActivity(intent);
            }
        });
        all_BTN_first.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNewActivityFirsts();
            }
        });

        all_BTN_adds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNewActivityAdds();
            }
        });

        all_BTN_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNewActivityRecipesMain();
            }
        });
        all_BTN_favorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), FavoritesActivity.class);
                startActivity(intent);
                finish();
            }
        });
        all_BTN_favoritesSelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Recipes_Favorites.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void findViews() {
        all_BTN_first = findViewById(R.id.all_BTN_first);
        all_BTN_adds = findViewById(R.id.all_BTN_adds);
        all_BTN_main = findViewById(R.id.all_BTN_main);
        all_BTN_dessert = findViewById(R.id.all_BTN_dessert);
        all_BTN_favorites = findViewById(R.id.all_BTN_favorites);
        back = findViewById(R.id.back);
        all_BTN_favoritesSelected = findViewById(R.id.all_BTN_favoritesSelected);
        all_BTN_fromHome = findViewById(R.id.all_BTN_fromHome);
    }

    private void openNewActivityFirsts() {
        Intent intent = new Intent(this, Recipes_Firsts.class);
        startActivity(intent);
        finish();
    }

    private void openNewActivityAdds() {
        Intent intent = new Intent(this, Recipes_Adds.class);
        startActivity(intent);
        finish();
    }

    private void openNewActivityRecipesMain() {
        Intent intent = new Intent(this, com.example.cookbook.activity.Recipes_Main.class);
        startActivity(intent);
        finish();
    }

    private void openNewActivityDessert() {
        Intent intent = new Intent(this, Recipes_Dessert.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        openNewActivityMain();
    }

    private void openNewActivityMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }


    private void initListData() {
        myRef.child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        Account tempAccount = ds.getValue(Account.class);
                        allAccounts.add(tempAccount);
                    }
                    if (tempAllAccounts != null) {
                        tempAllAccounts.setAllAccounts(allAccounts);
                        account = tempAllAccounts.getAccountByUUid(uuid);
                        putOnMSP();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void putOnMSP() {
        if(account == null) {
            account = new Account(getContentResolver());
        }
        String accountTemp = gson.toJson(account);
        msp.putString(KEY_Account, accountTemp);
    }


}



