package com.example.cookbook.data;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.cookbook.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class MainAdapterRecipes extends BaseAdapter {


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
    public static final String KEY_Account = KEYS.KEY_Account;



    public MainAdapterRecipes(Context context, List<String> listGroup, String uuid) {
        this.context = context;
        this.listGroup = listGroup;
        this.uuid = uuid;
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
            convertView = layoutInflater.inflate(R.layout.list_group, null);
        }
        TextView textView = convertView.findViewById(R.id.list_parent);
        textView.setText(group);
        return convertView;
    }


    private void getFromMSP() {
        msp = new MySheredP(context);

        ArrayList<Recipe> AddsRecipes = new ArrayList<>();
        String data = msp.getString(KEY_Account, "NA");
        if (data != "NA") {
            account = new Gson().fromJson(data, Account.class);
            AddsRecipes = account.getRecipesAdds();
            account.setRecipesAdds(AddsRecipes);
        }
        for (int i = 0; i < account.getRecipesAdds().size(); i++) {
            allRecipesAdds.add(account.getRecipesAdds().get(i));
        }
    }


    private void putOnMSP() {
        String accountTemp = gson.toJson(account);
        msp.putString(KEY_Account, accountTemp);

    }


    public void cleanData() {
        account.clearRecipesAdds();
        allRecipesAdds.clear();
        listGroup.clear();
        allAccounts.clear();
    }
    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }

}


