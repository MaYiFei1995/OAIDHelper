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
            // 华硕
            ret = new AsusDevice(context);
        } else if (RomUtil.isCoolpad(context)) {
            // 酷派
            ret = new CoolpadDevice(context);
        } else if (RomUtil.isCoosea()) {
            // 酷赛
            ret = new CooseaDevice(context);
        } else if (RomUtil.isFreeme()) {
            // 卓易
            ret = new FreemeDevice(context);
        } else if (RomUtil.isHW(manufacturer, brand)) {
            // 华为、EMUI
            ret = new HWDevice(context);
        } else if (RomUtil.isLenovo(manufacturer, brand)) {
            // 联想、乐檬、ZUI、摩托罗拉
            ret = new LenovoDevice(context);
        } else if (RomUtil.isMeizu(manufacturer, brand)) {
            // 魅族
            ret = new MeizuDevice(context);
        } else if (RomUtil.isMi(manufacturer, brand)) {
            // 小米、红米、黑鲨、MIUI
            ret = new MiDevice(context);
        } else if (RomUtil.isNubia(manufacturer, brand)) {
            // 努比亚
            ret = new NubiaDevice(context);
        } else if (RomUtil.isOppo(manufacturer, brand)) {
            // oppo、realme、一加
            ret = new OppoDevice(context);
        } else if (RomUtil.isSamsung(manufacturer, brand)) {
            // 星星星
            ret = new SamsungDevice(context);
        } else if (RomUtil.isVivo(manufacturer, brand)) {
            // Vivo、IQOO
            ret = new VivoDevice(context);
        } else {
            ret = null;
        }
        return ret;
    }

    private static BaseDevice createUniversalDevice(Context context) {
        // 若各厂商自家没有提供接口，则优先尝试移动安全联盟的接口
        BaseDevice device = new MSADevice(context);
        if (device.isSupport()) {
            return device;
        }
        // 若不支持移动安全联盟的接口，则尝试谷歌服务框架的接口
        device = new GmsDevice(context);
        if (device.isSupport()) {
            return device;
        }
        // 其他情况，不支持
        return null;
    }


}
