package com.example.smartfridge;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartfridge.Objects.MealAdapter;
import com.example.smartfridge.Objects.MealData;
import c;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;

import java.io.IOException;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RecipeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecipeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public RecipeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RecipeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RecipeFragment newInstance(String param1, String param2) {
        RecipeFragment fragment = new RecipeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_recipe, container, false);

        RecyclerView recyclerView = v.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // GET request to find recipes by ingredients
        get("https://spoonacular-recipe-food-nutrition-v1.p.rapidapi.com/recipes/findByIngredients?number=5&ranking=1&ignorePantry=false&ingredients=apples%252Cflour%252Csugar", "", new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Log.d("----Rest Response Fail", e.toString());
            }
            @Override
            public void onResponse(Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseStr = response.body().string();
                    JSONArray jsonArray = new JSONArray(response);

                    Log.d("----Rest Response", responseStr);
                } else {
                    Log.d("----Rest Response Fail", response.toString());
                }
            }
        });

        // Generate meal planner
        MealData[] mealData = new MealData[]{
                new MealData("meal1", "descp1", R.drawable.ava),
                new MealData("meal2", "descp2", R.drawable.ava),
                new MealData("meal1", "descp1", R.drawable.ava),
                new MealData("meal2", "descp2", R.drawable.ava),
                new MealData("meal1", "descp1", R.drawable.ava),
                new MealData("meal2", "descp2", R.drawable.ava),
                new MealData("meal1", "descp1", R.drawable.ava),
                new MealData("meal2", "descp2", R.drawable.ava)
        };


        MealAdapter myMovieAdapter = new MealAdapter(mealData);
        recyclerView.setAdapter(myMovieAdapter);

        return v;
    }

    OkHttpClient client = new OkHttpClient();
    // Func: Get request to Spoonacular API
    Call get(String url, String json, Callback callback) {
        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("x-rapidapi-host", "spoonacular-recipe-food-nutrition-v1.p.rapidapi.com")
                .addHeader("x-rapidapi-key", "895ce719e4mshcb836fa18684a5ap1c69f2jsnf7e37492c80d")
                .build();
        Call call = client.newCall(request);
        call.enqueue(callback);
        return call;
    }

}