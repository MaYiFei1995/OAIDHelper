package com.mai.oaid.helper.system.device;

import android.content.Context;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.util.Pair;

import com.mai.oaid.helper.OAIDError;
import com.mai.oaid.helper.system.base.BaseDevice;

import java.util.Objects;

/**
 * 魅族
 */
public class MeizuDevice implements BaseDevice {

    private static final String TAG = "MeizuDeviceIDHelper";

    private final Context context;

    public MeizuDevice(Context context) {
        this.context = context;
    }

    @Override
    public boolean isSupport() {
        if (context == null) {
            return false;
        }
        try {
            ProviderInfo pi = context.getPackageManager().resolveContentProvider("com.meizu.flyme.openidsdk", 0);
            return pi != null;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Pair<String, OAIDError> getOAID() throws Exception {
        Uri uri = Uri.parse("content://com.meizu.flyme.openidsdk/");
        try (Cursor cursor = context.getContentResolver().query(uri, null, null,
                new String[]{"oaid"}, null)) {
            Objects.requireNonNull(cursor).moveToFirst();
            String oaid = parse(cursor);
            if (oaid == null || oaid.length() == 0) {
                Log.e(TAG, "return empty");
                return new Pair<>(null, OAIDError.RETURN_EMPTY);
            }
            return new Pair<>(oaid, null);
        } catch (Exception e) {
            Log.e(TAG, "unknown exception", e);
            return new Pair<>(null, OAIDError.SERVICE_ERROR);
        }
    }

    private String parse(Cursor cursor) {
        String ret = "";
        if (cursor != null && !cursor.isClosed()) {
            cursor.moveToFirst();
            int value = cursor.getColumnIndex("value");
            if (value > 0) {
                ret = cursor.getString(value);
            }
        }
        return ret;
    }

}
