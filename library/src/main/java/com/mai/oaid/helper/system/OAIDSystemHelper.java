package com.mai.oaid.helper.system;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.util.Pair;

import androidx.annotation.NonNull;

import com.mai.oaid.helper.OAIDError;
import com.mai.oaid.helper.system.base.BaseDevice;
import com.mai.oaid.helper.system.device.AsusDevice;
import com.mai.oaid.helper.system.device.CoolpadDevice;
import com.mai.oaid.helper.system.device.CooseaDevice;
import com.mai.oaid.helper.system.device.FreemeDevice;
import com.mai.oaid.helper.system.device.GmsDevice;
import com.mai.oaid.helper.system.device.HWDevice;
import com.mai.oaid.helper.system.device.LenovoDevice;
import com.mai.oaid.helper.system.device.MSADevice;
import com.mai.oaid.helper.system.device.MeizuDevice;
import com.mai.oaid.helper.system.device.MiDevice;
import com.mai.oaid.helper.system.device.NubiaDevice;
import com.mai.oaid.helper.system.device.OppoDevice;
import com.mai.oaid.helper.system.device.SamsungDevice;
import com.mai.oaid.helper.system.device.VivoDevice;
import com.mai.oaid.helper.system.util.RomUtil;

public class OAIDSystemHelper {

    private static final String TAG = "OAIDSystemHelper";

    private static boolean isRequesting = false;
    private static boolean isNotSupport = false;

    @NonNull
    public static Pair<String, OAIDError> tryGetOaid(final Context context) {
        if (isRequesting) {
            return new Pair<>(null, OAIDError.REQUESTING);
        }
        isRequesting = true;
        if (isNotSupport) {
            return new Pair<>(null, OAIDError.NOT_SUPPORT);
        }

        try {
            BaseDevice device = createManufacturerDevice(context);
            if (device == null) {
                device = createUniversalDevice(context);
            }
            if (device != null && device.isSupport()) {
                Log.d(TAG, "MSA device class: [" + device.getClass().getName() + "]");
                return device.getOAID();
            } else {
                isNotSupport = true;
                Log.d(TAG, "MSA device class not found");
                return new Pair<>(null, OAIDError.NOT_SUPPORT);
            }
        } catch (Throwable tr) {
            Log.e(TAG, "get oaid error", tr);
            return new Pair<>(null, OAIDError.UNKNOWN_ERROR);
        }
    }

    private static BaseDevice createManufacturerDevice(Context context) {
        BaseDevice ret;
        String manufacturer = Build.MANUFACTURER.toUpperCase();
        String brand = Build.BRAND.toUpperCase();
        if (RomUtil.isASUS(manufacturer, brand)) {
            // ??????
            ret = new AsusDevice(context);
        } else if (RomUtil.isCoolpad(context)) {
            // ??????
            ret = new CoolpadDevice(context);
        } else if (RomUtil.isCoosea()) {
            // ??????
            ret = new CooseaDevice(context);
        } else if (RomUtil.isFreeme()) {
            // ??????
            ret = new FreemeDevice(context);
        } else if (RomUtil.isHW(manufacturer, brand)) {
            // ?????????EMUI
            ret = new HWDevice(context);
        } else if (RomUtil.isLenovo(manufacturer, brand)) {
            // ??????????????????ZUI???????????????
            ret = new LenovoDevice(context);
        } else if (RomUtil.isMeizu(manufacturer, brand)) {
            // ??????
            ret = new MeizuDevice(context);
        } else if (RomUtil.isMi(manufacturer, brand)) {
            // ???????????????????????????MIUI
            ret = new MiDevice(context);
        } else if (RomUtil.isNubia(manufacturer, brand)) {
            // ?????????
            ret = new NubiaDevice(context);
        } else if (RomUtil.isOppo(manufacturer, brand)) {
            // oppo???realme?????????
            ret = new OppoDevice(context);
        } else if (RomUtil.isSamsung(manufacturer, brand)) {
            // ?????????
            ret = new SamsungDevice(context);
        } else if (RomUtil.isVivo(manufacturer, brand)) {
            // Vivo???IQOO
            ret = new VivoDevice(context);
        } else {
            ret = null;
        }
        return ret;
    }

    private static BaseDevice createUniversalDevice(Context context) {
        // ?????????????????????????????????????????????????????????????????????????????????
        BaseDevice device = new MSADevice(context);
        if (device.isSupport()) {
            return device;
        }
        // ??????????????????????????????????????????????????????????????????????????????
        device = new GmsDevice(context);
        if (device.isSupport()) {
            return device;
        }
        // ????????????????????????
        return null;
    }


}
