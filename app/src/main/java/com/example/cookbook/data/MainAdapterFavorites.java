package com.example.cookbook.data;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.cookbook.R;
import com.example.cookbook.activity.Recipes_Favorites;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class MainAdapterFavorites extends BaseAdapter {


    Context context;
    List<String> listGroup = new ArrayList<>();;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("message");

    private ArrayList<Account> allAccounts = new ArrayList<>();
    private Account account = new Account();
    private ArrayList<Recipe> allRecipesAdds = new ArrayList<>();
    private String uuid;
    private MySheredP msp;
    private Gson gson = new Gson();
    private Button  addToFavorites;
    private ArrayList<String> favoritesRecipe = new ArrayList<String>();

    public MainAdapterFavorites(Context context, List<String> listGroup, String uuid) {
        this.context = context;
        this.listGroup = listGroup;
        this.uuid = uuid;
    }
    public void findViews(View v) {
        addToFavorites = v.findViewById(R.id.addToFavorites);
    }

    @Override
    public int getCount() {
        return listGroup.size();
    }

    @Override
    public Object getItem(int position) {
        return this.listGroup.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String group = (String) getItem(position);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_item_favorites, null);
        }
        msp = new MySheredP(context.getApplicationContext());
        favoritesRecipe = getFromMSP();
        TextView textView = convertView.findViewById(R.id.list_parent1);
        Button favorites = convertView.findViewById(R.id.addToFavorites);
        textView.setText(group);
        findViews(convertView);
        if (favoritesRecipe.contains(textView.getText().toString())) {
            favorites.setBackgroundResource(R.drawable.img_selected_favorites);
        }
        else {
            favorites.setBackgroundResource(R.drawable.favorites);
        }
        favorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (favoritesRecipe.contains(textView.getText().toString())) {
                    favorites.setBackgroundResource(R.drawable.favorites);
                    favoritesRecipe.remove(textView.getText().toString());
                }
                else {
                    favoritesRecipe.add(textView.getText().toString());
                    favorites.setBackgroundResource(R.drawable.img_selected_favorites);
                }
                putOnMSP();
            }
        });
        return convertView;
    }

    private ArrayList<String> getFromMSP() {
        ArrayList<String> temp = new ArrayList<>();
        String data = msp.getString(Recipes_Favorites.KEY_FavoritesSelected, "NA");
        if(data!= "NA")
            temp = gson.fromJson(data, ArrayList.class);
        return temp;
    }

    private void putOnMSP() {
        String accountTemp = gson.toJson(favoritesRecipe);
        msp.putString(Recipes_Favorites.KEY_FavoritesSelected, accountTemp);
    }
}


