package com.google.androidbrowserhelper.trusted;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * The purpose of this Activity is to allow this app's Task to be brought to the foreground.
 *
 * A PendingIntent to this Activity is provided to the browser. When the browser wants to bring
 * the app to the foreground (eg as the result of a web notification calling WindowClient.focus()),
 * the browser can launch that PendingIntent bringing this Activity to the foreground. This Activity
 * then finishes itself, revealing the other Activities in the app's Task. Hopefully the topmost
 * Activity will be the web browsing Activity.
 */
public class FocusActivity extends AppCompatActivity {
    // This value should be moved into androidx.browser.
    private static final String EXTRA_FOCUS_INTENT =
            "androidx.browser.customtabs.extra.FOCUS_INTENT";

    public static void addToIntent(Intent containerIntent, Context context) {
        Intent focusIntent = new Intent(context, FocusActivity.class);

        // This class may not be included in app's manifest, don't add it in that case.
        if (focusIntent.resolveActivityInfo(context.getPackageManager(), 0) == null) return;

        // This Intent will be launched from the background so we need NEW_TASK, however if an
        // existing task is suitable, that will be brought to the foreground despite the name of the
        // flag.
        focusIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        containerIntent.putExtra(EXTRA_FOCUS_INTENT,
                PendingIntent.getActivity(context, 0, focusIntent, 0));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        finish();
    }
}