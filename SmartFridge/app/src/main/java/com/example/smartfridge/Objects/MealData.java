package com.example.smartfridge.Objects;
import android.graphics.Bitmap;
import com.example.smartfridge.DbBitmapUtility;

public class MealData {
    private String mealName;
    private String mealDescription;
    private String mealImage;

    public MealData(String mealName, String mealDescription, String mealImage) {
        setMealName(mealName);
        setMealDescription(mealDescription);
        setMealImage(mealImage);
    }

    // Getter Methods
    public String getMealName() {
        return this.mealName;
    }

    public String getMealDescription() {
        return this.mealDescription;
    }

    public String getMealImage() {
        return this.mealImage;
    }

    // Setter Methods
    public void setMealName(String mealName) {
        this.mealName = mealName;
    }

    public void setMealDescription(String mealDescription) {
        this.mealDescription = mealDescription;
    }

    public void setMealImage(String mealImage) {
        this.mealImage = mealImage;
    }
}
