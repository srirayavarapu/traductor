package com.transjan.traductor;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Set;

public class FavoritesActivity extends AppCompatActivity {
    ListView favoritesListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        appluTheme();

        ArrayList<String> favoritesList = new ArrayList<String>();

        favoritesListView = (ListView) findViewById(R.id.favorite_list);

        Set<String> favoriteList = GetServices.getFavoriteList(this);
        for (String entry : favoriteList) {
            favoritesList.add(entry);
        }


        favoritesListView.setAdapter(new FavoriteAdapter(this, favoritesList));

    }


    public void appluTheme() {
        try {
            int savedColor = getResources().getColor(getResources().getIdentifier(GetServices.getSavedColor(this), "color", getPackageName()));
            LinearLayout header = (LinearLayout) findViewById(R.id.header);
            GetServices.changeBgColor(header, savedColor);
        } catch (Exception e) {

        }

    }
}
