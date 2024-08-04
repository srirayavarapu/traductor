package com.transjan.traductor;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.facebook.CallbackManager;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.ironsource.mediationsdk.IronSource;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import cz.msebera.android.httpclient.Header;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {


    private static final int REQ_CODE_SPEECH_INPUT = 100;
    private EditText mVoiceInputTv;
    public TextView mOutput,mTranslate;
    private ImageView mSpeakBtn;
    public Spinner mSelectLanguage, mFinalLanguage;
    public List<String> mLaunguageList = new ArrayList<>();
    public String mLanguage = "es-ES", mTranslationLanguage;
    public Context context;
    public Dialog progressDialog = null;
    private String translatedText;
    private ArrayList<String> add_Transtale_text;
    private RelativeLayout ll_Output;
    private String TextMearge = "";
    private ImageView imagVieSwitchLanguage;
    private ImageView imageViewClear;
    private ImageView imageViewSpeck;
    private ImageView imageViewShare;
    private ImageView imageViewWhatsApp;
    private ImageView imageViewMessager;
    private ImageView imageViewCopy;
    private ImageView imageViewFacebookPost;
    private ShareDialog shareDialog;
    private ArrayAdapter<String> dataAdapter;
    private TextToSpeech tts;
    private ImageView webTranslation;

    private Button hideTuto1;
    private Button hideTuto2;

    private ImageView loveBtn;
    private ImageButton settingsBtn;
    private ImageButton favoritesBtn;

    private AdView mBannerAd;

    private RelativeLayout guide1;
    private RelativeLayout guide2;

    private LinearLayout contentView;
    private boolean isKeyBoardOpen = false;

    static int count = 0;
    private boolean isShowDialog = false;
    private CallbackManager callbackManager;

    private RotateAnimation mRotate;
//Camera Feature Img to text
//    private ImageView imageViewTranslation;

    private MediaPlayer mp;
    private boolean adFailed = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        GetServices.changeTheLangage(context, MainActivity.class);
        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(MainActivity.this);
        mVoiceInputTv = (EditText) findViewById(R.id.voiceInput);

        webTranslation = (ImageView) findViewById(R.id.webTranslation);

        settingsBtn = (ImageButton) findViewById(R.id.settingsBtn);
        loveBtn = (ImageView) findViewById(R.id.loveBtn);
        favoritesBtn = (ImageButton) findViewById(R.id.favorites_btn);

        mSpeakBtn = (ImageView) findViewById(R.id.btnSpeak);
        mTranslate = (TextView) findViewById(R.id.btnTranslate);
        mOutput = (TextView) findViewById(R.id.voiceOutput);
        ll_Output = (RelativeLayout) findViewById(R.id.ll_output);
        mSelectLanguage = (Spinner) findViewById(R.id.select_language);
        mFinalLanguage = (Spinner) findViewById(R.id.final_language);
        imagVieSwitchLanguage = (ImageView) findViewById(R.id.imagVieSwitchLanguage);
        imageViewFacebookPost = (ImageView) findViewById(R.id.imageViewFacebookPost);
        imageViewClear = (ImageView) findViewById(R.id.imageViewClear);
        imageViewSpeck = (ImageView) findViewById(R.id.imageViewSpeck);
        imageViewShare = (ImageView) findViewById(R.id.imageViewShare);
        imageViewWhatsApp = (ImageView) findViewById(R.id.imageViewWhatsApp);
        imageViewMessager = (ImageView) findViewById(R.id.imageViewMessager);
        imageViewCopy = (ImageView) findViewById(R.id.imageViewCopy);
//Camera Feature Img to text
//        imageViewTranslation = (ImageView) findViewById(R.id.imageViewTranslation);
        contentView = (LinearLayout) findViewById(R.id.contentView);
        mBannerAd = (AdView) findViewById(R.id.adView);

        if (GetServices.editTextData != null) {
            mVoiceInputTv.setText(GetServices.editTextData.getValue());
            GetServices.editTextData = null;
        }

        if (GetServices.editTextString != null) {
            mVoiceInputTv.setText(GetServices.editTextString);
            GetServices.editTextString = null;
        }
//        AdView adView = new AdView(this);
//        adView.setAdSize(AdSize.SMART_BANNER);
//        InterstitialAd intAd = new InterstitialAd(this);
//        intAd.setAdUnitId(getString(R.string.ad_id_interstitial));
        tts = new TextToSpeech(this, MainActivity.this);
        mSpeakBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startVoiceInput();
            }
        });
//AdMob ads
        ShowBannerAds();
        loadInterAd();

        hideTuto1 = (Button) findViewById(R.id.hideTuto1);
        guide1 = (RelativeLayout) findViewById(R.id.guide1);

        hideTuto1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                guide2.setVisibility(View.VISIBLE);
                guide1.setVisibility(View.GONE);
            }
        });


        hideTuto2 = (Button) findViewById(R.id.hideTuto2);
        guide2 = (RelativeLayout) findViewById(R.id.guide2);

        hideTuto2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GetServices.saveObject(context, "tutorial", "yes");
                contentView.setVisibility(View.VISIBLE);
                guide2.setVisibility(View.GONE);
            }
        });


        String tutorial = GetServices.getObject(context, "tutorial", "no");

        if (tutorial.compareToIgnoreCase("yes") == 0) {
            contentView.setVisibility(View.VISIBLE);
            guide1.setVisibility(View.GONE);
            guide2.setVisibility(View.GONE);
        }


        String currentLangage = GetServices.getObject(context, "currentLangage", "en");

        guide1.setBackgroundResource(getResources().getIdentifier("guide1" + currentLangage, "drawable", getPackageName()));
        guide2.setBackgroundResource(getResources().getIdentifier("guide2" + currentLangage, "drawable", getPackageName()));


        setLanguages();


        appluTheme();

        settingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(MainActivity.this, SettingsActivity.class);
                MainActivity.this.startActivity(myIntent);
            }
        });

        loveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String srcText = mVoiceInputTv.getText().toString();
                String toText = mOutput.getText().toString();
                if (srcText.compareToIgnoreCase("") == 0 || toText.compareToIgnoreCase("") == 0) {
                    return;
                }
                HashSet<String> favoritesList = new HashSet<String>();

                Set<String> favoriteList = GetServices.getFavoriteList(context);
                for (String entry : favoriteList) {
                    favoritesList.add(entry);
                }


                favoritesList.add(mLanguage.split("-")[0] + "@-@kam@-@" + srcText + "@-@kam@-@" + mTranslationLanguage.split("-")[0] + "@-@kam@-@" + toText);

                GetServices.saveFavoriteList(context, favoritesList);

                Intent myIntent = new Intent(MainActivity.this, FavoritesActivity.class);
                MainActivity.this.startActivity(myIntent);
            }
        });


        favoritesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(MainActivity.this, FavoritesActivity.class);
                MainActivity.this.startActivity(myIntent);
            }
        });

        mp = MediaPlayer.create(this, R.raw.loader);
        mTranslate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (adFailed) loadInterAd();
                mp.start();
                float ROTATE_FROM = 0.0f;
                float ROTATE_TO = -10.0f * 360.0f;

                String getText = mVoiceInputTv.getText().toString();
                if (TextUtils.isEmpty(getText)) {
                    GetServices.ShowsnackBar(getWindow().getDecorView().findViewById(android.R.id.content), getString(R.string.textinputempty_1));
                } else {
                    if (!mLanguage.equals("") && !mTranslationLanguage.equals("")) {

                        mRotate = new RotateAnimation(0, 180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                        mRotate.setDuration(500);
                        mRotate.setRepeatCount(1);
                        mRotate.setInterpolator(new LinearInterpolator());

//                        mTranslate.startAnimation(mRotate);
                        hideKeyboard(MainActivity.this);
                        GetTranslatedData(getText, mLanguage, mTranslationLanguage);

                    }
                }
            }
        });


        webTranslation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(MainActivity.this, WebTranslationActivity.class);
                myIntent.putExtra("currentLangage", mLanguage.split("-")[0]);
                MainActivity.this.startActivity(myIntent);
            }
        });


        imagVieSwitchLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int positionSpinner1 = mSelectLanguage.getSelectedItemPosition();
                int positionSpinner2 = mFinalLanguage.getSelectedItemPosition();
                mSelectLanguage.setSelection(positionSpinner2);
                mFinalLanguage.setSelection(positionSpinner1);
                String output = mOutput.getText().toString();
                String input = mVoiceInputTv.getText().toString();
                mOutput.setText(" ");
                mVoiceInputTv.setText(" ");
                mOutput.setText(input);
                mVoiceInputTv.setText(output);
            }
        });

        imageViewClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mVoiceInputTv.setText("");
                mOutput.setText("");
            }
        });

        imageViewSpeck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speechOutput();
            }
        });

        imageViewFacebookPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(mOutput.getText().toString())) {
                    if (ShareDialog.canShow(ShareLinkContent.class)) {
                        Share();
                    }
                }
            }
        });

        imageViewShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(mOutput.getText().toString())) {
                    Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");
                    sharingIntent.putExtra(Intent.EXTRA_TEXT, mOutput.getText().toString() + "\n" + Constrent.AppUrl);
                    startActivity(Intent.createChooser(sharingIntent, ""));
                }
            }
        });

        imageViewWhatsApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(mOutput.getText().toString().trim())) {
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, mOutput.getText().toString() + "\n" + Constrent.AppUrl);
                    sendIntent.setType("text/plain");
                    sendIntent.setPackage("com.whatsapp");
                    try {
                        startActivity(sendIntent);
                    } catch (ActivityNotFoundException ex) {
                        Toast.makeText(context, getString(R.string.pleaseInstallWhatsapp_1), Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        imageViewMessager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(mOutput.getText().toString().trim())) {
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, mOutput.getText().toString() + "\n" + Constrent.AppUrl);
                    sendIntent.setType("text/plain");
                    sendIntent.setPackage("com.facebook.orca");
                    try {
                        startActivity(sendIntent);
                    } catch (ActivityNotFoundException ex) {
                        Toast.makeText(context, getString(R.string.pleaseInstallFacebook_1), Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        imageViewCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(mOutput.getText().toString())) {
                    ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("Copy", mOutput.getText().toString());
                    clipboard.setPrimaryClip(clip);
                    Toast.makeText(context, getString(R.string.copy_clipboard_1), Toast.LENGTH_SHORT).show();
                }
            }
        });
//Camera Feature Img to text
//        imageViewTranslation.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(MainActivity.this, ImageTranslationActivity.class);
//                startActivity(intent);
//            }
//        });

        contentView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                Rect r = new Rect();
                contentView.getWindowVisibleDisplayFrame(r);
                int screenHeight = contentView.getRootView().getHeight();

                int keypadHeight = screenHeight - r.bottom;


                if (keypadHeight > screenHeight * 0.15) { // 0.15 ratio is perhaps enough to determine keypad height.
                    // keyboard is opened

                    isKeyBoardOpen = true;
                } else {
                    // keyboard is closed
                    if (isKeyBoardOpen) {
                        if (!TextUtils.isEmpty(mVoiceInputTv.getText().toString())) {
                            String text = mVoiceInputTv.getText().toString();
                            Log.e("kkkkkkk", "pppppppp");
//                            GetTranslatedData(text, mLanguage, mTranslationLanguage);
                            isKeyBoardOpen = false;
                        }
                    }
                }
            }
        });

        //Intent detailsIntent = new Intent(RecognizerIntent.ACTION_GET_LANGUAGE_DETAILS);
        //sendOrderedBroadcast(detailsIntent, null, new LanguageDetailsChecker(), null, Activity.RESULT_OK, null, null);

        cc(null, null, "es-ES");

        mSelectLanguage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);

                String TypeName = "";
                String LangName = "";
                TypeName = parent.getItemAtPosition(position).toString();
                if (mLaunguageList.size() != 0) {
                    LangName = mLaunguageList.get(position);

                    if (!LangName.equals("")) {
                        mLanguage = LangName;
                        GetServices.saveObject(context, "sl", mLanguage);
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

        mFinalLanguage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);

                String TypeName = "";
                String LangName = "";
                TypeName = parent.getItemAtPosition(position).toString();
                if (mLaunguageList.size() != 0) {
                    LangName = mLaunguageList.get(position);
                    if (!LangName.equals("")) {
                        mTranslationLanguage = LangName;
                        GetServices.saveObject(context, "tl", mTranslationLanguage);
                    }
                } else {
                    GetServices.ShowsnackBar(getWindow().getDecorView().findViewById(android.R.id.content), getString(R.string.please_try_1));
                }
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ll_Output.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(MainActivity.this);
            }
        });
//


//        ShowBannerAds();
//        createNewIntAd();


        hideKeyboard(MainActivity.this);
    }

    private void loadInterAd() {
        adFailed = false;
    }

    private void showInterAd() {
    }

    private void Share() {
        if (!TextUtils.isEmpty(mOutput.getText().toString())) {
            if (ShareDialog.canShow(ShareLinkContent.class)) {
                ShareLinkContent linkContent = new ShareLinkContent.Builder()
                        .setQuote(mOutput.getText().toString())
                        .setContentUrl(Uri.parse(Constrent.AppUrl))
                        .build();
                shareDialog.show(linkContent);
            }
        }
    }


    private void startVoiceInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, mLanguage);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, getString(R.string.hello_help_you_1));

        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.googlequicksearchbox"));
            startActivity(browserIntent);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    mVoiceInputTv.setText(result.get(0));
                    GetTranslatedData(result.get(0), mLanguage, mTranslationLanguage);
                }
                break;
            }
        }
    }

    @Override
    public void onInit(int status) {
        try {
            if (status == TextToSpeech.SUCCESS) {
                int launage = tts.setLanguage(Locale.US);
                if (launage == TextToSpeech.LANG_MISSING_DATA && launage == TextToSpeech.LANG_NOT_SUPPORTED) {
                } else {
                    speechOutput();
                }
            } else {
            }
        } catch (Exception e) {
        }
    }

    private void speechOutput() {
        if (!TextUtils.isEmpty(mOutput.getText().toString())) {
            String speechText = mOutput.getText().toString().trim();
            tts.speak(speechText, TextToSpeech.QUEUE_FLUSH, null);
        }
    }

    public class LanguageDetailsChecker extends BroadcastReceiver {
        private List<String> supportedLanguages, supportedLanguageNames;

        private String languagePreference;

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle results = getResultExtras(true);
            if (results.containsKey(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE)) {
//                languagePreference = results.getString(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE);

            }
            if (results.containsKey(RecognizerIntent.EXTRA_SUPPORTED_LANGUAGES)) {
                supportedLanguages = results.getStringArrayList(RecognizerIntent.EXTRA_SUPPORTED_LANGUAGES);
                supportedLanguageNames = results.getStringArrayList("android.speech.extra.SUPPORTED_LANGUAGE_NAMES");
                if (supportedLanguageNames != null && supportedLanguageNames.size() != 0) {
                    mLaunguageList = supportedLanguages;
                    cc(supportedLanguageNames, supportedLanguages, languagePreference);
                }
            }
        }
    }

    public void cc(List<String> supportedLanguageNames, List<String> supportedLanguages, String languagePreference) {
//        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, supportedLanguageNames);
//        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        supportedLanguageNames = GetServices.getLanguageListNames(context);
        supportedLanguages = GetServices.getLanguageListSymbs();


        mLaunguageList = supportedLanguages;

        dataAdapter = new ArrayAdapter<String>(this, R.layout.spinner_text, supportedLanguageNames);
        dataAdapter.setDropDownViewResource(R.layout.spinner_text_dropdown);
        if (supportedLanguageNames.size() != 0) {
            mSelectLanguage.setAdapter(dataAdapter);
            mFinalLanguage.setAdapter(dataAdapter);
        }
        for (int i = 0; i < supportedLanguages.size(); i++) {
            if (supportedLanguages.get(i).compareToIgnoreCase(mLanguage) == 0) {
                mSelectLanguage.setSelection(i);
            }
        }

        for (int i = 0; i < supportedLanguages.size(); i++) {
            if (supportedLanguages.get(i).compareToIgnoreCase(mTranslationLanguage) == 0) {
                mFinalLanguage.setSelection(i);
            }
        }

        setLanguages();
    }

    public void GetTranslatedData(String Text, String From, String To) {

        if (count > 2) {
            count = 0;
            showInterAd();
        } else {
            count++;
        }

        if (GetServices.GetNetworkStatus(context)) {
            String queryText = null;
            try {
                queryText = URLEncoder.encode(Text, "UTF-8");
                String decodedUrl = URLDecoder.decode(queryText, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            String Url = "https://translate.googleapis.com/translate_a/single?client=gtx&sl=" + From + "&tl=" + To + "&dt=t&q=" + queryText;
            String Url2 = "";
            try {
                Url2 = URLEncoder.encode(Url, "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            final AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(60 * 1000);
            client.setURLEncodingEnabled(true);
            client.get(context, Url, null, "application/json", new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    TextMearge = "";
                    mOutput.setText(TextMearge);


                    try {
                        add_Transtale_text = new ArrayList<String>();
                        JSONArray array = response.getJSONArray(0);
                        for (int i = 0; i < array.length(); i++) {
                            TextMearge += array.getJSONArray(i).getString(0) + "\n";
                        }
                        mOutput.setText(TextMearge);
                        try {
                            mRotate.cancel();
                        } catch (Exception e) {
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                    progressDialog = GetServices.hideProgressDialog(progressDialog);
                    GetServices.ShowToast(context, getString(R.string.oops_1));
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    progressDialog = GetServices.hideProgressDialog(progressDialog);
                    GetServices.ShowToast(context, getString(R.string.oops_1));
                }
            });

        } else {
            Toast.makeText(context, getString(R.string.check_internet_1), Toast.LENGTH_SHORT).show();
        }
    }


    public void GetTranslateDataOk(String Text, String From, String To) {
        if (GetServices.GetNetworkStatus(context)) {
            progressDialog = GetServices.showProgressDialog(context, "");
            String queryText = null;
            try {
                queryText = URLEncoder.encode(Text, "UTF-8");
                String decodedUrl = URLDecoder.decode(queryText, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            String Url = "https://translate.googleapis.com/translate_a/single?client=gtx&sl=" + From + "&tl=" + To + "&dt=t&q=" + queryText;
            OkHttpHandler okHttpHandler = new OkHttpHandler(Url);
            okHttpHandler.execute();

        } else {
            Toast.makeText(context, getString(R.string.check_internet_1), Toast.LENGTH_SHORT).show();
        }
    }


    public class OkHttpHandler extends AsyncTask {

        OkHttpClient client = new OkHttpClient();
        String Url;

        private OkHttpHandler(String _Url) {
            this.Url = _Url;
        }

        @Override
        protected Object doInBackground(Object[] params) {
            final Request request = new Request.Builder()
                    .url(Url)
                    .get()
                    .addHeader("content-type", "application/json")
                    .build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {

                    String s = response.body().toString();
                    try {
                        JSONArray array = new JSONArray(s);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            if (o != null) {
                progressDialog = GetServices.hideProgressDialog(progressDialog);
                super.onPostExecute(o);
            } else {
                progressDialog = GetServices.hideProgressDialog(progressDialog);
            }
        }
    }

    public void hideKeyboard(Activity activity) {
        if (activity != null && activity.getWindow() != null && activity.getWindow().getDecorView() != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(activity.getWindow().getDecorView().getWindowToken(), 0);
        } else {
        }
    }


//    private void createNewIntAd() {
//
//        Log.e("bbbbbbb", "eeeeeeee");
//        try {
//            mInterstitialAd = new InterstitialAd(this);
//            mInterstitialAd.setAdUnitId(getString(R.string.ad_id_interstitial));
//
//
//            try {
//                mInterstitialAd.setAdListener(new AdListener() {
//                    @Override
//                    public void onAdLoaded() {
//                        try {
//                            mInterstitialAd.show();
//                        } catch (Exception e) {
//                        }
//                    }
//
//                    @Override
//                    public void onAdClosed() {
//                        super.onAdClosed();
//                    }
//
//                    @Override
//                    public void onAdFailedToLoad(int i) {
//                        super.onAdFailedToLoad(i);
//                    }
//                });
//            } catch (Exception e) {
//            }
//            mInterstitialAd.loadAd(new AdRequest.Builder().build());
//        } catch (Exception e) {
//        }
//    }
//
//    private void loadIntAdd() {
//        AdRequest adRequest = new AdRequest.Builder().build();
//        mInterstitialAd.loadAd(adRequest);
//        mInterstitialAd.show();
//    }


    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {

        if (doubleBackToExitPressedOnce || GetServices.getIsRating(context) || GetServices.isFirstUse(this)) {
            super.onBackPressed();
            finish();
            return;
        }


        this.doubleBackToExitPressedOnce = true;

        if (!GetServices.isFirstUse(context)) {
            Show_AlertDialog();
        }

    }

    public void Show_AlertDialog() {
        try {
            if (!GetServices.getIsRating(context) && !isShowDialog) {
                isShowDialog = true;
                final Dialog dialog = new Dialog(context);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setCanceledOnTouchOutside(true);


                dialog.setContentView(R.layout.rating_dialog);
                Button btnNosetReating = (Button) dialog.findViewById(R.id.btnNosetReating);
                Button btnLater = (Button) dialog.findViewById(R.id.btnLater);
                Button btnYessetReating = (Button) dialog.findViewById(R.id.btnYessetReating);
                btnNosetReating.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        GetServices.setIsRating(context, true);
                        dialog.dismiss();
                    }
                });
                btnLater.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                btnYessetReating.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            GetServices.setIsRating(context, true);
                            Intent i = new Intent(android.content.Intent.ACTION_VIEW);
                            i.setData(Uri.parse(Constrent.AppUrl));
                            startActivity(i);
                        } catch (Exception e) {
                        }
                    }
                });
                try {
                    dialog.show();
                } catch (WindowManager.BadTokenException ex) {
                }

            }
        } catch (Exception e) {
        }
    }


    private boolean isAppIsInBackground(Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            isInBackground = false;
                        }
                    }
                }
            }
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                isInBackground = false;
            }
        }

        return isInBackground;
    }


    public void ShowBannerAds() {
        AdRequest adRequest = new AdRequest.Builder().build();
        mBannerAd.loadAd(adRequest);
    }

    public void appluTheme() {
        try {
            int savedColor = getResources().getColor(getResources().getIdentifier(GetServices.getSavedColor(context), "color", getPackageName()));
            LinearLayout header = (LinearLayout) findViewById(R.id.header);
            GetServices.changeBgColor(header, savedColor);

            LinearLayout centerBar = (LinearLayout) findViewById(R.id.center_bar);
//            GetServices.changeBgColor(centerBar, savedColor);

//            LinearLayout footer = (LinearLayout) findViewById(R.id.footer);
//            GetServices.changeBgColor(footer, savedColor);
        } catch (Exception e) {
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        IronSource.onResume(this);
        try {
            appluTheme();
        } catch (Exception e) {

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        try {
            tts.shutdown();
        } catch (Exception e) {
        }

//        try{
//            Date beginupd =  new Date();
//            Long time = ((beginupd.getTime()+1000)/1000);
//
//            GetServices.saveObject(context, "closedAt" , String.valueOf(time));
//        }catch (Exception e){
//        }
        if (mp != null) mp.release();
    }


    public void setLanguages() {

        String currentLangage = GetServices.getObject(context, "currentLangage", "en");

        mLanguage = GetServices.getObject(context, "sl", "en-US");
        mTranslationLanguage = GetServices.getObject(context, "tl", "es-ES");


        if (currentLangage.compareToIgnoreCase("de") == 0) {
            mLanguage = GetServices.getObject(context, "sl", "de-DE");
            mTranslationLanguage = GetServices.getObject(context, "tl", "en-US");
        } else if (currentLangage.compareToIgnoreCase("es") == 0) {
            mLanguage = GetServices.getObject(context, "sl", "es-ES");
            mTranslationLanguage = GetServices.getObject(context, "tl", "en-US");
        } else if (currentLangage.compareToIgnoreCase("fr") == 0) {
            mLanguage = GetServices.getObject(context, "sl", "fr-FR");
            mTranslationLanguage = GetServices.getObject(context, "tl", "en-US");
        } else if (currentLangage.compareToIgnoreCase("it") == 0) {
            mLanguage = GetServices.getObject(context, "sl", "it-IT");
            mTranslationLanguage = GetServices.getObject(context, "tl", "en-US");
        } else if (currentLangage.compareToIgnoreCase("pt") == 0) {
            mLanguage = GetServices.getObject(context, "sl", "pt-PT");
            mTranslationLanguage = GetServices.getObject(context, "tl", "en-US");
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        IronSource.onPause(this);
    }

}
