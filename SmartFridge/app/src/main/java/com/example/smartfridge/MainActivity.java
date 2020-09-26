package com.example.smartfridge;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
//    OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TaskProvider taskProvider = new TaskProvider();

        // Navigation bar
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        NavController navController = Navigation.findNavController(this, R.id.fragment);
//        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(R.id.ingredientFragment, R.id.profileFragment, R.id.recipeFragment, R.id.scanFragment, R.id.shoppingFragment).build();
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);


        // Initiate database
        try {
            SQLiteDatabase sqLiteDatabase = this.openOrCreateDatabase("smartfridge", MODE_PRIVATE, null);

            // Create schema for table that saves user's food inventory
//            sqLiteDatabase.execSQL("DROP TABLE FactFridge");
            if (taskProvider.checkForTableNotExists(sqLiteDatabase, "FactFridge"))
            {
                sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS FactFridge (ID INTEGER PRIMARY KEY, Timestamp DATETIME DEFAULT CURRENT_TIMESTAMP, IngredientName VARCHAR, Amount INT(5), Unit VARCHAR, ImageID VARCHAR, InFridge INT(1), ExpirationDate VARCHAR)");
                sqLiteDatabase.execSQL("INSERT INTO FactFridge (IngredientName, Amount, Unit, ImageID, InFridge, ExpirationDate) VALUES ('strawberry',3, 'box', 'strawberry.png', 1, '30/09/2020')");
                sqLiteDatabase.execSQL("INSERT INTO FactFridge (IngredientName, Amount, Unit, ImageID, InFridge, ExpirationDate) VALUES ('steak',2, 'lbs', 'steak.png', 1, '30/09/2020')");
                sqLiteDatabase.execSQL("INSERT INTO FactFridge (IngredientName, Amount, Unit, ImageID, InFridge, ExpirationDate) VALUES ('asparagus',1, 'bunch', 'asparagus.png', 1, '30/09/2020')");
                sqLiteDatabase.execSQL("INSERT INTO FactFridge (IngredientName, Amount, Unit, ImageID, InFridge, ExpirationDate) VALUES ('peach',1, 'fruit', 'peach.png', 1, '30/09/2020')");
            }

            // Create schema and data for table that saves ingredients within app's inventory
            if (taskProvider.checkForTableNotExists(sqLiteDatabase, "DimIngredient"))
            {
                sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS DimIngredient (ID INTEGER PRIMARY KEY, IngredientName VARCHAR, Category VARCHAR, Perishable INT, EstimatedPerishDay INT(4))");
                sqLiteDatabase.execSQL("INSERT INTO DimIngredient (IngredientName, Category, Perishable, EstimatedPerishDay) VALUES ('ground cinnamon', 'Condiment', 0, NULL)");
                sqLiteDatabase.execSQL("INSERT INTO DimIngredient (IngredientName, Category, Perishable, EstimatedPerishDay) VALUES ('eggs', 'Meat', 1, 14)");
                sqLiteDatabase.execSQL("INSERT INTO DimIngredient (IngredientName, Category, Perishable, EstimatedPerishDay) VALUES ('ground beef', 'Meat', 1, 14)");
                sqLiteDatabase.execSQL("INSERT INTO DimIngredient (IngredientName, Category, Perishable, EstimatedPerishDay) VALUES ('strawberry', 'Fruit', 1, 5)");
                sqLiteDatabase.execSQL("INSERT INTO DimIngredient (IngredientName, Category, Perishable, EstimatedPerishDay) VALUES ('steak', 'Meat', 1, 14)");
                sqLiteDatabase.execSQL("INSERT INTO DimIngredient (IngredientName, Category, Perishable, EstimatedPerishDay) VALUES ('asparagus', 'Vegetable', 1, 5)");
                sqLiteDatabase.execSQL("INSERT INTO DimIngredient (IngredientName, Category, Perishable, EstimatedPerishDay) VALUES ('peach', 'Fruit', 1, 10)");
            }

            // Create schema and data for table that saves recipes within app's inventory
            if (taskProvider.checkForTableNotExists(sqLiteDatabase, "DimRecipe")) {
                sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS DimRecipe (ID INTEGER PRIMARY KEY, Title VARCHAR, SourceName VARCHAR, ReadyInMinutes INT(3), Servings INT(2), SourceUrl VARCHAR)");
                sqLiteDatabase.execSQL("INSERT INTO DimRecipe (Title, SourceName, ReadyInMinute, Servings, SourceUrl) VALUES ('400-Calorie Breakfasts Peach Parfait ','Martha Stewart',7,1,'http://www.prevention.com/food/healthy-recipes/400-calorie-breakfasts?s=6')");
                sqLiteDatabase.execSQL("INSERT INTO DimRecipe (Title, SourceName, ReadyInMinute, Servings, SourceUrl) VALUES ('Sloppy Joe Casserole','Cravings of a Lunatic',30,6,'http://www.cravingsofalunatic.com/2013/10/sloppy-joe-casserole.html')");
            }

            // Create view showing all items expiring within 3 days
//            sqLiteDatabase.execSQL("DROP VIEW ItemsExp3Days");
            sqLiteDatabase.execSQL("CREATE VIEW IF NOT EXISTS ItemsExpDays (IngredientName, ExpMonth, TimeDelta) AS SELECT IngredientName, strftime('%m', ExpirationDate), JulianDay(substr(ExpirationDate, 7) || \"-\" || substr(ExpirationDate,4,2)  || \"-\" || substr(ExpirationDate, 1,2)) - JulianDay('now') FROM FactFridge WHERE InFridge = 1;");

            Cursor c = sqLiteDatabase.rawQuery("SELECT * FROM ItemsExpDays", null);
            int IngredientNameIndex = c.getColumnIndex("IngredientName");
            int TimeDeltaIndex = c.getColumnIndex("TimeDelta");
            c.moveToFirst();

            while (c != null) {
                Log.i("IngredientName ", c.getString(IngredientNameIndex));
                Log.i("TimeDelta ", Integer.toString(c.getInt(TimeDeltaIndex)));

                c.moveToNext();
            }
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // GET request to find recipes by ingredients
//        get("https://spoonacular-recipe-food-nutrition-v1.p.rapidapi.com/recipes/findByIngredients?number=5&ranking=1&ignorePantry=false&ingredients=apples%252Cflour%252Csugar", "", new Callback() {
//            @Override
//            public void onFailure(Request request, IOException e) {
//                Log.d("----Rest Response Fail", e.toString());
//            }
//            @Override
//            public void onResponse(Response response) throws IOException {
//                if (response.isSuccessful()) {
//                    String responseStr = response.body().string();
//                    Log.d("----Rest Response", responseStr);
//                } else {
//                    Log.d("----Rest Response Fail", response.toString());
//                }
//            }
//        });
    }

    // Func: Get request to Spoonacular API
//    Call get(String url, String json, Callback callback) {
//        Request request = new Request.Builder()
//                .url(url)
//                .get()
//                .addHeader("x-rapidapi-host", "spoonacular-recipe-food-nutrition-v1.p.rapidapi.com")
//                .addHeader("x-rapidapi-key", "895ce719e4mshcb836fa18684a5ap1c69f2jsnf7e37492c80d")
//                .build();
//        Call call = client.newCall(request);
//        call.enqueue(callback);
//        return call;
//    }
}