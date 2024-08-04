package com.transjan.traductor;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SecondStartActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_second);

        findViewById(R.id.startAppButton).setOnClickListener(v ->
                startActivity(new Intent(this, MainActivity.class))
        );

        findViewById(R.id.shareAppButton).setOnClickListener(v ->
                shareAppLink()
        );

        findViewById(R.id.rateAppButton).setOnClickListener(v ->
                rateApp()
        );

        findViewById(R.id.privacyPolicyButton).setOnClickListener(v ->
                openURL()
        );
    }

    private void shareAppLink() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        String appLink = "https://play.google.com/store/apps/details?id=" + getPackageName(); // Replace with your app's actual link
        String shareMessage = "Check out this cool app: " + appLink;
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
        startActivity(Intent.createChooser(shareIntent, "Share App via"));
    }

    private void rateApp() {
        try {
            Uri uri = Uri.parse("market://details?id=" + getPackageName());
            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
            goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                    Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
            startActivity(goToMarket);
        } catch (Exception e) {
            Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName());
            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(goToMarket);
        }
    }

    private void openURL() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(getResources().getString(R.string.privacy_policy_url)));
        startActivity(intent);
    }
}
