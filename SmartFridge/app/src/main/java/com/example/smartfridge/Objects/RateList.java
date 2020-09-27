package com.example.smartfridge.Objects;

import java.util.ArrayList;

public class RateList {

    public int id;
    public String title;
    public String image;
    public String imageType;
    public int usedIngredientCount;
    public int missedIngredientCount;
    public String likes;

    public ArrayList<Ingredient> missedIngredients;
    public ArrayList<Ingredient> usedIngredients;

}
