package com.google.androidbrowserhelper.trusted;

import android.app.Activity;
import android.content.Context;

import androidx.browser.trusted.TrustedWebActivityIntent;

public interface ITwaIntentCustomizer {

    public TrustedWebActivityIntent getIntent(Context context, TrustedWebActivityIntent intent);

}
