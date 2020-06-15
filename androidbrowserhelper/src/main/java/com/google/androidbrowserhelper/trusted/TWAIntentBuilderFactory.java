package com.google.androidbrowserhelper.trusted;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import androidx.browser.trusted.TrustedWebActivityIntentBuilder;

public class TWAIntentBuilderFactory {

    private Context context;
    private final String TWA_INTENT_BUILDER_CANONICAL_NAME
            = "com.google.androidbrowserhelper.trusted.builder";

    public TWAIntentBuilderFactory(Context ctx){
        context = ctx;
    }

    public TrustedWebActivityIntentBuilder createNew(Uri uri) {

        String builderCanonicalName = getBuilderCanonicalName();
        if(builderCanonicalName == null || builderCanonicalName.isEmpty()) {
            return new TrustedWebActivityIntentBuilder((uri));
        }

        TrustedWebActivityIntentBuilder builder;
        try {
            Class builderClass = Class.forName(getBuilderCanonicalName());
            builder = (TrustedWebActivityIntentBuilder)
                    builderClass.getDeclaredConstructor(Uri.class).newInstance(uri);
            return builder;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new TrustedWebActivityIntentBuilder((uri));
    }

    private String getBuilderCanonicalName() {
        String name = "";
        try {
            ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(
                    context.getPackageName(), PackageManager.GET_META_DATA);
            if (appInfo.metaData != null) {
                name = appInfo.metaData.getString(TWA_INTENT_BUILDER_CANONICAL_NAME);
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return name;
    }

}