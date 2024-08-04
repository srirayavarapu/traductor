package com.transjan.traductor;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.google.android.gms.vision.text.TextBlock;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public class GetServices {

    static AlertDialog.Builder alertDialog;

    public static String[] langageArray = {"en", "fr", "es", "pt", "de", "it"};

    public static TextBlock editTextData = null;
    public static String editTextString = null;

    static Context msgContext;

    static Boolean userRating = false;

    static ProgressDialog dialog;

    static final String rating = "RATING";

    public static void changeBgColor(View v, int color) {
        v.setBackgroundColor(color);
    }

    public static void ShowsnackBar(View v, String msg) {
        Snackbar.make(v, msg, 2000).show();
    }

    public static void ShowToast(Context mContext, String message) {
        Toast toast = Toast.makeText(mContext, message, Toast.LENGTH_SHORT);
        toast.show();
    }

    public static void NewIntent(Context ourContext, Class ourClass) {
        Intent intent = new Intent(ourContext, ourClass);
        ourContext.startActivity(intent);
    }

    public static void NewIntentWithData(Context ourContext, Class ourClass, ArrayList<String> DataList) {
        Intent intent = new Intent(ourContext, ourClass);
        Bundle bundle = new Bundle();
        bundle.putSerializable("IntentData", DataList);
        intent.putExtras(bundle);
        ourContext.startActivity(intent);
    }

    public static void ShowProgressDialog(Context mContext) {
        dialog = new ProgressDialog(mContext);
        dialog.setMessage("Please Wait...");
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    public static void HideProgressDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    public static Dialog showProgressDialog(final Context context, String Msg) {

//        compile 'com.github.ybq:Android-SpinKit:1.1.0'

        final Dialog d = new Dialog(context);
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);
        d.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        final LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View v = li.inflate(R.layout.progress, null);

        final TextView lblMessage = (TextView) v.findViewById(R.id.lblMessage);

        if (!Msg.equals("")) {
            lblMessage.setText(Msg);
        }

        d.setContentView(v);
        d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        d.setCancelable(false);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(d.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        d.show();
        d.getWindow().setAttributes(lp);

        return d;
    }

    public static Dialog hideProgressDialog(final Dialog d) {
        if (d != null && d.isShowing()) {
            d.hide();
        }
        return null;
    }

    public static void ShowMessage(Context mContext, String Msg) {

        alertDialog = new AlertDialog.Builder(mContext);

        alertDialog.setTitle(R.string.app_name);

        // Setting Dialog Message
        alertDialog.setMessage(Msg);

        // On pressing Settings button
        alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogs, int which) {
                dialogs.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }


    public static boolean GetNetworkStatus(Context context) {
        ConnectivityManager cn = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo nf = cn.getActiveNetworkInfo();

        if (nf != null && nf.isConnected() == true) {
            return true;
        } else {
            return false;
        }
    }

    public static void setIsRating(Context ctx, Boolean userLogin) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putBoolean(rating, userLogin);
        editor.commit();
    }

    public static Boolean getIsRating(Context ctx) {
        return getSharedPreferences(ctx).getBoolean(rating, userRating);
    }

    static SharedPreferences getSharedPreferences(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    public static void saveMainColor(Context ctx, String color) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        color = color.toLowerCase().replace(" ", "_");
        editor.putString("theme", color);
        editor.apply();
    }

    public static String getSavedColor(Context ctx) {
        SharedPreferences prefs = getSharedPreferences(ctx);
        return prefs.getString("theme", "colorPrimaryDark");
    }


    public static Set<String> getFavoriteList(Context ctx) {
        SharedPreferences prefs = getSharedPreferences(ctx);
        return prefs.getStringSet("favoriteList", new HashSet<String>());
    }

    public static void saveFavoriteList(Context ctx, HashSet<String> favoriteList) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putStringSet("favoriteList", favoriteList);
        editor.apply();
    }

    public static boolean isFirstUse(Context ctx) {
        SharedPreferences prefs = getSharedPreferences(ctx);
        Boolean firstUse = prefs.getBoolean("firstUse", true);
        if (firstUse) {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("firstUse", false);
            editor.apply();
        }
        return firstUse;
    }


    public static void saveObject(Context ctx, String name, String value) {
        SharedPreferences prefs = getSharedPreferences(ctx);

        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(name, value);
        editor.apply();
    }


    public static String getObject(Context ctx, String name, String defaultVal) {
        SharedPreferences prefs = getSharedPreferences(ctx);
        return prefs.getString(name, defaultVal);
    }

    public static ArrayList<String> getLanguageListNames(Context c) {


        ArrayList<String> supportedLanguageNames = new ArrayList<String>();


        String currentLangage = GetServices.getObject(c, "currentLangage", "");

        if (currentLangage.compareToIgnoreCase("es") == 0) {

            supportedLanguageNames.add("Afrikaans (Suid-Afrika)");
            supportedLanguageNames.add("Azərbaycanca");
            supportedLanguageNames.add("Bahasa Indonesia (Indonesia)");
            supportedLanguageNames.add("Bahasa Melayu (Malasia)");
            supportedLanguageNames.add("Basa Jawa (Indonesia)");
            supportedLanguageNames.add("Basa Sunda (Indonesia)");
            supportedLanguageNames.add("Català (España)");
            supportedLanguageNames.add("Čeština (Česká Republika)");
            supportedLanguageNames.add("Dansk (Danmark)");
            supportedLanguageNames.add("Alemán (Deutschland)");
            supportedLanguageNames.add("Inglés (Australia)");
            supportedLanguageNames.add("Inglés (Canadá)");
            supportedLanguageNames.add("Inglés (genérico)");
            supportedLanguageNames.add("Inglés (Ghana)");
            supportedLanguageNames.add("Inglés (India)");
            supportedLanguageNames.add("Inglés (Irlanda)");
            supportedLanguageNames.add("Inglés (Kenia)");
            supportedLanguageNames.add("Inglés (Nueva Zelanda)");
            supportedLanguageNames.add("Inglés (Nigeria)");
            supportedLanguageNames.add("Inglés (Filipinas)");
            supportedLanguageNames.add("Inglés (Sudáfrica)");
            supportedLanguageNames.add("Inglés (Tanzania)");
            supportedLanguageNames.add("Inglés (Reino Unido)");
            supportedLanguageNames.add("Inglés (EE. UU.)");
            supportedLanguageNames.add("Español (Argentina)");
            supportedLanguageNames.add("Español (Bolivia)");
            supportedLanguageNames.add("Español (Chile)");
            supportedLanguageNames.add("Español (Colombia)");
            supportedLanguageNames.add("Español (Costa Rica)");
            supportedLanguageNames.add("Español (Ecuador)");
            supportedLanguageNames.add("Español (EE.UU.)");
            supportedLanguageNames.add("Español (El Salvador)");
            supportedLanguageNames.add("Español (España)");
            supportedLanguageNames.add("Español (Guatemala)");
            supportedLanguageNames.add("Español (Honduras)");
            supportedLanguageNames.add("Español (México)");
            supportedLanguageNames.add("Español (Nicaragua)");
            supportedLanguageNames.add("Español (Panamá)");
            supportedLanguageNames.add("Español (Paraguay)");
            supportedLanguageNames.add("Español (Perú)");
            supportedLanguageNames.add("Español (Puerto Rico)");
            supportedLanguageNames.add("Español (República Dominicana)");
            supportedLanguageNames.add("Español (Uruguay)");
            supportedLanguageNames.add("Español (Venezuela)");
            supportedLanguageNames.add("Euskara (España)");
            supportedLanguageNames.add("Filipino (Pilipinas)");
            supportedLanguageNames.add("Francés (Francia)");
            supportedLanguageNames.add("Francés (Canadá)");
            supportedLanguageNames.add("Galego (España)");
            supportedLanguageNames.add("Hrvatski (Hrvatska)");
            supportedLanguageNames.add("IsiZulu (Ningizimu Afrika)");
            supportedLanguageNames.add("Íslenska (Ísland)");
            supportedLanguageNames.add("Italiano (Italia)");
            supportedLanguageNames.add("Kiswahili (Kenia)");
            supportedLanguageNames.add("Kiswahili (Tanzania)");
            supportedLanguageNames.add("Latviešu (Latviešu)");
            supportedLanguageNames.add("Lietuvių (Lietuva)");
            supportedLanguageNames.add("Magyar (Magyarország)");
            supportedLanguageNames.add("Nederlands (Nederland)");
            supportedLanguageNames.add("Norsk bokmål (Norge)");
            supportedLanguageNames.add("Polski (Polska)");
            supportedLanguageNames.add("Português (Brasil)");
            supportedLanguageNames.add("Português (Portugal)");
            supportedLanguageNames.add("Română (România)");
            supportedLanguageNames.add("Slovenščina (Slovenija)");
            supportedLanguageNames.add("Slovenčina (Slovensko)");
            supportedLanguageNames.add("Suomi (Suomi)");
            supportedLanguageNames.add("Svenska (Sverige)");
            supportedLanguageNames.add("Tiếng Việt (Việt Nam)");
            supportedLanguageNames.add("Türkçe (Türkiye)");
            supportedLanguageNames.add("Ελληνικά (Ελλάδα)");
            supportedLanguageNames.add("Български (България)");
            supportedLanguageNames.add("Русский (Россия)");
            supportedLanguageNames.add("Српски (Србија)");
            supportedLanguageNames.add("Українська (Україна)");
            supportedLanguageNames.add("ქართული");
            supportedLanguageNames.add("Հայերեն");
            supportedLanguageNames.add("עברית (ישראל)");
            supportedLanguageNames.add("árabe");
            supportedLanguageNames.add("Farsi");
            supportedLanguageNames.add("Urdo");
            supportedLanguageNames.add("አማርኛ (ኢትዮጵያ)");
            supportedLanguageNames.add("हिन्दी (भारत)");
            supportedLanguageNames.add("தமிழ்தியா (இந்தியா)");
            supportedLanguageNames.add("தமிழ்கை (இலங்கை)");
            supportedLanguageNames.add("தமிழ் (சிங்கப்பூர்)");
            supportedLanguageNames.add("தமிழS (மலேசியா)");
            supportedLanguageNames.add("বাংলা (বাংলাদেশ)");
            supportedLanguageNames.add("বাংলা (ভারত)");
            supportedLanguageNames.add("ភាសាខ្មែរ");
            supportedLanguageNames.add("ಕನ್ನಡ");
            supportedLanguageNames.add("मराठी");
            supportedLanguageNames.add("ગુજરાતી");
            supportedLanguageNames.add("සිංහල");
            supportedLanguageNames.add("తెలుగు");
            supportedLanguageNames.add("മലയാളം");
            supportedLanguageNames.add("नेपाली भाषा");
            supportedLanguageNames.add("ລາວ");
            supportedLanguageNames.add("ไทย (ประเทศไทย)");
            supportedLanguageNames.add("한국어 (대한민국)");
            supportedLanguageNames.add("普通话 (中国 大陆)");
            supportedLanguageNames.add("普通話 (香港)");
            supportedLanguageNames.add("國語 (台灣)");
            supportedLanguageNames.add("廣東話 (香港)");
            supportedLanguageNames.add("日本語 (日本)");

        } else if (currentLangage.compareToIgnoreCase("en") == 0) {


            supportedLanguageNames.add("Afrikaans (Suid-Afrika)");
            supportedLanguageNames.add("Azərbaycanca");
            supportedLanguageNames.add("Bahasa Indonesia (Indonesia)");
            supportedLanguageNames.add("Bahasa Melayu (Malaysia)");
            supportedLanguageNames.add("Basa Jawa (Indonesia)");
            supportedLanguageNames.add("Basa Sunda (Indonesia)");
            supportedLanguageNames.add("Català (Spain)");
            supportedLanguageNames.add("Čeština (Česká Republika)");
            supportedLanguageNames.add("Dansk (Danmark)");
            supportedLanguageNames.add("German (Deutschland)");
            supportedLanguageNames.add("English (Australia)");
            supportedLanguageNames.add("English (Canada)");
            supportedLanguageNames.add("English (Generic)");
            supportedLanguageNames.add("English (Ghana)");
            supportedLanguageNames.add("English (India)");
            supportedLanguageNames.add("English (Ireland)");
            supportedLanguageNames.add("English (Kenya)");
            supportedLanguageNames.add("English (New Zealand)");
            supportedLanguageNames.add("English (Nigeria)");
            supportedLanguageNames.add("English (Philippines)");
            supportedLanguageNames.add("English (South Africa)");
            supportedLanguageNames.add("English (Tanzania)");
            supportedLanguageNames.add("English (UK)");
            supportedLanguageNames.add("English (US)");
            supportedLanguageNames.add("Español (Argentina)");
            supportedLanguageNames.add("Español (Bolivia)");
            supportedLanguageNames.add("Español (Chile)");
            supportedLanguageNames.add("Español (Colombia)");
            supportedLanguageNames.add("Español (Costa Rica)");
            supportedLanguageNames.add("Español (Ecuador)");
            supportedLanguageNames.add("Español (EE.UU.)");
            supportedLanguageNames.add("Español (El Salvador)");
            supportedLanguageNames.add("Español (España)");
            supportedLanguageNames.add("Español (Guatemala)");
            supportedLanguageNames.add("Español (Honduras)");
            supportedLanguageNames.add("Español (México)");
            supportedLanguageNames.add("Español (Nicaragua)");
            supportedLanguageNames.add("Español (Panama)");
            supportedLanguageNames.add("Español (Paraguay)");
            supportedLanguageNames.add("Español (Perú)");
            supportedLanguageNames.add("Español (Puerto Rico)");
            supportedLanguageNames.add("Español (República Dominicana)");
            supportedLanguageNames.add("Español (Uruguay)");
            supportedLanguageNames.add("Español (Venezuela)");
            supportedLanguageNames.add("Euskara (Espainia)");
            supportedLanguageNames.add("Filipino (Pilipinas)");
            supportedLanguageNames.add("French France)");
            supportedLanguageNames.add("French (Canada)");
            supportedLanguageNames.add("Galego (España)");
            supportedLanguageNames.add("Hrvatski (Hrvatska)");
            supportedLanguageNames.add("IsiZulu (Ningizimu Afrika)");
            supportedLanguageNames.add("Íslenska (Ísland)");
            supportedLanguageNames.add("Italiano (Italia)");
            supportedLanguageNames.add("Kiswahili (Kenya)");
            supportedLanguageNames.add("Kiswahili (Tanzania)");
            supportedLanguageNames.add("Latviešu (Latviešu)");
            supportedLanguageNames.add("Lietuvių (Lietuva)");
            supportedLanguageNames.add("Magyar (Magyarország)");
            supportedLanguageNames.add("Nederlands (Nederland)");
            supportedLanguageNames.add("Norsk bokmål (Norge)");
            supportedLanguageNames.add("Polski (Polska)");
            supportedLanguageNames.add("Português (Brasil)");
            supportedLanguageNames.add("Português (Portugal)");
            supportedLanguageNames.add("Română (România)");
            supportedLanguageNames.add("Slovenščina (Slovenija)");
            supportedLanguageNames.add("Slovenčina (Slovensko)");
            supportedLanguageNames.add("Suomi (Suomi)");
            supportedLanguageNames.add("Svenska (Sverige)");
            supportedLanguageNames.add("Tiếng Việt (Việt Nam)");
            supportedLanguageNames.add("Türkçe (Türkiye)");
            supportedLanguageNames.add("Ελληνικά (Ελλάδα)");
            supportedLanguageNames.add("Български (България)");
            supportedLanguageNames.add("Русский (Россия)");
            supportedLanguageNames.add("Српски (Србија)");
            supportedLanguageNames.add("Українська (Україна)");
            supportedLanguageNames.add("ქართული");
            supportedLanguageNames.add("Հայերեն");
            supportedLanguageNames.add("עברית (ישראל)");
            supportedLanguageNames.add("arabic");
            supportedLanguageNames.add("Farsi");
            supportedLanguageNames.add("Urdo");
            supportedLanguageNames.add("አማርኛ (ኢትዮጵያ)");
            supportedLanguageNames.add("हिन्दी (भारत)");
            supportedLanguageNames.add("தமிழ் (இந்தியா)");
            supportedLanguageNames.add("தமிழ் (இலங்கை)");
            supportedLanguageNames.add("தமிழ் (சிங்கப்பூர்)");
            supportedLanguageNames.add("தமிழ் (மலேசியா)");
            supportedLanguageNames.add("বাংলা (বাংলাদেশ)");
            supportedLanguageNames.add("বাংলা (ভারত)");
            supportedLanguageNames.add("ភាសាខ្មែរ");
            supportedLanguageNames.add("ಕನ್ನಡ");
            supportedLanguageNames.add("मराठी");
            supportedLanguageNames.add("ગુજરાતી");
            supportedLanguageNames.add("සිංහල");
            supportedLanguageNames.add("తెలుగు");
            supportedLanguageNames.add("മലയാളം");
            supportedLanguageNames.add("नेपाली भाषा");
            supportedLanguageNames.add("ລາວ");
            supportedLanguageNames.add("ไทย (ประเทศไทย)");
            supportedLanguageNames.add("한국어 (대한민국)");
            supportedLanguageNames.add("普通话 (中国 大陆)");
            supportedLanguageNames.add("普通話 (香港)");
            supportedLanguageNames.add("國語 (台灣)");
            supportedLanguageNames.add("廣東話 (香港)");
            supportedLanguageNames.add("日本語 (日本)");

        } else if (currentLangage.compareToIgnoreCase("de") == 0) {

            supportedLanguageNames.add("Afrikaans (Suid-Afrika)");
            supportedLanguageNames.add("Azərbaycanca");
            supportedLanguageNames.add("Bahasa Indonesia (Indonesien)");
            supportedLanguageNames.add("Bahasa Melayu (Malaysia)");
            supportedLanguageNames.add("Basa Jawa (Indonesien)");
            supportedLanguageNames.add("Basa Sunda (Indonesien)");
            supportedLanguageNames.add("Català (Spanien)");
            supportedLanguageNames.add("Čeština (Česká Republika)");
            supportedLanguageNames.add("Dansk (Dänemark)");
            supportedLanguageNames.add("Deutsch (Deutschland)");
            supportedLanguageNames.add("Englisch (Australien)");
            supportedLanguageNames.add("Englisch (Kanada)");
            supportedLanguageNames.add("Englisch (Generic)");
            supportedLanguageNames.add("Englisch (Ghana)");
            supportedLanguageNames.add("Englisch (Indien)");
            supportedLanguageNames.add("Englisch (Irland)");
            supportedLanguageNames.add("Englisch (Kenia)");
            supportedLanguageNames.add("Englisch (Neuseeland)");
            supportedLanguageNames.add("Englisch (Nigeria)");
            supportedLanguageNames.add("Englisch (Philippinen)");
            supportedLanguageNames.add("Englisch (Südafrika)");
            supportedLanguageNames.add("Englisch (Tansania)");
            supportedLanguageNames.add("Englisch (UK)");
            supportedLanguageNames.add("Englisch (US)");
            supportedLanguageNames.add("Spanisch (Argentinien)");
            supportedLanguageNames.add("Spanisch (Bolivien)");
            supportedLanguageNames.add("Spanisch (Chile)");
            supportedLanguageNames.add("Spanisch (Kolumbien)");
            supportedLanguageNames.add("Spanisch (Costa Rica)");
            supportedLanguageNames.add("Spanisch (Ecuador)");
            supportedLanguageNames.add("Spanisch (EE.UU)");
            supportedLanguageNames.add("Spanisch (El Salvador)");
            supportedLanguageNames.add("Spanisch (Spanien)");
            supportedLanguageNames.add("Spanisch (Guatemala)");
            supportedLanguageNames.add("Spanisch (Honduras)");
            supportedLanguageNames.add("Spanisch (Mexiko)");
            supportedLanguageNames.add("Spanisch (Nicaragua)");
            supportedLanguageNames.add("Spanisch (Panama)");
            supportedLanguageNames.add("Spanisch (Paraguay)");
            supportedLanguageNames.add("Spanisch (Peru)");
            supportedLanguageNames.add("Spanisch (Puerto Rico)");
            supportedLanguageNames.add("Spanisch (República Dominicana)");
            supportedLanguageNames.add("Spanisch (Uruguay)");
            supportedLanguageNames.add("Spanisch (Venezuela)");
            supportedLanguageNames.add("Euskara (Espanien)");
            supportedLanguageNames.add("Filipino (Pilipinas)");
            supportedLanguageNames.add("Französisch (Frankreich)");
            supportedLanguageNames.add("Französisch (Kanada)");
            supportedLanguageNames.add("Galego (Spanien)");
            supportedLanguageNames.add("Hrvatski (Hrvatska)");
            supportedLanguageNames.add("IsiZulu (Ningizimu Afrika)");
            supportedLanguageNames.add("Íslenska (Ísland)");
            supportedLanguageNames.add("Italienisch (Italien)");
            supportedLanguageNames.add("Kiswahili (Kenia)");
            supportedLanguageNames.add("Kiswahili (Tansania)");
            supportedLanguageNames.add("Latviešu (Latviešu)");
            supportedLanguageNames.add("Lietuvių (Lietuva)");
            supportedLanguageNames.add("Magyar (Magyarország)");
            supportedLanguageNames.add("Nederlands (Niederlande)");
            supportedLanguageNames.add("Norsk Bokmål (Norwegen)");
            supportedLanguageNames.add("Polski (Polska)");
            supportedLanguageNames.add("Português (Brasilien)");
            supportedLanguageNames.add("Português (Portugal)");
            supportedLanguageNames.add("Română (România)");
            supportedLanguageNames.add("Slovenščina (Slowenien)");
            supportedLanguageNames.add("Slovenčina (Slovensko)");
            supportedLanguageNames.add("Suomi");
            supportedLanguageNames.add("Svenska (Schweden)");
            supportedLanguageNames.add("Tiếng Việt (Việt Nam)");
            supportedLanguageNames.add("Türkçe (Türkiye)");
            supportedLanguageNames.add("Ελληνικά (Ελλάδα)");
            supportedLanguageNames.add("Български (България)");
            supportedLanguageNames.add("Русский (Russland)");
            supportedLanguageNames.add("Српски (Србија)");
            supportedLanguageNames.add("Українська (Україна)");
            supportedLanguageNames.add("ქართული");
            supportedLanguageNames.add("Հայերեն");
            supportedLanguageNames.add("עבעבית (שששאא)");
            supportedLanguageNames.add("Arabisch");
            supportedLanguageNames.add("Farsi");
            supportedLanguageNames.add("Urdo");
            supportedLanguageNames.add("አማርኛ (ኢትዮጵያ)");
            supportedLanguageNames.add("दी्दी (भभतत)");
            supportedLanguageNames.add("(தமிழ (இநஇநதியா)");
            supportedLanguageNames.add("(தமிழ (இலஙஇலஙகை)");
            supportedLanguageNames.add("(தமிழ (சிஙசிஙகபகபபூரபூர)");
            supportedLanguageNames.add("(தமிழ (மலேசியா)");
            supportedLanguageNames.add("বাংলা (বাংলাদেশ)");
            supportedLanguageNames.add("বাংলা (ভারত)");
            supportedLanguageNames.add("ភាសាខ្មែរ");
            supportedLanguageNames.add("ಕನ್ನಡ");
            supportedLanguageNames.add("मराठी");
            supportedLanguageNames.add("ગુજરાતી");
            supportedLanguageNames.add("සිංහල");
            supportedLanguageNames.add("తెలుగు");
            supportedLanguageNames.add("മലയാളം");
            supportedLanguageNames.add("नेपनेपली भभषष");
            supportedLanguageNames.add("ລາວ");
            supportedLanguageNames.add("ไทย (ประเทศไทย)");
            supportedLanguageNames.add("한국어 (대한민국)");
            supportedLanguageNames.add("普通话 (中国 大陆)");
            supportedLanguageNames.add("普通話 (香港)");
            supportedLanguageNames.add("國語 (台灣)");
            supportedLanguageNames.add("廣東話 (香港)");
            supportedLanguageNames.add("日本語 (日本)");

        } else if (currentLangage.compareToIgnoreCase("fr") == 0) {


            supportedLanguageNames.add("Afrikaans (Suid-Afrika)");
            supportedLanguageNames.add("Azərbaycanca");
            supportedLanguageNames.add("Bahasa Indonésie (Indonésie)");
            supportedLanguageNames.add("Bahasa Melayu (Malaisie)");
            supportedLanguageNames.add("Basa Jawa (Indonésien)");
            supportedLanguageNames.add("Basa Sunda (Indonésien)");
            supportedLanguageNames.add("Català (Espanya)");
            supportedLanguageNames.add("Čeština (Česká republika)");
            supportedLanguageNames.add("Dansk (Danmark)");
            supportedLanguageNames.add("Allemand (Deutschland)");
            supportedLanguageNames.add("Anglais (Australie)");
            supportedLanguageNames.add("Anglais (Canada)");
            supportedLanguageNames.add("Anglais (Générique)");
            supportedLanguageNames.add("Anglais (Ghana)");
            supportedLanguageNames.add("Anglais (Inde)");
            supportedLanguageNames.add("Anglais (Irlande)");
            supportedLanguageNames.add("Anglais (Kenya)");
            supportedLanguageNames.add("Anglais (Nouvelle Zélande)");
            supportedLanguageNames.add("Anglais (Nigeria)");
            supportedLanguageNames.add("Anglais (Philippines)");
            supportedLanguageNames.add("Anglais (Afrique du Sud)");
            supportedLanguageNames.add("Anglais (Tanzanie)");
            supportedLanguageNames.add("Anglais (UK)");
            supportedLanguageNames.add("Anglais (États-Unis)");
            supportedLanguageNames.add("Espagnol (Argentine)");
            supportedLanguageNames.add("Espagnol (Bolivie)");
            supportedLanguageNames.add("Espagnol (Chili)");
            supportedLanguageNames.add("Espagnol (Colombie)");
            supportedLanguageNames.add("Espagnol (Costa Rica)");
            supportedLanguageNames.add("Espagnol (Equateur)");
            supportedLanguageNames.add("Espagnol (Etats-Unis)");
            supportedLanguageNames.add("Espagnol (El Salvador)");
            supportedLanguageNames.add("Espagnol - Espagne)");
            supportedLanguageNames.add("Espagnol (Guatemala)");
            supportedLanguageNames.add("Espagnol (Honduras)");
            supportedLanguageNames.add("Espagnol - Mexique)");
            supportedLanguageNames.add("Espagnol (Nicaragua)");
            supportedLanguageNames.add("Espagnol (Panama)");
            supportedLanguageNames.add("Espagnol (Paraguay)");
            supportedLanguageNames.add("Espagnol (Pérou)");
            supportedLanguageNames.add("Espagnol (Porto Rico)");
            supportedLanguageNames.add("Espagnol (République Dominicaine)");
            supportedLanguageNames.add("Espagnol (Uruguay)");
            supportedLanguageNames.add("Espagnol (Venezuela)");
            supportedLanguageNames.add("Euskara (Espagne)");
            supportedLanguageNames.add("Philippin (Pilipinas)");
            supportedLanguageNames.add("Français (France)");
            supportedLanguageNames.add("Français (Canada)");
            supportedLanguageNames.add("Galego (Espagne)");
            supportedLanguageNames.add("Hrvatski (Hrvatska)");
            supportedLanguageNames.add("IsiZulu (Ningizimu Afrika)");
            supportedLanguageNames.add("Íslenska (Ísland)");
            supportedLanguageNames.add("Italien (Italie)");
            supportedLanguageNames.add("Kiswahili (Kenya)");
            supportedLanguageNames.add("Kiswahili (Tanzanie)");
            supportedLanguageNames.add("Latviešu (Latviešu)");
            supportedLanguageNames.add("Lietuvių (Lietuva)");
            supportedLanguageNames.add("Magyar (Magyarország)");
            supportedLanguageNames.add("Nederlands (Nederland)");
            supportedLanguageNames.add("Norsk bokmål (Norge)");
            supportedLanguageNames.add("Polski (Polska)");
            supportedLanguageNames.add("Português (Brésil)");
            supportedLanguageNames.add("Português (Portugal)");
            supportedLanguageNames.add("Română (România)");
            supportedLanguageNames.add("Slovenščina (Slovenija)");
            supportedLanguageNames.add("Slovenčina (Slovensko)");
            supportedLanguageNames.add("Suomi (Suomi)");
            supportedLanguageNames.add("Svenska (Sverige)");
            supportedLanguageNames.add("Tiếng Việt (Việt Nam)");
            supportedLanguageNames.add("Türkçe (Türkiye)");
            supportedLanguageNames.add("Ελληνικά (Ελλάδα)");
            supportedLanguageNames.add("Български (България)");
            supportedLanguageNames.add("Русский (Россия)");
            supportedLanguageNames.add("Српски (Србија)");
            supportedLanguageNames.add("Українська (Україна)");
            supportedLanguageNames.add("ქართული");
            supportedLanguageNames.add("Հայերեն");
            supportedLanguageNames.add("תרית (ישראל)");
            supportedLanguageNames.add("Arabe");
            supportedLanguageNames.add("Farsi");
            supportedLanguageNames.add("Urdo");
            supportedLanguageNames.add("አማርኛ (ኢትዮጵያ)");
            supportedLanguageNames.add("हिन्दी (भारत)");
            supportedLanguageNames.add("தமிழ் (இந்தியா)");
            supportedLanguageNames.add("தமிழ் (இலங்கை)");
            supportedLanguageNames.add("தமிழ் (சிங்கப்பூர்)");
            supportedLanguageNames.add("தமிழ் (மலேசியா)");
            supportedLanguageNames.add("বাংলা (বাংলাদেশ)");
            supportedLanguageNames.add("বাংলা (ভারত)");
            supportedLanguageNames.add("ភាសាខ្មែរ");
            supportedLanguageNames.add("ಕನ್ನಡ");
            supportedLanguageNames.add("मराठी");
            supportedLanguageNames.add("ગુજરાતી");
            supportedLanguageNames.add("සිංහල");
            supportedLanguageNames.add("తెలుగు");
            supportedLanguageNames.add("മലയാളം");
            supportedLanguageNames.add("नेपाली भाषा");
            supportedLanguageNames.add("ລາວ");
            supportedLanguageNames.add("ไทย (ประเทศไทย)");
            supportedLanguageNames.add("한국어 (대한민국)");
            supportedLanguageNames.add("普通话 (中国 大陆)");
            supportedLanguageNames.add("普通話 (香港)");
            supportedLanguageNames.add("國語 (台灣)");
            supportedLanguageNames.add("廣東話 (香港)");
            supportedLanguageNames.add("日本語 (日本)");

        } else if (currentLangage.compareToIgnoreCase("it") == 0) {

            supportedLanguageNames.add("Afrikaans (Suid-Afrika)");
            supportedLanguageNames.add("Azərbaycanca");
            supportedLanguageNames.add("Bahasa Indonesia (Indonesia)");
            supportedLanguageNames.add("Bahasa Melayu (Malesia)");
            supportedLanguageNames.add("Basa Jawa (Indonesia)");
            supportedLanguageNames.add("Basa Sunda (Indonesia)");
            supportedLanguageNames.add("Català (Spagna)");
            supportedLanguageNames.add("Čeština (Česká Republika)");
            supportedLanguageNames.add("Dansk (Danimarca)");
            supportedLanguageNames.add("Tedesco (Deutschland)");
            supportedLanguageNames.add("Inglese (Australia)");
            supportedLanguageNames.add("Inglese (Canada)");
            supportedLanguageNames.add("Inglese (generico)");
            supportedLanguageNames.add("Inglese (Ghana)");
            supportedLanguageNames.add("Inglese (India)");
            supportedLanguageNames.add("Inglese (Irlanda)");
            supportedLanguageNames.add("Inglese (Kenya)");
            supportedLanguageNames.add("Inglese (Nuova Zelanda)");
            supportedLanguageNames.add("Inglese (Nigeria)");
            supportedLanguageNames.add("Inglese (Filippine)");
            supportedLanguageNames.add("Inglese (Sudafrica)");
            supportedLanguageNames.add("Inglese (Tanzania)");
            supportedLanguageNames.add("Inglese (Regno Unito)");
            supportedLanguageNames.add("Inglese (Stati Uniti)");
            supportedLanguageNames.add("Español (Argentina)");
            supportedLanguageNames.add("Español (Bolivia)");
            supportedLanguageNames.add("Español (Cile)");
            supportedLanguageNames.add("Español (Colombia)");
            supportedLanguageNames.add("Español (Costa Rica)");
            supportedLanguageNames.add("Español (Ecuador)");
            supportedLanguageNames.add("Español (EE.UU.)");
            supportedLanguageNames.add("Español (El Salvador)");
            supportedLanguageNames.add("Español (España)");
            supportedLanguageNames.add("Español (Guatemala)");
            supportedLanguageNames.add("Español (Honduras)");
            supportedLanguageNames.add("Español (Messico)");
            supportedLanguageNames.add("Español (Nicaragua)");
            supportedLanguageNames.add("Español (Panama)");
            supportedLanguageNames.add("Español (Paraguay)");
            supportedLanguageNames.add("Español (Perú)");
            supportedLanguageNames.add("Español (Porto Rico)");
            supportedLanguageNames.add("Español (Repubblica Dominicana)");
            supportedLanguageNames.add("Español (Uruguay)");
            supportedLanguageNames.add("Español (Venezuela)");
            supportedLanguageNames.add("Euskara (Espanya)");
            supportedLanguageNames.add("Filippino (Pilipinas)");
            supportedLanguageNames.add("Francese (Francia)");
            supportedLanguageNames.add("Francese (Canada)");
            supportedLanguageNames.add("Galego (España)");
            supportedLanguageNames.add("Hrvatski (Hrvatska)");
            supportedLanguageNames.add("IsiZulu (Ningizimu Afrika)");
            supportedLanguageNames.add("Íslenska (Ísland)");
            supportedLanguageNames.add("Italiano (Italia)");
            supportedLanguageNames.add("Kiswahili (Kenya)");
            supportedLanguageNames.add("Kiswahili (Tanzania)");
            supportedLanguageNames.add("Latviešu (Latviešu)");
            supportedLanguageNames.add("Lietuvių (Lietuva)");
            supportedLanguageNames.add("Magyar (Magyarország)");
            supportedLanguageNames.add("Nederlands (Nederland)");
            supportedLanguageNames.add("Norsk bokmål (Norge)");
            supportedLanguageNames.add("Polski (Polska)");
            supportedLanguageNames.add("Português (Brasile)");
            supportedLanguageNames.add("Português (Portogallo)");
            supportedLanguageNames.add("Română (România)");
            supportedLanguageNames.add("Slovenščina (Slovenija)");
            supportedLanguageNames.add("Slovenčina (Slovensko)");
            supportedLanguageNames.add("Suomi (Suomi)");
            supportedLanguageNames.add("Svenska (Sverige)");
            supportedLanguageNames.add("Tiếng Việt (Việt Nam)");
            supportedLanguageNames.add("Türkçe (Türkiye)");
            supportedLanguageNames.add("Ελληνικά (Ελλάδα)");
            supportedLanguageNames.add("Български (България)");
            supportedLanguageNames.add("Русский (Россия)");
            supportedLanguageNames.add("Српски (Србија)");
            supportedLanguageNames.add("Українська (Україна)");
            supportedLanguageNames.add("ქართული");
            supportedLanguageNames.add("Հայերեն");
            supportedLanguageNames.add("עברית (ישראל)");
            supportedLanguageNames.add("arabo");
            supportedLanguageNames.add("Farsi");
            supportedLanguageNames.add("Urdo");
            supportedLanguageNames.add("አማርኛ (ኢትዮጵያ)");
            supportedLanguageNames.add("हिन्दी (भारत)");
            supportedLanguageNames.add("தமிழ் (இந்தியா)");
            supportedLanguageNames.add("தமிழ் (இலங்கை)");
            supportedLanguageNames.add("தமிழ் (சிங்கப்பூர்)");
            supportedLanguageNames.add("தமிழ் (மலேசியா)");
            supportedLanguageNames.add("বাংলা (বাংলাদেশ)");
            supportedLanguageNames.add("বাংলা (ভারত)");
            supportedLanguageNames.add("ភាសាខ្មែរ");
            supportedLanguageNames.add("ಕನ್ನಡ");
            supportedLanguageNames.add("मराठी");
            supportedLanguageNames.add("ગુજરાતી");
            supportedLanguageNames.add("සිංහල");
            supportedLanguageNames.add("తెలుగు");
            supportedLanguageNames.add("മലയാളം");
            supportedLanguageNames.add("नेपाली भाषा");
            supportedLanguageNames.add("ລາວ");
            supportedLanguageNames.add("ไทย (ประเทศไทย)");
            supportedLanguageNames.add("한국어 (대한민국)");
            supportedLanguageNames.add("普通话 (中国 大陆)");
            supportedLanguageNames.add("普通話 (香港)");
            supportedLanguageNames.add("國語 (台灣)");
            supportedLanguageNames.add("廣東話 (香港)");
            supportedLanguageNames.add("日本語 (日本)");


        } else if (currentLangage.compareToIgnoreCase("pt") == 0) {

            supportedLanguageNames.add("Afrikaans (Suid-Afrika)");
            supportedLanguageNames.add("Azərbaycanca");
            supportedLanguageNames.add("Bahasa Indonesia (Indonésia)");
            supportedLanguageNames.add("Bahasa Melayu (Malásia)");
            supportedLanguageNames.add("Basa Jawa (Indonésia)");
            supportedLanguageNames.add("Basa Sunda (Indonésia)");
            supportedLanguageNames.add("Català (Espanha)");
            supportedLanguageNames.add("Čeština (Česká Republika)");
            supportedLanguageNames.add("Dansk (Dinamarca)");
            supportedLanguageNames.add("Alemão (Deutschland)");
            supportedLanguageNames.add("Inglês (Austrália)");
            supportedLanguageNames.add("Inglês (Canadá)");
            supportedLanguageNames.add("Inglês (genérico)");
            supportedLanguageNames.add("Inglês (Gana)");
            supportedLanguageNames.add("Inglês (Índia)");
            supportedLanguageNames.add("Inglês (Irlanda)");
            supportedLanguageNames.add("Inglês (Quênia)");
            supportedLanguageNames.add("Inglês (Nova Zelândia)");
            supportedLanguageNames.add("Inglês (Nigéria)");
            supportedLanguageNames.add("Inglês (Filipinas)");
            supportedLanguageNames.add("Inglês (África do Sul)");
            supportedLanguageNames.add("Inglês (Tanzânia)");
            supportedLanguageNames.add("Inglês (UK)");
            supportedLanguageNames.add("Inglês (EUA)");
            supportedLanguageNames.add("Español (Argentina)");
            supportedLanguageNames.add("Español (Bolivia)");
            supportedLanguageNames.add("Español (Chile)");
            supportedLanguageNames.add("Español (Colombia)");
            supportedLanguageNames.add("Español (Costa Rica)");
            supportedLanguageNames.add("Español (Equador)");
            supportedLanguageNames.add("Español (EE.UU.)");
            supportedLanguageNames.add("Español (El Salvador)");
            supportedLanguageNames.add("Español (España)");
            supportedLanguageNames.add("Español (Guatemala)");
            supportedLanguageNames.add("Español (Honduras)");
            supportedLanguageNames.add("Español (México)");
            supportedLanguageNames.add("Español (Nicarágua)");
            supportedLanguageNames.add("Español (Panama)");
            supportedLanguageNames.add("Español (Paraguay)");
            supportedLanguageNames.add("Español (Perú)");
            supportedLanguageNames.add("Español (Porto Rico)");
            supportedLanguageNames.add("Español (República Dominicana)");
            supportedLanguageNames.add("Español (Uruguai)");
            supportedLanguageNames.add("Español (Venezuela)");
            supportedLanguageNames.add("Euskara (Espainia)");
            supportedLanguageNames.add("Filipino (Pilipinas)");
            supportedLanguageNames.add("Francês (França)");
            supportedLanguageNames.add("Francês (Canadá)");
            supportedLanguageNames.add("Galego (España)");
            supportedLanguageNames.add("Hrvatski (Hrvatska)");
            supportedLanguageNames.add("IsiZulu (Ningizimu Afrika)");
            supportedLanguageNames.add("Íslenska (Ísland)");
            supportedLanguageNames.add("Italiano (Italia)");
            supportedLanguageNames.add("Kiswahili (Quênia)");
            supportedLanguageNames.add("Kiswahili (Tanzânia)");
            supportedLanguageNames.add("Latviešu (Latviešu)");
            supportedLanguageNames.add("Lietuvių (Lietuva)");
            supportedLanguageNames.add("Magiar (Magyarország)");
            supportedLanguageNames.add("Nederlands (Nederland)");
            supportedLanguageNames.add("Norsk Bokmål (Norge)");
            supportedLanguageNames.add("Polski (Polska)");
            supportedLanguageNames.add("Português (Brasil)");
            supportedLanguageNames.add("Português (Portugal)");
            supportedLanguageNames.add("Română (România)");
            supportedLanguageNames.add("Eslovščina (Slovenija)");
            supportedLanguageNames.add("Eslovênia (Slovensko)");
            supportedLanguageNames.add("Suomi (Suomi)");
            supportedLanguageNames.add("Svenska (Sverige)");
            supportedLanguageNames.add("Tiếng Việt (Việt Nam)");
            supportedLanguageNames.add("Türkçe (Türkiye)");
            supportedLanguageNames.add("Ελληνικά (Ελλάδα)");
            supportedLanguageNames.add("Български (България)");
            supportedLanguageNames.add("Русский (Россия)");
            supportedLanguageNames.add("Српски (Србија)");
            supportedLanguageNames.add("Українська (Україна)");
            supportedLanguageNames.add("ქართული");
            supportedLanguageNames.add("Հայերեն");
            supportedLanguageNames.add("(ברית (ישראל)");
            supportedLanguageNames.add("árabe");
            supportedLanguageNames.add("Farsi");
            supportedLanguageNames.add("Urdo");
            supportedLanguageNames.add("አማርኛ (ኢትዮጵያ)");
            supportedLanguageNames.add("दी्दी (तारत)");
            supportedLanguageNames.add("தமிழ் (இந்தியா)");
            supportedLanguageNames.add("தமிழ் (இலங்கை)");
            supportedLanguageNames.add("தமிழ் (சிங்கப்பூர்)");
            supportedLanguageNames.add("(் (மலேசியா)");
            supportedLanguageNames.add("বাংলা (বাংলাদেশ)");
            supportedLanguageNames.add("বাংলা (ভারত)");
            supportedLanguageNames.add("ភាសាខ្មែរ");
            supportedLanguageNames.add("ಕನ್ನಡ");
            supportedLanguageNames.add("मराठी");
            supportedLanguageNames.add("ગુજરાતી");
            supportedLanguageNames.add("සිංහල");
            supportedLanguageNames.add("తెలుగు");
            supportedLanguageNames.add("മലയാളം");
            supportedLanguageNames.add("लीाली भाषा");
            supportedLanguageNames.add("ລາວ");
            supportedLanguageNames.add("ไทย (ประเทศไทย)");
            supportedLanguageNames.add("한국어 (대한민국)");
            supportedLanguageNames.add("普通话 (中国 大陆)");
            supportedLanguageNames.add("普通話 (香港)");
            supportedLanguageNames.add("國語 (台灣)");
            supportedLanguageNames.add("廣東話 (香港)");
            supportedLanguageNames.add("日本語 (日本)");

        }


        return supportedLanguageNames;
    }


    public static ArrayList<String> getLanguageListSymbs() {

        ArrayList<String> supportedLanguages = new ArrayList<String>();

        supportedLanguages.add("af-ZA");
        supportedLanguages.add("az-AZ");
        supportedLanguages.add("id-ID");
        supportedLanguages.add("ms-MY");
        supportedLanguages.add("jv-ID");
        supportedLanguages.add("su-ID");
        supportedLanguages.add("ca-ES");
        supportedLanguages.add("cs-CZ");
        supportedLanguages.add("da-DK");
        supportedLanguages.add("de-DE");
        supportedLanguages.add("en-AU");
        supportedLanguages.add("en-CA");
        supportedLanguages.add("en-001");
        supportedLanguages.add("en-GH");
        supportedLanguages.add("en-IN");
        supportedLanguages.add("en-IE");
        supportedLanguages.add("en-KE");
        supportedLanguages.add("en-NZ");
        supportedLanguages.add("en-NG");
        supportedLanguages.add("en-PH");
        supportedLanguages.add("en-ZA");
        supportedLanguages.add("en-TZ");
        supportedLanguages.add("en-GB");
        supportedLanguages.add("en-US");
        supportedLanguages.add("es-AR");
        supportedLanguages.add("es-BO");
        supportedLanguages.add("es-CL");
        supportedLanguages.add("es-CO");
        supportedLanguages.add("es-CR");
        supportedLanguages.add("es-EC");
        supportedLanguages.add("es-US");
        supportedLanguages.add("es-SV");
        supportedLanguages.add("es-ES");
        supportedLanguages.add("es-GT");
        supportedLanguages.add("es-HN");
        supportedLanguages.add("es-MX");
        supportedLanguages.add("es-NI");
        supportedLanguages.add("es-PA");
        supportedLanguages.add("es-PY");
        supportedLanguages.add("es-PE");
        supportedLanguages.add("es-PR");
        supportedLanguages.add("es-DO");
        supportedLanguages.add("es-UY");
        supportedLanguages.add("es-VE");
        supportedLanguages.add("eu-ES");
        supportedLanguages.add("fil-PH");
        supportedLanguages.add("fr-FR");
        supportedLanguages.add("fr-CA");
        supportedLanguages.add("gl-ES");
        supportedLanguages.add("hr-HR");
        supportedLanguages.add("zu-ZA");
        supportedLanguages.add("is-IS");
        supportedLanguages.add("it-IT");
        supportedLanguages.add("sw");
        supportedLanguages.add("sw-TZ");
        supportedLanguages.add("lv-LV");
        supportedLanguages.add("lt-LT");
        supportedLanguages.add("hu-HU");
        supportedLanguages.add("nl-NL");
        supportedLanguages.add("nb-NO");
        supportedLanguages.add("pl-PL");
        supportedLanguages.add("pt-BR");
        supportedLanguages.add("pt-PT");
        supportedLanguages.add("ro-RO");
        supportedLanguages.add("sl-SI");
        supportedLanguages.add("sk-SK");
        supportedLanguages.add("fi-FI");
        supportedLanguages.add("sv-SE");
        supportedLanguages.add("vi-VN");
        supportedLanguages.add("tr-TR");
        supportedLanguages.add("el-GR");
        supportedLanguages.add("bg-BG");
        supportedLanguages.add("ru-RU");
        supportedLanguages.add("sr-RS");
        supportedLanguages.add("uk-UA");
        supportedLanguages.add("ka-GE");
        supportedLanguages.add("hy-AM");
        supportedLanguages.add("he-IL");
        supportedLanguages.add("ar-SA");
        supportedLanguages.add("fa-IR");
        supportedLanguages.add("ur-PK");
        supportedLanguages.add("am-ET");
        supportedLanguages.add("hi-IN");
        supportedLanguages.add("ta-IN");
        supportedLanguages.add("ta-LK");
        supportedLanguages.add("ta-SG");
        supportedLanguages.add("ta-MY");
        supportedLanguages.add("bn-BD");
        supportedLanguages.add("bn-IN");
        supportedLanguages.add("km-KH");
        supportedLanguages.add("kn-IN");
        supportedLanguages.add("mr-IN");
        supportedLanguages.add("gu-IN");
        supportedLanguages.add("si-LK");
        supportedLanguages.add("te-IN");
        supportedLanguages.add("ml-IN");
        supportedLanguages.add("ne-NP");
        supportedLanguages.add("lo-LA");
        supportedLanguages.add("th-TH");
        supportedLanguages.add("ko-KR");
        supportedLanguages.add("cmn-Hans-CN");
        supportedLanguages.add("cmn-Hans-HK");
        supportedLanguages.add("cmn-Hant-TW");
        supportedLanguages.add("yue-Hant-HK");
        supportedLanguages.add("ja-JP");

        return supportedLanguages;
    }


    public static void setLocale(Context context, String lang, Class c) {
        Locale myLocale = new Locale(lang);
        Resources res = context.getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        Intent refresh = new Intent(context, c);
        context.startActivity(refresh);
        ((Activity) context).finish();
    }

    public static String getDeviseLocale() {
        return Locale.getDefault().getLanguage();
    }

    public static String getLocale(Context context) {
        return context.getResources().getConfiguration().locale.getLanguage();
    }

    public static void changeTheLangage(Context context, Class c) {
        String currentLangage = getObject(context, "currentLangage", "");
        if (currentLangage != getDeviseLocale()) {
            String deviseLangage = getDeviseLocale();
            String defaultLangage = "en";
            if (Arrays.asList(langageArray).contains(deviseLangage)) {
                defaultLangage = deviseLangage;
            }

            saveObject(context, "currentLangage", defaultLangage);
            currentLangage = defaultLangage;
        }

        if (currentLangage.compareToIgnoreCase(getLocale(context)) != 0) {
            saveObject(context, "currentLangage", currentLangage);
            setLocale(context, currentLangage, c);
        }
    }


}
