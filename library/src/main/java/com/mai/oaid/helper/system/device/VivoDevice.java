package com.mai.oaid.helper.system.device;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.util.Pair;

import com.mai.oaid.helper.OAIDError;
import com.mai.oaid.helper.system.base.BaseDevice;
import com.mai.oaid.helper.system.util.RomUtil;

import java.util.Objects;

/**
 * Vivo、IQOO
 */
public class VivoDevice implements BaseDevice {

    private static final String TAG = "VivoDeviceIDHelper";

    private final Context context;

    public VivoDevice(Context paramContext) {
        this.context = paramContext;
    }

    @Override
    public boolean isSupport() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
            return false;
        }
        return RomUtil.sysProperty("persist.sys.identifierid.supported", "0").equals("1");
    }

    @Override
    public Pair<String, OAIDError> getOAID() throws Exception {
        Uri uri = Uri.parse("content://com.vivo.vms.IdProvider/IdentifierId/OAID");
        try (Cursor cursor = context.getContentResolver().query(uri, null, null,
                null, null)) {
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
