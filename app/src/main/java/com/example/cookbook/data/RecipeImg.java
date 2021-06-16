package com.example.cookbook.data;

import android.widget.ImageView;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RecipeImg {
    private String name;
    private int type;
    private ImageView image;


    public RecipeImg(String name, int type, ImageView image) {
        this.name = name;
        this.type = type;
        this.image = image;

    }

    public RecipeImg(RecipeImg other) {
        this.name = other.name;
        this.type = other.type;
        this.image = other.image;
    }

    public RecipeImg() {
    }

    public String RecipesToString(RecipeImg temp){
        return temp.getName()+": \n" + temp.toString();

    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecipeImg recipe = (RecipeImg) o;
        return Objects.equals(name, recipe.name);
    }

}
