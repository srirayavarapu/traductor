package com.transjan.traductor;


import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    Spinner listTheme;
    Button saveSettings;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);


        listTheme = (Spinner) findViewById(R.id.list_theme);
        saveSettings = (Button) findViewById(R.id.save_settings);
        context = this;
        appluTheme();

        saveSettings.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                GetServices.saveMainColor(context, String.valueOf(listTheme.getSelectedItem()));
                appluTheme();
            }

        });

    }


    public void appluTheme() {
        int savedColor = getResources().getColor(getResources().getIdentifier(GetServices.getSavedColor(this), "color", getPackageName()));
        LinearLayout header = (LinearLayout) findViewById(R.id.header);
        GetServices.changeBgColor(header, savedColor);
        GetServices.changeBgColor(saveSettings, savedColor);
    }

}
