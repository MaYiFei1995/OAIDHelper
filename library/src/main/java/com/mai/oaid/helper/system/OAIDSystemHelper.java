package com.mai.oaid.helper.system;

import android.content.Context;
import android.os.Build;

import com.mai.oaid.helper.OAIDCallback;

import java.util.concurrent.Executors;

public class OAIDSystemHelper {

    private static final String TAG = "OAIDSystemHelper";

    private static boolean isRequesting = false;
    private static boolean isNotSupport = false;

    public static void tryGetOaid(final Context context, OAIDCallback oaidCallback) {
        if (isRequesting) {
            return;
        }
        isRequesting = true;
        if (isNotSupport) {
            return;
        }
        Executors.newCachedThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    String manufacturer = Build.MANUFACTURER.toUpperCase();
                    String oaid = null;
                    switch (manufacturer) {

                    }
                } catch (Throwable tr) {
                    oaidCallback.onResult(null);
                }
            }
        });
    }

}
