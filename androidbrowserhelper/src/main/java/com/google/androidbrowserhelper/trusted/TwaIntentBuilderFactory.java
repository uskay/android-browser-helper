package com.google.androidbrowserhelper.trusted;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.browser.customtabs.CustomTabsSession;
import androidx.browser.trusted.TrustedWebActivityIntent;
import androidx.browser.trusted.TrustedWebActivityIntentBuilder;

public class TwaIntentBuilderFactory {

    private Context context;
    private final String TWA_INTENT_CUSTOMIZER_CANONICAL_NAME
            = "com.google.androidbrowserhelper.trusted.intentcustomizer";

    public TwaIntentBuilderFactory(Context context){
        this.context = context;
    }

    public TrustedWebActivityIntentBuilder createNew(Uri uri) {

        String customIntentCanonicalName = getIntentCustomizerCanonicalName();
        if(customIntentCanonicalName == null || customIntentCanonicalName.isEmpty()) {
            return new TrustedWebActivityIntentBuilder(uri);
        }

        try {
            Class customizerClass = Class.forName(getIntentCustomizerCanonicalName());
            ITwaIntentCustomizer customizer = (ITwaIntentCustomizer)
                    customizerClass.getDeclaredConstructor().newInstance();
            CustomTwaIntentBuilder customIntent = new CustomTwaIntentBuilder(uri);
            customIntent.setContext(context);
            customIntent.setTwaIntentCustomizer(customizer);
            return customIntent;
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

        return new TrustedWebActivityIntentBuilder(uri);
    }

    private String getIntentCustomizerCanonicalName() {
        String name = "";

        try {
            ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(
                    context.getPackageName(), PackageManager.GET_META_DATA);
            if (appInfo.metaData != null) {
                name = appInfo.metaData.getString(TWA_INTENT_CUSTOMIZER_CANONICAL_NAME);
            }
        } catch (PackageManager.NameNotFoundException e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return name;
    }

    class CustomTwaIntentBuilder extends TrustedWebActivityIntentBuilder {

        private Context context;
        private ITwaIntentCustomizer customizer;

        public CustomTwaIntentBuilder(@NonNull Uri uri) {
            super(uri);
        }

        public void setContext(Context context) {
            this.context = context;
        }

        public void setTwaIntentCustomizer(ITwaIntentCustomizer customizer) {
            this.customizer = customizer;
        }

        @Override
        public TrustedWebActivityIntent build(CustomTabsSession session) {
            TrustedWebActivityIntent twaIntent = super.build(session);
            return customizer.getIntent(context, twaIntent);
        }
    }

}