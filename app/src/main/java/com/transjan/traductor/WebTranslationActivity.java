package com.transjan.traductor;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.ironsource.mediationsdk.IronSource;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class WebTranslationActivity extends AppCompatActivity {

    Context mContext;
    EditText urlText;
    String url;
    Spinner spinnerBtn;
    Button goBtn;
    CircleImageView okBtnFlag;
    WebView webView;
    String currentLangage = "es";
    ArrayList<String> supportedLanguages;
    ArrayList<String> supportedLanguageNames;

    private AdView mBannerAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_translation);


        urlText = (EditText) findViewById(R.id.url_text);
        spinnerBtn = (Spinner) findViewById(R.id.ok_btn);
        goBtn = (Button) findViewById(R.id.go_btn);
        okBtnFlag = (CircleImageView) findViewById(R.id.ok_btn_flag);
        webView = (WebView) findViewById(R.id.webView);
        webView.loadData("", "text/html", "UTF-8");
        mContext = this;

        appluTheme();


        currentLangage = GetServices.getObject(this, "browserLanguage", "");


        if (currentLangage.compareToIgnoreCase("") == 0) {
            currentLangage = GetServices.getObject(this, "currentLangage", "en");

            if (currentLangage.compareToIgnoreCase("en") == 0) {
                currentLangage = "es";
            } else {
                currentLangage = "en";
            }

        }


        cc(currentLangage);

        updateIcon();
        initAds();

        spinnerBtn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String LangName = "";
                if (supportedLanguages.size() != 0) {
                    LangName = supportedLanguages.get(position);

                    if (!LangName.equals("")) {
                        currentLangage = LangName.split("-")[0];
                        updateIcon();
                        GetServices.saveObject(mContext, "browserLanguage", currentLangage);
                    }
                } else {
                    GetServices.ShowsnackBar(getWindow().getDecorView().findViewById(android.R.id.content), getString(R.string.please_try_1));
                }
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        urlText.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    return true;
                }
                return false;
            }
        });


        goBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadWebSite();
            }
        });
    }

    void initAds() {
        mBannerAd = (AdView) findViewById(R.id.adView);
        AdView adView = new AdView(this);
        adView.setAdSize(AdSize.SMART_BANNER);
        ShowBannerAds();


    }

    public void ShowBannerAds() {
        AdRequest adRequest = new AdRequest.Builder()
                .build();
        mBannerAd.loadAd(adRequest);
    }


    private void createNewIntAd() {

    }


    public void updateIcon() {
        int id = getResources().getIdentifier("pic" + currentLangage, "drawable", getPackageName());
        okBtnFlag.setImageResource(id);
    }


    public void cc(String languagePreference) {
//        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, supportedLanguageNames);
//        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        supportedLanguages = new ArrayList<String>();
        supportedLanguageNames = new ArrayList<String>();

        supportedLanguageNames = GetServices.getLanguageListNames(mContext);
        supportedLanguages = GetServices.getLanguageListSymbs();


        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(mContext, R.layout.spinner_text, supportedLanguageNames);
        dataAdapter.setDropDownViewResource(R.layout.spinner_text_dropdown);
        if (supportedLanguageNames.size() != 0) {
            spinnerBtn.setAdapter(dataAdapter);
        }
        for (int i = 0; i < supportedLanguages.size(); i++) {
            if (supportedLanguages.get(i).split("-")[0].compareToIgnoreCase(languagePreference) == 0) {
                spinnerBtn.setSelection(i);
            }
        }

    }


    public void loadWebSite() {
        hideTheKeryboard();
        url = urlText.getText().toString();
        if (url.compareToIgnoreCase("") == 0) {
            return;
        }

        webView.getSettings().setJavaScriptEnabled(true);
        webView.setBackgroundColor(Color.WHITE);
        webView.setWebViewClient(new MyWebViewClient());


        String finalUrl = "https://translate.googleusercontent.com/translate_c?act=url&depth=4&hl=es&ie=UTF8&nv=1&prev=_t&rurl=translate.google.com&sl=auto&sp=nmt4&tl=" + currentLangage + "&u=" + url + "&xid=25657,15700022,15700105,15700122,15700124,15700149,15700168,15700201&usg=ALkJrhi4xRImroTDMVt89Kx54KBp9v0wxQ";
        webView.loadUrl(finalUrl);
        finishTranslation();
        urlText.setSelectAllOnFocus(true);
    }


    public class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);

            return true;

        }

        @Override
        public void onLoadResource(WebView view, String url) {
            super.onLoadResource(view, url);
            finishTranslation();
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            finishTranslation();
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            finishTranslation();
            createNewIntAd();
        }
    }


    private void hideTheKeryboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


    private void finishTranslation() {
        webView.loadUrl(
                "javascript:(function() { " +
                        "var element = document.getElementById('gt-nvframe');"
                        + "element.parentNode.style.top=0;"
                        + "element.parentNode.removeChild(element);" +
                        "})()");

    }


    public void appluTheme() {
        try {
            int savedColor = getResources().getColor(getResources().getIdentifier(GetServices.getSavedColor(this), "color", getPackageName()));
            LinearLayout header = (LinearLayout) findViewById(R.id.header);
            GetServices.changeBgColor(header, savedColor);
        } catch (Exception e) {
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        IronSource.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        IronSource.onPause(this);
    }

}
